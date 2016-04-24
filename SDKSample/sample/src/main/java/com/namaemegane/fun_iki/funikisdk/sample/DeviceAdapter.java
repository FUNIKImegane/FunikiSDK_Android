package com.namaemegane.fun_iki.funikisdk.sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.namaemegane.fun_iki.funikisdk.FunikiDeviceInfo;

import java.util.ArrayList;

/**
 * Created by NAMAEMEGANE Inc. on 16/04/24.
 * DeviceAdapter
 */
public class DeviceAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<FunikiDeviceInfo> devices;
    private SparseArray<View> views = new SparseArray<>();
    private DeviceAdapterCallback callback = null;

    @SuppressWarnings("unused")
    private DeviceAdapter(){}
    public DeviceAdapter(@NonNull Context context, @NonNull DeviceAdapterCallback callback){
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.callback = callback;
    }

    public void setDevices(@NonNull ArrayList<FunikiDeviceInfo> devices){
        this.devices = devices;
        views.clear();
    }

    @Override
    public int getCount() {
        return devices!=null ? devices.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return devices!=null ? devices.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static private class ViewHolder{
        CheckBox connectCheck;
        TextView nameText;
        TextView addressText;
        TextView connectionText;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.cell, null);
            holder = new ViewHolder();
            holder.connectCheck = (CheckBox) convertView.findViewById(R.id.connect_check);
            holder.nameText = (TextView) convertView.findViewById(R.id.name);
            holder.addressText = (TextView) convertView.findViewById(R.id.address);
            holder.connectionText = (TextView) convertView.findViewById(R.id.connection);
            convertView.setTag(holder);
            views.put(position, convertView);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        FunikiDeviceInfo device = (FunikiDeviceInfo) getItem(position);

        holder.connectCheck.setChecked(device.isConnected());
        if(!holder.connectCheck.hasOnClickListeners()){
            final int pos = position;
            holder.connectCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox c = (CheckBox)v;
                    callback.onConnectCheckBoxClick(c.isChecked(), pos);
                    c.setChecked(!c.isChecked());
                }
            });
        }
        holder.nameText.setText(device.getName());
        holder.addressText.setText(device.getAddress());
        holder.connectionText.setText(device.isConnected() ? R.string.connected : R.string.disconnect);

        return convertView;
    }

}
