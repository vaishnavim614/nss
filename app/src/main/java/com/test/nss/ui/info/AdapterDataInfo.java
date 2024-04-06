package com.test.nss.ui.info;

public class AdapterDataInfo {
    private String infoName;
    private String infoDesc;
    private String gitId;
    private String linkdId;

    public AdapterDataInfo(String infoName, String infoDesc, String gitId, String linkdId) {
        this.infoName = infoName;
        this.infoDesc = infoDesc;
        this.gitId = gitId;
        this.linkdId = linkdId;

    }

    public String getGitId() {
        return gitId;
    }

    public String getInfoName() {
        return infoName;
    }

    public String getInfoDesc() {
        return infoDesc;
    }

    public String getLinkdId() {
        return linkdId;
    }
}
