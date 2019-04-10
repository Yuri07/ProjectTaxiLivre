package com.rsm.yuri.projecttaxilivredriver.main.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.rsm.yuri.projecttaxilivredriver.R;
import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverApp;
import com.rsm.yuri.projecttaxilivredriver.avaliation.ui.AvaliationFragment;
import com.rsm.yuri.projecttaxilivredriver.chat.ui.ChatActivity;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Car;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui.HistoricChatsListActivity;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.ui.HistoricTravelListActivity;
import com.rsm.yuri.projecttaxilivredriver.home.ui.HomeFragment;
import com.rsm.yuri.projecttaxilivredriver.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivredriver.login.ui.LoginActivity;
import com.rsm.yuri.projecttaxilivredriver.main.MainPresenter;
import com.rsm.yuri.projecttaxilivredriver.main.broadcast.LocalBroadcastMainActivity;
import com.rsm.yuri.projecttaxilivredriver.main.di.MainComponent;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;
import com.rsm.yuri.projecttaxilivredriver.money.ui.MoneyFragment;
import com.rsm.yuri.projecttaxilivredriver.profile.ui.ProfileFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rsm.yuri.projecttaxilivredriver.chat.ui.ChatActivity.EMAIL_KEY;
import static com.rsm.yuri.projecttaxilivredriver.chat.ui.ChatActivity.STATUS_KEY;
import static com.rsm.yuri.projecttaxilivredriver.chat.ui.ChatActivity.URL_KEY;

public class MainActivity extends AppCompatActivity implements MainView, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.main_container)
    DrawerLayout main_container;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.switch_main)
    Switch switchMain;
    @BindView(R.id.mainAppBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.custom_toolbar_title)
    TextView customToolbaTitle;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;

    @Inject
    MainPresenter presenter;
    @Inject
    FragmentManager fragmentManager;
    @Inject
    Fragment[] fragments;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    ImageLoader imageLoader;

    private OnSwitchButtonClickedListener listener;

    private ActionBar actionBar;

    TaxiLivreDriverApp app;

    public final static int FRAGMENT_HOME_IN_ARRAY = 0;
    public final static int FRAGMENT_MONEY_IN_ARRAY = 1;
    public final static int FRAGMENT_AVALIATION_IN_ARRAY = 2;
    public final static int FRAGMENT_PROFILE_IN_ARRAY = 3;

    public final static String ONLINE = "ONLINE";
    public final static String OFFLINE = "OFFLINE";
    public final static String IN_TRAVEL = "Em Viagem";

    public static final String FILTRO_KEY = "MainActivity_KEY";
    public static final String MENSAGEM_KEY = "MainActivity_MENSAGEM_KEY";

    private LocalBroadcastMainActivity broadcast;

    private BroadcastReceiver mBroadcastReceiver;

    private static final String NOTIFICATION_MSG_KEY = "notificationMsg";
    public static final String DATA_REQUEST_TRAVEL_MSG_KEY = "dataRequestTravelMsg";
    public static final String DATA_REQUEST_TRAVEL_MSG= "true";

    public static final String RECEIVER_INTENT = "RECEIVER_INTENT";
    //public static final String RECEIVER_MESSAGE = "RECEIVER_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        app = (TaxiLivreDriverApp) getApplication();

        setupBottomNavigationView();

        //toolbar.setTitle(OFFLINE);
        setSupportActionBar(toolbar);

        customToolbaTitle.setText(OFFLINE);

        if(!app.getConectivityStatus(this)) {
            switchMain.setEnabled(false);
            Toast.makeText(this, "Não foi possível se conectar a rede TaxiLivre", Toast.LENGTH_SHORT).show();
            Log.d("d", "MainActivity - onCreate() - Não foi possível se conectar a rede TaxiLivre");
        }

        //actionBar = getSupportActionBar();
        //actionBar.setCustomView(R.layout.app_bar_main);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, main_container, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        main_container.addDrawerListener(toggle);//setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        setupInjection();

        /* INTENT FILTER */
        /*broadcast = new LocalBroadcastMainActivity(this);
        IntentFilter intentFilter = new IntentFilter( FILTRO_KEY );
        LocalBroadcastManager.getInstance(this).registerReceiver( broadcast, intentFilter );*/

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String dataRequestTravelMsg = intent.getStringExtra(DATA_REQUEST_TRAVEL_MSG_KEY);
                if(dataRequestTravelMsg!=null) {
                    //Log.d("d", "MainActivity dataRequestTravelFirebaseCloudMsg: " + dataRequestTravelMsg);
                    if (dataRequestTravelMsg.equals(DATA_REQUEST_TRAVEL_MSG)) {
                        Bundle extras = intent.getExtras();

                        getIntent().removeExtra(DATA_REQUEST_TRAVEL_MSG_KEY);

                        listener.onTravelRequest(extras);
                    }
                }
            }
        };


        if (fragments[FRAGMENT_HOME_IN_ARRAY] instanceof OnSwitchButtonClickedListener) {
            listener = (OnSwitchButtonClickedListener) fragments[FRAGMENT_HOME_IN_ARRAY];
            Log.d("d", "MainActivity.onCreate():listener inicializado");
        } else {
            Log.d("d", "MainActivity.onCreate():listener nao inicializado");
            throw new ClassCastException();

        }
        //listener = (OnSharedPreferencesReadyListener) fragments[FRAGMENT_PROFILE_IN_ARRAY];

        //if(getSupportActionBar()!=null) {
            //getSupportActionBar().setDisplayShowTitleEnabled(true);
            //getSupportActionBar().setTitle("OFFLINE");
        //}


        fragmentManager.beginTransaction()
                .add(R.id.content_frame, fragments[FRAGMENT_HOME_IN_ARRAY])
                .commit();

        presenter.onCreate();
        presenter.checkForSession();

    }

    private void setupInjection() {

        Fragment[] fragments = new Fragment[]{new HomeFragment(),
                new MoneyFragment(), new AvaliationFragment(), new ProfileFragment()};

        //TaxiLivreDriverApp app = (TaxiLivreDriverApp) getApplication();
        MainComponent mainComponent = app.getMainComponent(this, this, getSupportFragmentManager(), fragments);
        mainComponent.inject(this);

    }

    private void setupBottomNavigationView() {
        BottomNavigationViewHelper.removeShiftMode(bottomNavigation);

        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                displayFragment(fragments[FRAGMENT_HOME_IN_ARRAY]);
                                break;
                            /*case R.id.action_money:
                                displayFragment(fragments[FRAGMENT_MONEY_IN_ARRAY]);
                                break;*/
                            case R.id.action_avaliation:
                                displayFragment(fragments[FRAGMENT_AVALIATION_IN_ARRAY]);
                                break;
                            case R.id.action_profile:
                                /*Bundle bundle = new Bundle();
                                bundle.putString(TaxiLivreDriverApp.EMAIL_KEY, sharedPreferences.getString(TaxiLivreDriverApp.EMAIL_KEY,""));
                                bundle.putString(TaxiLivreDriverApp.NOME_KEY, sharedPreferences.getString(TaxiLivreDriverApp.NOME_KEY,""));
                                bundle.putString(TaxiLivreDriverApp.URL_PHOTO_DRIVER_KEY, sharedPreferences.getString(TaxiLivreDriverApp.URL_PHOTO_DRIVER_KEY,""));
                                fragments[FRAGMENT_PROFILE_IN_ARRAY].setArguments(bundle);*/
                                displayFragment(fragments[FRAGMENT_PROFILE_IN_ARRAY]);
                                break;
                        }
                        return true;
                    }
                });
    }

    private void displayFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public void setUIVisibility(boolean enabled) {
        main_container.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void navigateToLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void setLoggedUser(Driver loggedUser) {
        if (loggedUser.getEmail() != null) {
            String emailKey = TaxiLivreDriverApp.EMAIL_KEY;
            String nomeKey = TaxiLivreDriverApp.NOME_KEY;
            String urlPhotoUserKey = TaxiLivreDriverApp.URL_PHOTO_DRIVER_KEY;
            String statusKey = TaxiLivreDriverApp.STATUS_KEY;
            String averagRatingKey = TaxiLivreDriverApp.AVERAG_RATING_KEY;
            String totalRatingsKey = TaxiLivreDriverApp.TOTAL_RATINGS_KEY;
            String count1StarsKey = TaxiLivreDriverApp.COUNT_1_STARS_KEY;
            String count2StarsKey = TaxiLivreDriverApp.COUNT_2_STARS_KEY;
            String count3StarsKey = TaxiLivreDriverApp.COUNT_3_STARS_KEY;
            String count4StarsKey = TaxiLivreDriverApp.COUNT_4_STARS_KEY;
            String count5StarsKey = TaxiLivreDriverApp.COUNT_5_STARS_KEY;
            sharedPreferences.edit().putString(emailKey, loggedUser.getEmail()).apply();//.commit();//commit() e o que tem no codigo original lesson4.edx
            sharedPreferences.edit().putString(nomeKey, loggedUser.getNome()).apply();
            sharedPreferences.edit().putString(urlPhotoUserKey, loggedUser.getUrlPhotoDriver()).apply();
            sharedPreferences.edit().putLong(statusKey, Driver.ONLINE).apply();
            sharedPreferences.edit().putFloat(averagRatingKey, (float) loggedUser.getAverageRating()).apply();
            sharedPreferences.edit().putInt(totalRatingsKey, loggedUser.getTotalRatings()).apply();
            sharedPreferences.edit().putInt(count1StarsKey, loggedUser.getCount1Stars()).apply();
            sharedPreferences.edit().putInt(count2StarsKey, loggedUser.getCount2Stars()).apply();
            sharedPreferences.edit().putInt(count3StarsKey, loggedUser.getCount3Stars()).apply();
            sharedPreferences.edit().putInt(count4StarsKey, loggedUser.getCount4Stars()).apply();
            sharedPreferences.edit().putInt(count5StarsKey, loggedUser.getCount5Stars()).apply();
            Log.d("d", "MainActivity. loggedUser.getaverageRating(): " + loggedUser.getAverageRating());
            Log.d("d", "MainActivity. loggedUser.getTotalRating(): " + loggedUser.getTotalRatings());
            Log.d("d", "MainActivity. loggedUser.getCount1Stars(): " + loggedUser.getCount1Stars());
            //Log.d("d", "MainActivity. loggedUser.getEmail(): "+loggedUser.getEmail());
            //Log.d("d", "MainActivity. loggedUser.getUrlPhotoDriver(): "+loggedUser.getUrlPhotoDriver());
            //listener.onSharedPreferencesReady(loggedUser.getEmail(), loggedUser.getNome(), loggedUser.getUrlPhotoDriver());

            presenter.getMyCar();

            verifyToken();


        }
    }

    @Override
    public void setMyCar(Car myCar) {
        sharedPreferences.edit().putString(TaxiLivreDriverApp.MODELO_KEY, myCar.getModelo()).apply();//.commit();//commit() e o que tem no codigo original lesson4.edx
        sharedPreferences.edit().putString(TaxiLivreDriverApp.URL_PHOTO_CAR_KEY, myCar.getUrlPhotoCar()).apply();
        sharedPreferences.edit().putString(TaxiLivreDriverApp.MARCA_KEY, myCar.getMarca()).apply();
        sharedPreferences.edit().putString(TaxiLivreDriverApp.COR_KEY, myCar.getCor()).apply();
        sharedPreferences.edit().putLong(TaxiLivreDriverApp.ANO_KEY, myCar.getAno()).apply();
        sharedPreferences.edit().putString(TaxiLivreDriverApp.PLACA_KEY, myCar.getPlaca()).apply();
    }

    private void verifyToken() {
        String firebaseNotificationToken = FirebaseInstanceId.getInstance().getToken();
        presenter.sendFirebaseNotificationTokenToServer(firebaseNotificationToken);
    }

    @Override
    public void onFailedToRecoverMyCar(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSucceessToSaveFirebaseTokenInServer() {

    }

    @Override
    public void onFailedToSaveFirebaseTokenInServer(String errorMessage) {
        Log.d("d", errorMessage );
    }

    public interface OnSwitchButtonClickedListener {
        public void onSwitchButtonClicked(boolean switchStatus);

        public void onTravelRequest(Bundle dataTravelRequester);
    }


    @Override
    public void logout() {
        presenter.logout();
        sharedPreferences.edit().clear().apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_viagens) {
            startActivity(new Intent(this, HistoricTravelListActivity.class));
        } else if (id == R.id.nav_historico_chats) {
            startActivity(new Intent(this, HistoricChatsListActivity.class));
        } else if (id == R.id.nav_ajuda) {

        } else if (id == R.id.nav_legal) {

        } else if (id == R.id.nav_sair) {
            //Log.d("d", "MainActivityonNavigationItemSelected(),R.id.nav_sair " );
            logout();
        }

        //Log.d("d", "MainActivityonNavigationItemSelected()");
        main_container.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (main_container.isDrawerOpen(GravityCompat.START)) {
            main_container.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

        String notificationMsg = intent.getStringExtra(NOTIFICATION_MSG_KEY);
        Log.d("d", "MainActiity NotificationFirebaseCloudMsg: " + notificationMsg);

        if(notificationMsg!=null){
            //Log.d("d", "MainActiity NotificationFirebaseCloudMsg: " + notificationMsg);
            if(notificationMsg.equals("true")) {
                String emailSender = intent.getStringExtra(EMAIL_KEY);
                Log.d("d", "emailSender: " + emailSender);
                String statusSender = intent.getStringExtra(STATUS_KEY);
                Log.d("d", "statusSender: " + statusSender);
                String urlSender = intent.getStringExtra(URL_KEY);
                getIntent().removeExtra(NOTIFICATION_MSG_KEY);

                Intent i = new Intent(this, ChatActivity.class);
                i.putExtra(ChatActivity.EMAIL_KEY, emailSender);
                long longStatus = Long.parseLong(statusSender);
                i.putExtra(ChatActivity.STATUS_KEY, longStatus);
                i.putExtra(ChatActivity.URL_KEY, urlSender);

                startActivity(i);
            }

        }//else {

            /*String dataRequestTravelMsg = intent.getStringExtra(DATA_REQUEST_TRAVEL_MSG_KEY);
            Log.d("d", "MainActivity dataRequestTravelFirebaseCloudMsg: " + dataRequestTravelMsg);

            if(dataRequestTravelMsg!=null) {
                //Log.d("d", "MainActivity dataRequestTravelFirebaseCloudMsg: " + dataRequestTravelMsg);
                if (dataRequestTravelMsg.equals("true")) {
                    Bundle extras = intent.getExtras();

                    getIntent().removeExtra(DATA_REQUEST_TRAVEL_MSG_KEY);

                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragments[FRAGMENT_HOME_IN_ARRAY])
                            .commit();
                    if(fragments[FRAGMENT_HOME_IN_ARRAY].isAdded()) {
                        listener.onTravelRequest(extras);
                        Log.d("d", "MainActivity fragments[FRAGMENT_HOME_IN_ARRAY]  is Added");
                    }else{
                        Log.d("d", "MainActivity fragments[FRAGMENT_HOME_IN_ARRAY]  is not Added");
                    }

                }

            }*/

            long status = sharedPreferences.getLong(TaxiLivreDriverApp.STATUS_KEY, 0);
            if (status == Driver.OFFLINE) {
                sharedPreferences.edit().putLong(TaxiLivreDriverApp.STATUS_KEY, Driver.ONLINE).apply();
                presenter.changeToOnlineStatus();
            }

        //}

        verifyToken();


    }

    public void callHomeFragmentOnTravelRequest(Bundle extras){

    }

    @Override
    protected void onPause() {
        long status = sharedPreferences.getLong(TaxiLivreDriverApp.STATUS_KEY, 0);
        if(status==Driver.ONLINE) {//so fico offline se
            sharedPreferences.edit().putLong(TaxiLivreDriverApp.STATUS_KEY, Driver.OFFLINE).apply();
            presenter.changeToOfflineStatus();
        }
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @OnClick(R.id.switch_main)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.switch_main:
                if(switchMain.isChecked()){
                    //getSupportActionBar().setTitle("ONLINE");
                    //toolbar.setTitle(ONLINE);
                    //setTitle(ONLINE);
                    customToolbaTitle.setText(ONLINE);

                    if (Build.VERSION.SDK_INT >= 21) {
                        Drawable color = new ColorDrawable(Color.parseColor("#FF9800"));//getResources().getColor(R.values.colors.colorAccent);//android.R.color.holo_green_dark));
                        getSupportActionBar().setBackgroundDrawable(color);
                        changeStatusBarColor(true);
                    }
                    sharedPreferences.edit().putLong(TaxiLivreDriverApp.STATUS_KEY, Driver.WAITING_TRAVEL).apply();
                    presenter.startWaitingTravel();
                    listener.onSwitchButtonClicked(true);
                    //onStop();
                }else{
                    //getSupportActionBar().setTitle("OFFLINE");
                    //toolbar.setTitle(OFFLINE);
                    customToolbaTitle.setText(OFFLINE);

                    if (Build.VERSION.SDK_INT >= 21) {
                        Drawable color = new ColorDrawable(Color.parseColor("#009688"));//getResources().getColor(R.values.colors.colorAccent);//android.R.color.holo_green_dark));
                        getSupportActionBar().setBackgroundDrawable(color);
                        changeStatusBarColor(false);
                    }
                    sharedPreferences.edit().putLong(TaxiLivreDriverApp.STATUS_KEY, Driver.ONLINE).apply();
                    presenter.stopWaitingTravel();
                    listener.onSwitchButtonClicked(false);
                }
                break;
        }
    }

    public void msgTravelAcceptedFromHomeFragment(){
        Log.d("d", "MainActivity - msgTravelAcceptedFromHomeFragment");
        switchMain.setChecked(false);
        //customToolbaTitle.setText(IN_TRAVEL);

        if (Build.VERSION.SDK_INT >= 21) {
            Drawable color = new ColorDrawable(Color.parseColor("#009688"));//getResources().getColor(R.values.colors.colorAccent);//android.R.color.holo_green_dark));
            getSupportActionBar().setBackgroundDrawable(color);
            changeStatusBarColor(false);
        }

        bottomNavigation.setVisibility(View.GONE);
        appBarLayout.setVisibility(View.GONE);

        sharedPreferences.edit().putLong(TaxiLivreDriverApp.STATUS_KEY, Driver.IN_TRAVEL).apply();

        presenter.startInTravelStatus();

        listener.onSwitchButtonClicked(false);

    }

    public void msgTravelCompletedFromHomeFragment(){
        Log.d("d", "MainActivity - msgTravelCompletedFromHomeFragment");
        switchMain.setChecked(true);
        //customToolbaTitle.setText(IN_TRAVEL);

        if (Build.VERSION.SDK_INT >= 21) {
            Drawable color = new ColorDrawable(Color.parseColor("#FF9800"));//getResources().getColor(R.values.colors.colorAccent);//android.R.color.holo_green_dark));
            getSupportActionBar().setBackgroundDrawable(color);
            changeStatusBarColor(true);
        }

        bottomNavigation.setVisibility(View.VISIBLE);
        appBarLayout.setVisibility(View.VISIBLE);

        sharedPreferences.edit().putLong(TaxiLivreDriverApp.STATUS_KEY, Driver.ONLINE).apply();

        presenter.startInTravelStatus();

        listener.onSwitchButtonClicked(true);
    }

    public void disableDrawNavigation(){
        Log.d("d", "MainActivity - disableDrawNavigation");
        main_container.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    public void enableDrawNavigation(){
        Log.d("d", "MainActivity - enableDrawNavigation");
        main_container.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void logMensagem(String mensagem ){

    }

    @Override
    protected void onDestroy() {
        //sharedPreferences.edit().putLong(TaxiLivreDriverApp.STATUS_KEY, Driver.OFFLINE).apply();
        //presenter.changeToOfflineStatus();
        presenter.onDestroy();
        /*LocalBroadcastManager
                .getInstance(this)
                .unregisterReceiver( broadcast );*/

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver((mBroadcastReceiver), new IntentFilter(RECEIVER_INTENT));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void changeStatusBarColor(boolean status){
        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        if(status)
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
        else
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }

}
