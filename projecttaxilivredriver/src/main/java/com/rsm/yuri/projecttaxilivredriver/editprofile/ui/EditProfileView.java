package com.rsm.yuri.projecttaxilivredriver.editprofile.ui;

import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Car;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;

/**
 * Created by yuri_ on 14/03/2018.
 */

public interface EditProfileView {

    void onUploadInit();
    void onUploadComplete(String urlPhotoUser);
    void onUploadError(String error);

    void showInput();
    void hideInput();
    void showProgress();
    void hideProgress();

    void profileEdited(Driver driver, Car car);
    void profileNotEdited(String msgError);

}
