package com.example.studit.study.registerstudy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.studit.R;
import com.example.studit.retrofit.Link;
import com.example.studit.retrofit.RetrofitInterface;
import com.example.studit.retrofit.study.registerstudy.ModelRegisterStudy;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RegisterStudyActivity extends Activity {
    final private String TAG = getClass().getSimpleName();

    Link link = new Link();

    List sMessage;

    private ArrayAdapter adapter;
    EditText title_regi, content_regi;
    Spinner activity_regi;
    Button regi_button;
    ImageView regi_close;
    String userid = "";

    String Title, Activity;

    Intent intent;

//    private final ArrayList<RegisterStudyModel> RegisterList = new ArrayList<>();
//    RegisterStudyAdapter adapter;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_study);

        Intent intent = new Intent(this.getIntent());
        // userID를 변수로 받음 ( 수정필요할듯 토큰을 받아오는 형식으로 ..? 아니면 본인 유저아이디 받아오도록)
        userid = getIntent().getStringExtra("userID");

        preferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");

        //컴포넌트 초기화
        title_regi = findViewById(R.id.title_regi);
        content_regi = findViewById(R.id.comment_regi);
        regi_button = findViewById(R.id.regi_button);
        regi_close = findViewById(R.id.regi_close);

        // 스피너
        activity_regi = findViewById(R.id.activity_spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.activity, android.R.layout.simple_dropdown_item_1line);
        activity_regi.setAdapter(adapter);

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

        // 닫기 버튼 클릭시
        regi_close.setOnClickListener(view -> {
            this.finish();
        });

        // 등록 버튼 클릭시
        regi_button.setOnClickListener(view -> {
            final String title = title_regi.getText().toString();
            final String content = content_regi.getText().toString();
            final String activity = activity_regi.getSelectedItem().toString();
            Log.e(TAG, "1");

            // 정보 미기입시
            if (title.equals("") || content.equals("") ||  activity.equals("")) {
                Log.e(TAG, "내용 입력 필요");
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterStudyActivity.this);
                builder.setTitle("알림")
                        .setMessage("정보를 모두 기입해주세요.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {

                RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
                ModelRegisterStudy modelRegisterStudy = new ModelRegisterStudy(activity, title, content);
                Call<ModelRegisterStudy> call = retrofitInterface.postRegisterStudy(modelRegisterStudy, "Bearer " + token);

                call.enqueue(new Callback<ModelRegisterStudy>() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(@NonNull Call<ModelRegisterStudy> call, @NonNull Response<ModelRegisterStudy> response) {
                        Log.d(TAG, "2");
                        ModelRegisterStudy responseBody = response.body();
//                    Title = responseBody.getName();
//                    Activity = responseBody.getActivity();

                        if (response.isSuccessful() && response.body() != null) {
//                        if (activity == "온라인") {
//                            String ONLINE;
//                        }
//                        if (activity == "오프라인") {
//                            String OFFLINE;
//                        }
//                        else {
//                            String INTEGRATION;
//                        }
                            Log.e(TAG, "스터디 등록 완료!");
                            Toast.makeText(RegisterStudyActivity.this, "스터디가 개설되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                            adapter.notifyDataSetChanged();

                            // 스터디 개설 성공시 팝업 닫기
                            //finish();

                        } else {
                            try {
                                String body = response.errorBody().string();
                                Log.e(TAG, "error - body" + body);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ModelRegisterStudy> call, @NonNull Throwable t) {
                        Log.e(TAG, "faillllllllllllll" + t.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public boolean onTouchEvent (MotionEvent event){
        // 바깥 레이어 클릭시 닫히지 않도록
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    private OkHttpClient provideOkHttpClient () {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);
        return okhttpClientBuilder.build();
    }

}

