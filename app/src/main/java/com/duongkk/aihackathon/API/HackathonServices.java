package com.duongkk.aihackathon.API;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by DuongKK on 5/13/2017.
 */

public interface HackathonServices {
    @Multipart
    @POST("/upload")
    Call<ResponseData> upload(
            @Part MultipartBody.Part file
    );
}
