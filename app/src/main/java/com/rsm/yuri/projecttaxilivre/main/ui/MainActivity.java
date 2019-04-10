package com.rsm.yuri.projecttaxilivre.main.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.rsm.yuri.projecttaxilivre.R;
import com.rsm.yuri.projecttaxilivre.TaxiLivreApp;
import com.rsm.yuri.projecttaxilivre.chat.ui.ChatActivity;
import com.rsm.yuri.projecttaxilivre.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivre.historicchatslist.ui.HistoricChatsListActivity;
import com.rsm.yuri.projecttaxilivre.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivre.login.ui.LoginActivity;
import com.rsm.yuri.projecttaxilivre.main.MainPresenter;
import com.rsm.yuri.projecttaxilivre.main.di.MainComponent;
import com.rsm.yuri.projecttaxilivre.map.InteractiveInfoWindow.InfoWindowFragment;
import com.rsm.yuri.projecttaxilivre.map.entities.Driver;
import com.rsm.yuri.projecttaxilivre.map.ui.MapFragment;
import com.rsm.yuri.projecttaxilivre.profile.ui.ProfileActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.rsm.yuri.projecttaxilivre.chat.ui.ChatActivity.EMAIL_KEY;
import static com.rsm.yuri.projecttaxilivre.chat.ui.ChatActivity.STATUS_KEY;
import static com.rsm.yuri.projecttaxilivre.chat.ui.ChatActivity.URL_KEY;

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

    private OnTravelStatusChangedListener listener;

    HeaderViewHolder headerViewHolder;
    View headerLayout;

    private Location lastLocation;
    private TaxiLivreApp app;

    private BroadcastReceiver mBroadcastReceiver;

    public final static int UPDATE_PROFILE = 0;

    private static final String NOTIFICATION_MSG_KEY = "notificationMsg";

    public static final String RECEIVER_INTENT = "RECEIVER_INTENT";

    public static final String DATA_INITIATE_TRAVEL_MSG_KEY = "dataInitiateTravelMsg";
    public static final String DATA_TERMINATE_TRAVEL_MSG_KEY = "dataTerminateTravelMsg";

    public static final String DATA_INITIATE_TRAVEL_MSG = "true";
    public static final String DATA_TERMINATE_TRAVEL_MSG = "true";

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

        listener = mapFragment;

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d("d", "MainActivity - mBroadCastReceiver - onReceiver()");

                String dataInitiateTravelMsg = intent.getStringExtra(DATA_INITIATE_TRAVEL_MSG_KEY);
                String dataTerminateTravelMsg = intent.getStringExtra(DATA_TERMINATE_TRAVEL_MSG_KEY);

                if(dataInitiateTravelMsg!=null) {
                    //Log.d("d", "MainActivity dataRequestTravelFirebaseCloudMsg: " + dataRequestTravelMsg);
                    if (dataInitiateTravelMsg.equals(DATA_INITIATE_TRAVEL_MSG)) {
                        //Bundle extras = intent.getExtras();

                        getIntent().removeExtra(DATA_INITIATE_TRAVEL_MSG_KEY);

                        listener.onTravelInitiate();
                    }

                }else if(dataTerminateTravelMsg!=null){

                    if (dataTerminateTravelMsg.equals(DATA_TERMINATE_TRAVEL_MSG)) {
                        //Bundle extras = intent.getExtras();

                        getIntent().removeExtra(DATA_TERMINATE_TRAVEL_MSG_KEY);

                        listener.onTravelTerminate();
                    }

                }
            }
        };

        /*if(mapFragment instanceof OnTravelStatusChangedListener){
            listener = (OnTravelStatusChangedListener) mapFragment;
            Log.d("d", "MainActivity.onCreate():listener inicializado");
        }else{
            Log.d("d", "MainActivity.onCreate():listener nao inicializado");
            throw new ClassCastException();
        }*/

        fragmentManager.beginTransaction()
                .add(R.id.map_fragment_content_frame, mapFragment)
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
                .replace(R.id.map_fragment_content_frame, fragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        //boolean notificationMsg = intent.getBooleanExtra(NOTIFICATION_MSG_KEY, false);
        String notificationMsg = intent.getStringExtra(NOTIFICATION_MSG_KEY);
        if(notificationMsg!=null){
            Log.d("d", "NotificationMsg: " + notificationMsg);
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
        }else{
            presenter.onResume();
            verifyToken();
        }


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

    public interface OnTravelStatusChangedListener {
        public void onTravelInitiate();

        public void onTravelTerminate();
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

    public MapFragment getMapFragment(){
        return mapFragment;
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

            verifyToken();

        }
    }

    private void verifyToken() {
        String firebaseNotificationToken = FirebaseInstanceId.getInstance().getToken();
        presenter.sendFirebaseNotificationTokenToServer(firebaseNotificationToken);
    }

    @Override
    public void onSucceessToSaveFirebaseTokenInServer() {

        //sharedPreferences.edit().putBoolean(TaxiLivreApp.FIREBASE_NOTIFICATION_TOKEN_UPDATED_KEY, true).apply();
    }

    @Override
    public void onFailedToSaveFirebaseTokenInServer(String errorMessage) {
        Log.d("d", errorMessage );
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
            //Log.d("d", "onActivityResult UPDATE_PROFILE");
            setupHeaderViewNavigation(email, nome, urlPhotoUser);
        }
        /*if(requestCode== InfoWindowFragment.PLACE_AUTOCOMPLETE_REQUEST_CODE){
            //data.getExtras()
        }*/
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
