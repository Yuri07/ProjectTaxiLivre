package com.rsm.yuri.projecttaxilivredriver;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.rsm.yuri.projecttaxilivredriver.avaliation.di.AvaliationComponent;
import com.rsm.yuri.projecttaxilivredriver.avaliation.di.AvaliationModule;
//import com.rsm.yuri.projecttaxilivredriver.avaliation.di.DaggerAvaliationComponent;
import com.rsm.yuri.projecttaxilivredriver.avaliation.di.DaggerAvaliationComponent;
import com.rsm.yuri.projecttaxilivredriver.avaliation.ui.AvaliationView;
import com.rsm.yuri.projecttaxilivredriver.chat.di.ChatComponet;
import com.rsm.yuri.projecttaxilivredriver.chat.di.ChatModule;
import com.rsm.yuri.projecttaxilivredriver.chat.di.DaggerChatComponet;
import com.rsm.yuri.projecttaxilivredriver.chat.ui.ChatView;
import com.rsm.yuri.projecttaxilivredriver.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivredriver.editprofile.di.DaggerEditProfileComponent;
import com.rsm.yuri.projecttaxilivredriver.editprofile.di.EditProfileComponent;
import com.rsm.yuri.projecttaxilivredriver.editprofile.di.EditProfileModule;
import com.rsm.yuri.projecttaxilivredriver.editprofile.ui.EditProfileView;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.di.DaggerHistoricChatsListComponent;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.di.HistoricChatsListComponent;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.di.HistoricChatsListModule;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui.ConnectivityListener;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui.HistoricChatsListView;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui.OnItemClickListener;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.di.DaggerHistoricTravelsListComponent;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.di.HistoricTravelsListComponent;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.di.HistoricTravelsListModule;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.ui.HistoricTravelListView;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.ui.OnHistoricTravelItemClickListener;
import com.rsm.yuri.projecttaxilivredriver.home.di.DaggerHomeComponent;
import com.rsm.yuri.projecttaxilivredriver.home.di.HomeComponent;
import com.rsm.yuri.projecttaxilivredriver.home.di.HomeModule;
import com.rsm.yuri.projecttaxilivredriver.home.ui.HomeFragment;
import com.rsm.yuri.projecttaxilivredriver.home.ui.HomeView;
import com.rsm.yuri.projecttaxilivredriver.lib.di.LibsModule;
import com.rsm.yuri.projecttaxilivredriver.login.di.DaggerLoginComponent;
import com.rsm.yuri.projecttaxilivredriver.login.di.LoginComponent;
import com.rsm.yuri.projecttaxilivredriver.login.di.LoginModule;
import com.rsm.yuri.projecttaxilivredriver.login.ui.LoginView;
import com.rsm.yuri.projecttaxilivredriver.main.di.DaggerMainComponent;
import com.rsm.yuri.projecttaxilivredriver.main.di.MainComponent;
import com.rsm.yuri.projecttaxilivredriver.main.di.MainModule;
import com.rsm.yuri.projecttaxilivredriver.main.ui.MainView;
import com.rsm.yuri.projecttaxilivredriver.profile.di.DaggerProfileComponent;
import com.rsm.yuri.projecttaxilivredriver.profile.di.ProfileComponent;
import com.rsm.yuri.projecttaxilivredriver.profile.di.ProfileModule;
import com.rsm.yuri.projecttaxilivredriver.profile.ui.ProfileView;

/**
 * Created by yuri_ on 02/03/2018.
 */

public class TaxiLivreDriverApp extends Application {

    private LibsModule libsModule;
    private DomainModule domainModule;
    private TaxiLivreDriverAppModule taxiLivreDriverAppModule;

    public final static String EMAIL_KEY = "email";
    public final static String NOME_KEY = "nome";
    public final static String URL_PHOTO_DRIVER_KEY = "urlPhotoDriver";
    public final static String STATUS_KEY = "status";
    public final static String AVERAG_RATING_KEY = "averagRating";
    public final static String TOTAL_RATINGS_KEY = "totalRatings";
    public final static String COUNT_1_STARS_KEY = "count1Stars";
    public final static String COUNT_2_STARS_KEY = "count2Stars";
    public final static String COUNT_3_STARS_KEY = "count3Stars";
    public final static String COUNT_4_STARS_KEY = "count4Stars";
    public final static String COUNT_5_STARS_KEY = "count5Stars";
    public final static String MODELO_KEY = "modelo";
    public final static String URL_PHOTO_CAR_KEY = "urlPhotoCar";
    public final static String MARCA_KEY = "marca";
    public final static String COR_KEY = "cor";
    public final static String ANO_KEY = "ano";
    public final static String PLACA_KEY = "placa";

    public final static String CIDADE_KEY = "cidade";

    @Override
    public void onCreate() {
        super.onCreate();
        initModules();
    }

    private void initModules() {
        libsModule = new LibsModule();
        domainModule = new DomainModule();
        taxiLivreDriverAppModule= new TaxiLivreDriverAppModule(this);
    }

    public boolean getConectivityStatus(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public LoginComponent getLoginComponent(LoginView view) {
        return DaggerLoginComponent
                .builder()
                .taxiLivreDriverAppModule(taxiLivreDriverAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .loginModule(new LoginModule(view))
                .build();
    }

    public MainComponent getMainComponent(Context context, MainView view, FragmentManager manager, Fragment[] fragments) {
        libsModule.setContext(context);

        return DaggerMainComponent
                .builder()
                .taxiLivreDriverAppModule(taxiLivreDriverAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .mainModule(new MainModule(view, manager, fragments))
                .build();
    }

    public HomeComponent getHomeComponent(HomeView view, Fragment fragment, String cidade){
        libsModule.setContext(fragment.getContext());

        return DaggerHomeComponent
                .builder()
                .taxiLivreDriverAppModule(taxiLivreDriverAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .homeModule(new HomeModule(view,cidade))
                .build();
    }

    public AvaliationComponent getAvaliationComponent(AvaliationView view, Fragment fragment){
        libsModule.setContext(fragment.getContext());
        return DaggerAvaliationComponent
                .builder()
                .taxiLivreDriverAppModule(taxiLivreDriverAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .avaliationModule(new AvaliationModule(view))
                .build();
    }

    public ProfileComponent getProfileComponent(ProfileView view, Fragment fragment){
        libsModule.setContext(fragment.getContext());
        return DaggerProfileComponent
                .builder()
                .taxiLivreDriverAppModule(taxiLivreDriverAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .profileModule(new ProfileModule(view))
                .build();
    }

    public HistoricChatsListComponent getHistoricChatsListComponent(Context context,
                                                                    HistoricChatsListView view,
                                                                    OnItemClickListener onItemClickListener,
                                                                    ConnectivityListener connectivityListener) {
        libsModule.setContext(context);

        return DaggerHistoricChatsListComponent
                .builder()
                .taxiLivreDriverAppModule(taxiLivreDriverAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .historicChatsListModule(new HistoricChatsListModule(view, onItemClickListener, connectivityListener))
                .build();
    }

    public ChatComponet getChatComponent(ChatView view, Context context) {
        libsModule.setContext(context);

        return DaggerChatComponet
                .builder()
                .taxiLivreDriverAppModule(taxiLivreDriverAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .chatModule(new ChatModule(view))
                .build();
    }

    public HistoricTravelsListComponent getHistoricTravelsListComponent(Context context,
                                                                        HistoricTravelListView view,
                                                                        OnHistoricTravelItemClickListener onItemClickListener){
        libsModule.setContext(context);

        return DaggerHistoricTravelsListComponent
                .builder()
                .taxiLivreDriverAppModule(taxiLivreDriverAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .historicTravelsListModule(new HistoricTravelsListModule(view, onItemClickListener))
                .build();

    }

    public EditProfileComponent getEditProfileComponent(EditProfileView view, Context context, int uploaderInjection){
        libsModule.setContext(context);
        //EditProfileModule editProfileModule = new EditProfileModule(view, uploaderInjection);

        return DaggerEditProfileComponent
                .builder()
                .taxiLivreDriverAppModule(taxiLivreDriverAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .editProfileModule(new EditProfileModule(view, uploaderInjection))
                .build();
    }

}
