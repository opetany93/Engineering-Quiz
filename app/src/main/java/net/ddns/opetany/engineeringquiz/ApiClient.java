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

   public static ApiClient ourInstance = new ApiClient();

    public static ApiClient getInstance()
    {
        return ourInstance;
    }

    public ApiClient()
    {
        // adres API
        Retrofit retrofit = new Retrofit.Builder()
                // adres API
                .baseUrl(server_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static Retrofit getRetrofit()
    {
       return getInstance().getRetrofit();
    }

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

    public abstract static class MyLoginResponse<T> extends MyResponse<T>
    {
        NetworkActivity networkActivity;

        public MyLoginResponse(NetworkActivity loginActivity)
        {
            this.networkActivity = loginActivity;
        }

        @Override
        void onFail(Throwable t)
        {
            networkActivity.showError(t.getMessage());
        }
    }
}
