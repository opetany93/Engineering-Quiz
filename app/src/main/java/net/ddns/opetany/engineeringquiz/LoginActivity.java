package net.ddns.opetany.engineeringquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginActivity extends AppCompatActivity
{
    //adres pliku php do obsługi bazy MySQL
    static final String URL_login = "http://opetany.ddns.net/android_mysql_connect/login.php";

    //zmienne do loginu i hasla
    private String login;
    private String password;

    //objekty do login taska i json'a
    private ProgressBar progressBar;
    private JSONObject jsonObject;
    private int loginSuccess;

    private EditText login_object;
    private EditText pass_object;
    CheckBox rememberMeCheckBox;

    //objekt SharedPreferences do zapamiętania, że użytkownik jest zalogowany
    SharedPreferences rememberMeSharedPref;
    SharedPreferences rememberUserName;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);

        //pobierz SharedPreferences
        rememberMeSharedPref = getSharedPreferences (getString(R.string.loginActivity_preference_file_key), Context.MODE_PRIVATE);
        rememberUserName = getSharedPreferences (getString(R.string.loginActivity_preference_file_key), Context.MODE_PRIVATE);

        //sprawdź czy zawiera pole "loogged"
        if ( rememberMeSharedPref.contains ("logged") )
        {
            //pobierz wartość pola "logged"
            if (rememberMeSharedPref.getBoolean ("logged", false))
            {
                Intent intent = new Intent (LoginActivity.this, MenuActivity.class);
                startActivity (intent);
                finish ();
            }
        }

        progressBar = (ProgressBar) findViewById (R.id.progressBar);
        progressBar.setVisibility (ProgressBar.INVISIBLE);

        login_object = (EditText) findViewById (R.id.login_EditText);
        pass_object = (EditText) findViewById (R.id.password_EditText);
        rememberMeCheckBox = (CheckBox) findViewById (R.id.rememberMeCheckBox);
    }

    public void loginOnClick (View view)
    {
        //pobierz tekst z pól do zmiennych
        login = login_object.getText ().toString ();
        password = pass_object.getText ().toString ();

        SharedPreferences.Editor edit;
        edit= rememberUserName.edit();
        edit.putString("login", login);
        edit.apply();

        //sprawdź czy jest połączenie
        if ( isOnline() )
        {
            //załącz taska do logowania
            new loginTask().execute();
        }
        else
        {
            CharSequence text = getString(R.string.noInternetConnection);
            Toast.makeText (LoginActivity.this, text, Toast.LENGTH_SHORT).show();
        }
    }

    private  class loginTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            //pokaż progressBar
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            //zapytanie POST do login.php
            String parameters = "login="+ login +"&password=" + password;

            try
            {
                //utworzenie połączenia
                URL url = new URL(URL_login);
                HttpURLConnection connection =(HttpURLConnection) url.openConnection ();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.connect();

                //wysłanie zapytania POST
                OutputStreamWriter request = new OutputStreamWriter( connection.getOutputStream() );
                request.write(parameters);
                request.flush();
                request.close();

                //odczyt odpowiedz od pliku login.php
                String line;
                InputStreamReader inputStreamReader = new InputStreamReader( connection.getInputStream() );
                BufferedReader bufferedReader = new BufferedReader( inputStreamReader );
                StringBuilder stringBuilder = new StringBuilder();

                while (( line = bufferedReader.readLine() ) != null) stringBuilder.append(line);

                //konwersja otrzymanej odpowiedzi w formie stringa do objektu JSON'a
                jsonObject = new JSONObject ( stringBuilder.toString() );

                inputStreamReader.close();
                bufferedReader.close();

                connection.disconnect();
            }
            catch (IOException | JSONException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            //schowaj progressBar
            progressBar.setVisibility(ProgressBar.INVISIBLE);

            try
            {
                //element z tablicy JSON'a do zmiennej calkowitej
                loginSuccess = jsonObject.getInt("success");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            if ( loginSuccess == 1 )
            {
                //sprawdż czy checkbox jest zaznaczony
                if( rememberMeCheckBox.isChecked() )
                {

                    SharedPreferences.Editor editor;
                    editor = rememberMeSharedPref.edit();
                    editor.putBoolean("logged", true);

                    editor.apply();
                }

                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
            else if( loginSuccess == -1 )
            {
                CharSequence text = getString(R.string.wrong_password);
                Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show ();
            }
            else if( loginSuccess == -2 )
            {
                CharSequence text = getString(R.string.wrong_login);
                Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show ();
            }
            else
            {
                CharSequence text = getString(R.string.registrationLoginError);
                Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show ();
            }
        }
    }

    public void registrationButtonOnClick (View view)
    {
        Intent intent = new Intent (LoginActivity.this, RegistrationActivity.class);

        startActivity (intent);
    }

    public boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}