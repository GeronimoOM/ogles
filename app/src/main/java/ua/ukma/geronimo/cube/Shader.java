package ua.ukma.geronimo.cube;

import static android.opengl.GLES20.*;

public class Shader {
    private int handle;

    public Shader(String source, int type) {
        handle = glCreateShader(type);
        if (handle == 0) {
            throw new RuntimeException("glCreateShader failed");
        }

        glShaderSource(handle, source);

        glCompileShader(handle);

        int[] status = new int[1];
        glGetShaderiv(handle, GL_COMPILE_STATUS, status, 0);
        if (status[0] == 0) {
            String msg = "Could not compile shader:" + glGetShaderInfoLog(handle);
            glDeleteShader(handle);
            handle = 0;
            throw new RuntimeException(msg);
        }
    }

    public int getHandle() {
        return handle;
    }
}
