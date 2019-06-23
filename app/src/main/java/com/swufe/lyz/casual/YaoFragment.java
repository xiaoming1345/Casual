package com.swufe.lyz.casual;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link YaoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link YaoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YaoFragment extends AppCompatActivity {
    private SensorManager sensorManager;
    private Vibrator vibrator;
    private static int pics[] = {R.mipmap.a,R.mipmap.b,R.mipmap.c,R.mipmap.d,R.mipmap.e};

    private static final int SENSOR_SHAKE = 10;
    private ImageView img;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.imageView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

    }

    protected void onResume(){
        super.onResume();
        if (sensorManager != null){//注册监听器
            sensorManager.registerListener(sensorEventListener,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
            //第一个参数是Listener，第二个参数是所的传感器类型，第三个参数值获取传感器信息的频率
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        if (sensorManager != null){//取消监听器
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            //传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0];//x轴方向的重力加速度，向右为正
            float y = values[0];//y轴方向的重力加速度，向前为正
            float z = values[0];//z轴方向的重力加速度，向上为正
            Log.i(TAG,"x[" + x + "] y[" + y + "] z[" + z + "]");
            //一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态
            int medumValue = 10;//不同的手机厂商这个数值可能会不同
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue){
                vibrator.vibrate(200);
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                handler.sendMessage(msg);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    Handler handler = new Handler(){

        @Override
        public void handleMessage (Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 10:
                    //Toast.makeText(MainActivity.this,"检测到摇晃，执行操作！" , Toast.LENGTH_SHORT).show();
                    Log.i(TAG,"检测到摇晃，执行操作！");
                    java.util.Random r = new java.util.Random();
                    int num = Math.abs(r.nextInt())%5;
                    img.setImageResource(pics[num]);
                    break;
            }
        }
    };

}
