package com.rsm.yuri.projecttaxilivredriver.editprofile.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rsm.yuri.projecttaxilivredriver.R;
import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverApp;
import com.rsm.yuri.projecttaxilivredriver.editprofile.EditProfilePresenter;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Car;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;
import com.rsm.yuri.projecttaxilivredriver.lib.base.ImageLoader;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements EditProfileView {

    @BindView(R.id.imgAvatarEditarProfileAct)
    CircleImageView imgAvatarProfileAct;
    @BindView(R.id.editTxtNome)
    EditText editTxtNome;
    @BindView(R.id.editTxtEmail)
    EditText editTxtEmail;
    @BindView(R.id.editTxtModelo)
    EditText editTxtModelo;
    @BindView(R.id.editTxtMarca)
    EditText editTxtMarca;
    @BindView(R.id.editTxtCor)
    EditText editTxtCor;
    @BindView(R.id.editTxtAno)
    EditText editTxtAno;
    @BindView(R.id.editTxtPlaca)
    EditText editTxtPlaca;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    EditProfilePresenter presenter;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        setupInjection();
        presenter.onCreate();
        setTextFields();
        setPhoto();

    }

    private void setupInjection() {
        TaxiLivreDriverApp app = (TaxiLivreDriverApp) getApplication();
        app.getEditProfileComponent(this, this).inject(this);
    }

    private void setTextFields() {

        String email = sharedPreferences.getString(TaxiLivreDriverApp.EMAIL_KEY, "email_sh_pr");
        String nome = sharedPreferences.getString(TaxiLivreDriverApp.NOME_KEY, "nome_sh_pr");


        String modelo = sharedPreferences.getString(TaxiLivreDriverApp.MODELO_KEY, "modelo_sh_pr");
        String marca = sharedPreferences.getString(TaxiLivreDriverApp.MARCA_KEY, "marca_sh_pr");
        String cor = sharedPreferences.getString(TaxiLivreDriverApp.COR_KEY, "cor_sh_pr");
        long ano = sharedPreferences.getLong(TaxiLivreDriverApp.ANO_KEY, 2000);
        String placa = sharedPreferences.getString(TaxiLivreDriverApp.PLACA_KEY, "placa_sh_pr");

        editTxtNome.setText(nome);
        editTxtEmail.setText(email);

        editTxtModelo.setText(modelo);
        editTxtMarca.setText(marca);
        editTxtCor.setText(cor);
        editTxtAno.setText(ano + "");
        editTxtPlaca.setText(placa);

    }

    private void setPhoto(){
        String urlPhotoUser = sharedPreferences.getString(TaxiLivreDriverApp.URL_PHOTO_DRIVER_KEY, "url_sh_pr");
        if ((!urlPhotoUser.equals("default"))&&!(urlPhotoUser.equals("url_sh_pr")))
            imageLoader.load(imgAvatarProfileAct, urlPhotoUser);
    }

    @OnClick({R.id.ok_edit_profile, R.id.imgAvatarEditarProfileAct})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ok_edit_profile:
                saveProfile();
                break;
            case R.id.imgAvatarEditarProfileAct:
                takePicture();
                break;
        }


    }

    private void saveProfile() {
        String email = sharedPreferences.getString(TaxiLivreDriverApp.EMAIL_KEY, "email_sh_pr");
        Driver driver = new Driver();
        driver.setNome(editTxtNome.getText().toString());
        driver.setEmail(email);
        Car car = new Car();
        car.setEmail(email);
        car.setModelo(editTxtModelo.getText().toString());
        car.setMarca(editTxtMarca.getText().toString());
        car.setCor(editTxtCor.getText().toString());
        car.setAno(Long.parseLong(editTxtAno.getText().toString()));
        car.setPlaca(editTxtPlaca.getText().toString());
        //presenter.updateDriver(driver);
        //presenter.updateCar(car);
        presenter.saveProfile(driver, car);
    }

    private void takePicture() {
        Toast.makeText(this, "takePicture clicked", Toast.LENGTH_SHORT).show();
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
    public void showInput() {
        editTxtNome.setVisibility(View.VISIBLE);
        editTxtEmail.setVisibility(View.VISIBLE);
        editTxtModelo.setVisibility(View.VISIBLE);
        editTxtMarca.setVisibility(View.VISIBLE);
        editTxtCor.setVisibility(View.VISIBLE);
        editTxtAno.setVisibility(View.VISIBLE);
        editTxtPlaca.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInput() {
        editTxtNome.setVisibility(View.GONE);
        editTxtEmail.setVisibility(View.GONE);
        editTxtModelo.setVisibility(View.GONE);
        editTxtMarca.setVisibility(View.GONE);
        editTxtCor.setVisibility(View.GONE);
        editTxtAno.setVisibility(View.GONE);
        editTxtPlaca.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void profileEdited(Driver driver, Car car) {

        sharedPreferences.edit().putString(TaxiLivreDriverApp.NOME_KEY, driver.getNome()).apply();

        sharedPreferences.edit().putString(TaxiLivreDriverApp.MODELO_KEY, car.getModelo()).apply();//.commit();//commit() e o que tem no codigo original lesson4.edx
        sharedPreferences.edit().putString(TaxiLivreDriverApp.MARCA_KEY, car.getMarca()).apply();
        sharedPreferences.edit().putString(TaxiLivreDriverApp.COR_KEY, car.getCor()).apply();
        sharedPreferences.edit().putLong(TaxiLivreDriverApp.ANO_KEY, car.getAno()).apply();
        sharedPreferences.edit().putString(TaxiLivreDriverApp.PLACA_KEY, car.getPlaca()).apply();
        finish();
    }

    @Override
    public void profileNotEdited(String msgError) {
        Toast.makeText(this, msgError, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTextFields();
    }

}
