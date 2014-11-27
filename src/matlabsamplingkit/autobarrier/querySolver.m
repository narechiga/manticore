

% Queries dReal about a logical formula, as long as it is the sort of
% formula that dReal can think about, i.e. no quantifiers and all that

function [result,queryModelFilename] = querySolver( formulas, X, Xlower, Xupper ) 
% Types are cell array of String, cell array of strings of vars (array of syms should also work), array of double, array of double

	start = clock();

	[~,mypath] = system( 'pwd' );
	mypath = strtrim( mypath );
	[~, myname] = system( 'hostname' );
	myname = strtrim( myname );
	queryID = round( 1000000*rand(1) );

	mydir = sprintf( '%s/drealqueries/%s/folder.%f.%i', mypath, myname, now, queryID );
	mkdir( sprintf( '%s', mydir) );

	queryFile = sprintf( '%s/query.%d.smt2', mydir, queryID );
	queryResult = sprintf( '%s/query.%d.result', mydir, queryID );
	queryModelFilename = sprintf( '%s/query.%d.smt2.model', mydir, queryID )
	q = fopen( queryFile, 'w+' );

	
	% Header
	fprintf(q, '(set-logic QF_NRA)\n\n' );
	
	% Declare vars
	for i = 1:length(X)
		fprintf( q, '(declare-fun %s () Real)\n', char(X(i)) );
	end
	fprintf( q, '\n' );
	
	% Declare region of interest
	for i = 1:length(X)
		eval( sprintf('this_lowerbound = Xlower(%i);', i) );
		eval( sprintf('this_upperbound = Xupper(%i);', i) );
	
		fprintf( q, '(assert (<= %f %s))\n', this_lowerbound, char(X(i)) );
		fprintf( q, '(assert (>= %f %s))\n', this_upperbound, char(X(i)) );
	end
	fprintf( q, '\n' );
	
	% Generate dReal query
	for i = 1:length( formulas )
		infile = sprintf('%s/in.%d.%d',  mydir, i, queryID);
		outfile = sprintf('%s/out.%d.%d',mydir, i, queryID);
		
		ifx2pfxIN = fopen( infile, 'w+' );
		fprintf( ifx2pfxIN, formulas{i} );
		fclose( ifx2pfxIN );

		system( sprintf('cd %s/infix2prefix; java Infix2Prefix %s > %s', mypath, infile, outfile));
		ifx2pfxOUT = fopen( outfile, 'r' );
		fprintf( q, ';; Formula is (%s)\n', formulas{i} );
		fprintf( q, '(assert %s )\n\n', fgetl( ifx2pfxOUT) );
		fclose( ifx2pfxOUT );
	end
	
	% End of file
	fprintf( q, '(check-sat)\n' );
	fprintf( q, '(exit)\n\n' );
	fclose( q );

	% Now run dReal --- NOTE that dReal needs to be in your path
	system(sprintf('cd %s; dReal --model --precision=0.000001 %s > %s', mydir, queryFile, queryResult));
	res = fopen(queryResult, 'r');
	result = fgetl( res );
	fclose(res); 
	
	stop = clock();
	fprintf(sprintf('Querying dReal took %i time units\n', etime(stop, start))); 
	
end
