package net.ddns.opetany.engineeringquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginActivity extends AppCompatActivity
{
    public static final String MY_JSON ="MY_JSON";

    private static final String JSON_URL = "http://opetany.ddns.net/site/android_mysql_connect/compare_user.php";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }


    public void registrationButton(View view)
    {
        Intent intent = new Intent(this, RegistrationActivity.class);

        startActivity(intent);
        finish();
    }

    public void login(View view)
    {

    }

    private void getJSON(String url)
    {
        class GetJSON extends AsyncTask<String, Void, String>
        {
            ProgressDialog loading;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this, "Please Wait...",null,true,true);
            }

            @Override
            protected String doInBackground(String... params)
            {
                String uri = params[0];

                BufferedReader bufferedReader = null;

                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));


                    String json;
                    while((json = bufferedReader.readLine())!= null)
                    {
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }
                catch(Exception e)
                {
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                textViewJSON.setText(s);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }
}
