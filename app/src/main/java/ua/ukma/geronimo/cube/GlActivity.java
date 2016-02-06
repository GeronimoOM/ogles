package ua.ukma.geronimo.cube;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class GlActivity extends AppCompatActivity {
    private static final float TRANSLATION_RATE = 0.5f;

    private Gl3dSurfaceView glSurfaceView;
    private PerspectiveCamera camera;

    private TextView posText;
    private float[] position = new float[3];
    private TextView rotText;
    private float[] rotation = new float[2];

    private Button buttonW;
    private Button buttonA;
    private Button buttonS;
    private Button buttonD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set ResourceLoader's static Context parameter
        // to be able to retrieve resources
        ResourceLoader.setContext(this);

        setContentView(R.layout.activity_gl);
        glSurfaceView = (Gl3dSurfaceView) findViewById(R.id.glSurfaceView);
        camera = glSurfaceView.getCamera();

        posText = (TextView) findViewById(R.id.posText);
        posText.setText(Arrays.toString(camera.observablePosition().getCopy()));
        camera.observablePosition().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                ObservableFloatArray observablePos = (ObservableFloatArray) observable;
                observablePos.copyTo(position);
                posText.setText(Arrays.toString(position));
            }
        });

        rotText = (TextView) findViewById(R.id.rotText);
        rotText.setText(Arrays.toString(camera.observableRotation().getCopy()));
        camera.observableRotation().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                ObservableFloatArray observableRot = (ObservableFloatArray) observable;
                observableRot.copyTo(rotation);
                rotText.setText(Arrays.toString(rotation));
            }
        });

        buttonW = (Button) findViewById(R.id.buttonW);
        buttonA = (Button) findViewById(R.id.buttonA);
        buttonS = (Button) findViewById(R.id.buttonS);
        buttonD = (Button) findViewById(R.id.buttonD);

        buttonW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.forward(TRANSLATION_RATE);
            }
        });

        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.leftward(TRANSLATION_RATE);
            }
        });

        buttonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.backward(TRANSLATION_RATE);
            }
        });

        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.rightward(TRANSLATION_RATE);
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

}
