package net.ddns.opetany.engineeringquiz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
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
import java.util.Random;


public class QuizActivity extends AppCompatActivity {
    //adres pliku php do obsługi bazy MySQL
    static final String URL_question = "http://opetany.ddns.net/android_mysql_connect/question.php";

   // private ProgressBar progressBar;

    private JSONObject jsonObject;

    TextView QuestionTextView;
    TextView lvlCntView;

    Button Ask1Button;
    Button Ask2Button;
    Button Ask3Button;
    Button Ask4Button;

    //zmienna do obliczania lvl
    int questionNumber = 1;
    int lvlCntInt = 1;
    //wskaznik na prawidlowa odp
    int good_ans;

    int id_question;
    int k=0;
    int tablica[] = new int[3];

    String ask1;
    String ask2;
    String ask3;
    String ask4;

    String question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionNumber = getIntent().getIntExtra("QESTION_NUMBER", 1);
        lvlCntInt = getIntent().getIntExtra("LVL_CNT_INT", 1);

        QuestionTextView = (TextView) findViewById(R.id.QuestionTextView);
        Ask1Button = (Button) findViewById(R.id.Ask1Button);
        Ask2Button = (Button) findViewById(R.id.Ask2Button);
        Ask3Button = (Button) findViewById(R.id.Ask3Button);
        Ask4Button = (Button) findViewById(R.id.Ask4Button);
        lvlCntView = (TextView) findViewById(R.id.LevelCounterTextView);

        // Task wykonuje zapytanie do bazy
        new questionTask().execute();

        lvlCntView.setText("LvL " + lvlCntInt);
    }

    private class questionTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            Intent intent = new Intent();

            //Zapytanie POST do question.php
            Random rand = new Random();
            id_question = rand.nextInt(7);
           //Sprawdzanie czy ID się nie powtórzyło

            k = getIntent().getIntExtra("CNT", 0);

            if(k == 0) intent.putExtra("ID0", id_question);
            if(k == 1){
                intent.putExtra("ID1", id_question);
                while (id_question == getIntent().getIntExtra("ID0",-1)){
                    id_question = rand.nextInt(7);
                }
            }
            if(k == 2){
                while ((id_question == getIntent().getIntExtra("ID0",-1)) && (id_question == getIntent().getIntExtra("ID1",-1))){
                    id_question = rand.nextInt(7);
                }
            }
            k++;
            intent.putExtra("CNT", k);

            String parameters = "lvl=" + lvlCntInt + "&id=" + id_question;

            try {
                //Utworzenie połączenia
                URL url = new URL(URL_question);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.connect();

                //wysłanie zapytania POST
                OutputStreamWriter request = new OutputStreamWriter(connection.getOutputStream());
                request.write(parameters);
                request.flush();
                request.close();

                //odczyt odpowiedz od pliku question.php
                String line;
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) stringBuilder.append(line);

                //konwersja otrzymanej odpowiedzi w formie stringa do objektu JSON'a
                jsonObject = new JSONObject(stringBuilder.toString());

                inputStreamReader.close();
                bufferedReader.close();

                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            try {

                //elementy z tablicy JSON'a do zmiennej całkowitej
                question = jsonObject.getString("question");
                ask1 = jsonObject.getString("ans1");
                ask2 = jsonObject.getString("ans2");
                ask3 = jsonObject.getString("ans3");
                ask4 = jsonObject.getString("ans4");
                good_ans = jsonObject.getInt("good_ans");

                //Wyświetlanie pobranych wartości
                QuestionTextView.setText(question);
                Ask1Button.setText(ask1);
                Ask2Button.setText(ask2);
                Ask3Button.setText(ask3);
                Ask4Button.setText(ask4);

                } catch (JSONException e) {
                e.printStackTrace();

            }
        }

    }

        // -----------------------------------------------------------> Poniżej sprawdzanie poprawności

        public void
        checkAsk(Integer idAsk) {
            Intent intent = new Intent();
            if (idAsk == good_ans) {
                // Co trzecie pytanie zwiększamy lvl
                if((questionNumber % 3) == 0) {
                    lvlCntInt++;
                    intent.putExtra("CNT", 0);
                    intent.putExtra("IDO", -1);
                    intent.putExtra("ID1", -1);
                }
                questionNumber++;

                intent = new Intent(this, QuizActivity.class);
                intent.putExtra("QESTION_NUMBER", questionNumber);
                intent.putExtra("LVL_CNT_INT", lvlCntInt);

                Toast.makeText(getApplicationContext(), "GOOD!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            } else {
                intent = new Intent(this, ResultActivity.class);
                intent.putExtra("LVL_CNT_INT", lvlCntInt);
                startActivity(intent);
                finish();
            }
        }



    //-------------------------------------------------------------> Obsluga przycisków

        public void Ask1ButtonClick(View view) {
            checkAsk(1);
        }

        public void Ask2ButtonClick(View view) {
            checkAsk(2);
        }

        public void Ask3ButtonClick(View view) {
            checkAsk(3);
        }

        public void Ask4ButtonClick(View view) {
            checkAsk(4);
        }

}
