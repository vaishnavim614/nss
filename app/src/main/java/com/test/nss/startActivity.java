package com.test.nss;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.test.nss.api.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class startActivity extends AppCompatActivity {

    String AUTH_TOKEN;
    String VEC;

    TextView forgotPass;
    TextView startReg;
    TextView startSummary;
    TextView startRemember;
    CheckBox startCheck;
    Button loginButton;
    EditText username;
    EditText password;
    LinearLayout linearLayout, linearLayout2;
    Context mContext;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        setContentView(R.layout.activity_start);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String date = sdf.format(new Date());
        mContext = startActivity.this;

        linearLayout = findViewById(R.id.linearLayout);
        linearLayout2 = findViewById(R.id.linearLayout2);

        startReg = findViewById(R.id.register);
        startSummary = findViewById(R.id.loginSummary);
        startRemember = findViewById(R.id.remember);
        startCheck = findViewById(R.id.startCheck);
        loginButton = findViewById(R.id.loginText);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        forgotPass = findViewById(R.id.forgotText);

        Spannable spannable = new SpannableStringBuilder("Need an account?");
        spannable.setSpan(
                new ForegroundColorSpan(mContext.getColor(R.color.blackish)),
                0, 7,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        startReg.setText(spannable);
        username.requestFocus();

        startRemember.setOnClickListener(view ->
                startCheck.setChecked(!startCheck.isChecked()));

        startReg.setOnClickListener(v -> {
            Intent m = new Intent(mContext, SignupActivity.class);
            startActivity(m);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        ActivityCompat.requestPermissions(startActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE},
                1);

        //AnimatorSet animation = (AnimatorSet) AnimatorInflater.loadAnimator( startActivity.this, R.animator.test);
        int[] loc = new int[2];
        loginButton.getLocationInWindow(loc);

        //Animation animation = AnimationUtils.loadAnimation(startActivity.this, new MyBounceInterpolator(5, 10));

        username.setOnTouchListener((view, motionEvent) -> {
            Animation animation3 = AnimationUtils.loadAnimation(startActivity.this, R.anim.bounce);
            animation3.setInterpolator(new MyBounceInterpolator(0.1, 10));
            linearLayout.startAnimation(animation3);
            return false;
        });

        password.setOnTouchListener((view, motionEvent) -> {
            Animation animation3 = AnimationUtils.loadAnimation(startActivity.this, R.anim.bounce);
            animation3.setInterpolator(new MyBounceInterpolator(0.1, 10));
            linearLayout2.startAnimation(animation3);
            return false;
        });

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        //String passPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@*#$%]).{8,20})";

        forgotPass.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext, R.style.BottomSheetStyleTheme);
            View view1 = LayoutInflater.from(mContext).inflate(R.layout.bottom_sheet_forgot_pass, findViewById(R.id.bottomSheetFrame));
            bottomSheetDialog.setContentView(view1);
            bottomSheetDialog.setCanceledOnTouchOutside(false);
            bottomSheetDialog.setDismissWithAnimation(true);
            bottomSheetDialog.show();

            LinearLayout slidingLinear = view1.findViewById(R.id.sliding);
            LinearLayout passLinear = view1.findViewById(R.id.passLinear);
            LinearLayout emailLinear = view1.findViewById(R.id.emailLinear);

            EditText textView = view1.findViewById(R.id.emailBottom);
            EditText pass = view1.findViewById(R.id.passBottom);
            EditText pass2 = view1.findViewById(R.id.passBottom2);
            EditText otp = view1.findViewById(R.id.otpBottom);
            ImageView next = view1.findViewById(R.id.nextBottom);
            ImageView next2 = view1.findViewById(R.id.nextBottom2);
            LottieAnimationView animationView = view1.findViewById(R.id.animation_view);

            next.setOnClickListener(view2 -> {
                if (!isEmpty(textView) && textView.getText().toString().matches(emailPattern)) {
                    emailLinear.setVisibility(View.GONE);
                    slidingLinear.setVisibility(View.VISIBLE);
                    Call<ResponseBody> sendEmail = RetrofitClient.getInstance().getApi().sendEmail(textView.getText().toString());
                    sendEmail.enqueue(new Callback<ResponseBody>() {
                        @EverythingIsNonNull
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(mContext, "Sent otp, please check email", Toast.LENGTH_SHORT).show();
                                slidingLinear.setVisibility(View.GONE);
                                passLinear.setVisibility(View.VISIBLE);
                                try {
                                    Log.e("AAA", response.body().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else if (response.errorBody() != null) {
                                try {
                                    Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                                    Log.e("error", "" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                bottomSheetDialog.cancel();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            bottomSheetDialog.cancel();
                        }
                    });
                } else
                    Toast.makeText(mContext, "Enter all details correctly", Toast.LENGTH_SHORT).show();
            });

            next2.setOnClickListener(view2 -> {
                if (!isEmpty(pass) && !isEmpty(pass2) && !(isEmpty(otp))) {
                    if (pass.getText().toString().equals(pass2.getText().toString())) {
                        if (pass.getText().toString().length() > 5) {
                            Call<ResponseBody> otpSend = RetrofitClient.getInstance().getApi().verifyPass(textView.getText().toString(), otp.getText().toString(), pass.getText().toString());
                            otpSend.enqueue(new Callback<ResponseBody>() {
                                @Override
                                @EverythingIsNonNull
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        passLinear.setVisibility(View.GONE);
                                        animationView.setVisibility(View.VISIBLE);
                                        animationView.playAnimation();

                                        new Handler().postDelayed(() -> {
                                            animationView.cancelAnimation();
                                            bottomSheetDialog.cancel();
                                        }, animationView.getDuration() + 250);
                                        try {
                                            Log.e("AAA", response.body().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (response.errorBody() != null) {
                                        try {
                                            Toast.makeText(mContext, "Failed: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                        } else
                            Toast.makeText(mContext, "Password length must be atleast 5", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(mContext, "Password doesn't matches", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(mContext, "Enter password", Toast.LENGTH_SHORT).show();
            });
        });
        //path.addCircle(x,y ,15, Path.Direction.CW);
        loginButton.setOnClickListener(view -> {
            if (!isEmpty(username) && !(isEmpty(password))) {
                Log.e("Start", "onClick: Logging in...");
                Snackbar.make(view, "Logging In", Snackbar.LENGTH_SHORT).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();

                Call<ResponseBody> call = RetrofitClient.getInstance().getApi().login(username.getText().toString(), password.getText().toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        try {
                            if (response.isSuccessful() && response.body() != null) {
                                SharedPreferences shareit = getSharedPreferences("KEY", MODE_PRIVATE);
                                SharedPreferences.Editor eddy = shareit.edit();

                                Log.e("onResponse", "Logged In");

                                JSONObject j = new JSONObject(response.body().string());
                                AUTH_TOKEN = j.getString("auth_token");
                                //Log.e("AUTH_TOKEN", AUTH_TOKEN);
                                VEC = username.getText().toString();
                                Log.e("AA", VEC);
                                eddy.putString("AUTH_TOKEN", AUTH_TOKEN);
                                eddy.putString("VEC", VEC);
                                eddy.putString("dateLogged", date);
                                eddy.apply();

                                Log.e("AA", "AAA" + AUTH_TOKEN);
                                Call<ResponseBody> insertUsers =
                                        RetrofitClient.getInstance().getApi().getUserDetail("Token " + AUTH_TOKEN);
                                insertUsers.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    @EverythingIsNonNull
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            try {
                                                JSONArray j = new JSONArray(response.body().string());

                                                if (j.length() >= 0) {
                                                    DatabaseAdapter mDbHelper = new DatabaseAdapter(startActivity.this);
                                                    mDbHelper.createDatabase();
                                                    mDbHelper.open();
                                                    deleteData("Registration");
                                                    for (int i = 0; i < j.length(); i++) {
                                                        j.getJSONObject(i).getString("State");
                                                        mDbHelper.insertUsers(
                                                                j.getJSONObject(i).getString("CollegeName"),
                                                                j.getJSONObject(i).getString("VEC"),
                                                                j.getJSONObject(i).getString("FirstName"),
                                                                j.getJSONObject(i).getString("LastName"),
                                                                j.getJSONObject(i).getString("Email"),
                                                                j.getJSONObject(i).getString("Contact"),
                                                                j.getJSONObject(i).getString("State"),
                                                                j.getJSONObject(i).getString("IsLeader")
                                                        );
                                                    }
                                                    mDbHelper.close();
                                                }
                                                if (startCheck.isChecked()) {
                                                    eddy.putInt("logged", 1);
                                                    eddy.apply();
                                                }
                                                if (j.getJSONObject(0).getString("IsLeader").equals("Appointed"))
                                                    eddy.putInt("isLeader", 1);
                                                else
                                                    eddy.putInt("isLeader", 0);

                                                eddy.apply();
                                                Intent i;
                                                if (!shareit.getBoolean("isAboutDone", false))
                                                    i = new Intent(mContext, About.class);
                                                else
                                                    i = new Intent(mContext, ediary.class);
                                                startActivity(i);
                                                finish();
                                            } catch (JSONException | IOException e) {
                                                Log.e("Failed", e.toString());
                                                e.printStackTrace();
                                            }
                                        } else if (response.errorBody() != null) {
                                            try {
                                                Log.e("Error", "" + response.errorBody().string());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    @EverythingIsNonNull
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    }
                                });
                            } else {
                                if (response.errorBody() != null) {
                                    Log.d("AAA", "onResponse: "+response);
                                    JSONObject j = new JSONObject(response.errorBody().string());
                                    String m = j.getString("non_field_errors");
                                    Toast.makeText(mContext, "" + m.substring(2, m.length() - 2), Toast.LENGTH_SHORT).show();
                                    username.setText("");
                                    password.setText("");
                                    username.requestFocus();
                                }
                            }
                            //String s = response.errorBody().string();
                            //Toast.makeText(startActivity.this, s, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("Start", "" + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        //Toast.makeText(startActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(mContext, "Please Connect to internet", Toast.LENGTH_SHORT).show();
                        Log.e("onFailure", "" + t.getMessage());
                    }
                });
            } else {
                if (isEmpty(username) && isEmpty(password)) {
                    Snackbar.make(view, "Enter details", Snackbar.LENGTH_SHORT).show();
                    username.setText("");
                    password.setText("");
                    username.requestFocus();
                } else if (isEmpty(username)) {
                    username.setError("Enter username");
                    username.setText("");
                    username.requestFocus();
                } else if (isEmpty(password)) {
                    password.setText("");
                    password.requestFocus();
                }
            }
        });
    }

    private boolean isEmpty(EditText e) {
        return e.getText().toString().trim().length() <= 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                finish();
                Toast.makeText(mContext, "Permission denied, please give permissions from settings", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void deleteData(String table) {
        DatabaseAdapter mDbHelper2 = new DatabaseAdapter(mContext);
        mDbHelper2.createDatabase();
        mDbHelper2.open();
        DataBaseHelper mDb2 = new DataBaseHelper(mContext);
        SQLiteDatabase m = mDb2.getWritableDatabase();
        m.execSQL("DELETE FROM " + table);
        mDbHelper2.close();
        m.close();
        mDb2.close();
    }
}