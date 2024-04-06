package com.test.nss.ui.data;

public class MyListAdapterModel {
    private String Img, text;

    public MyListAdapterModel(String img, String text) {
        this.Img = img;
        this.text = text;
    }

    public String getImg() {
        return Img;
    }

    public String getText() {
        return text;
    }
}
