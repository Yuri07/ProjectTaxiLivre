package com.rsm.yuri.projecttaxilivredriver.editprofile.ui;

import android.Manifest;
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
//import android.support.design.widget.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.rsm.yuri.projecttaxilivredriver.R;
import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverApp;
import com.rsm.yuri.projecttaxilivredriver.editprofile.EditProfilePresenter;
import com.rsm.yuri.projecttaxilivredriver.editprofile.di.EditProfileModule;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Car;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;
import com.rsm.yuri.projecttaxilivredriver.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivredriver.main.ui.MainActivity;

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
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements EditProfileView {

    @BindView(R.id.imgAvatarEditarProfileAct)
    CircleImageView imgAvatarProfileAct;
    @BindView(R.id.editTxtNome)
    EditText editTxtNome;
    @BindView(R.id.editTxtEmail)
    EditText editTxtEmail;
    @BindView(R.id.imgCarEditarProfileAct)
    CircleImageView imgCarProfileAct;
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
    @BindView(R.id.editProfileActivityContainer)
    RelativeLayout relativeLayout;

    @Inject
    EditProfilePresenter presenter;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    ImageLoader imageLoader;

    private String urlPhotoDriver, urlPhotoCar;
    private String photoPath;
    private final static int REQUEST_PICTURE = 0;
    private final static int PERMISSIONS_REQUEST_READ_MEDIA = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        setupInjection(EditProfileModule.UPLOADER_NULL);
        presenter.onCreate();
        /*setTextFields();
        setPhotos();*/
        presenter.retrieveDataUser();

    }

    private void setupInjection(int uploaderInjection) {
        TaxiLivreDriverApp app = (TaxiLivreDriverApp) getApplication();
        app.getEditProfileComponent(this, this, uploaderInjection).inject(this);
    }



    @OnClick({R.id.ok_edit_profile, R.id.imgAvatarEditarProfileAct, R.id.imgCarEditarProfileAct})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ok_edit_profile:
                saveProfile();
                break;
            case R.id.imgAvatarEditarProfileAct:
                setupInjection(EditProfileModule.UPLOADER_AVATAR);
                takePicture();
                break;
            case R.id.imgCarEditarProfileAct:
                setupInjection(EditProfileModule.UPLOADER_CAR);
                takePicture();
        }

    }

    private void saveProfile() {
        String email = sharedPreferences.getString(TaxiLivreDriverApp.EMAIL_KEY, "email_sh_pr");
        Driver driver = new Driver();
        driver.setNome(editTxtNome.getText().toString());
        driver.setUrlPhotoDriver(urlPhotoDriver);
        driver.setEmail(email);
        Car car = new Car();
        car.setEmail(email);
        car.setUrlPhotoCar(urlPhotoCar);
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
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_MEDIA);
        } else {
            takePhoto();
        }
    }

    private void takePhoto() {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);
        File photoFile = getFile();

        if (photoFile != null) {
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
                intentList = addIntentsToList(intentList, takePhotoIntent);
            }
        }

        if (pickIntent.resolveActivity(getPackageManager()) != null) {
            intentList = addIntentsToList(intentList, pickIntent);
        }

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    getString(R.string.main_message_picture_source));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }

        startActivityForResult(chooserIntent, REQUEST_PICTURE);
    }

    private List<Intent> addIntentsToList(List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }

    private File getFile() {
        File photoFile = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            photoFile = File.createTempFile(imageFileName, ".jpg", storageDir);
            photoPath = photoFile.getAbsolutePath();
            //Uri selectedImageUri = photoFile.ge
        } catch (IOException ex) {
            Snackbar.make(relativeLayout, R.string.main_error_dispatch_camera, Snackbar.LENGTH_SHORT).show();
        }
        return photoFile;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_PICTURE) {
            boolean isCamera = (data == null ||
                    data.getData() == null);

            if (isCamera) {
                addPicToGallery();
            } else {
                photoPath = getRealPathFromURI(data.getData());
            }

            //String urlPhotoUser = sharedPreferences.getString(TaxiLivreApp.URL_PHOTO_USER_KEY,"");
            presenter.uploadPhoto(Uri.fromFile(new File(photoPath)));

        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result = null;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            if (contentURI.toString().contains("mediaKey")) {
                cursor.close();

                try {
                    File file = File.createTempFile("tempImg", ".jpg", getCacheDir());
                    InputStream input = getContentResolver().openInputStream(contentURI);
                    OutputStream output = new FileOutputStream(file);

                    try {
                        byte[] buffer = new byte[4 * 1024];
                        int read;

                        while ((read = input.read(buffer)) != -1) {
                            output.write(buffer, 0, read);
                        }
                        output.flush();
                        result = file.getAbsolutePath();
                    } finally {
                        output.close();
                        input.close();
                    }

                } catch (Exception e) {
                    Log.e(MainActivity.class.getSimpleName(), "Error getting file path", e);
                }
            } else {
                cursor.moveToFirst();
                int dataColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(dataColumn);
                cursor.close();
            }

        }
        return result;
    }

    private void addPicToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onUploadInit() {
        Snackbar.make(relativeLayout, R.string.profile_notice_upload_init, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onUploadPhotoDriverComplete(String urlPhotoDriver) {
        imageLoader.load(imgAvatarProfileAct, urlPhotoDriver);
        this.urlPhotoDriver = urlPhotoDriver;
    }

    @Override
    public void onUploadPhotoCarComplete(String urlPhotoCar) {
        imageLoader.load(imgCarProfileAct, urlPhotoCar);
        this.urlPhotoCar = urlPhotoCar;
    }

    @Override
    public void onUploadError(String error) {
        Snackbar.make(relativeLayout, error, Snackbar.LENGTH_SHORT).show();
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

        sharedPreferences.edit().putString(TaxiLivreDriverApp.URL_PHOTO_DRIVER_KEY, driver.getUrlPhotoDriver()).apply();
        sharedPreferences.edit().putString(TaxiLivreDriverApp.NOME_KEY, driver.getNome()).apply();
        sharedPreferences.edit().putString(TaxiLivreDriverApp.URL_PHOTO_CAR_KEY, car.getUrlPhotoCar()).apply();
        sharedPreferences.edit().putString(TaxiLivreDriverApp.MODELO_KEY, car.getModelo()).apply();
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
    public void onSuccessToGetDataUser(Driver currentUser, Car car) {
        setTextFields(currentUser, car);
        displayPhotos(currentUser.getUrlPhotoDriver(), car.getUrlPhotoCar());
    }

    @Override
    public void onFailedToGetDataUser(String error) {
        Snackbar.make(relativeLayout, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setTextFields();
    }

    private void setTextFields(Driver currenUser, Car car) {

        /*String email = sharedPreferences.getString(TaxiLivreDriverApp.EMAIL_KEY, "email_sh_pr");
        String nome = sharedPreferences.getString(TaxiLivreDriverApp.NOME_KEY, "nome_sh_pr");

        urlPhotoCar = sharedPreferences.getString(TaxiLivreDriverApp.URL_PHOTO_CAR_KEY, "url_car_sh_pr");
        String modelo = sharedPreferences.getString(TaxiLivreDriverApp.MODELO_KEY, "modelo_sh_pr");
        String marca = sharedPreferences.getString(TaxiLivreDriverApp.MARCA_KEY, "marca_sh_pr");
        String cor = sharedPreferences.getString(TaxiLivreDriverApp.COR_KEY, "cor_sh_pr");
        long ano = sharedPreferences.getLong(TaxiLivreDriverApp.ANO_KEY, 2000);
        String placa = sharedPreferences.getString(TaxiLivreDriverApp.PLACA_KEY, "placa_sh_pr");*/

        editTxtNome.setText(currenUser.getNome());
        editTxtEmail.setText(currenUser.getEmail());

        editTxtModelo.setText(car.getModelo());
        editTxtMarca.setText(car.getMarca());
        editTxtCor.setText(car.getCor());
        editTxtAno.setText(car.getAno() + "");
        editTxtPlaca.setText(car.getPlaca());

    }

    private void displayPhotos(String urlPhotoDriver, String urlPhotoCar){
        //urlPhotoDriver = sharedPreferences.getString(TaxiLivreDriverApp.URL_PHOTO_DRIVER_KEY, "url_driver_sh_pr");
        this.urlPhotoDriver = urlPhotoDriver;
        if ((!urlPhotoDriver.equals("default"))&&!(urlPhotoDriver.equals("url_driver_sh_pr")))
            imageLoader.load(imgAvatarProfileAct, urlPhotoDriver);
        //urlPhotoCar = sharedPreferences.getString(TaxiLivreDriverApp.URL_PHOTO_CAR_KEY, "url_car_sh_pr");
        this.urlPhotoCar = urlPhotoCar;
        if ((!urlPhotoCar.equals("default"))&&!(urlPhotoCar.equals("url_car_sh_pr")))
            imageLoader.load(imgCarProfileAct, urlPhotoCar);


    }

}
