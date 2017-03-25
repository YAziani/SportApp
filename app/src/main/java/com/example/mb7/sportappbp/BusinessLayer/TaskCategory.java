package com.example.mb7.sportappbp.BusinessLayer;

import java.util.Date;

/**
 * Created by M.Braei on 25.03.2017.
 */

public class TaskCategory {

    private String subText;
    private Integer image;
    private String title;
    public TaskCategory(String title, String text, Integer image)
    {
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
