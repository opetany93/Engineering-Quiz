package net.ddns.opetany.engineeringquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity
{
    //adres serwera do obsługi bazy MySQL
    static final String server_URL = "http://opetany.ddns.net/android_mysql_connect/";

    //zmienne do loginu i hasla
    protected String login;
    protected String password;

    private EditText login_object;
    private EditText pass_object;
    CheckBox rememberMeCheckBox;

    //objekty do login taska i json'a
    private ProgressBar progressBar;

    //objekt SharedPreferences do zapamiętania, że użytkownik jest zalogowany
    SharedPreferences loginSharedPref;

    // =======================================================================================================================
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //pobierz SharedPreferences
        loginSharedPref = getSharedPreferences(getString(R.string.loginActivity_preference_file_key), Context.MODE_PRIVATE);

        //sprawdź czy zawiera pole "logged"
        if( loginSharedPref.contains("logged") )
        {
            //pobierz wartość pola "logged"
            if( loginSharedPref.getBoolean("logged", false) )
            {
                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        }

        progressBar = (ProgressBar) findViewById (R.id.progressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        login_object = (EditText) findViewById (R.id.login_EditText);
        pass_object = (EditText) findViewById (R.id.password_EditText);
        rememberMeCheckBox = (CheckBox) findViewById(R.id.rememberMeCheckBox);
    }

    // =======================================================================================================================
    public void loginOnClick (View view)
    {
        //pokaż progressBar
        progressBar.setVisibility(ProgressBar.VISIBLE);

        //pobierz tekst z pól do zmiennych
        login = login_object.getText().toString();
        password = pass_object.getText().toString();

        SharedPreferences.Editor edit;
        edit = loginSharedPref.edit();
        edit.putString("login", login);
        edit.apply();

        // connect
        // ustawiamy wybrane parametry adaptera
        Retrofit retrofit = new Retrofit.Builder()
                // adres API
                .baseUrl(server_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        // tworzymy klienta
        WebService webService = retrofit.create(WebService.class);

        final Call<LoginRegisterJSON> loginCall = webService.Login(login, password);

        loginCall.enqueue(new Callback<LoginRegisterJSON>()
        {
            @Override
            public void onResponse(Call<LoginRegisterJSON> call, Response<LoginRegisterJSON> response)
            {
                LoginRegisterJSON answer = response.body();

                //schowaj progressBar
                progressBar.setVisibility(ProgressBar.INVISIBLE);

                if(answer.success == 1)
                {
                    //sprawdż czy checkbox jest zaznaczony
                    if(rememberMeCheckBox.isChecked())
                    {

                        SharedPreferences.Editor editor;
                        editor = loginSharedPref.edit();
                        editor.putBoolean("logged", true);

                        editor.apply();
                    }

                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(answer.success == - 1)
                {
                    CharSequence text = getString(R.string.wrong_password);
                    Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
                }
                else if(answer.success == - 2)
                {
                    CharSequence text = getString(R.string.wrong_login);
                    Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    CharSequence text = getString(R.string.registrationLoginError);
                    Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginRegisterJSON> call, Throwable t)
            {
                CharSequence text = getString(R.string.noInternetConnection);
                Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });

    }

    // =======================================================================================================================
    public void registrationButtonOnClick (View view)
    {
        Intent intent = new Intent (LoginActivity.this, RegistrationActivity.class);

        startActivity (intent);
    }
}