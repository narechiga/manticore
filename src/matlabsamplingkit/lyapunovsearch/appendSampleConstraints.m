
function [A, b] = appendSampleConstraints( A, b, X, Z, f, newSamples )
	fprintf('Appending sample constraints...\n');
	LOG('Appending sample constraints...');

	start = clock();

	dZdX = jacobian(Z, X);
	derivAt = [];
	gradAt = [];
	Acounter = size(A,1) + 1;
	for i = 1:size( newSamples, 1 )
		gradAt(:,:,i) = subs( dZdX, X, transpose(newSamples(i, :)) );
		derivAt(:,:,i) = gradAt(:,:,i)*unpackValuation(f(newSamples(i,:)));
		A( Acounter, : ) = derivAt(:, :, i)';
		A( Acounter + 1, : ) = subs(-Z, X, transpose(newSamples(i, :)) );
		Acounter = Acounter + 2;
	end

	b = [b; 0*ones(size(A,1) - length(b), 1)];

	stop = clock();
	fprintf(sprintf('Appending sample constraints took %i time units\n', etime(stop, start)));
	LOG(sprintf('Appending sample constraints took %i time units\n', etime(stop, start)));


end
