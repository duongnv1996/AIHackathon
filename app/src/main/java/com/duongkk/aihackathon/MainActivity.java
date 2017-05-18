package com.duongkk.aihackathon;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.duongkk.aihackathon.API.ApiUtils;
import com.duongkk.aihackathon.API.CustomCallback;
import com.duongkk.aihackathon.API.HackathonServices;
import com.duongkk.aihackathon.API.ResponseData;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.camera)
    CameraView cameraView;
    @BindView(R.id.take_picture)
    ImageView takePicture;

    private HackathonServices mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mService = ApiUtils.getInstance().getRetrofit().create(HackathonServices.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},100);
        }
        cameraView.setCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] picture) {
                super.onPictureTaken(picture);

                // Create a bitmap
               // Bitmap result = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                new FileAsynTask(picture,MainActivity.this).execute();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @OnClick(R.id.take_picture)
    public void onViewClicked() {
        cameraView.captureImage();
    }


    class FileAsynTask extends AsyncTask<Void,Void,File>{
        byte[] bytes;
        Context context;
        ProgressDialog dialog;
        public FileAsynTask(byte[] bytes,Context context) {
            this.bytes = bytes;
            this.context = context;
            this.dialog = new ProgressDialog(context);
            dialog.setMessage("Uploading...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected File doInBackground(Void... voids) {
            File photo=new File(Environment.getExternalStorageDirectory(), "file.jpg");
            if (photo.exists()) {
                photo.delete();
            }

            try {
                FileOutputStream fos=new FileOutputStream(photo.getPath());
                fos.write(bytes[0]);
                fos.close();
            }
            catch (java.io.IOException e) {
                Log.e("PictureDemo", "Exception in photoCallback", e);
            }
            return photo;
        }
        private   final String MULTIPART_FORM_DATA = "multipart/form-data";
        private   MultipartBody.Part prepareFilePart(String partName, File file) {

            RequestBody requestFile =
                    RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);
            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        }
        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);

            MultipartBody.Part body =
                   prepareFilePart("file",file);
            Call<ResponseData> uploadAPI = mService.upload(body);
            uploadAPI.enqueue(new CustomCallback<ResponseData>(MainActivity.this, dialog,new CustomCallback.ICallBack<ResponseData>() {
                @Override
                public void onResponse(ResponseData response) {
                    LogUtils.e(response.getMsg().toString());
                }
            }));
        }
    }
}
