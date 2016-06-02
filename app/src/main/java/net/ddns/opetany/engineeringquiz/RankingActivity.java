package net.ddns.opetany.engineeringquiz;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.service.notification.NotificationListenerService;
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

import retrofit2.Call;
import retrofit2.http.POST;

import static net.ddns.opetany.engineeringquiz.ApiClient.getWebService;

public class RankingActivity extends AppCompatActivity {


    RecyclerView mRecyclerView;
    MyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);

        RankingClass Rank = new RankingClass();
        Rank.deleteAll(RankingClass.class);                          //Czyszcznie bazy

        // Task wykonuje zapytanie do bazy
        rankTask();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new MyAdapter(this);
        mRecyclerView.setAdapter(adapter);


    }
//=======================================================================================
    @Override
    protected void onResume() {
        super.onResume();

        adapter.loadData(this);
        adapter.notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView editionTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.title);
            editionTextView = (TextView) itemView.findViewById(R.id.edition);
        }
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        List<RankingClass> dataArray;

        public MyAdapter(Context context) {
            loadData(context);
        }

        public void loadData(Context context) {
            //TODO: odczytać zawartość pliku
            dataArray = RankingClass.listAll(RankingClass.class);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            //TODO uzupełnić dane
            holder.titleTextView.setText(dataArray.get(position).login);
            holder.editionTextView.setText(String.valueOf(dataArray.get(position).lvl));
        }

        @Override
        public int getItemCount() {
            return dataArray.size();
        }
    }



    // wyslanie wyniku do bazy danych
    private void rankTask(){
        int flag_post = 1;


            final Call<List<RankJSON>> rankCall = getWebService().Rank(flag_post);

            rankCall.enqueue(new ApiClient.MyResponse<List<RankJSON>>() {

                @Override
                void onSuccess(List<RankJSON> answer) {

                    //Zapisuje do bazy 10 najlepszych
                    for( int i =0; i<10 ; i++){
                       RankingClass Rank1 = new RankingClass(answer.get(i).login , answer.get(i).result);
                        Rank1.save();
                    }
                }

                @Override
                void onFail(Throwable t){
                CharSequence text = getString(R.string.noInternetConnection);
                Toast.makeText(RankingActivity.this, text, Toast.LENGTH_SHORT).show();

                }

             });

    }
}

