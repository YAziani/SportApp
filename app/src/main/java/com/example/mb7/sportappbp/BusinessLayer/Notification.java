package com.example.mb7.sportappbp.BusinessLayer;

import java.util.Date;

/**
 * Created by MB7 on 17.01.2017.
 */

public class Notification {

    private String subText;
    private Integer image;
    private String title;
    private Date date;
    public Notification(String title, String text, Integer image)
    {
        this.title = title;
        this.subText = text;
        this.image = image;
        this.date = new Date();
    }
    public Notification(String title, String text, Integer image, Date date)
    {
        this.title = title;
        this.subText = text;
        this.image = image;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public String getPresentationDate(){
        int diffDays= ((int)((new Date().getTime()/(24*60*60*1000))-(int)(date.getTime()/(24*60*60*1000))));
        if (diffDays != 0)
            return Integer.toString(diffDays) + "d";

        int diffHours = ((int) ((new Date().getTime()/(1000*60*60))-(int) (date.getTime()/(1000*60*60))));
        if (diffHours != 0)
            return Integer.toString(diffHours) + "h";

        int diffMins = ((int) ((new Date().getTime()/(1000*60)) - (int) (date.getTime()/(1000*60))));
        if (diffMins != 0)
            return Integer.toString(diffMins) + "m";

        int diffSecs= ((int) ((new Date().getTime()/(1000)) - (int) (date.getTime()/(1000))));
        return Integer.toString(Math.max(1, diffSecs)) + "s";
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }
}
