package net.ddns.opetany.engineeringquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LoginActivity extends AppCompatActivity
{

    static final String MySQL_PHP_Script_login = "http://opetany.ddns.net/android_mysql_connect/compare_user/php";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }


    public void registrationButtonOnClick(View view)
    {
        Intent intent = new Intent(this, RegistrationActivity.class);

        startActivity(intent);
        finish();
    }

    public void loginOnClick(View view)
    {


        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

}
