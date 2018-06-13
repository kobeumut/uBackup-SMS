package com.grisoft.umut.uBackup;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Umut on 24.01.2016.
 */
public interface RegisterAPI {
    @FormUrlEncoded
    @POST("/db.php")
    Call<APIpojo> insertSMS(@Field("backup") String backup);
}
