package ua.ukma.geronimo.cube;

import java.util.Observable;

public class ObservableFloatArray extends Observable {
    private float[] array;

    public ObservableFloatArray(float[] array) {
        this.array = array;
    }

    public ObservableFloatArray(int size) {
        this(new float[size]);
    }

    public void setValues(float... values) {
        for(int i = 0; i < array.length; ++i) {
            array[i] = values[i];
        }
        setChanged();
        notifyObservers();
    }

    public float get(int index) {
        return array[index];
    }

    public float[] getCopy() {
        float[] newArray = new float[array.length];
        copyTo(newArray);
        return newArray;
    }

    public void copyTo(float[] dest)
    {
        System.arraycopy(array, 0, dest, 0, array.length);
    }
}
