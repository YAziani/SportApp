package com.example.mb7.sportappbp.BusinessLayer;

/**
 * Created by MB7 on 17.01.2017.
 */

public class Notification {
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    private String Title;

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    private String Text;
    public Notification(String title, String text)
    {
        Title = title;
        Text = text;
    }

}
