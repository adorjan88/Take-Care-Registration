package Classes;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import com.example.takecare.MainActivity;

public class FallDetector implements SensorEventListener {

    private Context context;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private Activity activity1 = null;
    private static final float FALL_THRESHOLD = 10.0f;

    public void createVibrator(Context context){
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public FallDetector(Context context) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void start() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float gForce = (float) Math.sqrt(x * x + y * y + z * z);

        if (gForce > FALL_THRESHOLD) {
            if(Build.VERSION.SDK_INT >= 26)
                createVibrator(context);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
