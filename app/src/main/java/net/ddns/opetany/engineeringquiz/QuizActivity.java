package net.ddns.opetany.engineeringquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class QuizActivity extends AppCompatActivity {


    TextView    QuestionTextView;
    TextView lvlCntView;

    Button      Ask1Button;
    Button      Ask2Button;
    Button      Ask3Button;
    Button      Ask4Button;

    int questionNumber;
    int askNumber;
    int lvlCntInt;

    // Tablica dobrych odpowiedzi
    String[] questions  = new String[3];
    String[] asks       = new String[8];
    Integer[] answers   = new Integer[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

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
