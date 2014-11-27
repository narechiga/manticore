
function LOG( stringtolog )
	[~, myname] = system('hostname');
	myname = strtrim( myname ); % remove newline at end

	logfile = fopen( sprintf('/tmp/lyapunovsearch/drealqueries/%s/logfile', myname), 'a' );
	if (logfile == -1)
		error('could not open logfile');
	end
	fprintf( logfile, '%s\n', stringtolog );
	fclose( logfile );

end
