package net.ddns.opetany.engineeringquiz;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import retrofit2.Retrofit;

/**
 * Created by Arkadiusz Bochyński on 23.05.2016.
 */
public class NetworkActivity extends AppCompatActivity
{
    public Retrofit getRetrofit()
    {
        return ApiClient.getRetrofit();
    }

    public void showError(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT ).show();
    }
}
