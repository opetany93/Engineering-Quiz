package net.ddns.opetany.engineeringquiz;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * Created by opetany on 06.04.2016.
 */
public class GetLoginTask extends AsyncTask<String, Void, Array<JSONresponse>>
{
    @Override
    protected Array<JSONresponse> doInBackground(String... params)
    {
        Array<JSONresponse> array = new Array ();

        try
        {
            //ustaw połączenie i rodzaj metody request
            URL url = new URL (params[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection ();
            httpURLConnection.setRequestMethod ("POST");
            httpURLConnection.connect ();

            //odczytaj odpowiedź
            InputStream inputStream = httpURLConnection.getInputStream ();
            InputStreamReader inputStreamReader = new InputStreamReader (inputStream);
            BufferedReader bufferedReader = new BufferedReader (inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder ();
            String line;

            while((line = bufferedReader.readLine ()) != null) stringBuilder.append(line);

            String data = stringBuilder.toString ();
            JSONArray jsonArray = new JSONArray ();

            //============= tu skonczylem
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
