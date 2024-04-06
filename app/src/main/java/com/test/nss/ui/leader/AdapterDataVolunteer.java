package com.test.nss.ui.leader;

public class AdapterDataVolunteer {
    private String date;
    private String act;
    private String hours;
    private String id;
    private String state;
    private int actCode;
    private int assActCode;

    public AdapterDataVolunteer(String date, String act, String hours, String id, String state, int actCode, int assActCode) {
        this.date = date;
        this.act = act;
        this.hours = hours;
        this.id = id;
        this.state = state;
        this.actCode = actCode;
        this.assActCode = assActCode;
    }

    public int getActCode() {
        return actCode;
    }

    public String getDate() {
        return date;
    }

    public String getAct() {
        return act;
    }

    public String getHours() {
        return hours;
    }

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public int getAssActCode() {
        return assActCode;
    }
}
