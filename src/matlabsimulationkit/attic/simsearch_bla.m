function simsearch()
% File history
% ============
% 
% >> J. Kapinski
% >> Toyota MBD
% >> 2-20
% Created as an example of Lyapunov Function Search Using Optimization13
% This script performs semidefinite programming operations to automatically
% compute Lyapunov candidates for a nonlinear sytem using only information
% from simulation traces.  The Lyapunov candidates will ideally be a true
% Lyapunov function. This is a modified version of Example 4.2 from Johannson's
% "Piecewise Linear Control Systems". This version has a switching surface 
% described by a transcendental function.
% Original Author:
%
% >> Nikos Arechiga
% >> 6/30/2014
% Modified to be part of the Manticore strategy engine


clc;
close all;
clear all;
% Load Manticore classpath
warning('off','YALMIP:strict')
addpath(genpath('/home/nikos/Programs/yalmip'));
javaaddpath .;

% Global variable used to store the list of traces explored during
% optimization operations.
global linear_constraint_list;
global initial_conditions_list;
global search_radius;
global trace_length;
global step_size;
global single_step_flag;
global fixed_step_flag;
global dynamics;
global create_plots;

tic;

dynamics = @dynsys;

search_radius = 1;
trace_length = 50;
step_size = .02;
single_step_flag = false;
fixed_step_flag = true;
initial_conditions_list = [];
create_plots = false;


%% Generate initial Lyapunov candidate

sdpvar x1 x2;
sdpvar alph;
sdpindets = [x1 x2];
sdpvector = monolist([x1;x2], [2]);
% We want to leave off the constant term
sdpvector = sdpvector(2:3);
P = sdpvar(2);

x0 = search_radius*.5*[1;1];
% Generate one trace for initial optimization call
xtrace = generate_trace(x0,fixed_step_flag,single_step_flag,step_size,step_size*trace_length);
add_lin_constr_for_trace(xtrace,[],P,alph,sdpvector,sdpindets);

if create_plots

% Generate quiver plot and feasible set
% Plot search domain
points = perim_plot_2d_poly(['x1^2+x2^2-' num2str(search_radius^2)]);
close
h4 = plot(points(1,:),points(2,:),'k--'); set(h4,'LineWidth',4);
hq = make_quiverplot();

x1ax = -1.5:.01:1.5;

% hs = plot([-1.5 1.5],[0 0],'k'); 
hs = plot(x1ax,0.1*exp(x1ax)-0.1,'k');
plot([0 0],[-1.5 1.5],'k');
axis(search_radius*1.5*[-1 1 -1 1])

end

OPTIONS = optimset('diagnostics','on','display','iter','Algorithm','interior-point','TolFun',1e-1,'MaxIter',4);%,'ObjectiveLimit',0);%,'MaxIter',10,'MaxFunEvals',10);
OPTIONS = optimset('diagnostics','on','display','iter','TolFun',1e-1,'MaxIter',4);%,'ObjectiveLimit',0);%,'MaxIter',10,'MaxFunEvals',10);


% Initialize variables used for outer (LMI) loop
optim_cost = -1;
max_iter = 100;
iter = 1;
% Begin outer (LMI) loop that takes the results of the inner (global)
% optimization to compute updated Lyapunov candidates.
while optim_cost<0&&iter<=max_iter
    
    % Perform LMI optimization that creates a P matrix that satisfies the
    % conditions for every trace in the list of traces
    F = set(P-eye(2)*0.001>0); % + set(P-eye(9)<0);
    F = F + set(-100<P(:)<100) + set(alph>0);
    F = F + linear_constraint_list;
    diagnostic = solvesdp(F,[], sdpsettings('usex0',1)); % Using [] for cost seems to do a better, faster job.  Need to check answer.
    Pout = double(P);
    
    fprintf('\nAlpha: %f\n',double(alph));
    
    if strfind(diagnostic.info,'Successfully solved')
        fprintf('\nLyapunov candidate successfully updated.\n');
    else
        fprintf('\n\nProblem solving semidefinite program to update Lyapunov candidate...\n');
        error('Could not update Lyapunov candidate.');
    end
    
    if true
        
        %% Use convex, bounded search
        if true
            % Generate 'random' points to initialize global optimizer
            %
            % Initialize randomizer with a seed so that we can reproduce the behavior
            s = RandStream('mt19937ar','Seed',iter);
            % Scale random numbers so that they range from -1 to 1 for x1 and x2
            seed_for_optimizer = rand(s,2,1)*2*search_radius-search_radius;
            
            seed_for_optimizer_temp = [];
            while size(seed_for_optimizer_temp,2)<10
                if seed_for_optimizer'*seed_for_optimizer<=search_radius^2
                    seed_for_optimizer_temp = [seed_for_optimizer_temp seed_for_optimizer];
                end
                seed_for_optimizer = rand(s,2,1)*2*search_radius-search_radius;
            end
            
            seed_for_optimizer = seed_for_optimizer_temp;
            
        else
            % Initialize randomizer with a seed so that we can reproduce the behavior
            s = RandStream('mt19937ar','Seed',iter);
            % Scale random numbers so that they range from -1 to 1 for x1 and x2
            rand_angle_offset = 120*rand(s,1,1);
            % Generate points on a circle as a seed for the optimizer
            seed_for_optimizer = .9*search_radius*[cos([0:2]*(120+rand_angle_offset)*pi/180);sin([0:2]*(120+rand_angle_offset)*pi/180)];
        end
        
        % Initialize variable used to test for minimum over all optimizer 'seeds'
        group_min = inf;
        
        % Perform optimization for each initial value in the 'seed'
        for ind = 1:size(seed_for_optimizer,2)
            
            % Nelder-Mead optimizer
        [optim_X, optim_cost, EXITFLAG, output] = ...
        nmoptimize(@cost,seed_for_optimizer(:,ind), [], [],[], [],[],[], @search_constr_func, 'superstrict', OPTIONS, [] ,Pout,P,alph,sdpvector,sdpindets);
            
            % Convex, bounded optimizer
%            [optim_X,optim_cost,EXITFLAG,OUTPUT] = fmincon(@cost,seed_for_optimizer(:,ind),[],[],[],[],[],[],@search_constr_func,OPTIONS,Pout,P,alph,sdpvector,sdpindets);
            if optim_cost<group_min&&EXITFLAG~=-2
                group_min = optim_cost;
                group_optim_x = optim_X;
            end
        end
        
        optim_X = group_optim_x;
        optim_cost = group_min;
        
    elseif false
        
        %% Use unbounded global optimizer
        % Initial condition for global optimizer
        x0 = [2;0];
        
        % Global, unbounded optimizer
        [optim_X,optim_cost,EXITFLAG,OUTPUT] = fminsearch(@cost,x0,OPTIONS,Pout,sdpvector,sdpindets);
    else
        % Perform 'stupid' search that simply places many random points
        % down in the search region and picks the best one.
        % Generate 'random' points to initialize global optimizer
        %
        % Initialize randomizer with a seed so that we can reproduce the behavior
        s = RandStream('mt19937ar','Seed',iter);
        % Scale random numbers so that they range from -1 to 1 for x1 and x2
        seed_for_optimizer = rand(s,2,20)*2*search_radius-search_radius;
        
        % Initialize variable used to test for minimum over all optimizer 'seeds'
        group_min = inf;
        
        % Confine the initial seed for the optimization to a ball of
        % fixed size
        seed_for_optimizer_temp = [];
        for ind = 1:size(seed_for_optimizer,2)
            if seed_for_optimizer(:,ind)'*seed_for_optimizer(:,ind)<=search_radius^2
                thiscost = cost(seed_for_optimizer(:,ind),Pout,P,alph,sdpvector,sdpindets);
                if thiscost<group_min
                    group_min = thiscost;
                    optim_X = seed_for_optimizer(:,ind);
                end
            end
        optim_cost = group_min;
        end
    end
    
    fprintf('\nOptim X: [%f %f],  Optim Cost: %f\n',optim_X(1),optim_X(2),optim_cost);
    iter = iter + 1;
end

fprintf('\n\nMinimum cost (J) for most recent global optimization: %e\n',optim_cost);
fprintf('Point where minimum cost (J) occurs for most recent global optimization: %e\n',optim_X);
fprintf('Final P matrix:\n');
disp(Pout);

fprintf('\nTime to find Lyapunov candidate: %f\n',toc);
 tic;


%% Determine Validity of Lyapunov Candidate

% Resulting Lyapunov candidate
v = sdpvector'*Pout*sdpvector;
% Jacobian used to test validity of candidate
delV = jacobian(v,[x1 x2]);


sdpvar lambda1 lambda2;

% Derivative of Lyapunov function
A1 = [-.1 1;-10 -.1];
A2 = [-.1 10;-1 -.1];
%     if (x(1)*x(2)>=0)
%             out = A1*x;
%     else
%             out = A2*x;
%     end
%     
deriv1 = delV*A1*[x1;x2];
deriv2 = delV*A2*[x1;x2];
% '[-x(2)-(3/2)*x(1)^2-(1/2)*x(1)^3; 3*x(1)-x(2)]'

% Prove that the negative is positive definite
FF = set(sos(-deriv1-lambda1*(x1*x2)))+set(lambda1>0)+set(sos(-deriv2-lambda2*(-x1*x2)))+set(lambda2>0);

% Compute Gram matrix decomposition to prove that the polynomial is
% negative definite.
[sol,vv,Q] = solvesos(FF);


fprintf('\nTime to verify candidate validity: %f\n',toc);
fprintf('\nSeDuMi diagnostic info for test of Lyapunov function global validity: %s\n\n',sol.info);

tic;

% Check the eigenvalues of the Gram matrix
eigs(double(Q{1}))

V = sdpvector'*Pout*sdpvector;
if true % Solve a semidefinite program to determine maximum levelset size
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
    F = sos(V-lambda*(sdpindets*sdpindets'-search_radius^2)-gam) + set(lambda>0);
    [sol,v,Q] = solvesos(F,-gam);
    maxsize = double(gam);
else % Use the feasible points from the optimizations to determine the maximum levelset size
    % Note that this is not a good way to determine levelset size, since
    % the levelset obtained can contain regions of the state space that
    % were not explored by the optimzer.
    maxsize = 0;
    for ind = 1:size(initial_conditions_list,2)
        tempsize = replace(V,sdpindets,initial_conditions_list(:,ind)');
        if tempsize>maxsize
            maxsize = tempsize;
        end
    end
end

fprintf('\nTime to find candidate invariant set size: %f\n',toc);

fprintf('\nOptimized levelset size: %f\n',maxsize);

phi_char = sdisplay(V);
phi_char=phi_char{1}
phi_char = regexprep(phi_char,'x1','(x1)');
phi_char = regexprep(phi_char,'x2','(x2)');
% Put a '.' right before each isntance of '^'.  Then use the following code
% to plot the level set.
phi_char = regexprep(phi_char,'\^','.^');

if create_plots
% Plot levelset of Lyapunov function
points = perim_plot_2d_poly([phi_char ' - ' num2str(maxsize)]);
close
h3 = plot(points(1,:),points(2,:),'k-.'); set(h3,'LineWidth',4);

h5 = plot(100,100,'k.-'); h2 = plot(100,100,'k*');
legend([h5 h2 h4 h3],'Traces Explored by Optimizer','Initial Conditions','Search Region','Invariant Set');

xlabel('x_1');
ylabel('x_2');

set(findall(gcf,'type','text'),'fontSize',20,'fontWeight','bold');
   fontsize=15;
    set(findall(gcf,'type','text'),'fontSize',fontsize,'fontWeight','bold');
    set(findall(gcf,'type','axes'),'fontSize',fontsize,'fontWeight','bold');
    axis equal
end

end
%%-------------------------------------------------------------------------
function J = cost(x,Pfixed,Pindet,alph,sdpvector,sdpvarlist)

global linear_constraint_list
global step_size
global trace_length
global single_step_flag
global fixed_step_flag

% Disallow initial conditions that are too close to the equilibrium point.
% If this region is too small (or nonexistent), then the subsequent LMI may
% fail.
if x'*x<1e-5
    J = 0;
    return;
end

% Take input to cost, x, as initial condition and generate a trace.
xtrace = generate_trace(x,fixed_step_flag,single_step_flag,step_size,step_size*trace_length);

% Add linear constraints corresponding to the new trace
add_lin_constr_for_trace(xtrace,Pfixed,Pindet,alph,sdpvector,sdpvarlist);

% Use the initial condition and the last point of the trace to compute
% the cost function, which is
%
% J = z^T*P*z-z_hat^T*P*z_hat,
%
% where z_hat is the last point in the simulation.
%
temp_min = inf;
for time_ind = 1:size(xtrace,1)-1
    % Define initial and final state conditions
    Ja = replace(sdpvector'*Pfixed*sdpvector,sdpvarlist,xtrace(time_ind,:));
    Jb = replace(sdpvector'*Pfixed*sdpvector,sdpvarlist,xtrace(time_ind+1,:));
    J = Ja - Jb - double(alph)*xtrace(time_ind,:)*xtrace(time_ind,:)';
    if J<temp_min
        temp_min = J;
    end
end
J = temp_min;

end
%%-------------------------------------------------------------------------
function out = dynsys(t,x)

    A1 = [-.1 1;-10 -.1];
    A2 = [-.1 10;-1 -.1];
    
    
    if x(1)>=0&&x(2)>=0.1*exp(x(1))-0.1
        out = A1*x;
    elseif x(1)>=0&&x(2)<0.1*exp(x(1))-0.1
        out = A2*x;
    elseif x(1)<0&&x(2)>=0.1*exp(x(1))-0.1
        out = A2*x;
    else
        out = A1*x;
    end
    
%     if (x(1)*x(2)>=0)
%             out = A1*x;
%     else
%             out = A2*x;
%     end
end
%%-------------------------------------------------------------------------
function xout = generate_trace(x,FIXED_STEP_SOLVER_FLAG,ONE_STEP_FLAG,SAMPLE_PERIOD,FINAL_TIME)

% This function returns a trace of the system given an initial
% condition, a flag that determines whether a fixed step solver is
% used, a flag that determines whether a one step trace is produced,
% a solution sample period, and the final time for the solution.

global dynamics

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

xout = y;

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
h = quiver(x,y,dxdt_1,dxdt_2,2,'k');
axis([-5.5 5.5 -5.5 5.5])
xlabel('x1')
ylabel('x2')
title('Switched Mode Example')
end
%%-------------------------------------------------------------------------
function add_lin_constr_for_trace(xtrace,Pfixed,Pindet,alph,sdpvector,sdpindets)
% Take a system trace and add the corresponding linear constraints to the
% constraints list.  Neglect any pair of simulation points that corresponds
% to a redundant constraint.

global linear_constraint_list
global initial_conditions_list
global create_plots

% Only add trace if it strictly satisfies the constraint
[C,Ceq] = search_constr_func(xtrace(1,:)',Pfixed,Pindet,sdpvector,sdpindets);
if C>0
    return;
end

if create_plots


% Indicate that this trace has been added to the list of traces by plotting
% it in green
ht = plot(xtrace(:,1),xtrace(:,2),'k.-');
hold on;
ht = plot(xtrace(1,1),xtrace(1,2),'k*');
drawnow;

end


% Set SDP/LINPROG options
options = sdpsettings('solver','linprog','verbose',1);
% Interior point method seems to have numerical problems with the linear
% programs corresponding to these constraints.  Try the Simplex method.
options.linprog.Simplex='on';
options.linprog.LargeScale='off';

if false % Try performing linear program to determin whether new constraint is redundant
    
    for time_ind = 1:size(xtrace,1)-1
        % Define initial and final state conditions
        Ztemp = replace(sdpvector,sdpindets,xtrace(time_ind,:));
        Ztempnext = replace(sdpvector,sdpindets,xtrace(time_ind+1,:));
        new_constraint_lhs = Ztemp'*Pindet*Ztemp-Ztempnext'*Pindet*Ztempnext;
        % Test new linear constraint to determine whether it is redundant
        % First, make sure linear program is bounded (without affecting the
        % resulting test for redundancy).
        SDP_Temp = linear_constraint_list + set(new_constraint_lhs>-1);
        % Solve a linear program to determine if the new constraint is
        % redundant.
        diagnostic = solvesdp(SDP_Temp,new_constraint_lhs,options);
        % If the result of the above linear program yields a point in x such
        % that the left hand side of the > constraint is < 0, then the new
        % constraint is NOT redundant, so add it to the list of constraints.
        % Otherwise, neglect it.
        if double(new_constraint_lhs)<0
            linear_constraint_list = linear_constraint_list + set(new_constraint_lhs>0);
        else
            disp('Trace neglected');
        end
    end
    
else % Add new trace regardless of whether it is redundant
    for time_ind = 1:size(xtrace,1)-1
        % Define initial and final state conditions
        Ztemp = replace(sdpvector,sdpindets,xtrace(time_ind,:));
        Ztempnext = replace(sdpvector,sdpindets,xtrace(time_ind+1,:));
        new_constraint_lhs = Ztemp'*Pindet*Ztemp-Ztempnext'*Pindet*Ztempnext;
        linear_constraint_list = linear_constraint_list + set(new_constraint_lhs - alph*xtrace(time_ind,:)*xtrace(time_ind,:)'>0);
    end
        initial_conditions_list = [initial_conditions_list xtrace(1,:)'];
end

end
