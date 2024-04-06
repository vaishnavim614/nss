package com.test.nss.ui.help;

public class AdapterDataHelp {
    private String leadName;
    private String leadEmail;
    private String leadCont;
    private String leadClg;

    public AdapterDataHelp(String leadName, String leadEmail, String leadCont,  String leadClg) {
        this.leadName = leadName;
        this.leadEmail = leadEmail;
        this.leadCont = leadCont;
        this.leadClg = leadClg;
    }



    public String getLeadName() {
        return leadName;
    }

    public String getLeadClg() {
        return leadClg;
    }

    public String getLeadEmail() {
        return leadEmail;
    }

    public String getLeadCont() {
        return leadCont;
    }
}
