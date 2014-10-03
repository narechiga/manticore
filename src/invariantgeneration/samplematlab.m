% A sample matlab file

syms x, y;
X = [x; y];

A = ;
P = lyap(A', eye(size(A)));

V = transpose(X)*P*X
