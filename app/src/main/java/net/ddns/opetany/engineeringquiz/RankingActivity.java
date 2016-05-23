package net.ddns.opetany.engineeringquiz;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.orm.SugarContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.http.POST;

public class RankingActivity extends AppCompatActivity {

    //adres serwera do obsługi bazy MySQL
    static final String URL_rank = "http://opetany.ddns.net/android_mysql_connect/rank.PHP";

    //objekt JSONA
    private JSONObject jsonObject;


    //Zmienne dla rankigu
   String user1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);

        // Task wykonuje zapytanie do bazy
        new rankTask().execute();


    }



    // wyslanie wyniku do bazy danych
    private class rankTask extends AsyncTask<Void, Void, Void> {
        int flag_post = 1;
        String parameters = "flag=" + flag_post;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                //Utworzenie połączenia
                URL url = new URL(URL_rank);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.connect();

                //wysłanie zapytania POST
                OutputStreamWriter request = new OutputStreamWriter(connection.getOutputStream());
                request.write(parameters);
                request.flush();
                request.close();


                //NOTATKI PROGRAMISTY: Całe wysłanie zapytanie do bazy nie działa bez poniższych lini
                //odczyt odpowiedz
                String line;
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) stringBuilder.append(line);

                //konwersja otrzymanej odpowiedzi w formie stringa do objektu JSON'a
                jsonObject = new JSONObject(stringBuilder.toString());

                inputStreamReader.close();
                bufferedReader.close();

                connection.disconnect();       //zamknięcie connectoin
            } catch (IOException e) {
                e.printStackTrace();

            }
            catch (JSONException e) {
                e.printStackTrace();

            }
            return null;
        }

  //      @Override
  //      protected void onPostExecute(Void result) {

 //           try {
                //user1 = jsonObject.getString("login");

//            } catch (JSONException e) {
//                e.printStackTrace();
//            }


 //       }

    }


}


