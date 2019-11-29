package com.m8.m8.Models;

import android.graphics.Bitmap;

public class AddImagePojo {

    Bitmap bitmap;

    public AddImagePojo(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
     public AddImagePojo(){

     }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
