package ua.ukma.geronimo.cube;

import static android.opengl.GLES20.*;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Cube {

    private static final int VERTEX_DATA_SIZE = 3;
    //private static final int NORMAL_DATA_SIZE = 3;
    private static final int TEX_COORD_DATA_SIZE = 2;

    private static float vertices[] = {
            //  X,     Y,     Z,
            // front face
            -0.5f,  0.5f,  0.5f,  // top left
            -0.5f, -0.5f,  0.5f,  // bottom left
            0.5f, -0.5f,  0.5f,   // bottom right
            0.5f, -0.5f,  0.5f,   // bottom right
            0.5f,  0.5f,  0.5f,   // top right
            -0.5f,  0.5f,  0.5f,  // top left
            // right face
            0.5f,  0.5f,  0.5f,
            0.5f, -0.5f,  0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f,  0.5f, -0.5f,
            0.5f,  0.5f,  0.5f,
            // back face
            0.5f,  0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f,
            0.5f,  0.5f, -0.5f,
            // left face
            -0.5f,  0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f,  0.5f,
            -0.5f, -0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f, -0.5f,
            // top face
            -0.5f,  0.5f, -0.5f,
            -0.5f,  0.5f,  0.5f,
            0.5f,  0.5f,  0.5f,
            0.5f,  0.5f,  0.5f,
            0.5f,  0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f,
            // bottom face
            -0.5f, -0.5f,  0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f,  0.5f,
            -0.5f, -0.5f,  0.5f,
    };

    private static float texCoords[] = {
            // U,     V
            // front face
            0.0f,  1.0f,  // top left
            0.0f,  0.0f,  // bottom left
            1.0f,  0.0f,  // bottom right
            1.0f,  0.0f,  // bottom right
            1.0f,  1.0f,  // top right
            0.0f,  1.0f,  // top left
            // right face
            0.0f,  1.0f,
            0.0f,  0.0f,
            1.0f,  0.0f,
            1.0f,  0.0f,
            1.0f,  1.0f,
            0.0f,  1.0f,
            // back face
            0.0f,  1.0f,
            0.0f,  0.0f,
            1.0f,  0.0f,
            1.0f,  0.0f,
            1.0f,  1.0f,
            0.0f,  1.0f,
            // left face
            0.0f,  1.0f,
            0.0f,  0.0f,
            1.0f,  0.0f,
            1.0f,  0.0f,
            1.0f,  1.0f,
            0.0f,  1.0f,
            // top face
            0.0f,  1.0f,
            0.0f,  0.0f,
            1.0f,  0.0f,
            1.0f,  0.0f,
            1.0f,  1.0f,
            0.0f,  1.0f,
            // bottom face
            0.0f,  1.0f,
            0.0f,  0.0f,
            1.0f,  0.0f,
            1.0f,  0.0f,
            1.0f,  1.0f,
            0.0f,  1.0f,
    };

    /*
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
    };*/

    private FloatBuffer vertexBuffer;
    //private FloatBuffer normalBuffer;
    private FloatBuffer texCoordBuffer;

    private Texture texture;

    private float[] modelMatrix = new float[16];

    public Cube() {
        vertexBuffer = ByteBuffer.allocateDirect(
                vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.flip();
        /*
        normalBuffer = ByteBuffer.allocateDirect(
                cubeNormals.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        normalBuffer.put(cubeNormals);
        normalBuffer.flip();
        */
        texCoordBuffer = ByteBuffer.allocateDirect(
                texCoords.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        texCoordBuffer.put(texCoords);
        texCoordBuffer.flip();

        texture = new Texture(ResourceLoader.loadBitmap(R.drawable.skeleton));

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

    public void draw(Program program) {
        assert(program.isInUse());
        glUniformMatrix4fv(program.uniform("uModel"), 1, false, modelMatrix, 0);

        glEnableVertexAttribArray(program.attrib("aPosition"));
        glVertexAttribPointer(program.attrib("aPosition"), VERTEX_DATA_SIZE,
                GL_FLOAT, false, 0, vertexBuffer);

        glEnableVertexAttribArray(program.attrib("aTexCoord"));
        glVertexAttribPointer(program.attrib("aTexCoord"), TEX_COORD_DATA_SIZE,
                GL_FLOAT, true, 0, texCoordBuffer);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getHandle());
        glUniform1i(program.uniform("texture"), 0);

        glDrawArrays(GL_TRIANGLES, 0, vertices.length / VERTEX_DATA_SIZE);

        glDisableVertexAttribArray(program.attrib("aPosition"));
        glDisableVertexAttribArray(program.attrib("aTexCoord"));
    }

}
