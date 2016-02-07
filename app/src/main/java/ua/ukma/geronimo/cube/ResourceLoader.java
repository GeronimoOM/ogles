package ua.ukma.geronimo.cube;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ResourceLoader {

    private static String TAG = ResourceLoader.class.getName();

    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        ResourceLoader.context = context;
    }

    public static String loadRaw(final int id) {
        StringBuilder str = new StringBuilder();
        try ( InputStream is = context.getResources().openRawResource(id);
              InputStreamReader isReader = new InputStreamReader(is);
              BufferedReader bReader = new BufferedReader(isReader)){
            String line;
            while((line = bReader.readLine()) != null ) {
                str.append(line).append('\n');
            }
        } catch (IOException e) {
            Log.d(TAG, "Failed to read resource: " + id);
        }
        return str.toString();
    }

    public static Bitmap loadBitmap(final int id) {
       return BitmapFactory.decodeResource(context.getResources(), id);
    }
}
