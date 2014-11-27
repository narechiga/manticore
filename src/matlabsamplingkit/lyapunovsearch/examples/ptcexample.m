clear all; close all;
addpath(genpath('..'));
tic

% state variables
syms p r pest ii;
X = [p; r; pest; ii];

% parameters
c1 = 0.41328;
c2 = 200;
c3 = -0.366;
c4 = 0.08979;
c5 = -0.0337;
c6 = 0.0001;
c7 = 2.821;
c8 = -0.05231;
c9 = 0.10299;
c10 = -0.00063;
c11 = 1.0;
c12 = 14.7;
c13 = 0.9;
c14 = 0.4;
c15 = 0.4;
c16 = 1;
u1hat = 23.0829;

% dynamics
%f = @([p; r; pest; i]) [ c1*(2*u1hat*sqrt( p/c11 - (p/c11)^2   ) - (c3 + c4*c2*p + c5*c2*p^2 + c6*c2^2*p ));
%	4*( (c3 + c4*c2*p + c5*c2*p^2 + c6*c2^2*p )/(c13*(c3 + c4*c2*pest^2 + c6*c2^2*pest)*(1 + i +c14*(r - c16) ) ) - r );
%	c1*(2*u1hat*sqrt( p/c11 - (p/c11)^2) - c13*(c3 + c4*c2*p + c5*c2*p^2 + c6*c2^2*p ));
%	c15*( r - c16) ];
f = @(X) [ c1*(2*u1hat*sqrt( X(1)/c11 - (X(1)/c11)^2   ) - (c3 + c4*c2*X(1) + c5*c2*X(1)^2 + c6*c2^2*X(1) ));
	4*( (c3 + c4*c2*X(1) + c5*c2*X(1)^2 + c6*c2^2*X(1) )/(c13*(c3 + c4*c2*X(3)^2 + c6*c2^2*X(3))*(1 + X(4) +c14*(X(2) - c16) ) ) - X(2) );
	c1*(2*u1hat*sqrt( X(1)/c11 - (X(1)/c11)^2) - c13*(c3 + c4*c2*X(1) + c5*c2*X(1)^2 + c6*c2^2*X(1) ));
	c15*( X(2) - c16) ];
        
%desired highest degree for the template monomials
degree = 4; 

% Region of interest
Xlower = -1*ones(size(X));
Xupper = 1*ones(size(X));
% Exclusion zone
exclusionRadius = 0.1;

% To how many decimal places should coefficients be computed?
precision = 2;

% Number of initial samples
samplenumber = 1; 
% Max number of iterations
maxouteriterations = 150;

[success, lyapunov] = lyapunovgenerator( X, f, degree, Xlower, Xupper, exclusionRadius, precision, samplenumber, maxouteriterations)
                
toc;
