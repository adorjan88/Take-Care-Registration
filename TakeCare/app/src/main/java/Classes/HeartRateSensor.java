package Classes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class HeartRateSensor {


    private final SensorManager sensorManager;
    private final Sensor heartRateSensor;
    private final SensorEventListener sensorEventListener;
    private OnHeartRateUpdateListener onHeartRateUpdateListener;

    public HeartRateSensor(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float heartRate = event.values[0];
                if (onHeartRateUpdateListener != null) {
                    onHeartRateUpdateListener.onHeartRateUpdate(heartRate);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }

    public void startTracking() {
        sensorManager.registerListener(sensorEventListener, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopTracking() {
        sensorManager.unregisterListener(sensorEventListener);
    }

    public void setOnHeartRateUpdateListener(OnHeartRateUpdateListener listener) {
        onHeartRateUpdateListener = listener;
    }

    public interface OnHeartRateUpdateListener {
        void onHeartRateUpdate(float heartRate);
    }
}
