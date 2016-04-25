package net.ddns.opetany.engineeringquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void StartQuiz(View view)
    {
        Intent intent = new Intent(MenuActivity.this, QuizActivity.class);
        startActivity(intent);
        finish();
    }

    public void LogOut(View view)
    {
        //pobierz SharedPreferences
        SharedPreferences rememberMeSharedPref = getSharedPreferences ( getString(R.string.loginActivity_preference_file_key),
                                                                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = rememberMeSharedPref.edit ();
        editor.putBoolean ("logged", false);
        editor.apply ();


        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
