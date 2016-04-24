package com.namaemegane.fun_iki.funikisdk.sample;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.namaemegane.fun_iki.funikisdk.ButtonEvent;
import com.namaemegane.fun_iki.funikisdk.FetchDevicesFlag;
import com.namaemegane.fun_iki.funikisdk.FunikiCallback;
import com.namaemegane.fun_iki.funikisdk.FunikiDeviceInfo;

import java.util.ArrayList;

/**
 * Created by NAMAEMEGANE Inc. on 16/04/24.
 * BaseActivity
 */
public class BaseActivity extends AppCompatActivity implements View.OnTouchListener, FunikiCallback {

    static final String TAG = "MEGANE";

    int color(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getColor(id);
        }else{
            //noinspection deprecation
            return getResources().getColor(id);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            v.setAlpha(0.6f);
        }else{
            v.setAlpha(1.0f);
        }
        return false;
    }

    @Override
    public void didPrepare() {
    }

    @Override
    public void didFetchDevices(ArrayList<FunikiDeviceInfo> devices, FetchDevicesFlag flag) {
    }

    @Override
    public void didGetMotionData(FunikiDeviceInfo device, float ax, float ay, float az, float gx, float gy, float gz) {
    }

    @Override
    public void didPushButton(FunikiDeviceInfo device, ButtonEvent buttonEvent) {
    }

}
