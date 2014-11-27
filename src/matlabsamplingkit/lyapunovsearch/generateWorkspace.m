
function generateWorkspace()

	import java.lang.*;
	base = '/tmp/lyapunovsearch';

	addpath(genpath('SOSTOOLS.300'));
	% Set up the local workspace for this machine
	[~, myname] = system('hostname');
	myname = strtrim( myname ); % remove newline at end
	discard = system(sprintf('mkdir %s', base));
	discard = system(sprintf('mkdir %s/drealqueries/', base));
	discard = system(sprintf('mkdir %s/drealqueries/%s', base, myname));
	discard = system(sprintf('rm %s/drealqueries/%s/*', base, myname)); %clear it if it exists from a previous run
	% Clear logfile
	logfile = fopen( sprintf('%s/drealqueries/%s/logfile', base, myname), 'w' );
	fprintf( logfile, 'Initialized on %s\n', date );
	fclose(logfile);
end
