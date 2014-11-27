clear all; close all;
addpath(genpath('..'));
tic

syms x1 x2;
X = [x1; x2];

mu = 1;
f = @(X) [ -X(2);
	-mu*(1-X(1)^2)*X(2) - X(1) ];
        
%desired highest degree for the template monomials
degree = 4; 

% Region of interest
Xlower = -0.9*ones(size(X));
Xupper = 0.9*ones(size(X));
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
