package net.ddns.opetany.engineeringquiz;

import java.util.List;

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
    Call<LoginRegisterJSON> Login(@Field("login") String login, @Field("password") String password);

    @FormUrlEncoded
    @POST("create_user.php")
    Call<LoginRegisterJSON> Register(@Field("login") String login, @Field("password") String password);

    @FormUrlEncoded
    @POST("question.php")
    Call<List<QuestionJSON>> Question(@Field("lvl") int lvl, @Field("id0") int id0 , @Field("id1") int id1 , @Field("id2") int id2);

    @FormUrlEncoded
    @POST("rank.php")
    Call<List<RankJSON>> Rank(@Field("flag") int flag);
}
