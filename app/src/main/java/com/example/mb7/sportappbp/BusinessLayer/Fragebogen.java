package com.example.mb7.sportappbp.BusinessLayer;

import java.io.Serializable;

/**
 * Created by Felix on 02.02.2017.
 */

public class Fragebogen implements Serializable {
    public String Date;
    public String FirebaseDate;
    public Integer berufstätig;
    public Integer sitzende_Tätigkeiten;
    public Integer mäßige_Bewegung;
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

    public String Aktivität_A_Name;
    public Integer Aktivität_A_Zeit;
    public Integer Aktivität_A_Einheiten;
    public Integer Aktivität_A_Minuten;
    public String Aktivität_B_Name;
    public Integer Aktivität_B_Zeit;
    public Integer Aktivität_B_Einheiten;
    public Integer Aktivität_B_Minuten;
    public String Aktivität_C_Name;
    public Integer Aktivität_C_Zeit;
    public Integer Aktivität_C_Einheiten;
    public Integer Aktivität_C_Minuten;

    public long Bewegungsscoring;
    public long Sportscoring;
    public long Gesamtscoring;
}
