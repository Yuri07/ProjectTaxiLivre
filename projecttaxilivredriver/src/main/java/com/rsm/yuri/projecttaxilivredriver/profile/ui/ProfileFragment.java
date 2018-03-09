package com.rsm.yuri.projecttaxilivredriver.profile.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rsm.yuri.projecttaxilivredriver.R;
import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverApp;
import com.rsm.yuri.projecttaxilivredriver.adddialog.ui.AddDialogFragment;
import com.rsm.yuri.projecttaxilivredriver.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivredriver.main.ui.MainActivity;
import com.rsm.yuri.projecttaxilivredriver.profile.ProfilePresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment implements ProfileView, MainActivity.OnSharedPreferencesReadyListener {

    @BindView(R.id.imgAvatarProfileAct)
    CircleImageView imgAvatarProfileAct;
    @BindView(R.id.nomeUserProfileAct)
    TextView nomeUserProfileAct;
    @BindView(R.id.emailUserProfileAct)
    TextView emailUserProfileAct;
    @BindView(R.id.frameLayoutEmail)
    FrameLayout frameLayoutEmail;
    @BindView(R.id.frameLayoutImgAvatar)
    FrameLayout frameLayoutImgAvatar;
    @BindView(R.id.frameLayoutNome)
    FrameLayout frameLayoutNome;
    @BindView(R.id.profileFragmentContainer)
    LinearLayout profileFragmentContainer;
    Unbinder unbinder;

    @Inject
    ProfilePresenter presenter;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    ImageLoader imageLoader;

    private String photoPath;
    private final static int REQUEST_PICTURE = 0;
    private final static int PERMISSIONS_REQUEST_READ_MEDIA = 10;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupInjection();

        //presenter.onCreate();
    }

    private void setupInjection() {
        TaxiLivreDriverApp app = (TaxiLivreDriverApp) getActivity().getApplication();
        app.getProfileComponent(this,this).inject(this);
        Log.d("d", "ProfileFragment.setupInjection:finalizada");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String email = sharedPreferences.getString(TaxiLivreDriverApp.EMAIL_KEY, "");
        String nome = sharedPreferences.getString(TaxiLivreDriverApp.NOME_KEY, "");
        String urlPhotoUser = sharedPreferences.getString(TaxiLivreDriverApp.URL_PHOTO_DRIVER_KEY, "");
        Log.d("d", "ProfileFragment. onSharedPreferencesReady()urlPhoto: "+ urlPhotoUser);
        if(!urlPhotoUser.equals("default"))
            imageLoader.load(imgAvatarProfileAct, urlPhotoUser);
            nomeUserProfileAct.setText(nome);
        emailUserProfileAct.setText(email);

    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSharedPreferencesReadyListener) {
            listener = (OnSharedPreferencesReadyListener) context;
        } else {
            throw new ClassCastException();
        }
        listener.onSharedPreferencesReady();
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onUploadInit() {

    }

    @Override
    public void onUploadComplete(String urlPhotoUser) {

    }

    @Override
    public void onUploadError(String error) {

    }

    @Override
    public void onSharedPreferencesReady(String email, String nome, String urlPhotoUser) {
        /*String email = sharedPreferences.getString(TaxiLivreDriverApp.EMAIL_KEY, "");
        String nome = sharedPreferences.getString(TaxiLivreDriverApp.NOME_KEY, "");
        String urlPhotoUser = sharedPreferences.getString(TaxiLivreDriverApp.URL_PHOTO_DRIVER_KEY, "");
        Log.d("d", "ProfileFragment. onSharedPreferencesReady()urlPhoto: "+ urlPhotoUser);
        if(!urlPhotoUser.equals("default"))
            //imageLoader.load(imgAvatarProfileAct, urlPhotoUser);
        nomeUserProfileAct.setText(nome);
        emailUserProfileAct.setText(email);*/
    }
}
