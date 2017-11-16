package com.example.dziewan.application.model;

import android.graphics.Bitmap;

/**
 * Created by coderion on 10.11.17.
 */

public interface KonwersjaZdjec {

    byte[] zakoduj(Bitmap bitmap);

    Bitmap odkoduj(byte[] array);

}
