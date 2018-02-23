package com.rsm.yuri.projecttaxilivre.lib;

import com.google.firebase.storage.FirebaseStorage;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.lib.base.ImageStorage;
import com.rsm.yuri.projecttaxilivre.lib.base.ImageStorageFinishedListener;

import java.io.File;

/**
 * Created by yuri_ on 18/02/2018.
 */

public class FirebaseImageStorage implements ImageStorage {

    private EventBus eventBus;
    private FirebaseStorage firebaseStorage;

    @Override
    public String getImageUrl(String id) {
        return null;
    }

    @Override
    public void upload(File file, String id, ImageStorageFinishedListener listener) {

    }
}
