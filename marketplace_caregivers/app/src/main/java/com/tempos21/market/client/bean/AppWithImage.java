package com.tempos21.market.client.bean;


import android.graphics.Bitmap;


public class AppWithImage extends App {

    private Bitmap promo;
    private Bitmap icon;


    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public Bitmap getPromo() {
        return promo;
    }

    public void setPromo(Bitmap promo) {
        this.promo = promo;
    }

}
