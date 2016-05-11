package net.ddns.opetany.engineeringquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResultActivity extends AppCompatActivity {

    //adres pliku php do obsługi bazy MySQL
    static final String URL_result = "http://opetany.ddns.net/android_mysql_connect/result.php";

    //zmienne do wyświetlania lvl
    TextView lvlCntView;
    int lvlCntInt;

    private String login;
    private String parameters;

    //objekt SharedPreferences do zapamiętania nazwy użytkownika
    SharedPreferences rememberUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //pobierz SharedPreferences
        rememberUserName = getSharedPreferences ( getString(R.string.loginActivity_preference_file_key) ,  Context.MODE_PRIVATE);

        //Wyświetlanie LVl na ekranie
        lvlCntView = (TextView) findViewById(R.id.lvlTextView);
        lvlCntInt = getIntent().getIntExtra("LVL_CNT_INT", 1);
        lvlCntView.setText("Lvl " + lvlCntInt);

        //Pobieranie z SharedPref
        login = rememberUserName.getString("LOGIN" , "Android");




        // wykonanie wysłania wyniku do bazy danych
        new resultTask().execute();
    }

    private class resultTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            //Zapytanie POST do result.php
            parameters = "lvl=" + lvlCntInt + "&user=" + login;
            //Toast.makeText(getApplicationContext(), parameters, Toast.LENGTH_SHORT).show();
            // TEN TOAST WYKRACZA APKĘ

            try {
                //Utworzenie połączenia
                URL url = new URL(URL_result);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.connect();

                //wysłanie zapytania POST
                OutputStreamWriter request = new OutputStreamWriter(connection.getOutputStream());
                request.write(parameters);
                request.flush();
                request.close();

                //odczyt odpowiedz
//                String line;
//                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                StringBuilder stringBuilder = new StringBuilder();
//
//                while ((line = bufferedReader.readLine()) != null) stringBuilder.append(line);
//
//                //konwersja otrzymanej odpowiedzi w formie stringa do objektu JSON'a
//                jsonObject = new JSONObject(stringBuilder.toString());
//
//                inputStreamReader.close();
//                bufferedReader.close();

                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
//             catch (JSONException e) {
//                e.printStackTrace();
//            }
            return null;
        }

//        @Override
//        protected void onPostExecute(Void result) {
//
//            try {
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//
//            }
//        }

    }
}
