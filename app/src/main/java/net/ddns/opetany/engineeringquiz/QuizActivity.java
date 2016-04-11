package net.ddns.opetany.engineeringquiz;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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


public class QuizActivity extends AppCompatActivity {


    //adres pliku php do obsługi bazy MySQL
    static final String URL_login = "http://opetany.ddns.net/android_mysql_connect/question.php";

    TextView    QuestionTextView;
    TextView    lvlCntView;

    Button      Ask1Button;
    Button      Ask2Button;
    Button      Ask3Button;
    Button      Ask4Button;

    //objekty do zapytania question
    ProgressBar progressBar;
    JSONObject jsonObject;

    //ZMIENNE DO PYTAN UMIESZCZONYCH W APLIKKACJI (ICH NIE BEDZIE)
    int questionNumber;
    int askNumber;

    // LICZNIK LVL
    int lvlCntInt;
    // Wskaznik na dobra odp
    int good_ans;

    // Tablica (DO PYTAN UMIESZCZONYCH W APCE) Tego nie będzie
    String[] questions  = new String[3];
    String[] asks       = new String[8];
    Integer[] answers   = new Integer[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        progressBar = (ProgressBar) findViewById (R.id.progressBar);
        progressBar.setVisibility (ProgressBar.INVISIBLE);

        questionNumber    = getIntent().getIntExtra("QESTION_NUMBER", 0);
        lvlCntInt         = getIntent().getIntExtra("LVL_CNT_INT",1);
        answers[0]        = 2;
        answers[1]        = 3;
        // Wypelnienie pytan

        questions[0] = "Dokończ zdanie: Grupa cyfrowa to?";
        asks[0] = "Dno";
        asks[1] = "Elita";
        asks[2] = "Jelenie";
        asks[3] = "Cieniasy";

        questions[1] = "Lysy jest?";
        asks[4] = "Mistrzem";
        asks[5] = "Jeleniem";
        asks[6] = "Smerfem";
        asks[7] = "Aktorem";


        QuestionTextView = (TextView) findViewById(R.id.QuestionTextView);
        Ask1Button       = (Button) findViewById(R.id.Ask1Button);
        Ask2Button       = (Button) findViewById(R.id.Ask2Button);
        Ask3Button       = (Button) findViewById(R.id.Ask3Button);
        Ask4Button       = (Button) findViewById(R.id.Ask4Button);
        lvlCntView       = (TextView) findViewById(R.id.LevelCounterTextView);

        lvlCntView.setText("LvL " + lvlCntInt);
        QuestionTextView.setText(questions[questionNumber]);
        askNumber = questionNumber * 4;
        Ask1Button.setText(asks[askNumber]);
        Ask2Button.setText(asks[askNumber + 1]);
        Ask3Button.setText(asks[askNumber + 2]);
        Ask4Button.setText(asks[askNumber + 3]);


        new questionTask ().execute ();
    }

    private class questionTask extends AsyncTask<Void, Void, Void>
    {


        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility (ProgressBar.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Zapytanie POST do login.php
            String parameters = "lvl=" + lvlCntInt + "&id=" + askNumber;

                try
                {
                    //Utworzenie połączenia
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

                    //odczyt odpowiedz od pliku question.php
                    String line;
                    InputStreamReader inputStreamReader = new InputStreamReader (connection.getInputStream ());
                    BufferedReader bufferedReader = new BufferedReader (inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder ();

                    while ((line = bufferedReader.readLine ()) != null) stringBuilder.append (line);

                    //konwersja otrzymanej odpowiedzi w formie stringa do objektu JSON'a
                    jsonObject = new JSONObject(stringBuilder.toString());

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
            //elementy z tablicy JSON'a do zmiennej całkowitej
            QuestionTextView = jsonObject.getString("question");
            Ask1Button       = jsonObject.getString("ans1");
            Ask2Button       = jsonObject.getString("ans2");
            Ask3Button       = jsonObject.getString("ans3");
            Ask4Button       = jsonObject.getString("ans4");
            good_ans         = jsonObject.getInt("good_ans");
        }
        catch (JSONException e)
        {
            e.printStackTrace ();
        }
    }


    // -----------------------------------------------------------> Poniżej sprawdzanie poprawności

    public void
    checkAsk (Integer idAsk){
        Intent intent;
        if(idAsk == answers[questionNumber]){
            intent = new Intent(this, QuizActivity.class);
            intent.putExtra("QESTION_NUMBER", 1);
            intent.putExtra("LVL_CNT_INT",2);
            Toast.makeText(getApplicationContext(), "GOOD!" ,Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "WRONG!" ,Toast.LENGTH_SHORT).show();
        }
    }

    //-------------------------------------------------------------> Obsluga przycisków

    public void Ask1ButtonClick(View view){
        checkAsk(1);
    }

    public void Ask2ButtonClick(View view){
        checkAsk(2);
    }

    public void Ask3ButtonClick(View view){
        checkAsk(3);
    }

    public void Ask4ButtonClick(View view){
        checkAsk(4);
    }
}

    //Klasa wyświetlająca kolejne pytania.
