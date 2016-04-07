package net.ddns.opetany.engineeringquiz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    static final String URL_create_user = "http://opetany.ddns.net/android_mysql_connect/create_user.php";

    //zmienne do loginu i hasla
    String login;
    String password;

    //objekty do login taska i json'a
    ProgressBar progressBar;
    JSONObject jsonObject;
    int loginSuccess;

    EditText login_object;
    EditText pass_object;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);

        progressBar = (ProgressBar) findViewById (R.id.progressBar);
        progressBar.setVisibility (ProgressBar.INVISIBLE);

        login_object = (EditText) findViewById (R.id.login_EditText);
        pass_object = (EditText) findViewById (R.id.password_EditText);
    }

    public void loginOnClick (View view)
    {
        login = login_object.getText ().toString ();
        password = pass_object.getText ().toString ();

        new loginTask ().execute ();
    }

    private  class loginTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility (ProgressBar.VISIBLE);
        }

        @Override
        protected Void doInBackground (Void... params)
        {
            //zapytanie POST do login.php
            String parameters = "login="+ login +"&password=" + password;

            try
            {
                //utworzenie połączenia
                URL url = new URL (URL_login);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection ();
                connection.setDoOutput (true);
                connection.setRequestProperty ("Content-Type", "application/x-www-form-urlencoded");
                connection.connect();

                //wysłanie zapytania POST
                OutputStreamWriter request = new OutputStreamWriter (connection.getOutputStream ());
                request.write (parameters);
                request.flush ();
                request.close ();

                //odczyt odpowiedz od pliku login.php
                String line;
                InputStreamReader inputStreamReader = new InputStreamReader (connection.getInputStream ());
                BufferedReader bufferedReader = new BufferedReader (inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder ();

                while ((line = bufferedReader.readLine ()) != null) stringBuilder.append (line);

                //konwersja otrzymanej odpowiedzi w formie stringa do objektu JSON'a
                jsonObject = new JSONObject (stringBuilder.toString());

                inputStreamReader.close ();
                bufferedReader.close ();

                connection.disconnect ();
            }
            catch (IOException e)
            {
                e.printStackTrace ();
            }
            catch (JSONException e)
            {
                e.printStackTrace ();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            progressBar.setVisibility (ProgressBar.INVISIBLE);

            try
            {
                //element z tablicy JSON'a do zmiennej calkowitej
                loginSuccess = jsonObject.getInt ("success");
            }
            catch (JSONException e)
            {
                e.printStackTrace ();
            }

            if (loginSuccess == 1)
            {
                Intent intent = new Intent (LoginActivity.this, MenuActivity.class);
                startActivity (intent);
                finish ();
            }
            else
            {
                CharSequence text = getString(R.string.toast_wrong_loginOrPassword);
                Toast.makeText (LoginActivity.this, text, Toast.LENGTH_SHORT).show ();
            }
        }
    }

    public void registrationButtonOnClick (View view)
    {
        Intent intent = new Intent (this, RegistrationActivity.class);

        startActivity (intent);
    }
}
