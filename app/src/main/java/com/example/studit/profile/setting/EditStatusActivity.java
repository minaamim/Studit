package com.example.studit.profile.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.studit.R;
import com.example.studit.retrofit.Link;
import com.example.studit.retrofit.RetrofitInterface;
import com.example.studit.retrofit.home.profile.setting.Model_StatusMessage;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EditStatusActivity extends Activity {

    private AlertDialog dialog;

    private EditText mStatus;
    private Button bt_edit;
    private Button bt_cancel;

    private long userId;

    List sMessage;

    Intent intent;

    Link link = new Link();
    private SharedPreferences preferences;

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_status);

        mStatus = findViewById(R.id.status_edittext);

        intent = getIntent();

        preferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");

        Gson gson = new Gson();

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(link.getBASE_URL())
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();


        bt_cancel = findViewById(R.id.bt_status_cancel);
        bt_cancel.setOnClickListener(view -> {
            finish();
        });

        bt_edit = findViewById(R.id.bt_edit_status);
        bt_edit.setOnClickListener(view -> {
            final String Status = mStatus.getText().toString();

            if(Status.equals("")){
                AlertDialog.Builder builder = new AlertDialog.Builder(EditStatusActivity.this);
                dialog = builder.setMessage(("상태 메세지를 입력해주세요.")).setNegativeButton("확인",null).create();
                dialog.show();
            }

            RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
            Model_StatusMessage userStatusEdit = new Model_StatusMessage(Status);
            Call<Model_StatusMessage> call = retrofitInterface.patchStatusMessage("Bearer " + token,userStatusEdit);

            call.enqueue(new Callback<Model_StatusMessage>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<Model_StatusMessage> call, Response<Model_StatusMessage> response) {
                    Model_StatusMessage responseBody = response.body();
                    sMessage = responseBody.getMessage();
                    String aMessage = String.join(",",sMessage);

                    if(response.isSuccessful() && response.body() != null){

                        if(responseBody.getIsSuccess() == false){
                            Log.e(TAG, "상태 메세지 변경 실패 : " + aMessage); //서버에서 받은 message 보여줌
                            AlertDialog.Builder builder = new AlertDialog.Builder((EditStatusActivity.this));
                            dialog = builder.setMessage(aMessage).setNegativeButton("확인", null).create();
                            dialog.show();

                        } else {
                            Log.e(TAG, "상태 메세지 변경 성공!");
                            Toast.makeText(getApplicationContext(), "상태 메세지 변경 성공!", Toast.LENGTH_LONG).show();
                            finish();
                        }

                    } else {
                        try {
                            String body = response.errorBody().string();
                            Log.e(TAG, "상태 메세지 변경 error!!! - body : " + body);
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
                
                @Override
                public void onFailure(Call<Model_StatusMessage> call, Throwable t) {
                    Log.e(TAG, "상태 메세지 fail!!! " + t.getMessage());
                }
            });
        });

    }
}