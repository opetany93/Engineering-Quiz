package net.ddns.opetany.engineeringquiz;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class RegistrationActivity extends AppCompatActivity
{
    static final String URL_create_user = "http://opetany.ddns.net/android_mysql_connect/create_user.php";

    //zmienne do loginu i hasla
    private String login;
    private String password;

    //objekty do login taska i json'a
    private ProgressBar progressBar;
    private JSONObject jsonObject;

    private EditText login_object;
    private EditText pass_object;
    private EditText re_pass_object;

    private int registrationSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressBar = (ProgressBar) findViewById (R.id.progressBar_reg);
        progressBar.setVisibility (ProgressBar.INVISIBLE);

        login_object = (EditText) findViewById (R.id.registration_login_EditText);
        pass_object = (EditText) findViewById (R.id.registration_password_EditText);
        re_pass_object = (EditText) findViewById (R.id.rePassword_EditText);
    }

    public void registrationDoneButton(View view)
    {
        //pobierz tekst z pól do zmiennych
        login = login_object.getText().toString();
        password = pass_object.getText().toString ();
        String re_password = re_pass_object.getText().toString();

        if( (password.length() <= 20) && (password.length() >= 6) && (login.length() >= 4) && (login.length() <= 15) )
        {
            if ( password.equals(re_password) )
            {
                //sprawdź czy jest połączenie
                if ( isOnline() )
                {
                    //załącz taska do logowania
                    new registrationTask().execute();
                }
                else
                {
                    CharSequence text = getString(R.string.noInternetConnection);
                    Toast.makeText(RegistrationActivity.this, text, Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                CharSequence text = getString(R.string.givenPassNotEquals);
                Toast.makeText(RegistrationActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            //komunikat jeśli podany login jest za krótki
            if( login.length() < 4 )
            {
                CharSequence text = getString(R.string.tooShortLogin);
                Toast.makeText(RegistrationActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            //komunikat jeśli podany login jest za długi
            if( login.length() > 15 )
            {
                CharSequence text = getString(R.string.tooLongLogin);
                Toast.makeText(RegistrationActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            //komunikat jeśli podane hasło jest za krótkie
            if( password.length() < 6 )
            {
                CharSequence text = getString (R.string.tooShortPassword);
                Toast.makeText(RegistrationActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            //komunikat jeśli podane hasło jeest za długie
            if( password.length() > 20 )
            {
                CharSequence text = getString (R.string.tooLongPassword);
                Toast.makeText(RegistrationActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private  class registrationTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            //pokaż progressBar
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Void doInBackground (Void... params)
        {
            //zapytanie POST do login.php
            String parameters = "login="+ login +"&password=" + password;

            try
            {
                //utworzenie połączenia
                URL url = new URL(URL_create_user);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.connect();

                //wysłanie zapytania POST
                OutputStreamWriter request = new OutputStreamWriter(connection.getOutputStream ());
                request.write(parameters);
                request.flush();
                request.close();

                //odczyt odpowiedz od pliku create_user.php
                String line;
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream ());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();

                while ( (line = bufferedReader.readLine()) != null ) stringBuilder.append(line);

                //konwersja otrzymanej odpowiedzi w formie stringa do objektu JSON'a
                jsonObject = new JSONObject(stringBuilder.toString ());

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
                registrationSuccess = jsonObject.getInt("success");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            if ( registrationSuccess == 1 )
            {
                Intent intent = new Intent(RegistrationActivity.this, MenuActivity.class);

                startActivity(intent);
                finish();
            }
            else if ( registrationSuccess == -1 )
            {
                CharSequence text = getString(R.string.userAlreadyExist);
                Toast.makeText(RegistrationActivity.this, text, Toast.LENGTH_SHORT).show();
            }
            else
            {
                CharSequence text = getString(R.string.registrationLoginError);
                Toast.makeText(RegistrationActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
