package com.tud.bp.fitup.BusinessLayer;

import java.util.Date;

/**
 * Created by M.Braei on 18.03.2017.
 */

public class Task {

    private Date date;
    private String description;
    private Integer image;

    public Task() {
        date = new Date();
    }

    public Task(String description, Integer image) {
        date = new Date();
        this.description = description;
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

}
