clear all; close all;
addpath(genpath('..'));
tic

syms x1; 
X = [x1];

f = @(X) [ X(1) ];

degree = 2; %desired highest degree for the template monomials

% Region of interest
Xlower = [-10];
Xupper = [10];
% Exclusion zone
exclusionRadius = 0.5;

% To how many decimal places should coefficients be computed?
precision = 1;
% Number of initial samples
samplenumber = 1; 
% Max number of iterations
maxouteriterations = 150;

[success, lyapunov] =lyapunovgenerator( X, f, degree, Xlower, Xupper, exclusionRadius, precision, samplenumber, maxouteriterations)
                
toc;
