package com.luobotie.kingshun.mychat.net;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface GetChatRequest {
    @GET("/iqa/query?")
    Call<chatBean> getChat(@Query("question") String question, @Header("Authorization") String appcode);
}
