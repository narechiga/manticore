
function [V, Xsamples, A, b] = improveWithOptimizer( V, Xsamples, Xlower, Xupper, A, b, X, Z, f, maxiterations, precision )
	
	iterations = 0;
	success = 0;

	while ( (iterations < maxiterations) && (success == 0) )
		fprintf(sprintf('Starting optimizer iteration %i', iterations))
		LOG(sprintf('Starting optimizer iteration %i', iterations))

		Vfunc = matlabFunction( V, 'vars', {X} );

		dVdt = vpa(jacobian(V, X)*f(X));
		minusdVfunc = matlabFunction( -dVdt, 'vars', {X} );

		startpos = clock();
		[posminx, posmin, posexitflag, posoutput] = fmincon(Vfunc, zeros(length(X), 1), [], [], [], [], Xlower, Xupper);
		endpos = clock();
		fprintf(sprintf('Checking positivity with optimizer took %i', etime(endpos, startpos)))
		LOG(sprintf('Checking positivity with optimizer took %i', etime(endpos, startpos)))
		startneg = clock();
		[negmaxx, negmax, negexitflag, negoutput] = fmincon(minusdVfunc, zeros(length(X), 1), [], [], [], [], Xlower, Xupper);
		endneg = clock();
		fprintf(sprintf('Checking negativity with optimizer took %i', etime(endneg, startneg)))
		LOG(sprintf('Checking negativity with optimizer took %i', etime(endneg, startneg)))

		if ( posmin < 0 )
			fprintf('Improving function positivity with optimizer counterexample...\n');
			LOG('Improving function positivity with optimizer counterexample...');
			Xsamples = appendSample( Xsamples, transpose(posminx) )
			[A, b] = appendSampleConstraints( A, b, X, Z, f, transpose(posminx) );
			success = 0;
		else
			fprintf('Function positivity succeeds w.r.t. optimizer.\n');
			LOG('Function positivity succeeds w.r.t. optimizer.');
			success = 1;
		end

		if ( negmax < 0 )
			fprintf('Improving derivative negativity with optimizer counterexample...\n');
			LOG('Improving derivative negativity with optimizer counterexample...');
			Xsamples = appendSample( Xsamples, transpose(negmaxx) )
			[A, b] = appendSampleConstraints( A, b, X, Z, f, transpose(negmaxx ));
			success = success & 0;
		else
			fprintf('Derivative negativity succeeds w.r.t. optimizer\n');
			LOG('Derivative negativity succeeds w.r.t. optimizer');
			success = success & 1;
		end

		if ( success == 0 )
			%[A, b] = generateSampleConstraints( Xsamples, X, Z, f );
			V = generateCandidate(Z, A, b, precision )
			fprintf('Generated new candidate through optimizer feedback: %s', char(V) );
			LOG(sprintf('Generated new candidate through optimizer feedback: %s', char(V) ));
			
		end

		iterations = iterations + 1;

	end

	if ( success == 1 )
		fprintf('Candidate is approved by the optimizer\n');
		V
		LOG('Candidate is approved by the optimizer');
	else
		fprintf('Candidate is not yet at its best w.r.t. optimizer, but maximum iterations have been reached\n');
		LOG('Candidate is not yet at its best w.r.t. optimizer, but maximum iterations have been reached');
	end
end
