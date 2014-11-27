

function dxdt = plantmodel( t, x )

	% Inverted pendulum on moving cart, from Khalil Ex. 1.15 p. 28
	g = 9.8;
	m = 1;
	M = 2;
	L = 1;
	I = 1;
	k = 0.15;

	% Name change
	theta = x(1); thetadot = x(2);
	y = x(3); ydot = x(4);
	F = 0; % control input

	% Delta(theta), greater than zero, as noted in part (b) of Ex. 1.15
	delta = ( I + m*L^2)*(m + M) - m^2*L^2*cos( theta )^2;

	G = [	m+M,			-m*L*cos(theta); ...
		m*L*cos(theta),		I + m*L^3	];

	H = [	m*g*L*sin(theta); ...
		F + m*L*thetadot^2*sin( theta ) - k*ydot ];

	accelerations = (1/delta)*( G*H );
	
	dxdt(1) = thetadot;
	dxdt(2) = accelerations(1);
	dxdt(3) = ydot;
	dxdt(4) = accelerations(2);
	dxdt(5) = 0; % because we don't move the control input
	
	dxdt = transpose( dxdt );

end

	
