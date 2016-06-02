package net.ddns.opetany.engineeringquiz;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.util.List;
import retrofit2.Call;

import static net.ddns.opetany.engineeringquiz.ApiClient.getWebService;


public class RankingActivity extends AppCompatActivity {

    //adres serwera do obsługi bazy MySQL
    static final String URL_rank = "http://opetany.ddns.net/android_mysql_connect/rank.php";

    //objekt JSONA
    private JSONObject jsonObject;

    //Zmienne dla rankigu
     String user1,user2;

    RecyclerView mRecyclerView;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);

        // Task wykonuje zapytanie do bazy
        new rankTask().execute();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new MyAdapter(this);
        mRecyclerView.setAdapter(adapter);

        RankingClass Rank = new RankingClass("lvl", 1);
        Rank.save();
    }

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
    private class rankTask extends AsyncTask<Void, Void, Void> {
        int flag_post = 1;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            final Call<List<RankJSON>> rankCall = getWebService().Rank(flag_post);

            rankCall.enqueue(new ApiClient.MyResponse<List<RankJSON>>() {

                @Override
                void onSuccess(List<RankJSON> answer) {
                   user1 = answer.get(0).login;
                    Toast.makeText(getApplicationContext(), user1 , Toast.LENGTH_SHORT).show();
                }

                @Override
                void onFail(Throwable t) {
                    CharSequence text = getString(R.string.noInternetConnection);
                    Toast.makeText(RankingActivity.this, text, Toast.LENGTH_SHORT).show();
                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }

    }


}


