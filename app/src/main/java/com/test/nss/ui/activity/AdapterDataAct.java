package com.test.nss.ui.activity;

public class AdapterDataAct {
    private String act;
    private String hours;
    private String endDate;

    public AdapterDataAct(String act, String hours, String endDate) {
        this.act = act;
        this.hours = hours;
        this.endDate = endDate;
    }

    public String getAct() {
        return act;
    }

    public String getHours() {
        return hours;
    }

    public String getEndDate() {
        return endDate;
    }
}
