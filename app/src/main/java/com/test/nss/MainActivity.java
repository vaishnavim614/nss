package com.test.nss;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import com.test.nss.api.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class
MainActivity extends AppCompatActivity {
    MotionLayout motionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("KEY", MODE_PRIVATE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_main);

        motionLayout = findViewById(R.id.actMainMotionLayout);
        //motionLayout.transitionToEnd();

        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {
                if (sharedPreferences.getBoolean("isAboutDone", false) && sharedPreferences.getInt("logged", 0) == 1 && isNetworkAvailable()) {
                    Log.e("MA", "onTransitionCompleted: ");
                    Call<ResponseBody> call2 = RetrofitClient.getInstance().getApi().isLeader("Token " + sharedPreferences.getString("AUTH_TOKEN", ""));
                    call2.enqueue(new Callback<ResponseBody>() {
                        int isLeader;
                        int leaderId;

                        @Override
                        @EverythingIsNonNull
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            SharedPreferences.Editor eddy = sharedPreferences.edit();
                            if (response.isSuccessful() && response.body() != null) {
                                try {
                                    JSONArray j2 = new JSONArray(response.body().string());
                                    if (j2.length() > 0) {
                                        isLeader = 1;
                                        eddy.putInt("isLeader", 1);
                                        eddy.putInt("leaderId", j2.getJSONObject(0).getInt("id"));
                                        isLeader = 1;
                                        leaderId = j2.getJSONObject(0).getInt("id");
                                    } else {
                                        eddy.putInt("isLeader", 0);
                                        eddy.putInt("leaderId", 0);
                                        isLeader = 0;
                                    }
                                    eddy.apply();
                                    Intent intent;
                                    if (sharedPreferences.getInt("logged", 0) == 1) {
                                        if (!sharedPreferences.getBoolean("isAboutDone", false))
                                            intent = new Intent(MainActivity.this, About.class);
                                        else
                                            intent = new Intent(MainActivity.this, ediary.class);
                                    } else {
                                        intent = new Intent(MainActivity.this,
                                                startActivity.class);
                                    }
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    finish();
                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if ((response.errorBody() != null)) {
                                    try {
                                        Log.e("AA", response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                Intent intent;
                                if (sharedPreferences.getInt("logged", 0) == 1) {
                                    if (!sharedPreferences.getBoolean("isAboutDone", false))
                                        intent = new Intent(MainActivity.this, About.class);
                                    else
                                        intent = new Intent(MainActivity.this, ediary.class);
                                } else {
                                    intent = new Intent(MainActivity.this,
                                            startActivity.class);
                                }
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                            }
                        }

                        @Override
                        @EverythingIsNonNull
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Intent intent;
                            if (sharedPreferences.getInt("logged", 0) == 1) {
                                if (!sharedPreferences.getBoolean("isAboutDone", false))
                                    intent = new Intent(MainActivity.this, About.class);
                                else
                                    intent = new Intent(MainActivity.this, ediary.class);
                            } else {
                                intent = new Intent(MainActivity.this,
                                        startActivity.class);
                            }
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }
                    });
                } else {
                    Intent intent;
                    if (sharedPreferences.getInt("logged", 0) == 1) {
                        if (!sharedPreferences.getBoolean("isAboutDone", false))
                            intent = new Intent(MainActivity.this, About.class);
                        else
                            intent = new Intent(MainActivity.this, ediary.class);
                    } else {
                        intent = new Intent(MainActivity.this,
                                startActivity.class);
                    }
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }
        });

        //new Handler().postDelayed(() -> {
        //}, SPLASH_SCREEN_TIME_OUT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        motionLayout.transitionToEnd();
        motionLayout.startLayoutAnimation();
    }

    public boolean isNetworkAvailable() {
        NetworkInfo.State wifiState;
        NetworkInfo.State mobileState;
        ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = Objects.requireNonNull(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState();
        mobileState = Objects.requireNonNull(cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState();

        boolean a = NetworkInfo.State.CONNECTED == wifiState;
        boolean b = NetworkInfo.State.CONNECTED == mobileState;

        Log.e("AAA", a + "" + b);
        ConnectivityManager connectivityManager
                = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected() && (a || b));
    }
}