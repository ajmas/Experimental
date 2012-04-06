package ajmas74.experimental.opengl;

import java.nio.ByteBuffer;

public class Texture {
    private ByteBuffer pixels;

    private int width;

    private int height;

    public Texture(ByteBuffer pixels, int width, int height) {
        this.height = height;
        this.pixels = pixels;
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ByteBuffer getPixels() {
        return pixels;
    }

}
