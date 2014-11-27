
function samples = sampleAllOrthants( center, minRadius, maxRadius, pointsPerOrthant )

	if ( minRadius > maxRadius )
		error('minRadius is larger than maxRadius');
	end

	zone = 0.1*minRadius;

	samples = [];
	n = length(center);

	declareSyms = 'syms ';
	declareX = 'X = [';
	for i = 1:n
		declareSyms = sprintf('%s x%i', declareSyms, i);
		declareX = sprintf('%s; x%i', declareX, i) ;
	end
	declareX = sprintf('%s ];', declareX);
	eval( declareSyms );
	eval(declareX);

	last = 2^(length(center)) - 1;

	for count = 0:last
		formulas = {};

		formulas{end+1} = sprintf('%s < %1.5f', char(vpa(transpose(X - center)*(X - center))), maxRadius^2 );
		formulas{end+1} = sprintf('%s > %1.5f', char(vpa(transpose(X - center)*(X - center))), minRadius^2 );

		orthant = dec2bin(count, n);

		% Sample an orthant
		forceField = (maxRadius - minRadius)/pointsPerOrthant;
		for j = 1:n
			if ( orthant(j) == '1' )
				formulas{end + 1} = sprintf('x%i - %f > %f', j, center(j), zone );
			else
				formulas{end + 1} = sprintf('x%i - %f < %f', j, center(j), -zone );
			end
		end

		for j = 1:pointsPerOrthant
			[res, filename] = querySolver( formulas, X, center-maxRadius*ones(n, 1), center+maxRadius*ones(n,1) );

			if ( strcmp( res, 'sat' ) )
				thisSample = extractCEX( filename );
				samples = [ samples; thisSample ];
				formulas{end+1} = sprintf('%s > %1.5f', char(vpa(transpose(X - transpose(thisSample))*(X - transpose(thisSample)))),forceField);
			else
				warning('Could not find a sample');
				break;
			end

		end

	end

end
