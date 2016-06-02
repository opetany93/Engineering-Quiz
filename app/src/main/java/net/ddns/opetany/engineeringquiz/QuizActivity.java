package net.ddns.opetany.engineeringquiz;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Random;
import retrofit2.Call;


public class QuizActivity extends NetworkActivity {

    TextView QuestionTextView;
    TextView lvlCntView;

    Button Ask1Button;
    Button Ask2Button;
    Button Ask3Button;
    Button Ask4Button;

    ProgressBar CountDownProgressBar2;
    int progress;

    //zmienna do obliczania lvl
    int questionNumber = 1;
    int lvlCntInt = 1;

    //wskaznik na prawidlowa odp
    int good_ans;

    //zmienne do losowania ID pytania
    int id_question;
    int k=0;

    int ask;

    //flaga sygnaluzujaca lvl UP
    int lvlUP_flag =0;

    //Intent
    Intent intent = new Intent();

    CountDownTimer timDown = new CountDownTimer(10000,100){

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public void onTick(long millisUntilFinished) {
            progress -= 100;
            CountDownProgressBar2.setProgress(progress);
        }

        public void onFinish() {
            Random rand_ask = new Random();
            ask = rand_ask.nextInt(3) + 1;      //losowanie odpowiedzi po uplywie czasu
            checkAsk(ask);
        }
    };

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
        CountDownProgressBar2 = (ProgressBar) findViewById(R.id.CountDownProgressBar2);

        // Task wykonuje zapytanie do bazy
        new questionTask().execute();

        lvlCntView.setText("LvL " + lvlCntInt);

        progress = 10000;   //Wartosc do odliczania czasu na odp

        timDown.start();    //Wystartowanie timera odmierzajacego czas na odp
    }

<<<<<<< HEAD



=======
>>>>>>> parent of 77cd1c0... add xml files
    private class questionTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            //LOSOWANIE ID PYTANIA
            Random rand = new Random();
            id_question = rand.nextInt(7);
            //Sprawdzanie czy ID się nie powtórzyło

            k = getIntent().getIntExtra("CNT", 0);                          //Pobieranie k, jeżeli nie ma nic w CNT to k=0

            if(k == 0) intent.putExtra("ID0", id_question);                 //Jeżeli k =0 to zapisz id w ID0
            if(k == 1){
                while (id_question == getIntent().getIntExtra("ID0",1)){   //Jeżeli k = 1 ( drugie pyt_ to losuuj do puki bedzie unikatowe i zapisz do ID1
                    id_question = rand.nextInt(7);
                }
                intent.putExtra("ID1", id_question);
            }
            if(k == 2){                                                      //Jeżeli k = 2 ( trzecie pyt_ to  losuuj do puki bedzie unikatowe
                while ((id_question == getIntent().getIntExtra("ID0",1)) && (id_question == getIntent().getIntExtra("ID1",1))){
                    id_question = rand.nextInt(7);
                }
            }
            k++;
            if(k == 3 ) k = 0;                                                 //zabezpieczenie przed wyjsciem poza zakres
            intent.putExtra("CNT", k);                                         // zapisz do CNT aktualnej wartości k


            // =============================== dodany/zmieniony przeze mnie kod na retrofit'a =================================

            final Call<QuestionJSON> questionCall = getWebService().Question(lvlCntInt, id_question);

            questionCall.enqueue(new ApiClient.MyResponse<QuestionJSON>()
            {
                @Override
                void onSuccess(QuestionJSON answer)
                {
                    //Wyświetlanie pobranych wartości
                    QuestionTextView.setText(answer.question);
                    Ask1Button.setText(answer.ans1);
                    Ask2Button.setText(answer.ans2);
                    Ask3Button.setText(answer.ans3);
                    Ask4Button.setText(answer.ans4);

                    good_ans = answer.good_ans;
                }

                @Override
                void onFail(Throwable t)
                {
                    CharSequence text = getString(R.string.noInternetConnection);
                    Toast.makeText(QuizActivity.this, text, Toast.LENGTH_SHORT).show();
                }
            });
            // ================================================================================================================

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }
    // -----------------------------------------------------------> Poniżej sprawdzanie poprawności

    public void
    checkAsk(Integer idAsk) {

        timDown.cancel();           //wylaczenie timera odliczajacego czas na odp

        if (idAsk == good_ans) {
            // Co trzecie pytanie zwiększamy lvl
            if((questionNumber % 3) == 0) {
                lvlCntInt++;
                intent.putExtra("CNT", 0);

                lvlUP_flag = 1;
            }
            questionNumber++;

            if(lvlUP_flag == 0){
                intent = new Intent(this, QuizActivity.class);
            }

            if(lvlUP_flag == 1){
                lvlUP_flag = 0;
                intent = new Intent(this, lvlUP.class);
            }

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