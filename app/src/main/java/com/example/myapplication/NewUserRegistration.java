package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.API.ApiClient;
import com.example.myapplication.Interface.WebServiceInterface;
import com.example.myapplication.Model.UserRequest;
import com.example.myapplication.Model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NewUserRegistration extends AppCompatActivity {
    WebServiceInterface webServiceInterface;
//    create retrofit instance
    private static Retrofit.Builder builder = new Retrofit.Builder()
        .baseUrl("http://192.168.1.83:5050/")
        .addConverterFactory(GsonConverterFactory.create());

    public static  Retrofit retrofit = builder.build();



    EditText usrname, usrpass, usrmail , usrmobile;

    Button createaccBtn;
    public static final String BASE_URL = "http://192.168.1.83:5050/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_registration);

        usrname = findViewById(R.id.nusrtxtbox);
        usrpass = findViewById(R.id.nusrpassbox);
        usrmail = findViewById(R.id.nusermailbox);
        usrmobile = findViewById(R.id.usrmobibox);

        createaccBtn = findViewById(R.id.newuserbtn);








    createaccBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveuser(createRequest());

        }
    });

    }





    public UserRequest createRequest(){
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(usrname.getText().toString());
        userRequest.setPassword(usrpass.getText().toString());
        userRequest.setEmail(usrmail.getText().toString());
        userRequest.setMobile(usrmobile.getText().toString());
        return userRequest;
    }

    public void saveuser(UserRequest userRequest){
        Call<UserResponse> call = ApiClient.getUserservice().saveuser(userRequest);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"saved success",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"failed successfully",Toast.LENGTH_SHORT).show();

            }
        });
    }

}



