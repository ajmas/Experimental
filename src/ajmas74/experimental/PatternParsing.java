package ajmas74.experimental;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternParsing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String uri = "/live-playback-2.0/index_videos/tluda//blonde%20girl%20-%20airvideo.m3u8";

		Pattern pattern = Pattern
				.compile("/live-playback-2.0/index_([^\\.]+)\\.m3u8");
		Matcher matcher = pattern.matcher(uri);

		if (matcher.matches()) {
			String playbackId = matcher.group(1);
			System.out.println(playbackId);
		}
	}

}
