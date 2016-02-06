package ua.ukma.geronimo.cube;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Cube {

    private static final int VERTEX_DATA_SIZE = 3;
    private static final int NORMAL_DATA_SIZE = 3;

    private static float cubeCoords[] = {
            // front face
            -0.5f, 0.5f, 0.5f,   // top left
            -0.5f, -0.5f, 0.5f,  // bottom left
            0.5f, -0.5f, 0.5f,   // bottom right
            0.5f, -0.5f, 0.5f,   // bottom right
            0.5f, 0.5f, 0.5f,   // top right
            -0.5f, 0.5f, 0.5f,   // top left
            // right face
            0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, 0.5f,
            // back face
            0.5f, 0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            // left face
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            // top face
            -0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            // bottom face
            -0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
    };

    private static float cubeNormals[] = {
            // front face
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            // right face
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            // back face
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            // left face
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            // top face
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            // bottom face
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
    };

    private static float color[] = { 0.0f, 0.5f, 0.5f, 1.0f };

    private FloatBuffer vertexBuffer;
    private FloatBuffer normalBuffer;

    private final int program;
    private int positionHandle;
    private int colorHandle;
    private int normalHandle;
    private int mvpMatrixHandle;
    private int mvMatrixHandle;
    private int lightPosHandle;

    private float[] modelMatrix = new float[16];
    private float[] mvMatrix = new float[16];
    private float[] mvpMatrix = new float[16];

    private float[] bufferMatrix = new float[16];

    public Cube() {
        vertexBuffer = ByteBuffer.allocateDirect(
                cubeCoords.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.put(cubeCoords);
        vertexBuffer.flip();

        normalBuffer = ByteBuffer.allocateDirect(
                cubeNormals.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        normalBuffer.put(cubeNormals);
        normalBuffer.flip();

        int vertexShader = GlUtils.compileShader(GLES20.GL_VERTEX_SHADER,
                R.raw.cube_vertex);
        int fragmentShader = GlUtils.compileShader(GLES20.GL_FRAGMENT_SHADER,
                R.raw.cube_fragment);
        program = GlUtils.createAndLinkProgram(vertexShader, fragmentShader);

        Matrix.setIdentityM(modelMatrix, 0);
    }

    public Cube(float x, float y, float z) {
        this();
        Matrix.translateM(modelMatrix, 0, x, y, z);
    }

    public Cube(float x, float y, float z, float scale) {
        this(x, y, z);
        Matrix.scaleM(modelMatrix, 0, scale, scale, scale);
    }

    public void draw(PerspectiveCamera camera, LightPoint light) {
        GLES20.glUseProgram(program);

        positionHandle = GLES20.glGetAttribLocation(program, "aPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, VERTEX_DATA_SIZE,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        colorHandle = GLES20.glGetUniformLocation(program, "uColor");
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        normalHandle = GLES20.glGetAttribLocation(program, "aNormal");
        GLES20.glEnableVertexAttribArray(normalHandle);
        GLES20.glVertexAttribPointer(normalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false,
                0, normalBuffer);

        camera.vpMatrix(bufferMatrix);
        Matrix.multiplyMM(mvpMatrix, 0, bufferMatrix, 0, modelMatrix, 0);
        mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMvpMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        camera.viewMatrix(bufferMatrix);
        Matrix.multiplyMM(mvMatrix, 0, bufferMatrix, 0, modelMatrix, 0);
        mvMatrixHandle = GLES20.glGetUniformLocation(program, "uMvMatrix");
        GLES20.glUniformMatrix4fv(mvMatrixHandle, 1, false, mvMatrix, 0);

        lightPosHandle = GLES20.glGetUniformLocation(program, "uLightPos");
        float[] lightPos = new float[] { 0.0f, 0.0f, 0.0f, 1.0f };
        Matrix.multiplyMV(lightPos, 0, light.getModelMatrix(), 0, lightPos, 0);
        Matrix.multiplyMV(lightPos, 0, bufferMatrix, 0, lightPos, 0);
        GLES20.glUniform3f(lightPosHandle, lightPos[0], lightPos[1], lightPos[2]);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, cubeCoords.length / VERTEX_DATA_SIZE);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(normalHandle);
        GLES20.glUseProgram(0);
    }

}
