package com.example.lukasz.wypozyczalnia.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Łukasz on 2017-01-06.
 */

public class SerializableBitmap  implements Serializable {
    private Bitmap currentImage;
    private Long isbn;

    public SerializableBitmap(Bitmap currentImage, Long isbn) {
        this.currentImage = currentImage;
        this.isbn = isbn;
    }

    public Bitmap getCurrentImage() {
        return currentImage;
    }

    public Long getIsbn() {
        return isbn;
    }
}
