package com.tud.bp.fitup.BusinessLayer;

/**
 * Created by M.Braei on 27.03.2017.
 */

public class Report {

    private String subText;
    private Integer image;
    private String title;

    public Report(String title, String text, Integer image) {
        this.title = title;
        this.subText = text;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String text) {
        this.subText = text;
    }


    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }
}
