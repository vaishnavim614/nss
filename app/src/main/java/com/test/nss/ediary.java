package com.test.nss;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.test.nss.api.RetrofitClient;
import com.test.nss.ui.data.DataActivity;
import com.test.nss.ui.info.InfoSharedActivity;
import com.test.nss.worker.MyWorker;
import com.test.nss.worker.MyWorkerDb;

import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static com.test.nss.Helper.AUTH_TOKEN;
import static com.test.nss.Helper.VEC;
import static com.test.nss.Helper.black;
import static com.test.nss.Helper.isFirst;
import static com.test.nss.Helper.isNight;
import static com.test.nss.Helper.name;

public class ediary extends AppCompatActivity implements View.OnClickListener {

    private static final Intent[] POWERMANAGER_INTENTS = {
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
            new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity")),
            new Intent().setComponent(new ComponentName("com.transsion.phonemanager", "com.itel.autobootmanager.activity.AutoBootMgrActivity"))
    };

    static int whichAvatar = 0;
    Activity app;
    AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    ImageView logout;
    ImageView refresh;
    FragmentManager fm;
    CheckConn checkConn;
    IntentFilter z;
    ImageView imageView;
    Context context;
    Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.white));
        getWindow().setNavigationBarColor(getColor(R.color.off_white));
        startRec();
        setContentView(R.layout.activity_ediary);

        app = this;
        context = ediary.this;
        helper = new Helper(context);

        imageView = findViewById(R.id.switchdark);
        refresh = findViewById(R.id.refresh);
        MyWorkerDb.enqueueSelf();

        if (!isNight) {
            imageView.setImageResource(R.drawable.ic_dark);
        } else {
            imageView.setImageResource(R.drawable.ic_light);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("KEY", MODE_PRIVATE);
        int isLeader = sharedPreferences.getInt("isLeader", 0);

        fm = getSupportFragmentManager();

        //TODO:
        //if (isNetworkAvailable()) {
        //} else {
        /*isLeader = sharedPreferences.getInt("isLeader", 0);
        leaderId = sharedPreferences.getInt("leaderId", 0);*/
        //}

        refresh.setOnClickListener(view -> {
            RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.setInterpolator(new MyBounceInterpolator(20, 5));
            animation.setDuration(700);
            refresh.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    finish();
                    clearData();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        });
        View v = findViewById(android.R.id.content);
        Snackbar s;
        if (isLeader == 1) {
            s = Snackbar.make(v, "Welcome Leader: " + name, Snackbar.LENGTH_SHORT);
            TextView tv = s.getView().findViewById(com.google.android.material.R.id.snackbar_text);
            tv.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_leader_24, 0, 0, 0);
            Typeface t = ResourcesCompat.getFont(context, R.font.nunito_semibold);
            tv.setTypeface(t);
            tv.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));

        } else {
            s = Snackbar.make(v, "Welcome: " + name, Snackbar.LENGTH_SHORT);
            TextView tv = s.getView().findViewById(com.google.android.material.R.id.snackbar_text);
            Typeface t = ResourcesCompat.getFont(context, R.font.nunito_semibold);
            tv.setTypeface(t);
        }

        s.setTextColor(context.getColor(R.color.white));
        //s.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        s.show();

        //Toast.makeText(context, AUTH_TOKEN, Toast.LENGTH_SHORT).show();
        drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        logout = findViewById(R.id.logoutbutton);

        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_main,
                R.id.nav_acti, R.id.nav_work, R.id.nav_camp, R.id.nav_leader, R.id.nav_help)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setItemIconTintList(null);

        Menu m = navigationView.getMenu().getItem(0).getSubMenu();

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                if (!sharedPreferences.getBoolean("isAboutDone", false)) {
                    new FancyShowCaseView.Builder(app)
                            .focusOn((findViewById(R.id.nav_main)))
                            .focusShape(FocusShape.ROUNDED_RECTANGLE)
                            .roundRectRadius(24)
                            .fitSystemWindows(true)
                            .title("This is your diary!")
                            .titleStyle(R.style.focusTitleStyle, Gravity.BOTTOM | Gravity.CENTER)
                            .build()
                            .show();

                    SharedPreferences sharedPreferences = getSharedPreferences("KEY", MODE_PRIVATE);
                    SharedPreferences.Editor eddy = sharedPreferences.edit();
                    eddy.putBoolean("isAboutDone", true);
                    eddy.apply();
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        View headerView = navigationView.getHeaderView(0);

        imageView = headerView.findViewById(R.id.imageView);

        whichAvatar = sharedPreferences.getInt("avatar", 0);
        switch (whichAvatar) {
            case 1:
                imageView.setImageResource(R.drawable.ic_women_1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_women_2);
                break;
            case 3:
                imageView.setImageResource(R.drawable.ic_women_3);
                break;
            case 4:
                imageView.setImageResource(R.drawable.ic_women_4);
                break;
            case 5:
                imageView.setImageResource(R.drawable.ic_man_1);
                break;
            case 6:
                imageView.setImageResource(R.drawable.ic_man_2);
                break;
            case 7:
                imageView.setImageResource(R.drawable.ic_man_3);
                break;
            case 8:
                imageView.setImageResource(R.drawable.ic_man_4);
                break;
            default:
            case 0:
                imageView.setImageResource(R.drawable.ic_man_0);
                break;
        }

        //Log.e("AAA", "" + m.getItem(1).getTitle());
        if (isLeader == 1)
            m.findItem(R.id.nav_leader).setVisible(true);

        else if (isLeader == 0)
            m.findItem(R.id.nav_leader).setVisible(false);

        m.findItem(R.id.nav_home).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent in = new Intent(context, About.class);
                startActivity(in);
                finish();
                return true;
            }
        });
        m = navigationView.getMenu();
        m.getItem(1).getSubMenu().getItem(1).setOnMenuItemClickListener(menuItem -> {
            Intent o = new Intent(context, InfoSharedActivity.class);

                /*View v1 = findViewById(R.id.imageView);
                Pair[] pair = new Pair[1];
                pair[0] = new Pair<>(v1, "trans");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ediary.context, pair);*/


            //startActivity(o, options.toBundle());
            startActivity(o);
            return true;
        });

        m.getItem(1).getSubMenu().getItem(2).setOnMenuItemClickListener(menuItem -> {
            Intent o = new Intent(context, DataActivity.class);
            startActivity(o);
            return true;
        });

        TextView navUsername = headerView.findViewById(R.id.nameHeader);
        TextView navVec = headerView.findViewById(R.id.vecNoHeader);
        navVec.setText(VEC);
        navUsername.setText(name);

        imageView.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.delDialog);
            builder.setMessage("Choose Avatar");
            builder.setView(R.layout.avatar_input);

            builder.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss());
            builder.show();
        });

        logout.setOnClickListener(view -> {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(context, R.style.delDialog);
            builder2.setMessage("Do you want to logout?");

            builder2.setNegativeButton("No", (dialog, which) -> dialog.cancel());

            builder2.setPositiveButton("Yes", (dialog, which) -> {
                clearData();
                dialog.dismiss();

                SharedPreferences shareit = getSharedPreferences("KEY", MODE_PRIVATE);
                SharedPreferences.Editor eddy = shareit.edit();
                eddy.putInt("logged", 0);
                eddy.putInt("isLeader", 0);
                eddy.putInt("leaderId", 0);
                eddy.putBoolean("isAboutDone", false);
                eddy.apply();

                Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show();

                Call<Void> logout = RetrofitClient.getInstance().getApi().delToken("Token " + AUTH_TOKEN);
                logout.enqueue(new Callback<Void>() {
                    @Override
                    @EverythingIsNonNull
                    public void onResponse(Call<Void> call, Response<Void> response) {
                    }

                    @Override
                    @EverythingIsNonNull
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Ediary:logout", t.toString());
                    }
                });
                Intent i = new Intent(context, startActivity.class);
                startActivity(i);
                finish();
                Toast.makeText(context, "Logged Out!", Toast.LENGTH_SHORT).show();
                AUTH_TOKEN = "";
            });

            builder2.show();
        });

        Log.e("Here", "onCreate: " + isFirst + isLeader + isNight);

        /*for (Intent intent : POWERMANAGER_INTENTS)
            if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                //startActivity(intent);
                break;
            }*/
        MyWorker.enqueueSelf();
    }

    private void clearData() {
        deleteData("ActivityListByAdmin");
        deleteData("AreaData");
        deleteData("AreaDataPrev");
        deleteData("CampActivities");
        deleteData("CampActivityList");
        deleteData("CampActivityListByAdmin");
        deleteData("CampDetails");
        deleteData("CollegeNames");
        deleteData("DailyActivity");
        deleteData("HoursList");
        deleteData("Leaders");
        deleteData("NatureOfActivity");
        deleteData("VolAct");
        deleteData("VolActAll");
        //deleteData("VolVecActAll");
        deleteData("WorkHoursSy");
        deleteData("WorkHoursFy");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                imageView.setImageResource(R.drawable.ic_man_0);
                whichAvatar = 0;
                break;
            case R.id.girl1:
                imageView.setImageResource(R.drawable.ic_women_1);
                whichAvatar = 1;
                break;
            case R.id.girl2:
                imageView.setImageResource(R.drawable.ic_women_2);
                whichAvatar = 2;
                break;
            case R.id.girl3:
                imageView.setImageResource(R.drawable.ic_women_3);
                whichAvatar = 3;
                break;
            case R.id.girl4:
                imageView.setImageResource(R.drawable.ic_women_4);
                whichAvatar = 4;
                break;
            case R.id.boy1:
                imageView.setImageResource(R.drawable.ic_man_1);
                whichAvatar = 5;
                break;
            case R.id.boy2:
                imageView.setImageResource(R.drawable.ic_man_2);
                whichAvatar = 6;
                break;
            case R.id.boy3:
                imageView.setImageResource(R.drawable.ic_man_3);
                whichAvatar = 7;
                break;
            case R.id.boy4:
                imageView.setImageResource(R.drawable.ic_man_4);
                whichAvatar = 8;
                break;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("KEY", MODE_PRIVATE);
        SharedPreferences.Editor eddy2 = sharedPreferences.edit();
        eddy2.putInt("avatar", whichAvatar);
        eddy2.apply();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setMode(View view) {
        ImageView imageView = findViewById(R.id.switchdark);

        RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setInterpolator(new MyBounceInterpolator(2, 8));
        animation.setDuration(1500);
        imageView.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isNight) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void startRec() {
        checkConn = new CheckConn();
        z = new IntentFilter();
        z.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(checkConn, z);
    }

    public void leaderRef() {
        checkConn.leaderRef();
    }

    private void deleteData(String table) {
        DatabaseAdapter mDbHelper2 = new DatabaseAdapter(context);
        mDbHelper2.createDatabase();
        mDbHelper2.open();
        DataBaseHelper mDb2 = new DataBaseHelper(context);
        SQLiteDatabase m = mDb2.getWritableDatabase();
        m.execSQL("DELETE FROM " + table);
        mDbHelper2.close();
        m.close();
        mDb2.close();
    }

    public String getKey(String value) {
        return helper.getKey(Integer.parseInt(value));
    }

    @Override
    protected void onStop() {
        try {
            unregisterReceiver(checkConn);
        } catch (Exception ignored) {
        }
        helper.add();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        helper.add();
    }
    /*private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void closeFragment(Fragment fragment) {
        fragment.getChildFragmentManager().popBackStack();
        Log.e("ediary", "after: " + fragment.toString());
    }*/
}