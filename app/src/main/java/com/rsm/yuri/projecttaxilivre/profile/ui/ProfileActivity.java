package com.rsm.yuri.projecttaxilivre.profile.ui;

import android.Manifest;
import android.app.Application;
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
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.rsm.yuri.projecttaxilivre.profile.ProfilePresenter;
import com.rsm.yuri.projecttaxilivre.profile.di.ProfileComponent;

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

public class ProfileActivity extends AppCompatActivity implements AddDialogFragmentListener, ProfileView {

    @BindView(R.id.imgAvatarProfileAct)
    CircleImageView imgAvatarProfileAct;
    @BindView(R.id.nomeUserProfileAct)
    TextView nomeUserProfileAct;
    @BindView(R.id.emailUserProfileAct)
    TextView emailUserProfileAct;
    @BindView(R.id.profileActivityContainer)
    ConstraintLayout constraintLayout;


    @Inject
    ProfilePresenter presenter;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    ImageLoader imageLoader;

    TaxiLivreApp app;

    private String photoPath;
    private final static int REQUEST_PICTURE = 0;
    private final static int PERMISSIONS_REQUEST_READ_MEDIA = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        app = (TaxiLivreApp) getApplication();
        setupInjection();

        presenter.onCreate();

        String email = sharedPreferences.getString(TaxiLivreApp.EMAIL_KEY, "");
        String nome = sharedPreferences.getString(TaxiLivreApp.NOME_KEY, "");
        String urlPhotoUser = sharedPreferences.getString(TaxiLivreApp.URL_PHOTO_USER_KEY, "");

        imageLoader.load(imgAvatarProfileAct, urlPhotoUser);
        nomeUserProfileAct.setText(nome);
        emailUserProfileAct.setText(email);



    }

    private void setupInjection() {
        /*AddDialogComponent addDialogComponent= app.getAddDialogComponent();
        addDialogComponent.inject(this);*/

        ProfileComponent profileComponent= app.getProfileComponent(this);
        profileComponent.inject(this);


    }

    @OnClick({R.id.frameLayoutImgAvatar, R.id.frameLayoutNome, R.id.frameLayoutEmail})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.frameLayoutImgAvatar:
                takePicture();

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

    public void takePicture() {
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
            Snackbar.make(constraintLayout, R.string.main_error_dispatch_camera, Snackbar.LENGTH_SHORT).show();
        }
        return photoFile;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    public void onBackPressed() {
        Intent intent=new Intent();
        setResult(MainActivity.UPDATE_PROFILE, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
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

    @Override
    public void onUploadInit() {
        Snackbar.make(constraintLayout, R.string.profile_notice_upload_init, Snackbar.LENGTH_SHORT).show();
        Log.d("d", "onUploadInit");
    }

    @Override
    public void onUploadComplete(String urlPhotoUser) {
        sharedPreferences.edit().putString(TaxiLivreApp.URL_PHOTO_USER_KEY, urlPhotoUser).apply();
        imageLoader.load(imgAvatarProfileAct, urlPhotoUser);
        Log.d("d", "onUploadComplete");
    }

    @Override
    public void onUploadError(String error) {
        Snackbar.make(constraintLayout, error, Snackbar.LENGTH_SHORT).show();

    }
}
