package ua.ukma.geronimo.cube;

import android.content.Context;
import android.opengl.GLES20;
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

    public PerspectiveCamera getCamera() {
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

    private class Gl3dRenderer implements GLSurfaceView.Renderer {

        private PerspectiveCamera camera;
        private LightPoint light;
        private List<Cube> cubes;

        public Gl3dRenderer() {
            camera = new PerspectiveCamera();
            camera.setPosition(1.0f, 2.0f, 1.5f);
            camera.setRotation(-15.0f, 15.0f);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);

            light = new LightPoint(2.0f, 1.5f, 1.0f);

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
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            camera.update();

            light.draw(camera);

            for (Cube cube : cubes) {
                cube.draw(camera, light);
            }
        }
    }


}
