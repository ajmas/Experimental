package ajmas74.experimental;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;

import java.nio.ByteBuffer;

import com.apple.cocoa.application.*;
import com.apple.cocoa.foundation.NSData;
import com.apple.cocoa.foundation.NSSize;

public class DockIcon {

	static boolean cocoaJavaAPI = hasCocoaJavaAPI();

	static boolean hasCocoaJavaAPI() {
		try {
			DockIcon.class.forName("com.apple.cocoa.application.NSImage");
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	static void drawDockIcon(byte[] pixelBytes, int width, int height) {

		NSImage image = new NSImage();

		NSBitmapImageRep rep = new NSBitmapImageRep(width, height, 8, 4, true,
				false, NSGraphics.DeviceRGBColorSpace, 0, 0); // 8bpp,
																// 3bytes/pixel,
																// no alpha
		rep.setBitmapData(pixelBytes);

		image.addRepresentation(rep);
		NSApplication.sharedApplication().setApplicationIconImage(image);
	}

	NSImage image;

	public static void main(String[] args) {

		Frame f = new Frame();
		f.setBounds(50, 50, 400, 300);
		f.setBackground(new Color(0, 0, 0, 0));
		f.setLayout(null);
		Button b = new Button("Hello");
		b.setBounds(50, 50, 100, 50);
		f.add(b);
		f.setVisible(true);

		byte[] pixelBytes = new byte[128 * 128 * 4];
		ByteBuffer pixelBuffer = ByteBuffer.wrap(pixelBytes);

		Color bgColor = Color.YELLOW;
		byte[] colorBytes = new byte[4];
		colorBytes[0] = (byte) bgColor.getRed();
		colorBytes[1] = (byte) bgColor.getGreen();
		colorBytes[2] = (byte) bgColor.getBlue();
		colorBytes[3] = (byte) 255;

		// Set the background color. We only need to
		// do it once, since we will only be drawing
		// the pixels that are changed when copying
		// the frames
		for (int i = 0; i < pixelBytes.length - 4; i += 4) {
			pixelBuffer.put(colorBytes);
		}
		pixelBuffer.rewind();

		int width = 128;
		int height = 128;

		System.out.println(hasCocoaJavaAPI());
		drawDockIcon(pixelBytes, width, height);

		while (true) {
			Thread.yield();
		}
	}
}
