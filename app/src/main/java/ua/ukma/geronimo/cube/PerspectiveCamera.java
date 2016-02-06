package ua.ukma.geronimo.cube;

import android.opengl.Matrix;

public class PerspectiveCamera {
    private static final float DEFAULT_FOV = 75.0f;
    private static final float DEFAULT_Z_NEAR = 0.1f;
    private static final float DEFAULT_Z_FAR = 10f;

    private float[] projectionMatrix = new float[16];

    private ObservableFloatArray position = new ObservableFloatArray(3);
    private ObservableFloatArray rotation = new ObservableFloatArray(2);

    private float[] viewMatrix = new float[16];
    private float[] vpMatrix = new float[16];

    public PerspectiveCamera() {
        Matrix.setIdentityM(projectionMatrix, 0);
        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.setIdentityM(vpMatrix, 0);
    }

    public float[] getPosition() {
        return position.getCopy();
    }

    public void setPosition(float x, float y, float z) {
        position.setValues(x, y, z);
    }

    public void translate(float dx, float dy, float dz) {
        setPosition(position.get(0) + dx,
                position.get(1) + dy,
                position.get(2) + dz);
    }

    public float[] getRotation() {
        return rotation.getCopy();
    }

    public void setRotation(float alpha, float theta) {
        rotation.setValues(alpha, theta);
    }

    public void rotate(float alpha, float theta) {
        setRotation(rotation.get(0) + alpha,
                rotation.get(1) + theta);
    }

    public void setBounds(int width, int height) {
        setProjection(DEFAULT_FOV, (float) width / height, DEFAULT_Z_NEAR, DEFAULT_Z_FAR);
    }

    public void setProjection(float fov, float aspect, float zNear, float zFar) {
        Matrix.perspectiveM(projectionMatrix, 0, fov, aspect, zNear, zFar);
    }

    public void update() {
        Matrix.setRotateM(viewMatrix, 0, -rotation.get(0), 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(viewMatrix, 0, -rotation.get(1), 0.0f, 1.0f, 0.0f);
        Matrix.translateM(viewMatrix, 0, -position.get(0), -position.get(1), -position.get(2));

        Matrix.multiplyMM(vpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

    public void viewMatrix(float[] dest) {
        System.arraycopy(viewMatrix, 0, dest, 0, viewMatrix.length);
    }

    public void vpMatrix(float[] dest) {
        System.arraycopy(vpMatrix, 0, dest, 0, vpMatrix.length);
    }

    public ObservableFloatArray observablePosition() {
        return position;
    }

    public ObservableFloatArray observableRotation() {
        return rotation;
    }

    public void forward(float dist) {
        translate(
                dist * (float)(-Math.sin(Math.toRadians(rotation.get(1))) * Math.cos(Math.toRadians(rotation.get(0)))),
                dist * (float)(Math.sin(Math.toRadians(rotation.get(0)))),
                dist * (float)(-Math.cos(Math.toRadians(rotation.get(1))) * Math.cos(Math.toRadians(rotation.get(0))))
        );
    }

    public void backward(float dist) {
        translate(
                dist * (float)(Math.sin(Math.toRadians(rotation.get(1))) * Math.cos(Math.toRadians(rotation.get(0)))),
                dist * (float)(-Math.sin(Math.toRadians(rotation.get(0)))),
                dist * (float)(Math.cos(Math.toRadians(rotation.get(1))) * Math.cos(Math.toRadians(rotation.get(0))))
        );
    }

    public void leftward(float dist) {
        translate(
                dist * (float)(-Math.cos(Math.toRadians(rotation.get(1))) * Math.cos(Math.toRadians(rotation.get(0)))),
                0,
                dist * (float)(Math.sin(Math.toRadians(rotation.get(1))) * Math.cos(Math.toRadians(rotation.get(0))))
        );
    }

    public void rightward(float dist) {
        translate(
                dist * (float)(Math.cos(Math.toRadians(rotation.get(1))) * Math.cos(Math.toRadians(rotation.get(0)))),
                0,
                dist * (float)(-Math.sin(Math.toRadians(rotation.get(1))) * Math.cos(Math.toRadians(rotation.get(0))))
        );
    }

    public void upward(float dist) {
        translate(
                0,//dist * (float)(Math.cos(Math.toRadians(rotation[1])) * Math.cos(Math.toRadians(rotation[0]))),
                dist * (float)(Math.cos(Math.toRadians(rotation.get(1)))),
                0//dist * (float)(-Math.sin(Math.toRadians(rotation[1])) * Math.cos(Math.toRadians(rotation[0])))
        );
    }

    public void downward(float dist) {
        translate(
                0,//dist * (float)(Math.cos(Math.toRadians(rotation[1])) * Math.cos(Math.toRadians(rotation[0]))),
                dist * (float)(-Math.cos(Math.toRadians(rotation.get(1)))),
                0//dist * (float)(Math.cos(Math.toRadians(rotation[1])) * Math.cos(Math.toRadians(rotation[0]))),
        );
    }

}
