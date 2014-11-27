clear all; close all;
addpath(genpath('..'));
tic

syms x1 x2;
X = [x1; x2];

f = @(X) [ X(2);
	-sin( X(1) ) - X(2) ];
        
%desired highest degree for the template monomials
degree = 2; 

% Region of interest
Xlower = -1*ones(size(X));
Xupper = 1*ones(size(X));
% Exclusion zone
exclusionRadius = 0.01;

% To how many decimal places should coefficients be computed?
precision = 1;

% Number of initial samples
samplenumber = 1; 
% Max number of iterations
maxouteriterations = 150;

[success, lyapunov] = lyapunovgenerator( X, f, degree, Xlower, Xupper, exclusionRadius, precision, samplenumber, maxouteriterations)
                
toc;
