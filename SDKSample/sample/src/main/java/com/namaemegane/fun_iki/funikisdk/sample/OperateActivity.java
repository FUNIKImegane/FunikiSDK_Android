package com.namaemegane.fun_iki.funikisdk.sample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.namaemegane.fun_iki.funikisdk.ButtonEvent;
import com.namaemegane.fun_iki.funikisdk.BuzzerVolume;
import com.namaemegane.fun_iki.funikisdk.Funiki;
import com.namaemegane.fun_iki.funikisdk.FunikiDeviceInfo;

/**
 * Created by NAMAEMEGANE Inc. on 16/04/24.
 * OperateActivity
 */
public class OperateActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    static final String EXTRA_DEVICE = "OperateActivity.EXTRA_DEVICE";

    SeekBar seek_duration, seek_frequency, seek_volume;
    TextView color_left, color_right, duration, frequency, volume, accel, gyro;
    View background;

    int leftColorIndex = 0;
    int rightColorIndex = 0;

    FunikiDeviceInfo device;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate);

        device = getIntent().getParcelableExtra(EXTRA_DEVICE);

        findViewById(R.id.left_color_red).setOnClickListener(this);
        findViewById(R.id.left_color_orange).setOnClickListener(this);
        findViewById(R.id.left_color_yellow).setOnClickListener(this);
        findViewById(R.id.left_color_green).setOnClickListener(this);
        findViewById(R.id.left_color_skyblue).setOnClickListener(this);
        findViewById(R.id.left_color_blue).setOnClickListener(this);
        findViewById(R.id.left_color_perple).setOnClickListener(this);
        findViewById(R.id.left_color_white).setOnClickListener(this);
        findViewById(R.id.left_color_black).setOnClickListener(this);

        findViewById(R.id.right_color_red).setOnClickListener(this);
        findViewById(R.id.right_color_orange).setOnClickListener(this);
        findViewById(R.id.right_color_yellow).setOnClickListener(this);
        findViewById(R.id.right_color_green).setOnClickListener(this);
        findViewById(R.id.right_color_skyblue).setOnClickListener(this);
        findViewById(R.id.right_color_blue).setOnClickListener(this);
        findViewById(R.id.right_color_perple).setOnClickListener(this);
        findViewById(R.id.right_color_white).setOnClickListener(this);
        findViewById(R.id.right_color_black).setOnClickListener(this);

        seek_duration = (SeekBar)findViewById(R.id.seek_duration);
        seek_duration.setOnSeekBarChangeListener(this);
        seek_frequency = (SeekBar)findViewById(R.id.seek_frequency);
        seek_frequency.setOnSeekBarChangeListener(this);
        seek_volume = (SeekBar)findViewById(R.id.seek_volume);
        seek_volume.setOnSeekBarChangeListener(this);

        color_right = (TextView)findViewById(R.id.color_right);
        color_left = (TextView)findViewById(R.id.color_left);
        duration = (TextView)findViewById(R.id.duration);
        frequency = (TextView)findViewById(R.id.frequency);
        volume = (TextView)findViewById(R.id.volume);
        accel = (TextView)findViewById(R.id.accelerometer_data);
        gyro = (TextView)findViewById(R.id.gyro_data);

        background = findViewById(R.id.background);

        View btn;
        btn = findViewById(R.id.send_btn);
        btn.setOnClickListener(this);
        btn.setOnTouchListener(this);
        btn = findViewById(R.id.clear_btn);
        btn.setOnClickListener(this);
        btn.setOnTouchListener(this);

        seek_duration.setProgress(10);
        seek_frequency.setProgress(10);
        seek_volume.setProgress(2);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.getCustomView().findViewById(R.id.refresh).setVisibility(View.INVISIBLE);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        reloadColorViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Funiki.addFunikiCallback(this);
        if(device!=null) {
            Funiki.startMotionSensor(device.getAddress());
        }else{
            Funiki.startMotionSensor();
        }
    }

    @Override
    protected void onPause() {
        if(device!=null) {
            Funiki.stopMotionSensor(device.getAddress());
        }else{
            Funiki.stopMotionSensor();
        }
        Funiki.removeFunikiCallback(this);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_color_red:
                leftColorIndex = 0;
                break;
            case R.id.left_color_orange:
                leftColorIndex = 1;
                break;
            case R.id.left_color_yellow:
                leftColorIndex = 2;
                break;
            case R.id.left_color_green:
                leftColorIndex = 3;
                break;
            case R.id.left_color_skyblue:
                leftColorIndex = 4;
                break;
            case R.id.left_color_blue:
                leftColorIndex = 5;
                break;
            case R.id.left_color_perple:
                leftColorIndex = 6;
                break;
            case R.id.left_color_white:
                leftColorIndex = 7;
                break;
            case R.id.left_color_black:
                leftColorIndex = -1;
                break;
            case R.id.right_color_red:
                rightColorIndex = 0;
                break;
            case R.id.right_color_orange:
                rightColorIndex = 1;
                break;
            case R.id.right_color_yellow:
                rightColorIndex = 2;
                break;
            case R.id.right_color_green:
                rightColorIndex = 3;
                break;
            case R.id.right_color_skyblue:
                rightColorIndex = 4;
                break;
            case R.id.right_color_blue:
                rightColorIndex = 5;
                break;
            case R.id.right_color_perple:
                rightColorIndex = 6;
                break;
            case R.id.right_color_white:
                rightColorIndex = 7;
                break;
            case R.id.right_color_black:
                rightColorIndex = -1;
                break;
            case R.id.send_btn:
            {
                int left = getLEDColor(leftColorIndex) & 0xFFFFFF;
                int right = getLEDColor(rightColorIndex) & 0xFFFFFF;
                double duration = seek_duration.getProgress() / 100.0;
                int freq = seek_frequency.getProgress();
                int volume = seek_volume.getProgress();

                if(device!=null) {
                    Funiki.perform(device.getAddress(), left, right, duration, freq, BuzzerVolume.getVolume(volume), duration);
                }else{
                    Funiki.perform(left, right, duration, freq, BuzzerVolume.getVolume(volume), duration);
                }
                break;
            }
            case R.id.clear_btn:
                if(device!=null) {
                    Funiki.clearColor(device.getAddress());
                }else{
                    Funiki.clearColor();
                }
                break;
        }
        reloadColorViews();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seek_duration:
                duration.setText(String.format("%.2f sec", (float)seekBar.getProgress() / 100f));
                break;
            case R.id.seek_frequency:
                frequency.setText(String.format("%d",seekBar.getProgress()));
                break;
            case R.id.seek_volume:
                volume.setText(String.format("%d",seekBar.getProgress()));
                break;
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @SuppressWarnings("ConstantConditions")
    void reloadColorViews() {
        ((ImageButton)findViewById(R.id.left_color_red)).setImageResource(leftColorIndex == 0 ? R.drawable.color_red_check : R.drawable.color_red);
        ((ImageButton)findViewById(R.id.left_color_orange)).setImageResource(leftColorIndex == 1 ? R.drawable.color_orange_check : R.drawable.color_orange);
        ((ImageButton)findViewById(R.id.left_color_yellow)).setImageResource(leftColorIndex == 2 ? R.drawable.color_yellow_check : R.drawable.color_yellow);
        ((ImageButton)findViewById(R.id.left_color_green)).setImageResource(leftColorIndex == 3 ? R.drawable.color_green_check : R.drawable.color_green);
        ((ImageButton)findViewById(R.id.left_color_skyblue)).setImageResource(leftColorIndex == 4 ? R.drawable.color_skyblue_check : R.drawable.color_skyblue);
        ((ImageButton)findViewById(R.id.left_color_blue)).setImageResource(leftColorIndex == 5 ? R.drawable.color_blue_check : R.drawable.color_blue);
        ((ImageButton)findViewById(R.id.left_color_perple)).setImageResource(leftColorIndex == 6 ? R.drawable.color_perple_check : R.drawable.color_perple);
        ((ImageButton)findViewById(R.id.left_color_white)).setImageResource(leftColorIndex == 7 ? R.drawable.color_white_check : R.drawable.color_white);
        ((ImageButton)findViewById(R.id.left_color_black)).setImageResource(leftColorIndex == -1 ? R.drawable.color_black_check : R.drawable.color_black);

        color_left.setText(String.format("#%06x", getLEDColor(leftColorIndex) & 0xFFFFFF));

        ((ImageButton)findViewById(R.id.right_color_red)).setImageResource(rightColorIndex == 0 ? R.drawable.color_red_check : R.drawable.color_red);
        ((ImageButton)findViewById(R.id.right_color_orange)).setImageResource(rightColorIndex == 1 ? R.drawable.color_orange_check : R.drawable.color_orange);
        ((ImageButton)findViewById(R.id.right_color_yellow)).setImageResource(rightColorIndex == 2 ? R.drawable.color_yellow_check : R.drawable.color_yellow);
        ((ImageButton)findViewById(R.id.right_color_green)).setImageResource(rightColorIndex == 3 ? R.drawable.color_green_check : R.drawable.color_green);
        ((ImageButton)findViewById(R.id.right_color_skyblue)).setImageResource(rightColorIndex == 4 ? R.drawable.color_skyblue_check : R.drawable.color_skyblue);
        ((ImageButton)findViewById(R.id.right_color_blue)).setImageResource(rightColorIndex == 5 ? R.drawable.color_blue_check : R.drawable.color_blue);
        ((ImageButton)findViewById(R.id.right_color_perple)).setImageResource(rightColorIndex == 6 ? R.drawable.color_perple_check : R.drawable.color_perple);
        ((ImageButton)findViewById(R.id.right_color_white)).setImageResource(rightColorIndex == 7 ? R.drawable.color_white_check : R.drawable.color_white);
        ((ImageButton)findViewById(R.id.right_color_black)).setImageResource(rightColorIndex == -1 ? R.drawable.color_black_check : R.drawable.color_black);

        color_right.setText(String.format("#%06x", getLEDColor(rightColorIndex) & 0xFFFFFF));
    }

    int getLEDColor(int index) {
        switch (index) {
            case 0: return color(R.color.ledRed);
            case 1: return color(R.color.ledOrange);
            case 2: return color(R.color.ledYellow);
            case 3: return color(R.color.ledGreen);
            case 4: return color(R.color.ledSkyBlue);
            case 5: return color(R.color.ledBlue);
            case 6: return color(R.color.ledPerple);
            case 7: return color(R.color.ledWhite);
            default:return color(R.color.ledClear);
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void didGetMotionData(FunikiDeviceInfo device, float ax, float ay, float az, float gx, float gy, float gz) {
//        Log.d(TAG, String.format("[%s] accel:(%f,%f,%f), gyro:(%f,%f,%f)", device.getAddress(), ax, ay, az, gx, gy, gz));
        accel.setText(String.format("x: %.3f, y: %.3f, z: %.3f", ax, ay, az));
        gyro.setText(String.format("x: %.3f, y: %.3f, z: %.3f", gx, gy, gz));
    }

    @Override
    public void didPushButton(FunikiDeviceInfo device, ButtonEvent buttonEvent) {
        Log.d(TAG, String.format("[%s] Button: %d", device.getAddress(), buttonEvent.getId()));
        long duration = 300;
        int repeatCount = 1;
        switch (buttonEvent){
            case SingleClick:
                break;
            case DoubleClick:
                repeatCount = 3;
                break;
            default:
                duration = 1000;
                break;
        }
        background.clearAnimation();
        AlphaAnimation alpha = new AlphaAnimation(0f, 0.5f);
        alpha.setDuration(duration);
        alpha.setFillEnabled(true);
        alpha.setFillAfter(true);
        alpha.setRepeatCount(repeatCount);
        alpha.setRepeatMode(AlphaAnimation.REVERSE);
        background.setAlpha(1);
        background.startAnimation(alpha);
    }

}
