package net.ddns.opetany.engineeringquiz;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Arkadiusz Bochyński on 23.05.2016.
 */
public class ApiClient
{
    //adres serwera do obsługi bazy MySQL
    static final String server_URL = "http://opetany.ddns.net/android_mysql_connect/";
    public Retrofit retrofit;
    public WebService webService;
    public static ApiClient ourInstance = new ApiClient();

    // =======================================================================================================================
    public static ApiClient getInstance()
    {
        return ourInstance;
    }

    // =======================================================================================================================
    public ApiClient()
    {
        // adres API
        retrofit = new Retrofit.Builder()
                .baseUrl(server_URL).addConverterFactory(GsonConverterFactory.create()).build();    // adres API

        webService = retrofit.create(WebService.class);
    }

    // =======================================================================================================================
    public static Retrofit getRetrofit()
    {
       return getInstance().retrofit;
    }

    // =======================================================================================================================
    public static WebService getWebService()
    {
        return getInstance().webService;
    }

    // =======================================================================================================================
    public static abstract class MyResponse<T> implements Callback<T>
    {
        @Override
        public void onResponse(Call<T> call, Response<T> response)
        {
            if(response.isSuccessful())
            {
                onSuccess(response.body());
            }
            else
            {
                //response.code()
                onFail(new Throwable("cos poszlo nie tak na serwerze"));
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t)
        {
            onFail(t);
        }

        abstract void onSuccess(T model);
        abstract void onFail(Throwable t);
    }
}