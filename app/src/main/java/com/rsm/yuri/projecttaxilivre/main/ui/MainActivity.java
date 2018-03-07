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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.rsm.yuri.projecttaxilivre.R;
import com.rsm.yuri.projecttaxilivre.TaxiLivreApp;
import com.rsm.yuri.projecttaxilivre.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivre.historicchatslist.ui.HistoricChatsListActivity;
import com.rsm.yuri.projecttaxilivre.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivre.login.ui.LoginActivity;
import com.rsm.yuri.projecttaxilivre.main.MainPresenter;
import com.rsm.yuri.projecttaxilivre.main.di.MainComponent;
import com.rsm.yuri.projecttaxilivre.map.ui.MapFragment;
import com.rsm.yuri.projecttaxilivre.profile.ui.ProfileActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainView {

    @BindView(R.id.toolbar_app_bar_main)
    Toolbar toolbar;
    //@BindView(R.id.fab)
    //FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Inject
    MainPresenter presenter;
    @Inject
    MapFragment mapFragment;
    @Inject
    FragmentManager fragmentManager;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    ImageLoader imageLoader;

    HeaderViewHolder headerViewHolder;
    View headerLayout;

    private Location lastLocation;
    private TaxiLivreApp app;

    public final static int UPDATE_PROFILE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //TextView nomeTextView = (TextView) findViewById(R.id.nomeTextView);

        app = (TaxiLivreApp) getApplication();

        setSupportActionBar(toolbar);

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);//setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        headerLayout = navigationView.getHeaderView(0);

        headerViewHolder = new HeaderViewHolder(headerLayout);

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

        MainComponent mainComponent = app.getMainComponent(this,this, fragmentManager, mapFragment);
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
            startActivity(new Intent(this, HistoricChatsListActivity.class));
        }else if (id == R.id.nav_ajuda) {

        } else if (id == R.id.nav_viagens_descontos) {

        } else if (id == R.id.nav_config) {
            startActivityForResult(new Intent(this, ProfileActivity.class), UPDATE_PROFILE);
        } else if (id == R.id.nav_dirija_taxilivre) {

        } else if (id == R.id.nav_legal) {

        } else if (id == R.id.nav_sair) {
            logout();
        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        drawerLayout.closeDrawer(GravityCompat.START);
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
    public void setLoggedUser(User loggedUser) {
        if (loggedUser.getEmail() != null) {
            String emailKey = app.getEmailKey();
            String nomeKey = app.getNomeKey();
            String urlPhotoUserKey = app.getUrlPhotoUserKey();
            /*Log.d("d", "loggedUser.Nome: " + loggedUser.getNome());
            Log.d("d", "loggedUser.UrlPhotoDriver: " + loggedUser.getUrlPhotoUser());*/
            sharedPreferences.edit().putString(emailKey, loggedUser.getEmail()).apply();//.commit();//commit() e o que tem no codigo original lesson4.edx
            sharedPreferences.edit().putString(nomeKey, loggedUser.getNome()).apply();
            sharedPreferences.edit().putString(urlPhotoUserKey, loggedUser.getUrlPhotoUser()).apply();
            setupHeaderViewNavigation(loggedUser.getEmail(), loggedUser.getNome(), loggedUser.getUrlPhotoUser());
            
        }
    }

    private void setupHeaderViewNavigation(String email, String nome, String urlPhotoUser) {
        /*String email = sharedPreferences.getString(app.getEmailKey(), "");
        String nome = sharedPreferences.getString(app.getNomeKey(), "");
        String urlPhotoUser = sharedPreferences.getString(app.getUrlPhotoUserKey(), "");*/
        /*Log.d("d", "onCreate().loggedUser.Nome: " + nome);
        Log.d("d", "onCreate().loggedUser.UrlPhotoDriver: " + urlPhotoUser);*/
        imageLoader.load(headerViewHolder.imgAvatar, urlPhotoUser);
        headerViewHolder.nomeTextView.setText(nome);
        headerViewHolder.emailTextView.setText(email);
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

    /*@Override
    public void checkForSession() {
        presenter.checkForSession();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==UPDATE_PROFILE ){
            String email = sharedPreferences.getString(TaxiLivreApp.EMAIL_KEY, "");
            String nome = sharedPreferences.getString(TaxiLivreApp.NOME_KEY, "");
            String urlPhotoUser = sharedPreferences.getString(TaxiLivreApp.URL_PHOTO_USER_KEY, "");
            Log.d("d", "onActivityResult UPDATE_PROFILE");
            setupHeaderViewNavigation(email, nome, urlPhotoUser);
        }
    }

    protected static class HeaderViewHolder {

        @BindView(R.id.imgAvatar)
        CircleImageView imgAvatar;
        @BindView(R.id.nomeTextView)
        TextView nomeTextView;
        @BindView(R.id.emailTextView)
        TextView emailTextView;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
