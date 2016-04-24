package com.namaemegane.fun_iki.funikisdk.sample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;

/**
 * Created by NAMAEMEGANE Inc. on 16/04/24.
 * Dialog
 */
public class Dialog extends AlertDialog.Builder {

    public static final int BUTTON_CANCEL = 0;
    public static final int BUTTON_POSITIVE = DialogInterface.BUTTON_POSITIVE;
    public static final int BUTTON_NEGATIVE = DialogInterface.BUTTON_NEGATIVE;

    private DialogCallback mCallback = null;
    private AlertDialog mDialog = null;

    public Dialog(Context context, String message, String ok){
        this(context, message, ok, null, null);
    }

    public Dialog(Context context, String message, String ok, String cancel, DialogCallback callback){
        this(context, message, ok, cancel, cancel != null, callback);
    }

    public Dialog(Context context, String message, String ok, String cancel, boolean cancelable, DialogCallback callback){
        super(context, R.style.AppDialog);
        mCallback = callback;

        setTitle(null);
        setMessage(message);
        if(ok!=null){
            setPositiveButton(ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    closeDialog(BUTTON_POSITIVE);
                    dialog.dismiss();
                }
            });
        }
        if(cancel!=null){
            setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    closeDialog(BUTTON_NEGATIVE);
                    dialog.dismiss();
                }
            });
        }
        setCancelable(cancelable);
        if (cancelable) {
            this.setOnCancelListener(new DialogInterface.OnCancelListener(){
                @Override
                public void onCancel(DialogInterface dialog) {
                    closeDialog(BUTTON_CANCEL);
                }
            });
        }
    }

    @NonNull
    @Override
    public AlertDialog show(){
        mDialog = super.show();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return mDialog;
    }

    @SuppressWarnings("unused")
    public void cancel(){
        mDialog.cancel();
    }

    protected void closeDialog(int buttonIndex){
        if(mCallback!=null)
            mCallback.onCloseDialog(buttonIndex);
    }

}
