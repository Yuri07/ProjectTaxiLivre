package com.rsm.yuri.projecttaxilivredriver.lib.base;

import java.io.File;

/**
 * Created by yuri_ on 28/12/2017.
 */

public interface ImageStorage {
    String getImageUrl(String id);
    void upload(File file, String id, ImageStorageFinishedListener listener);
}
