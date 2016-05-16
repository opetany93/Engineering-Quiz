package net.ddns.opetany.engineeringquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationActivity extends AppCompatActivity
{

    //zmienne do loginu i hasla
    protected String login;
    protected String password;

    //objekty do połączenia
    private ProgressBar progressBar;
    private EditText login_object;
    private EditText pass_object;
    private EditText re_pass_object;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressBar = (ProgressBar) findViewById (R.id.progressBar_reg);
        progressBar.setVisibility (ProgressBar.INVISIBLE);

        login_object = (EditText) findViewById (R.id.registration_login_EditText);
        pass_object = (EditText) findViewById (R.id.registration_password_EditText);
        re_pass_object = (EditText) findViewById (R.id.rePassword_EditText);
    }

    public void registrationDoneButton(View view)
    {
        //pokaż progressBar
        progressBar.setVisibility(ProgressBar.VISIBLE);

        //pobierz tekst z pól do zmiennych
        login = login_object.getText().toString();
        password = pass_object.getText().toString ();
        String re_password = re_pass_object.getText().toString();

        if( (password.length() <= 20) && (password.length() >= 6) && (login.length() >= 4) && (login.length() <= 15) )
        {
            if ( password.equals(re_password) )
            {
                // connect
                // ustawiamy wybrane parametry adaptera
                Retrofit retrofit = new Retrofit.Builder()
                        // adres API
                        .baseUrl(LoginActivity.server_URL)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                // tworzymy klienta
                WebService webService = retrofit.create(WebService.class);

                final Call<LoginRegisterJSON> loginCall = webService.Register(login, password);

                loginCall.enqueue(new Callback<LoginRegisterJSON>()
                {
                    @Override
                    public void onResponse(Call<LoginRegisterJSON> call, Response<LoginRegisterJSON> response)
                    {
                        LoginRegisterJSON answer = response.body();

                        //schowaj progressBar
                        progressBar.setVisibility(ProgressBar.INVISIBLE);

                        if ( answer.success == 1 )
                        {
                            Intent intent = new Intent(RegistrationActivity.this, MenuActivity.class);

                            startActivity(intent);
                            finish();
                        }
                        else if ( answer.success == -1 )
                        {
                            CharSequence text = getString(R.string.userAlreadyExist);
                            Toast.makeText(RegistrationActivity.this, text, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            CharSequence text = getString(R.string.registrationLoginError);
                            Toast.makeText(RegistrationActivity.this, text, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginRegisterJSON> call, Throwable t)
                    {
                        CharSequence text = getString(R.string.noInternetConnection);
                        Toast.makeText(RegistrationActivity.this, text, Toast.LENGTH_SHORT).show();
                    }
                });

            }
            else
            {
                //schowaj progressBar
                progressBar.setVisibility(ProgressBar.INVISIBLE);

                CharSequence text = getString(R.string.givenPassNotEquals);
                Toast.makeText(RegistrationActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            //schowaj progressBar
            progressBar.setVisibility(ProgressBar.INVISIBLE);

            //komunikat jeśli podany login jest za krótki
            if( login.length() < 4 )
            {
                CharSequence text = getString(R.string.tooShortLogin);
                Toast.makeText(RegistrationActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            //komunikat jeśli podany login jest za długi
            if( login.length() > 15 )
            {
                CharSequence text = getString(R.string.tooLongLogin);
                Toast.makeText(RegistrationActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            //komunikat jeśli podane hasło jest za krótkie
            if( password.length() < 6 )
            {
                CharSequence text = getString (R.string.tooShortPassword);
                Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if( v != null) v.setGravity(Gravity.CENTER);
                toast.show();
            }

            //komunikat jeśli podane hasło jeest za długie
            if( password.length() > 20 )
            {
                CharSequence text = getString (R.string.tooLongPassword);
                Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if( v != null) v.setGravity(Gravity.CENTER);
                toast.show();
            }
        }
    }
}