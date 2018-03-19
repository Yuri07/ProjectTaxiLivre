package com.rsm.yuri.projecttaxilivredriver.main.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.rsm.yuri.projecttaxilivredriver.R;
import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverApp;
import com.rsm.yuri.projecttaxilivredriver.avaliation.ui.AvaliationFragment;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Car;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui.HistoricChatsListActivity;
import com.rsm.yuri.projecttaxilivredriver.home.ui.HomeFragment;
import com.rsm.yuri.projecttaxilivredriver.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivredriver.login.ui.LoginActivity;
import com.rsm.yuri.projecttaxilivredriver.main.MainPresenter;
import com.rsm.yuri.projecttaxilivredriver.main.di.MainComponent;
import com.rsm.yuri.projecttaxilivredriver.money.ui.MoneyFragment;
import com.rsm.yuri.projecttaxilivredriver.profile.ui.ProfileFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView, NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.main_container)
    DrawerLayout main_container;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.switch_main)
    Switch switchMain;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
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

    //private OnSharedPreferencesReadyListener listener;

    public final static int FRAGMENT_HOME_IN_ARRAY = 0;
    public final static int FRAGMENT_MONEY_IN_ARRAY = 1;
    public final static int FRAGMENT_AVALIATION_IN_ARRAY = 2;
    public final static int FRAGMENT_PROFILE_IN_ARRAY = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupBottomNavigationView();

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, main_container, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        main_container.addDrawerListener(toggle);//setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        setupInjection();

        /*if (fragments[FRAGMENT_PROFILE_IN_ARRAY] instanceof OnSharedPreferencesReadyListener) {
            listener = (OnSharedPreferencesReadyListener) fragments[FRAGMENT_PROFILE_IN_ARRAY];
            Log.d("d", "MainActivity.onCreate():listener inicializado");
        } else {
            Log.d("d", "MainActivity.onCreate():listener nao inicializado");
            throw new ClassCastException();

        }
        listener = (OnSharedPreferencesReadyListener) fragments[FRAGMENT_PROFILE_IN_ARRAY];*/

        fragmentManager.beginTransaction()
                .add(R.id.content_frame, fragments[FRAGMENT_HOME_IN_ARRAY])
                .commit();

        presenter.onCreate();
        presenter.checkForSession();

    }

    private void setupInjection() {

        Fragment[] fragments = new Fragment[]{new HomeFragment(),
                new MoneyFragment(), new AvaliationFragment(), new ProfileFragment()};

        TaxiLivreDriverApp app = (TaxiLivreDriverApp) getApplication();
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
        main_container.setVisibility( enabled ? View.VISIBLE : View.INVISIBLE );
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
            sharedPreferences.edit().putFloat(averagRatingKey, (float)loggedUser.getAverageRating()).apply();
            sharedPreferences.edit().putInt(totalRatingsKey, loggedUser.getTotalRatings()).apply();
            sharedPreferences.edit().putInt(count1StarsKey, loggedUser.getCount1Stars()).apply();
            sharedPreferences.edit().putInt(count2StarsKey, loggedUser.getCount2Stars()).apply();
            sharedPreferences.edit().putInt(count3StarsKey, loggedUser.getCount3Stars()).apply();
            sharedPreferences.edit().putInt(count4StarsKey, loggedUser.getCount4Stars()).apply();
            sharedPreferences.edit().putInt(count5StarsKey, loggedUser.getCount5Stars()).apply();
            Log.d("d", "MainActivity. loggedUser.getaverageRating(): "+ loggedUser.getAverageRating());
            Log.d("d", "MainActivity. loggedUser.getTotalRating(): "+ loggedUser.getTotalRatings());
            Log.d("d", "MainActivity. loggedUser.getCount1Stars(): "+ loggedUser.getCount1Stars());
            //Log.d("d", "MainActivity. loggedUser.getEmail(): "+loggedUser.getEmail());
            //Log.d("d", "MainActivity. loggedUser.getUrlPhotoDriver(): "+loggedUser.getUrlPhotoDriver());
            //listener.onSharedPreferencesReady(loggedUser.getEmail(), loggedUser.getNome(), loggedUser.getUrlPhotoDriver());

            presenter.getMyCar();

        }
    }

    @Override
    public void setMyCar(Car myCar) {
        sharedPreferences.edit().putString(TaxiLivreDriverApp.MODELO_KEY, myCar.getModelo()).apply();//.commit();//commit() e o que tem no codigo original lesson4.edx
        sharedPreferences.edit().putString(TaxiLivreDriverApp.MARCA_KEY, myCar.getMarca()).apply();
        sharedPreferences.edit().putString(TaxiLivreDriverApp.COR_KEY, myCar.getCor()).apply();
        sharedPreferences.edit().putLong(TaxiLivreDriverApp.ANO_KEY, myCar.getAno()).apply();
        sharedPreferences.edit().putString(TaxiLivreDriverApp.PLACA_KEY, myCar.getPlaca()).apply();
    }

    @Override
    public void onFailedToRecoverMyCar(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    /*public interface OnSharedPreferencesReadyListener {
        public void onSharedPreferencesReady(String email, String nome, String urlPhotoUser);
    }*/

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

        } else if(id == R.id.nav_historico_chats){
            startActivity(new Intent(this, HistoricChatsListActivity.class));
        }else if (id == R.id.nav_ajuda) {

        } else if (id == R.id.nav_legal) {

        } else if (id == R.id.nav_sair) {
            Log.d("d", "MainActivityonNavigationItemSelected(),R.id.nav_sair " );
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



}
