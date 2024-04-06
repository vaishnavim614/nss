package com.test.nss.ui.camp;

public class AdapterCampActList {
    private String campTitle;
    private String campDesc;
    private String campDay;
    private String campId;

    public AdapterCampActList(String campTitle, String campDesc, String campDay, String campId) {
        this.campTitle = campTitle;
        this.campDesc = campDesc;
        this.campDay = campDay;
        this.campId = campId;
    }

    public String getCampTitle() {
        return campTitle;
    }

    public String getCampDesc() {
        return campDesc;
    }

    public String getCampDay() {
        return campDay;
    }

    public String getCampId() {
        return campId;
    }
}