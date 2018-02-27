package com.rsm.yuri.projecttaxilivre.profile.ui;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rsm.yuri.projecttaxilivre.R;
import com.rsm.yuri.projecttaxilivre.TaxiLivreApp;
import com.rsm.yuri.projecttaxilivre.adddialog.di.AddDialogComponent;
import com.rsm.yuri.projecttaxilivre.adddialog.dialoglistenercallback.AddDialogFragmentListener;
import com.rsm.yuri.projecttaxilivre.adddialog.ui.AddDialogFragment;
import com.rsm.yuri.projecttaxilivre.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivre.main.ui.MainActivity;
import com.rsm.yuri.projecttaxilivre.profile.di.ProfileComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements AddDialogFragmentListener {

    @BindView(R.id.imgAvatarProfileAct)
    CircleImageView imgAvatarProfileAct;
    @BindView(R.id.nomeUserProfileAct)
    TextView nomeUserProfileAct;
    @BindView(R.id.emailUserProfileAct)
    TextView emailUserProfileAct;

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    ImageLoader imageLoader;

    TaxiLivreApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        app = (TaxiLivreApp) getApplication();
        setupInjection();

        String email = sharedPreferences.getString(app.getEmailKey(), "");
        String nome = sharedPreferences.getString(app.getNomeKey(), "");
        String urlPhotoUser = sharedPreferences.getString(app.getUrlPhotoUserKey(), "");

        imageLoader.load(imgAvatarProfileAct, urlPhotoUser);
        nomeUserProfileAct.setText(nome);
        emailUserProfileAct.setText(email);

    }

    private void setupInjection() {
        /*AddDialogComponent addDialogComponent= app.getAddDialogComponent();
        addDialogComponent.inject(this);*/

        ProfileComponent profileComponent= app.getProfileComponent();
        profileComponent.inject(this);


    }

    @OnClick({R.id.frameLayoutImgAvatar, R.id.frameLayoutNome, R.id.frameLayoutEmail})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.frameLayoutImgAvatar:
                /*Snackbar.make(view, "FrameLayoutImgAvatar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                break;
            case R.id.frameLayoutNome:
                AddDialogFragment frag = new AddDialogFragment();
                Bundle args = new Bundle();
                args.putString(AddDialogFragment.KEY, TaxiLivreApp.NOME_KEY);
                frag.setArguments(args);
                frag.show(getFragmentManager(), "");
                break;
            case R.id.frameLayoutEmail:
                Snackbar.make(view, "FrameLayoutEmail", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
        }
    }

    @Override
    public void onFinishAddDialog(String key, String inputText) {

        switch (key) {
            case TaxiLivreApp.NOME_KEY:
                nomeUserProfileAct.setText(inputText);
                sharedPreferences.edit().putString(TaxiLivreApp.NOME_KEY, inputText).apply();
                break;
        }

    }
}
