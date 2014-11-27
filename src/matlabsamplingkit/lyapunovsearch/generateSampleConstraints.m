
function [A,b] = generateSampleConstraints( Xsamples, X, Z, f )
	fprintf('Generating sample constraints...\n');
	LOG('Generating sample constraints...\n');

	start = clock();

	dZdX = jacobian(Z, X);
	derivAt = [];
	gradAt = [];
	Acounter = 1;
	for i = 1:size( Xsamples, 1 )
		gradAt(:,:,i) = subs( dZdX, X, transpose(Xsamples(i, :)) );
		derivAt(:,:,i) = gradAt(:,:,i)*f(Xsamples(i,:));

		% Guard against imaginary stuff
		if ( prod( imag(derivAt(:, :, i)) == 0 ) ...
			&&  prod( imag( subs(-Z, X, transpose(Xsamples(i, :)) ) ) == 0 ) )

			A( Acounter, : ) = derivAt(:, :, i)';
			A( Acounter + 1, : ) = subs(-Z, X, transpose(Xsamples(i, :)) );
			Acounter = Acounter + 2;
		end
	end
	
	b = zeros( size(A,1), 1);
	
	stop = clock();
	fprintf(sprintf('Generating sample constraints took %i time units\n', etime(stop, start)));
	LOG(sprintf('Generating sample constraints took %i time units\n', etime(stop, start)));
end
