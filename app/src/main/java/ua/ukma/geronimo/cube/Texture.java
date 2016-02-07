package ua.ukma.geronimo.cube;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.opengl.GLUtils;

import static android.opengl.GLES20.*;

public class Texture {
    private int handle;

    public Texture(Bitmap bitmap,  int minMagFiler, int wrapMode) {
        bitmap = flipVertically(bitmap);

        int[] textureHandle = new int[1];
        glGenTextures(1, textureHandle, 0);
        handle = textureHandle[0];
        if(handle == 0)
            throw new RuntimeException("glGenTextures failed");

        glBindTexture(GL_TEXTURE_2D, handle);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minMagFiler);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, minMagFiler);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapMode);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapMode);
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private Bitmap flipVertically(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.setScale(1, -1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    public Texture(Bitmap bitmap) {
        this(bitmap, GL_LINEAR, GL_CLAMP_TO_EDGE);
    }

    public int getHandle() {
        return handle;
    }

}
