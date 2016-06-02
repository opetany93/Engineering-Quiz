package net.ddns.opetany.engineeringquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity
{
    SharedPreferences loginSharedPref;
    String login;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //pobierz SharedPreferences
        loginSharedPref = getSharedPreferences ( getString(R.string.loginActivity_preference_file_key), Context.MODE_PRIVATE);

        if(loginSharedPref.contains("login"))
        {
            login = loginSharedPref.getString("login", "guest" );
            if( getSupportActionBar() != null)  getSupportActionBar().setTitle("Zalogowany jako: " + login);
        }
    }

    public void StartQuiz(View view)
    {
        Intent intent = new Intent(MenuActivity.this, QuizActivity.class);
        startActivity(intent);
        finish();
    }

    public void LogOut(View view)
    {
        SharedPreferences.Editor editor;
        editor = loginSharedPref.edit ();
        editor.putBoolean ("logged", false);
        editor.remove("login");
        editor.apply ();

        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void Ranking(View view)
    {
        Intent intent = new Intent(MenuActivity.this, RankingActivity.class);
        startActivity(intent);
    }
}
