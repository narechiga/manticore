function cex = extractCEX( filename )

	cexfile = fopen( filename, 'r');
	fgetl( cexfile ); % discard human-friendly header
	clear cex_output; clear cex_lo; clear cex_hi;
	cex_output = textscan( cexfile, '%s : [%f, %f];');
	fclose( cexfile );
	cex_label = cex_output{1}; cex_lo = cex_output{2}; cex_hi = cex_output{3};

	%assert( length(cex_output{2}) == length(X), 'Something went wrong when trying to parse function positivity counterexample\n');
	cex = [];
	for i = 1:length(cex_lo)
		cexindex = find(strcmp( sprintf('x%i', i), cex_label));
		cex = [cex, (cex_lo(cexindex) + cex_hi(cexindex))/2];
	end
end
