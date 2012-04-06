package ajmas74.experiments.metadata;

import java.io.File;
import java.util.Map;

public interface MetadataExtractor {

	public String getName ();
	
	public String getDescription ();
	
	public String[] getSupportedFileExtensions();
	
	public String[] getSupportedMimeTypes();
	
	public void extractMetadata ( File file, Map<String, Object> metadataMap ) throws MetadataExtractorException;
}
