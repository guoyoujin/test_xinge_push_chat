package com.tx.customerservices.http;


import com.tx.customerservices.ProgersssDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

public abstract class DialogTextListener extends TextListener {

    private ProgersssDialog progressDialog;

    public DialogTextListener(Context context, boolean cancelable) {
        super(context);
        progressDialog = new ProgersssDialog(context);  
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(cancelable);
        if (cancelable) {
            progressDialog.setOnCancelListener(new OnCancelListener() {            
                public void onCancel(DialogInterface dialog) {
                    if (getRequestHandle() != null) {
                        getRequestHandle().cancel(true);
                    }    
                }
                
            });
        }
    }

    public DialogTextListener(Context context) {
        this(context, true);
    }

    @Override
    public void onStart() {
        progressDialog.show();
    }

    @Override
    public void onFinish() {
        progressDialog.dismiss();
    }

    @Override
    public void onSuccess(String responseString) {
    }

    @Override
    public void onFailure(Throwable throwable) {
    }

    @Override
    public void onCancel() {
    }

}
