package com.udacity.jwdnd.course1.cloudstorage.model;

public class Notes {
    private Integer noteid;
    private String notetitle;
    private String notedescription;
    private Integer userid;

    public Notes(Integer noteid, String notetitle, String notedescription, Integer userid) {
        this.noteid = noteid;
        this.notetitle = notetitle;
        this.notedescription = notedescription;
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "noteid=" + noteid +
                ", notetitle='" + notetitle + '\'' +
                '}';
    }

    public Integer getNoteid() {
        return noteid;
    }

    public void setNoteid(Integer noteid) {
        this.noteid = noteid;
    }

    public String getNotetitle() {
        return notetitle;
    }

    public void setNotetitle(String notetitle) {
        this.notetitle = notetitle;
    }

    public String getNotedescription() {
        return notedescription;
    }

    public void setNotedescription(String notedescription) {
        this.notedescription = notedescription;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}