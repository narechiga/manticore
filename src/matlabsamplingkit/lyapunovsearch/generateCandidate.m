
function V = generateCandidate( Z, A, b, precision )
	keyboard;
	fprintf('Generating candidate...\n');
	LOG('Generating candidate...');
	start = clock();

	objective = zeros( length(Z), 1);
	[x, fval, exitflag, output, lambda] = linprog( objective, A, b);
	
	if ( exitflag == 1 )
		fprintf('Candidate successfully computed\n');
		LOG('Candidate successfully computed');
	else
		fprintf('Optimizer reported errors, check exitflag and output\n');
		LOG('Optimizer reported errors, check exitflag and output');
	end
	
	% Round to the desired precision
	x = x*(10^precision);
	x = round(x);
	x = x/(10^precision);
	
	V = vpa(Z.'*x);
	stop = clock();
	fprintf(sprintf('Generating candidate took %i time units\n', etime(stop,start)));
	LOG(sprintf('Generating candidate took %i time units\n', etime(stop,start)));
end
