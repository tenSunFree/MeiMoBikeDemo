package com.example.administrator.meimobikedemo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.administrator.meimobikedemo.mobike.MoBikeView;
import com.meis.widget.MeiTextPathView;

/**
 * Created by wenshi on 2018/6/8.
 * Description
 */
public class MeiMoBikeActivity extends AppCompatActivity implements SensorEventListener {

    private MoBikeView mMobikeView;
    private MeiTextPathView meiTextPathView;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    private int[] mImgs = {
            R.mipmap.mario_money2, R.mipmap.mario_money2, R.mipmap.mario_money2,
            R.mipmap.mario_money2, R.mipmap.mario_money2, R.mipmap.mario_money2,
            R.mipmap.mario_money2, R.mipmap.mario_money2, R.mipmap.mario_money2,
            R.mipmap.mario_money2, R.mipmap.mario_money2
    };
    private int delayAfterClick = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mei_mobike_activity);

        initializationView();
        addViews();

        /** 點擊螢幕時, 金幣將會跳動, 文字也將暫時隱藏 */
        mMobikeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMobikeView.onRandomChanged();
                meiTextPathView.setVisibility(View.GONE);
                delayAfterClick = 4;
            }
        });

        /** 當點擊螢幕時, 暫時隱藏文字顯示, 4秒後才顯示 */
        checkEverySecondToSeeIfTextIsDisplayed();
    }

    private void initializationView() {
        mMobikeView = findViewById(R.id.mo_bike);
        meiTextPathView = findViewById(R.id.meiTextPathView);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    /**
     * 將10個金幣初始化
     */
    private void addViews() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        for (int i = 0; i < mImgs.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(mImgs[i]);
            iv.setTag(R.id.wd_view_circle_tag, true);
            mMobikeView.addView(iv, lp);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1] * 2.0f;
            mMobikeView.onSensorChanged(-x, y);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 當點擊螢幕時, 暫時隱藏文字顯示, 4秒後才顯示
     */
    private void checkEverySecondToSeeIfTextIsDisplayed() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (delayAfterClick > 0) {
                    meiTextPathView.setVisibility(View.GONE);
                    delayAfterClick--;
                } else if (delayAfterClick == 0) {
                    meiTextPathView.setVisibility(View.VISIBLE);
                }
                checkEverySecondToSeeIfTextIsDisplayed();
            }
        }, 1000);
    }
}
