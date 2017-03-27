package com.example.mb7.sportappbp.BusinessLayer;

import java.io.Serializable;

/**
 * Created by Felix on 02.02.2017.
 */

public class Fragebogen implements Serializable {
    public String Date;
    public String FirebaseDate;
    public Integer Berufstaetig;
    public Integer sitzende_Taetigkeiten;
    public Integer maeßige_Bewegung;
    public Integer intensive_Bewegung;
    public Integer sportlich_aktiv;

    public Integer zu_Fuß_zur_Arbeit;
    public Integer zu_Fuß_einkaufen;
    public Integer Rad_zur_Arbeit;
    public Integer Rad_fahren;
    public Integer Spazieren;
    public Integer Gartenarbeit;
    public Integer Hausarbeit;
    public Integer Pflegearbeit;
    public Integer Treppensteigen;

    public String Aktivitaet_A_Name;
    public Integer Aktivitaet_A_Zeit;
    public Integer Aktivitaet_A_Einheiten;
    public Integer Aktivitaet_A_Minuten;
    public String Aktivitaet_B_Name;
    public Integer Aktivitaet_B_Zeit;
    public Integer Aktivitaet_B_Einheiten;
    public Integer Aktivitaet_B_Minuten;
    public String Aktivitaet_C_Name;
    public Integer Aktivitaet_C_Zeit;
    public Integer Aktivitaet_C_Einheiten;
    public Integer Aktivitaet_C_Minuten;

    public long Bewegungsscoring;
    public long Sportscoring;
    public long Gesamtscoring;
}
