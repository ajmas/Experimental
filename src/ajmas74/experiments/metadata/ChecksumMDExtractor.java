package ajmas74.experiments.metadata;

import java.io.File;
import java.util.Map;

import ajmas74.experimental.ChecksumFile2;

public class ChecksumMDExtractor implements MetadataExtractor {

	@Override
	public String getName() {
		return "Checksum calculator";
	}

	@Override
	public String getDescription() {
		return "Caculates various checksums on the file";
	}

	@Override
	public String[] getSupportedFileExtensions() {
		return null;
	}

	@Override
	public String[] getSupportedMimeTypes() {
		return null;
	}

	@Override
	public void extractMetadata(File file, Map<String, Object> metadataMap)
			throws MetadataExtractorException {

		try {
			metadataMap.put("checksum.TTH",ChecksumFile2.getTTHChecksum(file) );
			metadataMap.put("checksum.MD5",ChecksumFile2.getMD5Checksum(file) );
			metadataMap.put("checksum.SHA",ChecksumFile2.getSHAChecksum(file) );
		} catch (Exception ex) {
			throw new MetadataExtractorException(ex);
		}

		

	}

}
