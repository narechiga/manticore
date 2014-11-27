
sdpvar x1 x2;
sdpvar alph;
sdpindets = [x1 x2];
sdpvector = monolist([x1;x2], [3]);
% We want to leave off the constant term
sdpvector = sdpvector(2:3);

%dimension = length(sdpindets);
lengthX = length(sdpindets);
lengthZ = length(sdpvector);
P = sdpvar( lengthZ );

x0 = search_radius*.5*ones(lengthX,1);
