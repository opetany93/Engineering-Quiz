package net.ddns.opetany.engineeringquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class lvlUP extends AppCompatActivity {

    //zmienne do wyświetlania lvl
    TextView lvlCntView;
    int lvlCntInt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lvlup);

        lvlCntView = (TextView) findViewById(R.id.lvlUpTextView);
        lvlCntInt = getIntent().getIntExtra("LVL_CNT_INT", 1);
        lvlCntView.setText("Lvl " + lvlCntInt);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    Intent intent = new Intent(lvlUP.this, QuizActivity.class);
                    intent.putExtra("LVL_CNT_INT", lvlCntInt);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}