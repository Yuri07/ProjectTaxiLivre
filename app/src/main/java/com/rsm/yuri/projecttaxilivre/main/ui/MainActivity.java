package com.rsm.yuri.projecttaxilivre.main.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.rsm.yuri.projecttaxilivre.R;
import com.rsm.yuri.projecttaxilivre.TaxiLivreApp;
import com.rsm.yuri.projecttaxilivre.login.ui.LoginActivity;
import com.rsm.yuri.projecttaxilivre.main.MainPresenter;
import com.rsm.yuri.projecttaxilivre.main.di.MainComponent;
import com.rsm.yuri.projecttaxilivre.map.ui.MapFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    //@BindView(R.id.emailtextView)
    //TextView emailTextView;

    @Inject
    MainPresenter presenter;
    @Inject
    MapFragment mapFragment;
    @Inject
    FragmentManager fragmentManager;
    @Inject
    SharedPreferences sharedPreferences;

    private Location lastLocation;
    private TaxiLivreApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        app = (TaxiLivreApp) getApplication();

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);//setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        setupInjection();

        fragmentManager.beginTransaction()
                .add(R.id.content_frame, mapFragment)
                .commit();

        presenter.onCreate();
        presenter.checkForSession();

    }

    private void setupInjection(){

        MapFragment mapFragment = new MapFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        MainComponent mainComponent = app.getMainComponent(this, fragmentManager, mapFragment);
        mainComponent.inject(this);
    }

    private void displayFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pagemento) {
            // Handle the camera action
            //Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            //startActivity(intent);
        } else if (id == R.id.nav_viagens) {
            //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //startActivity(intent);
        } else if(id == R.id.nav_historico_chats){

        }else if (id == R.id.nav_ajuda) {

        } else if (id == R.id.nav_viagens_descontos) {

        } else if (id == R.id.nav_config) {

        } else if (id == R.id.nav_dirija_taxilivre) {

        } else if (id == R.id.nav_legal) {

        } else if (id == R.id.nav_sair) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void setUIVisibility(boolean enabled) {
        drawerLayout.setVisibility( enabled ? View.VISIBLE : View.INVISIBLE );
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
    public void setUserEmail(String email) {
        if (email != null) {
            String key = app.getEmailKey();
            sharedPreferences.edit().putString(key, email).apply();//.commit();//commit() e o que tem no codigo original lesson4.edx
        }
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

    @Override
    public void checkForSession() {
        presenter.checkForSession();
    }
}
