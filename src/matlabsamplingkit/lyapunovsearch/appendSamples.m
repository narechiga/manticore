
function extendedSamples = appendSample( sampleList, newSample )
	fprintf('Appending sample...\n');
	newSample
	LOG('Appending sample...');
	extendedSamples = [sampleList; newSample];
end
