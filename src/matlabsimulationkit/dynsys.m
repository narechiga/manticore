
function dxdt = dynsys(t,x)
%inline(['[x(2);-sin(x(1))-x(2)]'],'t','x');
%	dxdt1 = x(2);
%	dxdt2 = -sin(x(1))-x(2);
%	dxdt = [dxdt1; dxdt2];

	valuation = '{ ';
	for i = 1:length(x)
		if (i == 1)
			valuation = sprintf('%s x%i -> %g', valuation, i, x(i));
		else
			valuation = sprintf('%s, x%i -> %g', valuation, i, x(i));
		end

	end
	valuation = sprintf('%s }', valuation);

	valstring = char(Manticore.runSimulate(sprintf('{x1''=x2, x2''=-sin(x1) - x2} %s', valuation)));

	commas = strfind( valstring, ',');
	dxdt = [];
	for i = 1:length(x)
		thisderivativestart = strfind(valstring, sprintf('_dx%idt_=', i ));
		init = thisderivativestart + length(num2str(i)) + 7;
		dxdt(i) = str2num(valstring(init:commas(i)));
	end
	dxdt = transpose(dxdt); % because default behavior seems to be to give a row vector
	
end
