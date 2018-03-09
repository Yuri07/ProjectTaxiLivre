package com.rsm.yuri.projecttaxilivredriver;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.rsm.yuri.projecttaxilivredriver.avaliation.di.AvaliationComponent;
import com.rsm.yuri.projecttaxilivredriver.avaliation.di.AvaliationModule;
import com.rsm.yuri.projecttaxilivredriver.avaliation.di.DaggerAvaliationComponent;
import com.rsm.yuri.projecttaxilivredriver.avaliation.ui.AvaliationView;
import com.rsm.yuri.projecttaxilivredriver.chat.di.ChatComponet;
import com.rsm.yuri.projecttaxilivredriver.chat.di.ChatModule;
import com.rsm.yuri.projecttaxilivredriver.chat.di.DaggerChatComponet;
import com.rsm.yuri.projecttaxilivredriver.chat.ui.ChatView;
import com.rsm.yuri.projecttaxilivredriver.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.di.DaggerHistoricChatsListComponent;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.di.HistoricChatsListComponent;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.di.HistoricChatsListModule;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui.HistoricChatsListView;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui.OnItemClickListener;
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

    public HistoricChatsListComponent getHistoricChatsListComponent(Context context,
                                                                    HistoricChatsListView view,
                                                                    OnItemClickListener onItemClickListener) {
        libsModule.setContext(context);

        return DaggerHistoricChatsListComponent
                .builder()
                .taxiLivreDriverAppModule(taxiLivreDriverAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .historicChatsListModule(new HistoricChatsListModule(view, onItemClickListener))
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

}
