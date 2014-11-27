
function [success, lyapunov] = lyapunovgenerator( X, f, degree, Xlower, Xupper, exclusionRadius, precision, samplenumber, maxiterations) 	
	overallstart = clock();

	[~, myname] = system('hostname');
	myname = strtrim( myname ); % remove newline at end

	generateWorkspace();
	%% Generate a monomial vector. Candidate will be z'*p, for p parameter vector
	Z = monomials(X, 1:degree);
	
	Xsamples = generateInitialSamples( X, Xlower, Xupper, samplenumber );
	A = []; b = [];
	[A, b] = appendSampleConstraints( A, b, X, Z, f, Xsamples );

	success = 0;
	iterations = 0;
	
	keyboard;
	while ( (success == 0) && (iterations < maxiterations) )
		LOG(sprintf('Starting iteration %i\n', iterations));
		%[A,b] = generateSampleConstraints( Xsamples, X, Z, f);
		V = generateCandidate( Z, A, b, precision )
		dVdt = vpa(jacobian(V, X))*f(X);

		[V, Xsamples, A, b] = improveWithOptimizer( V, Xsamples, Xlower, Xupper, A, b, X, Z, f, maxiterations, precision );
		dVdt = vpa(jacobian(V, X)*f(X));

		startdreal = clock();
		[posresult, negresult] = querySolver( V, dVdt, X, Xlower, Xupper, exclusionRadius );
		stopdreal = clock();
		fprintf(sprintf('Consulting dReal took %i\n', etime(stopdreal, startdreal)));


		if ( strcmp(posresult, 'unsat') && strcmp(negresult,'unsat' ) )
			fprintf('Candidate validated successfully!\n');
			LOG('Candidate validated successfully!\n');
			fprintf('Validated: %s\n', char(V));
			LOG(sprintf('Validated: %s', char(V)));
			success = 1;
		elseif ( iterations < maxiterations )
			fprintf('Candidate validation failed, seeing what I can learn from the dReal fallout.\n');
			LOG('Candidate validation failed, seeing what I can learn from the dReal fallout.');
			
			if ( strcmp( posresult, 'sat' ) )
				fprintf('Function was not positive. Extracting counterexample\n');
				LOG('Function was not positive. Extracting counterexample');
				poscex = extractCEX( sprintf('../drealqueries/%s/functionpositivity.smt2.model', myname) );
				Xsamples = appendSample( Xsamples, poscex );
				[A, b] = appendSampleConstraints( A, b, X, Z, f, poscex );
			else
				fprintf('Function was positive\n');
				LOG('Function was positive');
			end
		
			if ( strcmp( negresult, 'sat' ) )
				fprintf('Derivative was not negative. Extracting counterexample\n');
				LOG('Derivative was not negative. Extracting counterexample');
				negcex = extractCEX(sprintf('../drealqueries/%s/derivativenegativity.smt2.model',myname) ); 
				Xsamples = appendSample( Xsamples, negcex );
				[A, b] = appendSampleConstraints( A, b, X, Z, f, negcex );
				
			else
				fprintf('Derivative was negative\n');
				LOG('Derivative was negative');
			end
		else 
			fprintf('Validation failed, maximum number of iterations reached, giving up.\n');
			LOG('Validation failed, maximum number of iterations reached, giving up.');
		end
		
		iterations = iterations + 1;
	end

	if ( success == 0 )
		fprintf('Validation failed--returning only a candidate solution.\n');
		LOG('Validation failed--returning only a candidate solution.');
	end

	lyapunov = V;	

	overallstop = clock();
	fprintf(sprintf('Total elapsed time was %f\n', etime(overallstop, overallstart)));
	LOG(sprintf('Total elapsed time was %f\n', etime(overallstop, overallstart)));
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



