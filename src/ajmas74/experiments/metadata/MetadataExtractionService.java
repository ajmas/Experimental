package ajmas74.experiments.metadata;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ajmas74.experiments.metadata.macosx.MdsExtractImageDetails;

public class MetadataExtractionService {

	List<MetadataExtractor> metadataExtractors = new ArrayList<MetadataExtractor>();
	
	public MetadataExtractionService () {
		init();
	}
	
	public void init() {
		metadataExtractors.add(new MdsExtractImageDetails());
	}
	
	public void extractMetaData ( File file, Map<String,Object> metadataMap ) {
		for ( MetadataExtractor metadataExtractor : metadataExtractors ) {
			try {
				metadataExtractor.extractMetadata(file, metadataMap);				
			} catch ( MetadataExtractorException ex ) {
				ex.printStackTrace();
			}
		}
	}
	
}
