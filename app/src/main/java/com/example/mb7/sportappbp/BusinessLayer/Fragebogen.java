package com.example.mb7.sportappbp.BusinessLayer;

import java.io.Serializable;

/**
 * Created by Felix on 02.02.2017.
 */

public class Fragebogen implements Serializable {
    public String Date;
    public String FirebaseDate;
    public Integer Berufstätig;
    public Integer sitzendetätigkeiten;
    public Integer mäßigebewegung;
    public Integer intensivebewegung;
    public Integer sportlichaktiv;

    public Integer zufußzurarbeit;
    public Integer zufußeinkaufen;
    public Integer radzurarbeit;
    public Integer radfahren;
    public Integer spazieren;
    public Integer gartenarbeit;
    public Integer hausarbeit;
    public Integer pflegearbeit;
    public Integer treppensteigen;

    public String aktivitätaname;
    public Integer aktivitäta;
    public String aktivitätbname;
    public Integer aktivitätb;
    public String aktivitätcname;
    public Integer aktivitätc;

    public long bewegungscoring;
    public long sportscoring;
    public long Gesamtscoring;
}
