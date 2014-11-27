
function I = initialset( x )
	
	% Note that one must discard the input, otherwise this function will throw an error
	assert( length(x) == 4 );

	if ( norm(x) < 0.2 )
		I = true;
	else
		I = false;
	end

end

