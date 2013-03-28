package ajmas74.experimental.metadata.macosx;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import ajmas74.experimental.MdlsParser;
import ajmas74.experimental.metadata.MetadataExtractor;
import ajmas74.experimental.metadata.MetadataExtractorException;

public class MdsExtractImageDetails implements MetadataExtractor {

	private static final String NAME = "MacOS X MDS Extract Image Details";
	private static final String[] SUPPORTED_FILE_EXTENSIONS = new String[] {
		"jpg", "jpeg", "png", "bmp", "gif"
	};
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return "Uses the MacOS X metadata from spotlight for the source of " +
			   " details on the image. This avoids needing to do extra work";
	}

	@Override
	public String[] getSupportedFileExtensions() {
		return SUPPORTED_FILE_EXTENSIONS;
	}

	@Override
	public String[] getSupportedMimeTypes() {
		return null;
	}

	@Override
	public void extractMetadata(File file, Map<String, Object> metadata) throws MetadataExtractorException {
		try {
			Map<String,Object> mdlsMetadata = MdlsParser.getMetaData(file);
			
			addIfNotNull("kMDItemPixelHeight","ItemPixelHeight", mdlsMetadata, metadata );
			addIfNotNull("kMDItemPixelWidth","ItemPixelWidth", mdlsMetadata, metadata );
			addIfNotNull("kMDItemResolutionHeightDPI","ItemResolutionHeightDPI", mdlsMetadata, metadata );
			addIfNotNull("kMDItemResolutionWidthDPI","ItemResolutionWidthDPI", mdlsMetadata, metadata );
			addIfNotNull("kMDItemPixelCount","ItemPixelCount", mdlsMetadata, metadata );
			addIfNotNull("kMDItemProfileName","ItemProfileName", mdlsMetadata, metadata );
			addIfNotNull("kMDItemBitsPerSample","ItemBitsPerSample", mdlsMetadata, metadata );
			
		} catch (IOException ex) {
			throw new MetadataExtractorException(ex);
		}

	}
	
	private void addIfNotNull  ( String srcKey, String destKey, Map<String, Object> sourceMap, Map<String, Object> destMap ) {
		Object value = sourceMap.get(srcKey);
		if ( value != null ) {
			destMap.put(destKey, value);
		}
	}

}
