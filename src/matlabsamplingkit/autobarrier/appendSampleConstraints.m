function [A, b] = appendSampleConstraints( A, b, X, Z, f, InitialSet, FailSet, newSamples, precision )
        fprintf('Appending sample constraints...\n');
        %LOG('Appending sample constraints...');

        start = clock();

        dZdX = jacobian(Z, X);
        derivAt = [];
        gradAt = [];
        for i = 1:size( newSamples, 1 )
		
		thisX = transpose(newSamples(i, :));

		% Constraint matrix A
		if ( (InitialSet( thisX ) > 0) && ( FailSet( thisX ) > 0) )
			error('State belongs to both initial and fail set! Check that these sets are disjoint.');
		end

		if ( InitialSet( thisX ) > 0 ) % then we want B(x) <= 0
			% B(x) <= b(@)
			% Note that the zero here is a weight on beta
			A = [A; transpose(subs( Z, X, thisX )), 0];

			% Derivative rows -- to avoid doing this over the fail set because who cares over there
                	gradAt(:,:,i) = subs( dZdX, X, thisX );
                	derivAt(:,:,i) = gradAt(:,:,i)*f( thisX );
			A = [A; transpose(derivAt(:, :, i)), -1];

		elseif ( FailSet( thisX ) > 0 ) % then we want B(x) >= 0
			% -B(x) <= b(@)
			% Note that the zero here is a weight on beta
			A = [A; transpose(subs( -Z, X, thisX )), 0];
		else
			% Derivative rows -- to avoid doing this over the fail set because who cares over there
                	gradAt(:,:,i) = subs( dZdX, X, thisX );
                	derivAt(:,:,i) = gradAt(:,:,i)*f( thisX );
			A = [A; transpose(derivAt(:, :, i)), -1];
		end


        end

        %b = [b; zeros(size(A,1) - length(b), 1)];
	%b = [b; -1*ones(size(A,1) - length(b), 1)];
	b = [b; -1*ones(size(A,1) - length(b), 1)];
	A = double(round((10^(precision))*A)/(10^precision));

        stop = clock();
        fprintf(sprintf('Appending sample constraints took %i time units\n', etime(stop, start)));
        %LOG(sprintf('Appending sample constraints took %i time units\n', etime(stop, start)));


end

