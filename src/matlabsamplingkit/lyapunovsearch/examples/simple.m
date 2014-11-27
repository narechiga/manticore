clear all; close all;
addpath(genpath('..'));
tic

syms x1 x2 x3; 
X = [x1; x2; x3];

f = @(X) [ -5*X(1) + X(2) + 3*X(3);
        3*X(1) - 13*X(2) - X(3);
        -3*X(1) + 3*X(2) - 7*X(3)];

degree = 2; %desired highest degree for the template monomials

% Region of interest
Xlower = [-10; -10; -10];
Xupper = [10; 10; 10];
% Exclusion zone
exclusionRadius = 0.1;

% To how many decimal places should coefficients be computed?
precision = 2;
% Number of initial samples
samplenumber = 1; 
% Max number of iterations
maxouteriterations = 150;

[success, lyapunov] =lyapunovgenerator( X, f, degree, Xlower, Xupper, exclusionRadius, precision, samplenumber, maxouteriterations)
                
toc;
