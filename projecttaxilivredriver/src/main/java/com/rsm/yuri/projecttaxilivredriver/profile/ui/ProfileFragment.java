package com.rsm.yuri.projecttaxilivredriver.profile.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import androidx.core.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rsm.yuri.projecttaxilivredriver.R;
import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverApp;
import com.rsm.yuri.projecttaxilivredriver.editprofile.ui.EditProfileActivity;
import com.rsm.yuri.projecttaxilivredriver.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivredriver.profile.ProfilePresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment implements ProfileView {//, MainActivity.OnSharedPreferencesReadyListener {

    @BindView(R.id.imgAvatarProfileAct)
    CircleImageView imgAvatarProfileAct;
    @BindView(R.id.nomeUserProfileAct)
    TextView nomeUserProfileAct;
    @BindView(R.id.emailUserProfileAct)
    TextView emailUserProfileAct;
    @BindView(R.id.imgCarProfileAct)
    CircleImageView imgCarProfileAct;
    @BindView(R.id.modeloCarProfileAct)
    TextView modeloCarProfileAct;
    @BindView(R.id.marcaCarProfileAct)
    TextView marcaCarProfileAct;
    @BindView(R.id.corCarProfileAct)
    TextView corCarProfileAct;
    @BindView(R.id.anoCarProfileAct)
    TextView anoCarProfileAct;
    @BindView(R.id.placaCarProfileAct)
    TextView placaCarProfileAct;
    @BindView(R.id.frameLayoutEditarPerfil)
    FrameLayout frameLayoutEditarPerfil;
    Unbinder unbinder;

    @Inject
    ProfilePresenter presenter;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    ImageLoader imageLoader;

    public final static int UPDATE_PROFILE = 0;

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
        app.getProfileComponent(this, this).inject(this);
        //Log.d("d", "ProfileFragment.setupInjection:finalizada");
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

        setTextViews();
        setPhotos();

    }

    private void setTextViews() {
        String email = sharedPreferences.getString(TaxiLivreDriverApp.EMAIL_KEY, "email_sh_pr");
        String nome = sharedPreferences.getString(TaxiLivreDriverApp.NOME_KEY, "nome_sh_pr");

        String modelo = sharedPreferences.getString(TaxiLivreDriverApp.MODELO_KEY, "modelo_sh_pr");
        String marca = sharedPreferences.getString(TaxiLivreDriverApp.MARCA_KEY, "marca_sh_pr");
        String cor = sharedPreferences.getString(TaxiLivreDriverApp.COR_KEY, "cor_sh_pr");
        long ano = sharedPreferences.getLong(TaxiLivreDriverApp.ANO_KEY, 2000);
        String placa = sharedPreferences.getString(TaxiLivreDriverApp.PLACA_KEY, "placa_sh_pr");

        //Log.d("d", "ProfileFragment. onSharedPreferencesReady()urlPhoto: "+ urlPhotoUser);
        nomeUserProfileAct.setText(nome);
        emailUserProfileAct.setText(email);

        modeloCarProfileAct.setText(modelo);
        marcaCarProfileAct.setText(marca);
        corCarProfileAct.setText(cor);
        anoCarProfileAct.setText(ano+"");
        placaCarProfileAct.setText(placa);
    }

    private void setPhotos() {
        String urlPhotoUser = sharedPreferences.getString(TaxiLivreDriverApp.URL_PHOTO_DRIVER_KEY, "url_driver_sh_pr");
        if (!urlPhotoUser.equals("default"))
            imageLoader.load(imgAvatarProfileAct, urlPhotoUser);
        String urlPhotoCar = sharedPreferences.getString(TaxiLivreDriverApp.URL_PHOTO_CAR_KEY, "url_car_sh_pr");
        if (!urlPhotoUser.equals("default"))
            imageLoader.load(imgCarProfileAct, urlPhotoCar);
    }

    @OnClick(R.id.frameLayoutEditarPerfil)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.frameLayoutEditarPerfil:
                startActivityForResult(new Intent(getActivity(), EditProfileActivity.class),UPDATE_PROFILE);
                break;
        }
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
        unbinder.unbind();
        super.onDestroyView();

    }

    @Override
    public void onResume() {
        super.onResume();
        setTextViews();
        setPhotos();
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



    //@Override
    //public void onSharedPreferencesReady(String email, String nome, String urlPhotoUser) {
        /*String email = sharedPreferences.getString(TaxiLivreDriverApp.EMAIL_KEY, "");
        String nome = sharedPreferences.getString(TaxiLivreDriverApp.NOME_KEY, "");
        String urlPhotoUser = sharedPreferences.getString(TaxiLivreDriverApp.URL_PHOTO_DRIVER_KEY, "");
        Log.d("d", "ProfileFragment. onSharedPreferencesReady()urlPhoto: "+ urlPhotoUser);
        if(!urlPhotoUser.equals("default"))
            //imageLoader.load(imgAvatarProfileAct, urlPhotoUser);
        nomeUserProfileAct.setText(nome);
        emailUserProfileAct.setText(email);*/
    //}
}
