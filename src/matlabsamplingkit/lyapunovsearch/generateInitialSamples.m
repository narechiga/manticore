
function Xsamples = generateInitialSamples( X, Xlower, Xupper, samplenumber )

	fprintf('Generating initial samples...\n');
	LOG('Generating initial samples...\n');
	start = clock();
	for i = 1:length(X) %initialize empty sample arrays
		gensamples = sprintf('x%isamples = [];', i);
		eval(gensamples);
	end
	
	for i = 1:length(X)
		gensamples = sprintf('x%isamples = linspace( Xlower(%i), Xupper(%i), samplenumber );', i, i, i);
		eval(gensamples);
	end

	crosssamplegen = 'Xsamples = setprod( ';
	for i = 1:length(X)
		crosssamplegen = [crosssamplegen, sprintf('x%isamples,', i)];
	end
	crosssamplegen = [crosssamplegen(1:length(crosssamplegen)-1), ');']; 
	eval(crosssamplegen);

	stop = clock();
	fprintf(sprintf('Generating initial samples took %i time units\n', etime(stop, start)));
	LOG(sprintf('Generating initial samples took %i time units\n', etime(stop, start)));
end

