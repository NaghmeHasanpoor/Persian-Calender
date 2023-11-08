package com.arpitas.persiancalender.connections;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIInterface {
    @GET
    Call<ResponseBody>getAdInfo(@Url String url,
                                @HeaderMap Map<String, String> headers,
                                @Query("package") String packageName);

    // option 2: using a dynamic URL
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

    @GET
    Call<ResponseBody>requestView(@Url String url);
}
