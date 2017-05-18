package com.duongkk.aihackathon.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DuongKK on 5/13/2017.
 */

public class ApiUtils {
    static ApiUtils instance;
    Retrofit retrofit;
    private ApiUtils(){
        retrofit= new Retrofit.Builder()
                .baseUrl("http://192.168.1.28:2020/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    };
    public Retrofit getRetrofit(){
        return retrofit;
    }
   public static ApiUtils getInstance(){
       if(instance ==null){
           instance = new ApiUtils();
       }
       return instance;
   }
}
