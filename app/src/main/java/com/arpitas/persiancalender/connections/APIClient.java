package com.arpitas.persiancalender.connections;

import com.arpitas.persiancalender.Constants;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

   private static Retrofit retrofit = null;

   public static Retrofit getClient() {
      if (retrofit == null) {
         final OkHttpClient okHttpClient = new OkHttpClient.Builder()
               .connectTimeout(3, TimeUnit.SECONDS)
               .readTimeout(3, TimeUnit.SECONDS)
               .writeTimeout(3, TimeUnit.SECONDS)
               .build();

         retrofit = new Retrofit.Builder()
               .baseUrl(Constants.BASEURL)
               .addConverterFactory(GsonConverterFactory.create())
               .client(okHttpClient)
               .build();
      }
      return retrofit;
   }
}
