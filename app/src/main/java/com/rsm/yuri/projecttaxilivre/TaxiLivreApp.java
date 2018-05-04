package com.rsm.yuri.projecttaxilivre;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.rsm.yuri.projecttaxilivre.FirebaseService.di.DaggerFIIDServiceComponent;
import com.rsm.yuri.projecttaxilivre.FirebaseService.di.FIIDServiceComponent;
import com.rsm.yuri.projecttaxilivre.adddialog.di.AddDialogComponent;
import com.rsm.yuri.projecttaxilivre.adddialog.di.AddDialogModule;
import com.rsm.yuri.projecttaxilivre.adddialog.di.DaggerAddDialogComponent;
import com.rsm.yuri.projecttaxilivre.adddialog.ui.AddDialogView;
import com.rsm.yuri.projecttaxilivre.chat.di.ChatComponet;
import com.rsm.yuri.projecttaxilivre.chat.di.ChatModule;
import com.rsm.yuri.projecttaxilivre.chat.di.DaggerChatComponet;
import com.rsm.yuri.projecttaxilivre.chat.ui.ChatView;
import com.rsm.yuri.projecttaxilivre.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivre.historicchatslist.di.DaggerHistoricChatsListComponent;
import com.rsm.yuri.projecttaxilivre.historicchatslist.di.HistoricChatsListComponent;
import com.rsm.yuri.projecttaxilivre.historicchatslist.di.HistoricChatsListModule;
import com.rsm.yuri.projecttaxilivre.historicchatslist.ui.HistoricChatsListView;
import com.rsm.yuri.projecttaxilivre.historicchatslist.ui.OnItemClickListener;
import com.rsm.yuri.projecttaxilivre.lib.di.LibsModule;
import com.rsm.yuri.projecttaxilivre.login.di.DaggerLoginComponent;
import com.rsm.yuri.projecttaxilivre.login.di.LoginComponent;
import com.rsm.yuri.projecttaxilivre.login.di.LoginModule;
import com.rsm.yuri.projecttaxilivre.login.ui.LoginView;
import com.rsm.yuri.projecttaxilivre.main.di.DaggerMainComponent;
import com.rsm.yuri.projecttaxilivre.main.ui.MainView;
import com.rsm.yuri.projecttaxilivre.main.di.MainComponent;
import com.rsm.yuri.projecttaxilivre.main.di.MainModule;
import com.rsm.yuri.projecttaxilivre.map.InteractiveInfoWindow.di.DaggerInfoWindowComponent;
import com.rsm.yuri.projecttaxilivre.map.InteractiveInfoWindow.di.InfoWindowComponent;
import com.rsm.yuri.projecttaxilivre.map.InteractiveInfoWindow.di.InfoWindowModule;
import com.rsm.yuri.projecttaxilivre.map.di.DaggerMapComponent;
import com.rsm.yuri.projecttaxilivre.map.di.MapComponent;
import com.rsm.yuri.projecttaxilivre.map.di.MapModule;
import com.rsm.yuri.projecttaxilivre.map.ui.MapFragment;
import com.rsm.yuri.projecttaxilivre.map.ui.MapView;
import com.rsm.yuri.projecttaxilivre.profile.di.DaggerProfileComponent;
import com.rsm.yuri.projecttaxilivre.profile.di.ProfileComponent;
import com.rsm.yuri.projecttaxilivre.profile.di.ProfileModule;
import com.rsm.yuri.projecttaxilivre.profile.ui.ProfileView;

/**
 * Created by yuri_ on 12/01/2018.
 */

public class TaxiLivreApp extends Application{

    public final static String EMAIL_KEY = "email";
    public final static String NOME_KEY = "nome";
    public final static String URL_PHOTO_USER_KEY = "urlPhotoUser";

    public final static String FIREBASE_NOTIFICATION_TOKEN_KEY = "firebaseNotificationToken";
    public final static String FIREBASE_NOTIFICATION_TOKEN_UPDATED_KEY = "firebaseNotificationUpdatedToken";

    private LibsModule libsModule;
    private DomainModule domainModule;
    private TaxiLivreAppModule taxiLivreAppModule;

    @Override
    public void onCreate() {
        super.onCreate();
        initModules();
    }

    private void initModules() {
        libsModule = new LibsModule();
        domainModule = new DomainModule();
        taxiLivreAppModule= new TaxiLivreAppModule(this);
    }

    public static String getEmailKey() {
        return EMAIL_KEY;
    }

    public static String getUrlPhotoUserKey() {
        return URL_PHOTO_USER_KEY;
    }

    public final static String getNomeKey() {
        return NOME_KEY;
    }



    public LoginComponent getLoginComponent(LoginView view) {
        return DaggerLoginComponent
                .builder()
                .taxiLivreAppModule(taxiLivreAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .loginModule(new LoginModule(view))
                .build();
    }

    public MainComponent getMainComponent(Context context, MainView view, FragmentManager manager, MapFragment mapFragment) {
        libsModule.setContext(context);

        return DaggerMainComponent
                .builder()
                .taxiLivreAppModule(taxiLivreAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .mainModule(new MainModule(view, manager, mapFragment))
                .build();
    }

    public MapComponent getMapComponent(Fragment fragment, MapView mapView){
        libsModule.setContext(fragment.getContext());//era libsModule.setFragment(fragment);(ainda nao testado alteracao)

        return DaggerMapComponent
                .builder()
                .taxiLivreAppModule(taxiLivreAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .mapModule(new MapModule(mapView))
                .build();
    }

    public HistoricChatsListComponent getHistoricChatsListComponent(Context context,
                                                                    HistoricChatsListView view,
                                                                    OnItemClickListener onItemClickListener) {
        libsModule.setContext(context);

        return DaggerHistoricChatsListComponent
                .builder()
                .taxiLivreAppModule(taxiLivreAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .historicChatsListModule(new HistoricChatsListModule(view, onItemClickListener))
                .build();

    }

    public ChatComponet getChatComponent(ChatView view, Context context){
        libsModule.setContext(context);

        return DaggerChatComponet
                .builder()
                .taxiLivreAppModule(taxiLivreAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .chatModule(new ChatModule(view))
                .build();
    }

    public ProfileComponent getProfileComponent(ProfileView view) {
        return DaggerProfileComponent
                .builder()
                .taxiLivreAppModule(taxiLivreAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .profileModule(new ProfileModule(view))
                .build();
    }

    public AddDialogComponent getAddDialogComponent(AddDialogView view) {
        return DaggerAddDialogComponent
                .builder()
                .taxiLivreAppModule(taxiLivreAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .addDialogModule(new AddDialogModule(view))
                .build();
    }

    public InfoWindowComponent getInfoWindowComponent(Fragment fragment){
        libsModule.setContext(fragment.getContext());
        return DaggerInfoWindowComponent
                .builder()
                .taxiLivreAppModule(taxiLivreAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .infoWindowModule(new InfoWindowModule())
                .build();
    }

    public FIIDServiceComponent getFIIDServiceComponent(){
        return DaggerFIIDServiceComponent
                .builder()
                .taxiLivreAppModule(taxiLivreAppModule)
                .domainModule(domainModule)
                .build();
    }

}
