package net.ddns.opetany.engineeringquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class LoginActivity extends AppCompatActivity
{


    private static String compare_user = "http://opetany.ddns.net/site/android_mysql_connect/compare_user.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }


    public void registrationButton(View view)
    {
        Intent intent = new Intent(this, RegistrationActivity.class);

        startActivity(intent);
        finish();
    }

    public void login(View view)
    {

    }
}
