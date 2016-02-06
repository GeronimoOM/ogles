package ua.ukma.geronimo.cube;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class LightPoint {

    private final int program;
    private int mvpMatrixhandle;
    private int positionHandle;

    private float[] position = new float[3];
    private float[] modelMatrix = new float[16];
    private float[] mvpMatrix = new float[16];

    private float[] bufferMatrix = new float[16];

    public LightPoint(float x, float y, float z) {

        int vertexShader = GlUtils.compileShader(GLES20.GL_VERTEX_SHADER,
                R.raw.light1_vertex);
        int fragmentShader = GlUtils.compileShader(GLES20.GL_FRAGMENT_SHADER,
                R.raw.light1_fragment);
        program = GlUtils.createAndLinkProgram(vertexShader, fragmentShader);

        position[0] = x;
        position[1] = y;
        position[2] = z;

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, x, y, z);
    }

    public LightPoint() {
        this(0.0f, 0.0f, 0.0f);
    }

    public float[] getModelMatrix() {
        return modelMatrix;
    }

    public void draw(PerspectiveCamera camera) {
        GLES20.glUseProgram(program);

        positionHandle = GLES20.glGetAttribLocation(program, "aPosition");
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glVertexAttrib3f(positionHandle, 0.0f, 0.0f, 0.0f);
        mvpMatrixhandle = GLES20.glGetUniformLocation(program, "uMvpMatrix");
        camera.vpMatrix(bufferMatrix);
        Matrix.multiplyMM(mvpMatrix, 0, bufferMatrix, 0, modelMatrix, 0);
        GLES20.glUniformMatrix4fv(mvpMatrixhandle, 1, false, mvpMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
        GLES20.glUseProgram(0);
    }


}
