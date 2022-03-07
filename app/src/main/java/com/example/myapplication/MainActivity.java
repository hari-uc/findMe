package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Interface.WebServiceInterface;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button Btn_Login,mapbtn;
    EditText Edt_email,Edt_password;
    Intent mapintent, newuserintent;
    TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Btn_Login=findViewById(R.id.loginbtn);
        mapbtn = findViewById(R.id.skip2mapbtn);
        Edt_email=findViewById(R.id.usremailbox);
        Edt_password=findViewById(R.id.usrpassbox);
        textView= findViewById(R.id.newuserlink);

        Btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if (Edt_email.getText().toString().trim().equals(""))
                {
                    Edt_email.setError("Enter Valid email id");
                    Edt_email.requestFocus();


                }
                else if (Edt_password.getText().toString().trim().equals(""))
                {
                    Edt_password.setError("Password Required");
                    Edt_password.requestFocus();
                }
                else
                {
                    slotFetch();

                }
            }


        });
        mapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapintent = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(mapintent);
            }
        });


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeHyperLink();
            }
        });




    }



    private void slotFetch() {
        final String email = Edt_email.getText().toString();
        final String password = Edt_password.getText().toString();



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServiceInterface.weburl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        WebServiceInterface api = retrofit.create(WebServiceInterface.class);
        Call<String> call = api.getSlotData(email,password);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body());

                if (response.isSuccessful())
                {
                    mapintent = new Intent(getApplicationContext(),MapsActivity.class);
                    startActivity(mapintent);

                    if (response.body() != null)
                    {
                        Log.i("OnSuccess", response.body().toString());

                        String jsonResponse = response.body().toString();


                        try {

                            slotData(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.e("","DataFailed response");


            }
        });
    }

    private void slotData(String jsonResponse) throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonResponse);
        if (jsonObject.optString("status").equals("true")) {

            Toast.makeText(MainActivity.this,"Datafetched succesfully:",Toast.LENGTH_LONG).show();

        }


        if (jsonObject.optString("status").equals("false")) {

            Toast.makeText(MainActivity.this,"Data fetched Failure:",Toast.LENGTH_LONG).show();

        }
    }

    public void makeHyperLink(){

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        newuserintent = new Intent(getApplicationContext(),NewUserRegistration.class);
        startActivity(newuserintent);
    }
}