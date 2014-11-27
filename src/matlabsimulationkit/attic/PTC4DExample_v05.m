function PTC4DExample_v05()
%% Example of Discrete-time Lyapunov Function Search Using Optimization - A/F Control Example
% This script performs semidefinite programming operations to automatically
% compute Lyapunov candidates for a nonlinear sytem using only information
% from simulation traces.  The Lyapunov candidates will ideally be a true
% Lyapunov function.
%
% This example creates a candidate Lyapunov function and a corresponding
% invariant set for a system that represents an Air/Fuel (A/F) controller 
% automotive system.
%
% The YALMIP semidefinite programming (SDP) solving environment is used to
% perform the SoS procedure.
%
%
% J. Kapinski
% Toyota MBD
% 9-2013

%% Normalized Pendulum Model
clc;
close all;
clear all;

% Global variable used to store the list of traces explored during
% optimization operations.
global linear_constraint_list;
global initial_conditions_list;
global search_radius;
global trace_length;
global step_size;
global single_step_flag;
global fixed_step_flag;
global discrete_time_flag;
global dynamics;
global create_plots;
global only_linear_constraints;
global only_add_failing_constraints;
global system_parameters;
global dim;
global simpointcount;

simpointcount = 0;

%% Perform search to obtain candidate Lyapunov function

dynamics = @dyn_func;

search_radius = 0.1;
trace_length = 10;
step_size = .1;
single_step_flag = false;
fixed_step_flag = false;
discrete_time_flag = false;
initial_conditions_list = [];
create_plots = true;
only_linear_constraints = false;
only_add_failing_constraints = true;
dim = 4;

if create_plots
    
    h = figure;
    set(h,'Position',[1000 600 800 800])
    hold on;
    grid on;
    
    for ind = 1:dim
        subplot(dim,1,ind);
        title(['State x' num2str(ind)])
        xlabel('Time (sec.)'); ylabel(['x' num2str(ind)]);
        hold on;
    end
    
    fontsize=15;
    set(findall(gcf,'type','text'),'fontSize',fontsize,'fontWeight','bold');
    set(findall(gcf,'type','axes'),'fontSize',fontsize,'fontWeight','bold');
 
end




% % Compute the equilibrium point
% %
% % The following point was found using Mathematica
% Equilibrium_Point = [ 0.988733871910758   1.000009438665233   1.220764191080577  -0.005929701497498]';  
% % Minimize the magnitude of the vector field.  This is ideally an
% % equilibrium point (if the optimal cost is near zero).
% OPTIONS = optimset('diagnostics','on','display','iter','Algorithm','interior-point','TolFun',1e-40,'TolX',1e-40,'TolCon',10e-20);  
% fx_inline=@(x)(dynamics(0,x)'*dynamics(0,x));
% [optim_X,optim_cost,EXITFLAG,OUTPUT] = fmincon(fx_inline,Equilibrium_Point,[],[],[],[],[-10;-10;-10],[10;10;10],[],OPTIONS);
% Equilibrium_Point = optim_X;

tic;

%% Generate initial Lyapunov candidate

sdpvar x1 x2 x3 x4;
sdpvar alph;
sdpindets = [x1 x2 x3 x4];
sdpvector = monolist([x1;x2;x3;x4], [2]);
% We want to leave off the constant term
sdpvector = sdpvector(2:15);
P = sdpvar(lengthZ);

x0 = search_radius*.4*ones(dim,1);
% Generate one trace for initial optimization call
[time,xtrace] = generate_trace(x0,fixed_step_flag,single_step_flag,step_size,step_size*trace_length,discrete_time_flag);
add_lin_constr_for_trace(time,xtrace,[1:length(time)-1],[],P,alph,sdpvector,sdpindets);

OPTIONS = optimset('diagnostics','on','display','iter','Algorithm','interior-point','TolFun',1e-1,'MaxIter',10);%,'ObjectiveLimit',0);%,'MaxIter',10,'MaxFunEvals',10);

% Initialize variables used for outer (LMI) loop
optim_cost = -1;
max_iter = 100;
iter = 1;

% Begin outer (LMI) loop that takes the results of the inner (global)
% optimization to compute updated Lyapunov candidates.
while optim_cost<0&&iter<=max_iter
    
    % Perform LMI optimization that creates a P matrix that satisfies the
    % conditions for every trace in the list of traces
    if ~only_linear_constraints
        F = set(P-eye(lengthZ)*0.001>0); % + set(P-eye(9)<0);
        F = F + set(-100<P(:)<100) + set(alph>0);
    else
        F = set(-1e2<P(:)<1e2) + set(alph>1e-4);
    end
    F = F + linear_constraint_list;
%     diagnostic = solvesdp(F,sum(abs(P)), SDPSETTINGS('usex0',1)); % Using [] for cost seems to do a better, faster job.  Need to check answer.
      diagnostic = solvesdp(F,[], SDPSETTINGS('usex0',1)); % Using [] for cost seems to do a better, faster job.  Need to check answer.

    Pout = double(P);
    
    % If we are trying to use only LP (and not SDP), then check wheather
    % the resulting P matrix is actually positive definite. If not, then
    % resolve the problem using the explicit LMI constraint P>0. 
    if only_linear_constraints&&~all(eigs(Pout)<0)
        fprintf('\nForcing LMI solver.\n');
        F = F + set(P>0);
        diagnostic = solvesdp(F,[], SDPSETTINGS('usex0',1)); % Using [] for cost seems to do a better, faster job.  Need to check answer.
        Pout = double(P);
        if ~strfind(diagnostic.info,'Successfully solved')
            error('Could not update Lyapunov candidate.');
        end
    end
    
    fprintf('\nAlpha: %f\n',double(alph));
    
    if strfind(diagnostic.info,'Successfully solved')
        fprintf('\nLyapunov candidate successfully updated.\n');
    else
        diagnostic = solvesdp(F,-alph, SDPSETTINGS('usex0',1)); % Using [] for cost seems to do a better, faster job.  Need to check answer.
        Pout = double(P);
        fprintf('\nAlpha: %f\n',double(alph));
        if strfind(diagnostic.info,'Successfully solved')
            fprintf('\nLyapunov candidate successfully updated.\n');
        else
            fprintf('\n\nProblem solving semidefinite program to update Lyapunov candidate...\n');
            error('Could not update Lyapunov candidate.');
        end
    end
    
    %% Use convex, bounded search
    
    % Generate 'random' points to initialize global optimizer
    %
    % Initialize randomizer with a seed so that we can reproduce the behavior
    s = RandStream('mt19937ar','Seed',iter);
    % Scale random numbers so that they range from -1 to 1 for x1 and x2
    seed_for_optimizer = rand(s,dim,1)*2*search_radius-search_radius;
    
    seed_for_optimizer_temp = [];
    while size(seed_for_optimizer_temp,2)<100 
        if seed_for_optimizer'*seed_for_optimizer<=search_radius^2
            seed_for_optimizer_temp = [seed_for_optimizer_temp seed_for_optimizer];
        end
        seed_for_optimizer = rand(s,dim,1)*2*search_radius-search_radius;
    end
    
    seed_for_optimizer = seed_for_optimizer_temp;
    
    % Project all of these seed points onto the surface of the levelset
    % from the previous solution.
    %   
    % NOTE: this works as-is for quadratic but will have to be modified for
    % more complex Lyapunov templates.
    %
%     for ind = 1:size(seed_for_optimizer,2)
%         seed_for_optimizer(:,ind) = seed_for_optimizer(:,ind)/sqrt(seed_for_optimizer(:,ind)'*Pout_previous*seed_for_optimizer(:,ind));
%     end
    
    
    % Include points inside of modes that are not normally excercised
%     for ind = 1:5
%         seed_for_optimizer = [seed_for_optimizer rand(s,dim,1).*(.1*xmodecentroidlist(:,33))];
%     end

    
    % Initialize variable used to test for minimum over all optimizer 'seeds'
    group_min = inf;
      
    
%--------------------------------------------------------------------------
% On the first iteration, also add to the seeds the counterexamples  
% obtained from the SMT solver from previous attempts to identify a sound 
% Lyapunov candidate.

if iter == 1
    
    % The following points represent counterexamples Jyo found
    % using dReal
    problempoints = [...
        [0.023546 ; 0 ; -0.017598 ; 0.010312 ]];
    
    seed_for_optimizer = [problempoints  seed_for_optimizer];
    
    fprintf('\nCounterexample seeds added.\nThe first %i seeds from this iteration are from counterexamples.\n',size(problempoints,2));
end
%--------------------------------------------------------------------------    
     
    
    % Perform optimization for each initial value in the 'seed'
    for ind = 1:size(seed_for_optimizer,2)
        
        % Nelder-Mead optimizer
        [optim_X, optim_cost, EXITFLAG, output] = ...
            optimize(@cost,seed_for_optimizer(:,ind), [], [],[], [],[],[], @search_constr_func, 'superstrict', OPTIONS, 'fminsearch' ,Pout,P,alph,sdpvector,sdpindets); % Use neldermead or fminsearch
        
        % Convex, bounded optimizer
        %           [optim_X,optim_cost,EXITFLAG,OUTPUT] = fmincon(@cost,seed_for_optimizer(:,ind),[],[],[],[],[-.1;-.1],[.1;.1],@search_constr_func,OPTIONS,Pout,P,alph,sdpvector,sdpindets);
        if optim_cost<group_min&&EXITFLAG~=-2
            group_min = optim_cost;
            group_optim_x = optim_X;
        end
        fprintf('\nIteration: %i, Seed number: %i',iter,ind);
        fprintf('\nCurrent minimum cost: %i\n',group_min);
        if group_min<0
            break
        end
    end
    
    optim_X = group_optim_x;
    optim_cost = group_min;
    
    
    
    fprintf('\nOptim X: [%f %f],  Optim Cost: %f\n',optim_X(1),optim_X(2),optim_cost);
    iter = iter + 1;
end


if create_plots 
%     h1=plot(100,100,'ks');
%     h2=plot(100,100,'ko');
%     legend([h1 h2],'Initial Condition','Final State');
%     axis([-.1 .1 -.1 .1])
end

LyapTime = toc;

fprintf('\nTime to find Lyapunov candidate: %f\n',LyapTime);
fprintf('\nNumber of iterations: %i\n',iter);

fprintf('\n\nMinimum cost (J) for most recent global optimization: %e\n',optim_cost);
fprintf('Point where minimum cost (J) occurs for most recent global optimization: %e\n',optim_X);
fprintf('Final P matrix:\n');
disp(Pout);

tic;

% Resulting Lyapunov candidate
v = sdpvector'*Pout*sdpvector;
% Solve a semidefinite program to determine maximum levelset size

% Compute maximum sized levelset such that it is inscribed in the circle
% (which is the search domain for this problem).
sdpvar gam lambda;
%
% Lambda is the S-procedure variable and gamma is used to turn the
% nonconvex optimization problem into a convex relaxation. This is due to
% results by Shor, as reported in
%
% N. Z. Shor. Class of global minimum bounds of polynomial functions. Cybernetics,
% 23(6):731{734, 1987. (Russian orig.: Kibernetika, No. 6, (1987), 9{11).
%
% and
%
% L. Vandenberghe and S. Boyd. Semidefinite programming. SIAM Review,
% 38(1):49{95, Mar. 1996.
%
F = sos(v-lambda*(sdpindets*sdpindets'-search_radius^2)-gam) + set(lambda>0);
[sol,vv,Q] = solvesos(F,-gam);
maxsize = double(gam);

LevelSize = toc;

fprintf('\nTime to find levelset size: %f\n',LevelSize);

fprintf('Final levelset size:\n');
disp(maxsize);

save([ mfilename '_' datestr(now,30)]);

end
%%-------------------------------------------------------------------------
function out = dyn_func(t,x)

% Better equilibrium. Obtained numerically
% x_equilibrium = [ 0.988733871910758   1.000009438665233   1.220764191080577  -0.005929701497498]';

x_equilibrium = [  0.89877559179912198425437882605416;...
    1;...
 1.0779564350547793288065542452819;...
 -0.000000000000000026901249610632059296110104185358];


% x_equilibrium = zeros(4,1);

% Equilibrium we originally uesed (not accurate but converges to answer)
% x_equilibrium = [ 0.898775584353738  ; 0.999999788689746 ;  0.111111423310533 ];
x = x + x_equilibrium;

% System constants
c1=0.41328;
c2=200;         % Engine speed (rad/sec)
c3=-0.366;      % Coefficient for Pumping polynomial
c4=0.08979;     % Coefficient for Pumping polynomial
c5=-0.0337;     % Coefficient for Pumping polynomial
c6=0.0001;      % Coefficient for Pumping polynomial
c7=2.821;       % Coefficient for f(theta) polynomial
c8=-0.05231;    % Coefficient for f(theta) polynomial
c9=0.10299;     % Coefficient for f(theta) polynomial
c10=-0.00063;   % Coefficient for f(theta) polynomial
c11=1.0;        % Atmospheric pressure (bar)
c12=14.7;       % Stoichiometric ration of air-to-fuel
c13=0.9;        % Factor representing error between estimated and actual manifold pressure
c14=0.4;        % Proportional gain for PI controller
c15=0.4;        % Integral gain for PI controller
c16=1.0;        % lambda reference (desired A/F to stoichiometric ratio
c17=0.94513;    % Coefficient for $A/F$ polynomial
c18=-2.3981;    % Coefficient for $A/F$ polynomial
c19=1.4106;     % Coefficient for $A/F$ polynomial
c20=0.17882;    % Coefficient for $A/F$ polynomial
c21=-0.10835;   % Coefficient for $A/F$ polynomial
c22=-2.3421;    % Coefficient for square root polynomial
c23=2.7799;     % Coefficient for square root polynomial
c24=-0.3273;    % Coefficient for square root polynomial
u1 = 15.0;      % Throttle angle 15 or 25?

u1_hat = c7+c8*u1+c9*u1^2+c10*u1^3;

% Polynomial version
% out = [ c1*(2*u1_hat*(c22*x(1)^2+c23*x(1)+c24) - (c3+c4*c2*x(1)+c5*c2*x(1)^2+c6*x(1)*c2^2)) ;...
%     4*( c17+c18*((c13/c12)*(c3+c4*c2*x(3)+c5*c2*x(3)^2+c6*x(3)*c2^2)*(1+x(4)+c14*(x(2)-c16)))+c19*((c13/c12)*(c3+c4*c2*x(3)+c5*c2*x(3)^2+c6*x(3)*c2^2)*(1+x(4)+c14*(x(2)-c16)))^2+c20*(c3+c4*c2*x(1)+c5*c2*x(1)^2+c6*x(1)*c2^2)+c21*(c3+c4*c2*x(1)+c5*c2*x(1)^2+c6*x(1)*c2^2)*((c13/c12)*(c3+c4*c2*x(3)+c5*c2*x(3)^2+c6*x(3)*c2^2)*(1+x(4)+c14*(x(2)-c16))) - x(2) );...
%     c1*(2*u1_hat*(c22*x(1)^2+c23*x(1)+c24) - c13*(c3+c4*c2*x(3)+c5*c2*x(3)^2+c6*x(3)*c2^2));...
%     c15*(x(2)-c16) ];

out = [ c1*(2*u1_hat*sqrt((x(1)/c11)-(x(1)/c11)^2) - (c3 + c4*c2*x(1) + c5*c2*x(1)^2 + c6*x(1)*c2^2)); ...
          4*((c3 + c4*c2*x(1) + c5*c2*x(1)^2 + c6*x(1)*c2^2)/(c13*(c3 + c4*c2*x(3) + c5*c2*x(3)^2 + c6*x(3)*c2^2)*(1 + x(4) + c14*(x(2) - c16))) - x(2)); ...
          c1*(2*u1_hat*sqrt((x(1)/c11)-(x(1)/c11)^2) - c13*(c3 + c4*c2*x(3) + c5*c2*x(3)^2 + c6*c2^2*x(3)));...
          c15*(x(2) - c16)];


if any(imag(out)~=0)
    error(['Error: cost function returned an imaginary number.']);
end

end
%%-------------------------------------------------------------------------
function J= verificationcost(x,P)

global dynamics

nextx = dynamics(0,x);

J = x'*P*x-nextx'*P*nextx;


end
%%-------------------------------------------------------------------------
function J = cost(x,Pfixed,Pindet,alph,sdpvector,sdpvarlist)

global linear_constraint_list
global step_size
global trace_length
global single_step_flag
global fixed_step_flag
global discrete_time_flag
global only_add_failing_constraints;



% Disallow initial conditions that are too close to the equilibrium point.
% If this region is too small (or nonexistent), then the subsequent LMI may
% fail.
if x'*x<1e-5
    J = 0;
    return;
end

% Take input to cost, x, as initial condition and generate a trace.
[t,xtrace] = generate_trace(x,fixed_step_flag,single_step_flag,step_size,step_size*trace_length,discrete_time_flag);

% add_lin_constr_for_trace(xtrace,Pfixed,Pindet,alph,sdpvector,sdpvarlist);


% Use the initial condition and the last point of the trace to compute
% the cost function, which is
%
% J = z^T*P*z-z_hat^T*P*z_hat,
%
% where z_hat is the last point in the simulation.
%
failing_initial_conditions = [];
temp_min = inf;
inner_prod = sdpvector'*Pfixed*sdpvector;
for time_ind = 1:size(xtrace,1)-1
    % Define initial and final state conditions
    Ja = replace(inner_prod,sdpvarlist,xtrace(time_ind,:));
    Jb = replace(inner_prod,sdpvarlist,xtrace(time_ind+1,:));
    J = Ja - Jb - double(alph)*xtrace(time_ind,:)*xtrace(time_ind,:)';
    if J<temp_min
        temp_min = J;
    end
    if J<0
        failing_initial_conditions = [failing_initial_conditions time_ind];
    end
end
J = temp_min;

% % Only add constraints corresponding to traces that produce a negative cost
if ~only_add_failing_constraints;
    add_lin_constr_for_trace(t,xtrace,[1:size(xtrace,1)-1],Pfixed,Pindet,alph,sdpvector,sdpvarlist);
elseif J<0
    add_lin_constr_for_trace(t,xtrace,failing_initial_conditions,Pfixed,Pindet,alph,sdpvector,sdpvarlist);
end


end
%%-------------------------------------------------------------------------
function [t,xout] = generate_trace(x,FIXED_STEP_SOLVER_FLAG,ONE_STEP_FLAG,SAMPLE_PERIOD,FINAL_TIME,DISCRETE_TIME)

% This function returns a trace of the system given an initial
% condition, a flag that determines whether a fixed step solver is
% used, a flag that determines whether a one step trace is produced,
% a solution sample period, and the final time for the solution.

global dynamics
global simpointcount

if ~DISCRETE_TIME
    
    if ONE_STEP_FLAG
        [t,y]=ode45(dynamics,[0 SAMPLE_PERIOD 2*SAMPLE_PERIOD],x);
        t=t(1:2); y=y(1:2,:);
    else
        if FIXED_STEP_SOLVER_FLAG
            [t,y]=ode45(dynamics,[0:SAMPLE_PERIOD:FINAL_TIME],x);
        else
            [t,y]=ode45(dynamics,[0 FINAL_TIME],x);
        end
    end
    
else
    
    if ONE_STEP_FLAG
        t = [0;1];
        y = [x';dynamics(t(1),x)'];
    else
        t = [0:1:FINAL_TIME]';
        y(1,:) = x';
        for ind = 2:length(t)
            y(ind,:) = dynamics(t(ind),x)';
            x = y(ind,:)';
        end
    end
end


xout = y;

simpointcount = simpointcount + size(xout,1);

% Plot generated trace
% ht = plot(y(:,1),y(:,2),'r.-');
% hold on;
% drawnow;

end
%%-------------------------------------------------------------------------
function [C,Ceq] = search_constr_func(x,Pfixed,Pindet,alph,sdpvector,sdpvarlist)
% Confine the search to a ball around the origin of a given radius
global search_radius;
C = x'*x - search_radius^2;
Ceq = [];
end
%%-------------------------------------------------------------------------
function h=make_quiverplot()

global dynamics

[x,y] = meshgrid(-5:.2:5,-5:.2:5);
for m=1:size(x,1)
    for n=1:size(x,2)
        grad=dynamics(0,[x(m,n);y(m,n)]);
        dxdt_1(m,n)=grad(1);
        dxdt_2(m,n)=grad(2);
    end
end
h=quiver(x,y,dxdt_1,dxdt_2,2,'k');
axis([-5.5 5.5 -5.5 5.5])
xlabel('x1')
ylabel('x2')
title('')
end
%%-------------------------------------------------------------------------
function add_lin_constr_for_trace(time,xtrace,failing_initial_conditions,Pfixed,Pindet,alph,sdpvector,sdpindets)
% Take a system trace and add the corresponding linear constraints to the
% constraints list.  Neglect any pair of simulation points that corresponds
% to a redundant constraint.

global linear_constraint_list
global initial_conditions_list
global create_plots
global only_linear_constraints
global dim

% Only add trace if it strictly satisfies the constraint
[C,Ceq] = search_constr_func(xtrace(1,:)',Pfixed,Pindet,sdpvector,sdpindets);
if C>0
    return;
end

if create_plots
    % Indicate that this trace has been added to the list of traces by plotting
    % it in green
    
    for ind = 1:dim
        subplot(dim,1,ind);
        ht = plot(time,xtrace(:,ind),'ks-','MarkerSize',2);
    end
    drawnow;
end

for time_ind = failing_initial_conditions
    % Define initial and final state conditions
    Ztemp = replace(sdpvector,sdpindets,xtrace(time_ind,:));
    Ztempnext = replace(sdpvector,sdpindets,xtrace(time_ind+1,:));
    new_constraint_lhs = Ztemp'*Pindet*Ztemp-Ztempnext'*Pindet*Ztempnext;
    linear_constraint_list = linear_constraint_list + set(new_constraint_lhs - alph*xtrace(time_ind,:)*xtrace(time_ind,:)'>0);
    if only_linear_constraints
        linear_constraint_list = linear_constraint_list + set(Ztemp'*Pindet*Ztemp > 0);
    end
end
initial_conditions_list = [initial_conditions_list xtrace(1,:)'];


end

%%-------------------------------------------------------------------------
function ellips = return_points_on_ellipsoid(center,P_inv)

% Matrix used for linear transformation
A = inv(sqrtm(P_inv));


angle = linspace(0,2*pi);
x_circ = cos(angle);
y_circ = sin(angle);

for i=1:length(angle)
    ellips(:,i) = A*[ x_circ(i) ; y_circ(i) ] + center;
end


end


