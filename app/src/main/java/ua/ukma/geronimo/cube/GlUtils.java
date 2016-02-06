package ua.ukma.geronimo.cube;

import android.opengl.GLES20;
import android.util.Log;

public class GlUtils {

    private static String TAG = GlUtils.class.getName();

    private GlUtils() {}

    public static int compileShader(int type, int shaderId){
        int shader = GLES20.glCreateShader(type);
        String source = ResourceLoader.readRaw(shaderId);
        if (shader != 0) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                Log.e(TAG, "Could not compile shader " + type + ":");
                Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    public static int createAndLinkProgram(final int vertexShaderHandle,
                                           final int fragmentShaderHandle/*,
                                           final String[] attributes*/) {
        int programHandle = GLES20.glCreateProgram();
        if (programHandle != 0) {
            GLES20.glAttachShader(programHandle, vertexShaderHandle);
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);
            /*if (attributes != null) {
                for (int i = 0; i < attributes.length; i++) {
                    GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
                }
            }*/
            GLES20.glLinkProgram(programHandle);

            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            if (linkStatus[0] == 0) {
                Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }
        if (programHandle == 0) {
            throw new RuntimeException("Error creating program.");
        }

        return programHandle;
    }


}
