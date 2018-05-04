package com.rsm.yuri.projecttaxilivredriver.FirebaseService;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.rsm.yuri.projecttaxilivredriver.FirebaseService.di.FIIDServiceComponent;
import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverApp;

/**
 * Created by yuri_ on 24/04/2018.
 */

/*
* Classe não utilizada no projeto(service é inicializado sem que app possa ter nenhum controle e não é possível instanciar os recursos para enviar o token para o servidor em alguns momentos)
*/

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    //@Inject
    //SharedPreferences sharedPreferences;

    //@Inject
    //FirebaseAPI firebaseAPI;

    //private TaxiLivreApp app;

    private static final String TAG = "MyFirebaseIIDService";

    public MyFirebaseInstanceIDService() {
        //app = (TaxiLivreApp) getApplication();
        //setupInjection();

        Log.d("d", "MyFirebaseIIDService.Constructor() ");

    }

    private void setupInjection() {
        /*TaxiLivreDriverApp app = (TaxiLivreDriverApp) getApplication();
        if( app!=null ) {
            FIIDServiceComponent fiidServiceComponent = app.getFIIDServiceComponent();
            fiidServiceComponent.inject(this);
        }else{
            Log.d("d", "App esta recebendo nulo em  MyFirebaseInstanceIDService");
        }*/

    }

    public FIIDServiceComponent getFIIDServiceComponent(){
        return null;

    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("d", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {

        /*SharedPreferences sharedPreferences =

        sharedPreferences.edit().putString(TaxiLivreApp.FIREBASE_NOTIFICATION_TOKEN_KEY, token).apply();
        sharedPreferences.edit().putBoolean(TaxiLivreApp.FIREBASE_NOTIFICATION_TOKEN_UPDATED_KEY, false).apply();*/

        //firebaseAPI.sendTokenToServer(token);

        /*DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        DatabaseReference myTokenReference = databaseReference.getRoot()
                .child(FirebaseAPI.NOTIFICATION_TOKEN_PATH)
                .child(getAuthUserEmail().replace(".","_"));
        myTokenReference.setValue(token);*/

    }

}
