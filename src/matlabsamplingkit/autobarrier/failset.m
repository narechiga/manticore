
function F = failset(x)

	if ( abs(x(1)) > pi/2 )
		F = true;
	else
		F = false;
	end
end
