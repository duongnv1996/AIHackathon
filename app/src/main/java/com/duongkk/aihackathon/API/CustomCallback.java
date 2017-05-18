package com.duongkk.aihackathon.API;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.blankj.utilcode.util.LogUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DuongKK on 5/16/2017.
 */

public class CustomCallback<T> implements Callback<T> {
    Context context;
    ICallBack<T> callBack;
    ProgressDialog dialog;
    Dialog errorDialog;
    public CustomCallback(Context context,ICallBack<T> callBack){
        this.context = context;
        this.callBack = callBack;
        errorDialog = new AlertDialog.Builder(context)
        .setTitle("Error")
        .setMessage("Error occurred while connecting. please try again later! ")
        .setPositiveButton("Dismis", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(errorDialog!=null && errorDialog.isShowing()) errorDialog.dismiss();
            }
        }).create();

    }

    public CustomCallback(Context context,  ProgressDialog dialog,ICallBack<T> callBack) {
        this.context = context;
        this.callBack = callBack;
        this.dialog = dialog;
        if (dialog!=null && !dialog.isShowing()) dialog.show();
        errorDialog = new AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage("Error occurred while connecting. please try again later! ")
                .setPositiveButton("Dismis", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(errorDialog!=null && errorDialog.isShowing()) errorDialog.dismiss();
                    }
                }).create();

    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (dialog!=null && dialog.isShowing()) dialog.dismiss();

        if(response.isSuccessful()){
            callBack.onResponse(response.body());
        }else{
            LogUtils.e(response.code());
            errorDialog.show();
          // Toast.makeText(context,response.code(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (dialog!=null && dialog.isShowing()) dialog.dismiss();
        LogUtils.e(t.getMessage());
//        Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
        errorDialog.show();

    }

    public interface ICallBack<T>{
        void onResponse(T response);
    }
}
