package com.rsm.yuri.projecttaxilivre.domain;

/**
 * Created by yuri_ on 28/12/2017.
 */

public interface FirebaseStorageFinishedListener {
    void onSuccess(String url);
    void onError(String error);
}
