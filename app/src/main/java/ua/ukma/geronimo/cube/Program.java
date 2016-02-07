package ua.ukma.geronimo.cube;

import android.util.Log;

import java.util.List;

import static android.opengl.GLES20.*;

public class Program {

    private int handle;

    public Program(List<Shader> shaders) {
        if (shaders.isEmpty())
            throw new IllegalArgumentException("No shaders were provided to create the program");

        handle = glCreateProgram();
        if (handle == 0)
            throw new RuntimeException("glCreateProgram failed");

        for (Shader shader : shaders)
            glAttachShader(handle, shader.getHandle());

        glLinkProgram(handle);

        for (Shader shader : shaders)
            glDetachShader(handle, shader.getHandle());

        int[] status = new int[1];
        glGetProgramiv(handle, GL_LINK_STATUS, status, 0);
        if (status[0] == GL_FALSE) {
            String msg = "Error compiling program: " + glGetProgramInfoLog(handle);
            glDeleteProgram(handle);
            handle = 0;
            throw new RuntimeException(msg);
        }
    }

    public int getHandle() {
        return handle;
    }

    public void use() {
        glUseProgram(handle);
    }

    public boolean isInUse() {
        int[] currentProgram = new int[1];
        glGetIntegerv(GL_CURRENT_PROGRAM, currentProgram, 0);
        return (currentProgram[0] == handle);
    }

    public void stopUsing() {
        assert(isInUse());
        glUseProgram(0);
    }

    public int attrib(String attribName) {
        int attrib = glGetAttribLocation(handle, attribName);
        if(attrib == -1)
            throw new RuntimeException("Program attribute not found");
        return attrib;
    }

    public int uniform(String uniformName) {
        int uniform = glGetUniformLocation(handle, uniformName);
        if(uniform == -1)
            throw new RuntimeException("Program uniform not found");
        return uniform;
    }

}
