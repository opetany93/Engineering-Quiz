package net.ddns.opetany.engineeringquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import retrofit2.Call;

/**
 * Created by Arkadiusz Bochyński on 14.03.2016.
 */

public class LoginActivity extends NetworkActivity
{
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

        final Call<LoginRegisterJSON> loginCall = getWebService().Login(login, password);

        loginCall.enqueue(new ApiClient.MyResponse<LoginRegisterJSON>()
          {
              @Override
              void onSuccess(LoginRegisterJSON answer)
              {
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
              void onFail(Throwable t)
              {
                  //schowaj progressBar
                  progressBar.setVisibility(ProgressBar.INVISIBLE);

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