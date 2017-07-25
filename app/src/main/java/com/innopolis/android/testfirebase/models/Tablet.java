package com.innopolis.android.testfirebase.models;

import android.graphics.drawable.Drawable;

/**
 * Created by _red_ on 14.07.17.
 */

public class Tablet extends Device implements DeviceData {
    @Override
    public int getProduser() {
        return 0;
    }

    @Override
    public int getModel() {
        return 0;
    }

    @Override
    public String getDate() {
        return null;
    }

    @Override
    public Drawable getImage() {
        return null;
    }
}
