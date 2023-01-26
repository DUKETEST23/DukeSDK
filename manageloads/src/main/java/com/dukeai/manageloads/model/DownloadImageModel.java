package com.dukeai.manageloads.model;

import android.graphics.Bitmap;

public class DownloadImageModel extends ResponseModel {
    Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
