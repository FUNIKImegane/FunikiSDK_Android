package com.namaemegane.fun_iki.funikisdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;

import com.namaemegane.fun_iki.funikisdk.ButtonEvent;
import com.namaemegane.fun_iki.funikisdk.FetchDevicesFlag;
import com.namaemegane.fun_iki.funikisdk.Funiki;
import com.namaemegane.fun_iki.funikisdk.FunikiDeviceInfo;

import java.util.ArrayList;

/**
 * Created by NAMAEMEGANE Inc. on 16/04/24.
 * MainActivity
 */
public class MainActivity extends BaseActivity {

    ListView deviceList;
    DeviceAdapter adapter;
    View refreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceList = (ListView)findViewById(R.id.device_list);
        adapter = new DeviceAdapter(this, new DeviceAdapterCallback(){
            @Override
            public void onConnectCheckBoxClick(boolean check, int position) {
                FunikiDeviceInfo device = (FunikiDeviceInfo)adapter.getItem(position);
                if(check){
                    Funiki.connect(device.getAddress());
                }else{
                    Funiki.disconnect(device.getAddress());
                }
            }
        });
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FunikiDeviceInfo device = (FunikiDeviceInfo) adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, OperateActivity.class);
                intent.putExtra(OperateActivity.EXTRA_DEVICE, device);
                startActivity(intent);
            }
        });

        View btn = findViewById(R.id.sdk_button);
        assert btn != null;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OperateActivity.class);
                startActivity(intent);
            }
        });
        btn.setOnTouchListener(this);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setCustomView(R.layout.actionbar);
        refreshView = actionBar.getCustomView().findViewById(R.id.refresh);
        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Funiki.findDevices();
                v.clearAnimation();
                RotateAnimation anim = new RotateAnimation(0, -360, v.getWidth() / 2, v.getHeight() / 2);
                anim.setDuration(1000);
                anim.setRepeatCount(10);
                anim.setInterpolator(new LinearInterpolator());
                v.startAnimation(anim);
            }
        });
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Funiki.addFunikiCallback(this);
        if(Funiki.bind(this)) {
            if (Funiki.isEnabled() && !Funiki.isLatestApp()) {
                showPlayStoreDialog(R.string.old_app_message);
            }
        }else{
            showPlayStoreDialog(R.string.play_store_message);
        }
    }

    void showPlayStoreDialog(@StringRes int resId){
        new Dialog(this, getString(resId), getString(R.string.ok), null, false, new DialogCallback() {
            @Override
            public void onCloseDialog(int buttonIndex) {
                Funiki.openPlayStore(MainActivity.this);
            }
        }).show();
    }

    @Override
    protected void onPause() {
        Funiki.removeFunikiCallback(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Funiki.unbind();
        super.onDestroy();
    }

    @Override
    public void didPrepare() {
        Log.d(TAG, "FUN'IKI SDK is prerared.");
        if(Funiki.isEnabled()) {
            if(Funiki.isLatestApp()) {
                refreshView.callOnClick();
            }else{
                showPlayStoreDialog(R.string.old_app_message);
            }
        }
    }

    @Override
    public void didFetchDevices(ArrayList<FunikiDeviceInfo> devices, FetchDevicesFlag flag) {
        Log.d(TAG, "didFetchDevices: " + devices);
        adapter.setDevices(devices);
        deviceList.setAdapter(adapter);
        if(flag==FetchDevicesFlag.StopScan){
            refreshView.clearAnimation();
        }
    }

    @Override
    public void didPushButton(FunikiDeviceInfo device, ButtonEvent buttonEvent) {
        Log.d(TAG, String.format("[%s] Button: %d", device.getAddress(), buttonEvent.getId()));
    }
}
