%==============================================================================================
% Automatically generate envelope-invariant pairs through sampling
%==============================================================================================

close all; clear all;
start = clock();

addpath(genpath('SOSTOOLS.300'));
%startdir = pwd;
%pathdir = '/home/nikos/Programs/mpt/';
%cd( pathdir );
%path( pathdef );
%cd( startdir );

%%==============================================================================================
%%% Inverted pendulum on moving cart, from Khalil 3rd Edition 2002, Ex. 1.15 p. 28
%syms x1 x2 x3 x4 u;
%X = [x1; x2; x3; x4 ];
%XU = [X; u];
%%----------------------------------------------------------------------------------------------
%f1 = @(x) x(2);
%f2 = @(x) -(147*sin(x(1)))/(5*((cos(x(1)))^2 - 6)) - (3*x(4)*cos(x(1)))/(20*((cos(x(1)))^2 - 6)) + (x(2)^2*cos(x(1))*sin(x(1)))/((cos(x(1)))^2 - 6)
%f3 = @(x) x(4);
%f4 = @(x) (3*x(4))/(10*((cos(x(1)))^2 - 6)) - (2*x(2)^2*sin(x(1)))/((cos(x(1)))^2 - 6) - (49*cos(x(1))*sin(x(1)))/(5*((cos(x(1)))^2 - 6))
%f = @(x) [f1(x); f2(x); f3(x); f4(x)];
%%----------------------------------------------------------------------------------------------
%g1 = @(x) 0;
%g2 = @(x) (cos(x(1)))/((cos(x(1)))^2 - 6);
%g3 = @(x) 0;
%g4 = @(x) -(2)/((cos(x(1)))^2 - 6);
%g = @(x) [g1(x); g2(x); g3(x); g4(x)];
%%----------------------------------------------------------------------------------------------
%Xlower = [ -10 ; -10 ; -10 ; -10 ];
%Xupper = [ 10; 10; 10; 10 ];
%usatlo = -1e6;
%usathi = 1e6;
%InitialSet =@(x) 1 - ( x(1)^2 + x(2)^2 + x(3)^2 + x(4)^2 ); % unit ball
%FailSet = @(x) 1 - ( (x(1)-2)^2 + (x(2)-2)^2 + (x(3)-2)^2 + (x(4)-2)^2 ); % ball of radius 1, centered at (3,3,3,3)
%%==============================================================================================

%==============================================================================================
% Simple nonlinear example -- Jyo's system
%----------------------------------------------------------------------------------------------
syms x1 x2 x3 u;
X = [x1; x2; x3 ];
XU = [X; u];
%----------------------------------------------------------------------------------------------
f1 = @(x) -x(1);
f2 = @(x) 2*x(1)*x(2) + sin( x(2) );
f3 = @(x) 2*x(2);
f = @(x) [f1(x); f2(x); f3(x)];
%----------------------------------------------------------------------------------------------
g1 = @(x) exp(2*x(2));
g2 = @(x) 0.5;
g3 = @(x) 0;
g = @(x) [g1(x); g2(x); g3(x)];
%----------------------------------------------------------------------------------------------
Xlower = [ -10 ; -10; -10 ];
Xupper = [ 10; 10; 10 ];
usatlo = -1e6;
usathi = 1e6;
InitialSet =@(x) 1 - ( x(1)^2 + x(2)^2 + x(3)^2);
FailSet = @(x) -25 + ( (x(1))^2 + (x(2))^2 + x(3)^2); 
%==============================================================================================

%%==============================================================================================
%% Simple nonlinear example -- first two states of oscillator with extra input
%%----------------------------------------------------------------------------------------------
%syms x1 x2 u;
%X = [x1; x2 ];
%XU = [X; u];
%%----------------------------------------------------------------------------------------------
%f1 = @(x) x(2);
%f2 = @(x) -(147*sin(x(1)))/(5*((cos(x(1)))^2 - 6)) - (3*0*cos(x(1)))/(20*((cos(x(1)))^2 - 6)) + (x(2)^2*cos(x(1))*sin(x(1)))/((cos(x(1)))^2 - 6);
%f = @(x) [f1(x); f2(x)];
%%----------------------------------------------------------------------------------------------
%g1 = @(x) 0;
%g2 = @(x) (cos(x(1)))/((cos(x(1)))^2 - 6);
%g = @(x) [g1(x); g2(x) ];
%%----------------------------------------------------------------------------------------------
%Xlower = [ -10 ; -10 ];
%Xupper = [ 10; 10 ];
%usatlo = -1e6;
%usathi = 1e6;
%InitialSet =@(x) 0.1 - ( x(1)^2 + x(2)^2 );
%FailSet = @(x) 1 - ( (x(1)-2)^2 + (x(2)-2)^2 ); % ball of radius 1, centered at (2,2)
%%==============================================================================================

%==============================================================================================
% Very trivial linear example
%----------------------------------------------------------------------------------------------
%syms x1 x2 x3 x4 u;
%X = [x1; x2; x3; x4];
%XU = [X; u];
%
%%InitialSet =@(x) 1 - (x(1)^2 + x(2)^2 ); % unit ball
%%FailSet = @(x) 1 - ((x(1)-7)^2 + (x(2)-7)^2 ); % ball of radius 1, centered at (3,3,3,3)
%InitialSet =@(x) 1 - ( x(1)^2 + x(2)^2 + x(3)^2 + x(4)^2 ); % unit ball
%FailSet = @(x) 1 - ( (x(1)-7)^2 + (x(2)-7)^2 + (x(3)-7)^2 + (x(4)-7)^2 ); % ball of radius 1, centered at (3,3,3,3)
%
%Xlower = [ -10 ; -10 ; -10 ; -10 ];
%Xupper = [ 10; 10; 10; 10 ];
%%----------------------------------------------------------------------------------------------
%%f1 = @(x) -x(1);
%%f2 = @(x) -x(2);
%%f = @(x) [f1(x); f2(x)];
%f1 = @(x) x(1);
%f2 = @(x) x(2);
%f3 = @(x) x(3);
%f4 = @(x) x(4);
%f = @(x) [f1(x); f2(x); f3(x); f4(x)  ];
%%----------------------------------------------------------------------------------------------
%%g1 = @(x) 0;
%%g2 = @(x) 1;
%%g = @(x) [g1(x); g2(x)];
%g1 = @(x) 1;
%g2 = @(x) 2;
%g3 = @(x) 3;
%g4 = @(x) 4;
%g = @(x) [g1(x); g2(x); g3(x); g4(x) ];
%%----------------------------------------------------------------------------------------------
%usatlo = -10;
%usathi = 10;
%==============================================================================================


%==============================================================================================
%% Generate a  candidate classifier using sampling

Z = monomials( X, [0:2] );

Xsamples = [];
Origin = zeros(length(X),1);

Xsamples = generateInitialSamples( X, Xlower, Xupper, 5 );

%%% initial points
%Xsamples = [ Xsamples; sampleAllOrthants( Origin, 0.2, 1, 20 )];
%%fail points
%%Xsamples = [ Xsamples; sampleAllOrthants( [2;2;2;2], 0, 1, 1 )];
%Xsamples = [ Xsamples; sampleAllOrthants( Origin, 5, 10, 20 )];
%%intermediate points
%Xsamples = [ Xsamples; sampleAllOrthants( Origin, 1, 5, 20 )];

% bound decision variables
A = []; b = [];
A = [eye( length(Z) ), zeros(length(Z),1); -eye( length(Z) ), zeros(length(Z),1)]; 
b = [1000*ones( length(Z) , 1); 1000*ones( length(Z) , 1)];
%A = [eye( length(Z) +1 ); -eye( length(Z)+1 ) ]; 
%b = [1e6*ones( length(Z)+1 , 1); -1e6*ones( length(Z)+1 , 1)];


[A, b] = appendSampleConstraints( A, b, X, Z, f, InitialSet, FailSet, Xsamples, 3 );
obj = [zeros(length(Z),1); 1];

%opts = optimset('LargeScale','off','Simplex','on');
opts = optimset('Algorithm','active-set');
P = linprog( obj, A, b,  [], [], [], [], [], opts )
P = vpa(round(vpa(100*P))/100);
P = P(1:size(P,1)-1);

B = vpa(transpose(P)*Z);


%==============================================================================================
%% Validate the candidate as a state classifier

%----------------------------------------------------------------------------------------------
% Part 1: Ensure candidate is positive over the fail set
%		this is done by trying (and hopefully failing) to find a state 
%		s.t. barrier is less than 0.001.
fail = {};
fail{end+1} = sprintf('%s < 0.001', char(B) ); 
fail{end+1} = sprintf('%s > 0', char(vpa(FailSet(X) )));
[failresult, failQueryID] = querySolver( fail, X, Xlower, Xupper )

%% This piece tries to improve the candidate
while ( strcmp( failresult, 'sat' ) )
	
	Pold = P;
	[A, b] = appendSampleConstraints( A, b, X, Z, f, InitialSet, FailSet, extractCEX( failQueryID ), 3 );

	P = linprog( obj, A, b );
	P = vpa(round(vpa(100*P))/100);                                                                                                
	P = P(1:size(P,1)-1);                                                                                                          
	dist = vpa(norm(P - Pold));
	fprintf('Coefficients moved by: %f\n', double(dist) );
	B = vpa(transpose(P)*Z);

	fail = {};
	fail{end + 1} = sprintf('%s < 0.1', char(B) ); 
	fail{end + 1} = sprintf('%s > 0', char(vpa(FailSet(X) )));
	[failresult, failQueryID] = querySolver( fail, X, Xlower, Xupper );
end

%----------------------------------------------------------------------------------------------
% Part 2: Ensure candidate is negative over the initial set
%		this is done by tryng (and hopefully failing) to find a state
%		s.t. barrier is greater than -0.001
init = {};
init{end + 1} = sprintf('%s > -0.001', char(B) );
init{end + 1} = sprintf('%s > 0', char(vpa(InitialSet(X)) ) );
[initresult, initQueryID] = querySolver( init, X, Xlower, Xupper )

%% This piece tries to imrpove the candidate
%while ( strcmp( initresult, 'sat' ) )
%	Pold = P;
%	[A, b] = appendSampleConstraints( A, b, X, Z, f, InitialSet, FailSet, extractCEX( initQueryID ), 3 );
%
%	P = linprog( obj, A, b );
%	P = vpa(round(vpa(100*P))/100);                                                                                                
%	P = P(1:size(P,1)-1);                                                                                                          
%	dist = vpa(norm(P - Pold));
%	fprintf('Coefficients moved by: %f\n', double(dist) );
%	B = vpa(transpose(P)*Z);
%
%	init = {};
%	init{end + 1} = sprintf('%s > -0.001', char(B) );
%	init{end + 1} = sprintf('%s > 0', char(vpa(InitialSet(X)) ) );
%	[initresult, initQueryID] = querySolver( init, X, Xlower, Xupper );
%end

%----------------------------------------------------------------------------------------------
fail = {};
fail{end+1} = sprintf('%s < 0.1', char(B) ); 
fail{end+1} = sprintf('%s > 0', char(vpa(FailSet(X) )));
% Print results
fprintf('Init Result = \n');
initresult

fprintf('Fail Result = \n');
failresult

if ( ~(strcmp( initresult, 'unsat' ) & strcmp( initresult, 'unsat' )) ) error('Candidate failed!'); else fprintf('Candidate validated successfully!\n');
end

%==============================================================================================
% Sample state space, solve for set of feasible control inputs (that satisfy condition 3)

%----------------------------------------------------------------------------------------------
% Use dReal to find some zeroish points
numSeeds = 50;
excludes = {};
forceField = 0.001;
constraints = { sprintf('%s = 0', char(vpa(B))) };

excludeConstraint = '';
for i = 1:numSeeds
	[zeroresult, querymodel] = querySolver( constraints, X, Xlower, Xupper)
	
	if ( strcmp( zeroresult, 'unsat' ) )
		warning('Covered space with spheres! No more zeros.')
		break;
	end

	xP(i,:) = extractCEX( querymodel );
	%xP(i,:) = round( xP(i,:)*10 )/10;

	excludeConstraint = sprintf('%s > %1.5f', char(vpa(transpose(X - transpose(xP(i,:)))*(X - transpose(xP(i,:))))),forceField);
	constraints{ end + 1 } = excludeConstraint;

end


%----------------------------------------------------------------------------------------------
% Use these points to generate constraints on the control
partialBpartialX = @(x) subs(jacobian( B, X ), X, x);
Beval = @(x) subs( B, X, x );


controlconstraint = [];
p = {};
ctt = {};
pBMinterval = {};
Problems = {};



uenvlo = [];
uenvhi = [];
envelope = {};
isVacuous = {};
isValid = {};
SEARCHSTATE = 0;
CONVERGESTATE = 1;
TERMINATE = 2;
for i = 1:size(xP,1)
	STATE = SEARCHSTATE;
	thisNumMiniBalls{i} = 3;
	upperMiniBalls = 10;
	lowerMiniBalls = 0;
	previousNumMiniBalls = -1; % for the special logic that avoids entrapment
	while ( STATE ~= TERMINATE )
	
		%---------------------------------------------------------------------------------------------------------------------------------------------------------
		% Miniballs
		miniBallRadius = sqrt( forceField );
		clear Aminiball; clear bminiball; clear xExtra;
		Aminiball = []; bminiball = [];
		s = RandStream('mt19937ar','Seed',1);
		for j = 1:thisNumMiniBalls{i}
			xExtra = xP(i,:) + 0.99*miniBallRadius*rand(s,1,length(xP(i,:))) + 0.01*miniBallRadius*ones(1,length(xP(i,:))); 
	
			Aminiball = [Aminiball; double(vpa(partialBpartialX( transpose(xExtra) )*g( transpose(xExtra) )))];
			%bminiball = [bminiball; double(vpa(-0.001/(1 + abs(Beval(transpose(xExtra)) ) ) - partialBpartialX( transpose(xExtra) )*f( transpose(xExtra) )))];
			bminiball = [bminiball; double(vpa(-0.001 + Beval(transpose(xExtra))^2 - partialBpartialX( transpose(xExtra) )*f( transpose(xExtra) )))];
		end
		%---------------------------------------------------------------------------------------------------------------------------------------------------------
	
		%---------------------------------------------------------------------------------------------------------------------------------------------------------
		% Solve linear problems to obtain upper and lower bounds on the control variable
		problemLower.f = [1];
		problemLower.Aineq = [Aminiball; double(vpa(partialBpartialX( transpose(xP(i,:)) )*g( transpose(xP(i,:)) )))];
		problemLower.bineq = [bminiball; double(vpa(-partialBpartialX( transpose(xP(i,:)) )*f( transpose(xP(i,:)) ) -0.01))];
		problemLower.Aeq = [];
		problemLower.beq = [];
		problemLower.lb = usatlo;
		problemLower.ub = usathi;
		problemLower.x0 = [0];
		problemLower.solver = 'linprog';
		problemLower.options = optimset('LargeScale','off','Simplex','on');
	
		problemUpper.f = [-1];
		problemUpper.Aineq = [Aminiball; double(vpa(partialBpartialX( transpose(xP(i,:)) )*g( transpose(xP(i,:)) )))];
		problemUpper.bineq = [bminiball; double(vpa(-partialBpartialX( transpose(xP(i,:)) )*f( transpose(xP(i,:)) ) -0.01))];
		problemUpper.Aeq = [];
		problemUpper.beq = [];
		problemUpper.lb = usatlo;
		problemUpper.ub = usathi;
		problemUpper.x0 = [0];
		problemUpper.solver = 'linprog';
		problemUpper.options = optimset('LargeScale','off','Simplex','on');
	
		uenvlo(i) = linprog( problemLower );
		uenvhi(i) = linprog( problemUpper );
		%---------------------------------------------------------------------------------------------------------------------------------------------------------
	
		%---------------------------------------------------------------------------------------------------------------------------------------------------------
		% Try to validate this control envelope
	
		% Generate the constraints that represent the control envelope
		constraints = {};
		constraints{end + 1} = sprintf('u >= %f', uenvlo(i) + 0.1);
		constraints{end + 1} = sprintf('u <= %f', uenvhi(i) - 0.1);
		constraints{end+1} = sprintf('(%s <= %f)', char(vpa( transpose(X- transpose(xP(i,:)))*(X- transpose(xP(i,:))) )), forceField );
		envelope{end+1} = constraints;
	
		% Do some cleaning to get rid of numeric gunk
		XUlower = [Xlower; usatlo];
		XUupper = [Xupper; usathi];
		[ Bc, Bt ] = coeffs( B );
		Bc = vpa(round( 100*vpa( Bc ) )/100);
		cleanB = Bc*transpose(Bt);
	
	
		% Can't do the same for derivative, not a polynomial :(
		D = jacobian( cleanB, X )*(f(X) + g(X)*u);
	
		% B(x) = 0 implies dBdx*(f(x) + g(x)*u) < 0
		% assert if x is within an 0.5 ball of (each xP(i,:)) then we do the corresponding control law.
		%---------------------------------------------------------------------------------------------------------------------------------------------------------
		% Check vacuity
		thisEnvelope = envelope{end};
		thisEnvelope(3) = []; % get rid of the constraint on state variables, since we are only concerned with feasiblity of u
		[checkVacuous, vacuousFile] = querySolver( thisEnvelope, u, usatlo, usathi );

		% Check validity
		thisQuery = {};
		thisQuery = envelope{end}; % note that we reload the envelope, since the one for feasibility discarded state constraints
		thisQuery{end + 1} = sprintf('%s = 0',char( cleanB ));
		thisQuery{end + 1} = sprintf('%s >= 0', char( D ));

		[validateresult, validatefile] = querySolver( thisQuery, XU, XUlower, XUupper);

		if ( strcmp( checkVacuous, 'sat' ) )
			isVacuous{i} = 0
		else
			isVacuous{i} = 1
		end
		if ( strcmp( validateresult, 'unsat' ) )
			isValid{i} = 1
		else
			isValid{i} = 0
		end
		thisNumMiniBalls

		% for testing
		%lowerMiniBalls = upperMiniBalls;

		if ( (isValid{i} == 0) && (isVacuous{i} == 0) && ( STATE == SEARCHSTATE ) ) % Then we need to enlarge the interval
			lowerMiniBalls = thisNumMiniBalls{i};
			upperMiniBalls = upperMiniBalls + 10;
			thisNumMiniBalls{i} = upperMiniBalls;
			fprintf('Interval Search: Enlarging the interval\n');

		elseif ( (isValid{i} == 1) && (isVacuous{i} == 1) ) % Then we need to back off and look at lower numbers
			lowerMiniBalls = lowerMiniBalls
			upperMiniBalls = thisNumMiniBalls{i};
			thisNumMiniBalls{i} = floor((upperMiniBalls + lowerMiniBalls)/2);
			fprintf('Interval Search: Shrinking the interval\n');

		elseif ( (isValid{i} == 0) && (isVacuous{i} == 0) && ( STATE == CONVERGESTATE ) ) % Then we need to just look at a higher number of miniball
			lowerMiniBalls = thisNumMiniBalls{i};
			upperMiniBalls = upperMiniBalls;
			thisNumMiniBalls{i} = floor((upperMiniBalls + lowerMiniBalls)/2);
			fprintf('Convergence: Looking for a larger number\n');

			if ( (lowerMiniBalls == upperMiniBalls) && ( lowerMiniBalls == previousNumMiniBalls ) && (previousNumMiniBalls == thisNumMiniBalls{i} ) ) % know when to quit
				fprintf('Failed to find an appropriate number of miniballs\n');
				STATE = TERMINATE;
			elseif ( previousNumMiniBalls == thisNumMiniBalls{i} ) % To prevent the case when it gets trapped because of the floor
			%if ( previousNumMiniBalls == thisNumMiniBalls{i} ) % To prevent the case when it gets trapped because of the floor
				lowerMiniBalls = upperMiniBalls; % this is pretty much your last chance!
				thisNumMiniBalls{i} = ceil((upperMiniBalls + lowerMiniBalls)/2);
			end
			previousNumMiniBalls = thisNumMiniBalls{i};

		elseif ( (isValid{i} == 1) && (isVacuous{i} == 0) ) % Then we just want to know the smallest one that will work

			if ( (lowerMiniBalls == upperMiniBalls) && (STATE == CONVERGESTATE) )
				fprintf('Successfully converged, terminating\n')
				STATE = TERMINATE;

			%elseif ( (upperMiniBalls - lowerMiniBalls == 1) && (STATE == CONVERGESTATE ) )
			%	fprintf('Successfully converged, terminating\n')
			%	STATE = TERMINATE;
			else
				lowerMiniBalls = lowerMiniBalls;
				upperMiniBalls = thisNumMiniBalls{i};
				thisNumMiniBalls{i} = floor((upperMiniBalls + lowerMiniBalls)/2);
				fprintf('Found a satisfying number of miniballs! Just reducing the number from now on\n');
				STATE = CONVERGESTATE;
			end

		else
			error(sprintf('Unexpected result! isVacuous: %i, isValid: %i', isVacuous{i}, isGood{i} ));
		end
			
	
		% try to add some robustness, because this isn't working :(
		%modPoints = [xP(1:i-1,:); xP(i+1:size(xP,1),:)]\n;
		%[nearestPoint,d] = dsearchn( modPoints, xP(i,:) )
		%ctt{end} = ctt{end} + set(partialBpartialX( modPoints(nearestPoint,:) )*f( modPoints(nearestPoint,:) ) + partialBpartialX( modPoints(nearestPoint,:) )*g( modPoints(nearestPoint,:) )* u <= -1);
		%modPoints = [modPoints(1:nearestPoint-1,:); modPoints(nearestPoint+1:size(modPoints,1),:)];
		%[nearestPoint,d] = dsearchn( modPoints, xP(i,:) )
		%ctt{end} = ctt{end} + set(partialBpartialX( modPoints(nearestPoint,:) )*f( modPoints(nearestPoint,:) ) + partialBpartialX( modPoints(nearestPoint,:) )*g( modPoints(nearestPoint,:) )* u <= -1);
	end

end

stop = clock();
fprintf('Total elapsed time was: %f\n', etime(stop, start));

%[validateresult, validatefile] = querySolver( negativityQuery, XU, XUlower, XUupper );

