package ua.ukma.geronimo.cube;

import static android.opengl.GLES20.*;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.Arrays;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Gl3dSurfaceView extends GLSurfaceView {
    private static final float ROTATION_RATE = 0.3f;

    private Gl3dRenderer renderer;

    private MotionTracker tracker;

    public Gl3dSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public Gl3dSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setEGLContextClientVersion(2);

        renderer = new Gl3dRenderer();
        setRenderer(renderer);
    }

    public Camera getCamera() {
        return renderer.camera;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if(tracker == null)
                    tracker = new MotionTracker(x, y);
                else
                    tracker.reset(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                tracker.track(x, y);

                renderer.camera.rotate(
                        -tracker.getDy() * ROTATION_RATE,
                        -tracker.getDx() * ROTATION_RATE);
                break;
        }
        return true;
    }

    /**
     * GLSurfaceView.Renderer implementation for this GLSurfaceView
     */
    private class Gl3dRenderer implements GLSurfaceView.Renderer {

        private Program program;
        private Camera camera;
        private List<Cube> cubes;

        public Gl3dRenderer() {
            camera = new Camera();
            camera.setPosition(1.0f, 2.0f, 1.5f);
            camera.setRotation(-15.0f, 15.0f);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glEnable(GL_CULL_FACE);
            glEnable(GL_DEPTH_TEST);

            Shader vertexShader = new Shader(
                    ResourceLoader.loadRaw(R.raw.cube_vertex), GL_VERTEX_SHADER);
            Shader fragmentShader = new Shader(
                    ResourceLoader.loadRaw(R.raw.cube_fragment), GL_FRAGMENT_SHADER);
            program = new Program(
                    Arrays.asList(vertexShader, fragmentShader));

            cubes = Arrays.asList(
                    new Cube(),
                    new Cube(0.5f, 0.5f, -1.5f, 2.0f));
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            camera.setBounds(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
            program.use();

            glUniformMatrix4fv(program.uniform("uView"), 1, false, camera.viewMatrix(), 0);
            glUniformMatrix4fv(program.uniform("uProjection"), 1, false, camera.projectionMatrix(), 0);

            for (Cube cube : cubes) {
                cube.draw(program);
            }

            program.stopUsing();
        }
    }


}
