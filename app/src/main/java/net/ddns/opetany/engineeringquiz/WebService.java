package net.ddns.opetany.engineeringquiz;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by opetany on 16.05.2016.
 */
public interface WebService
{
    @FormUrlEncoded
    @POST("login.php")
    Call<LoginRegisterJSON> Login(@Field("login") String first, @Field("password") String last);

    @FormUrlEncoded
    @POST("create_user.php")
    Call<LoginRegisterJSON> Register(@Field("login") String first, @Field("password") String last);
}
