package com.tud.bp.fitup.BusinessLayer;

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

    public Integer Zu_Fuß_zur_Arbeit;
    public Integer Zu_Fuß_zur_Arbeit_Tag;
    public Integer Zu_Fuß_zur_Arbeit_Minuten;

    public Integer Zu_Fuß_einkaufen;
    public Integer Zu_Fuß_einkaufen_Tag;
    public Integer Zu_Fuß_einkaufen_Minuten;

    public Integer Rad_zur_Arbeit;
    public Integer Rad_zur_Arbeit_Tag;
    public Integer Rad_zur_Arbeit_Minuten;

    public Integer Radfahren;
    public Integer Radfahren_Tag;
    public Integer Radfahren_Minuten;

    public Integer Spazieren;
    public Integer Spazieren_Tag;
    public Integer Spazieren_Minuten;

    public Integer Gartenarbeit;
    public Integer Gartenarbeit_Tag;
    public Integer Gartenarbeit_Minuten;

    public Integer Hausarbeit;
    public Integer Hausarbeit_Tag;
    public Integer Hausarbeit_Minuten;

    public Integer Pflegearbeit;
    public Integer Pflegearbeit_Tag;
    public Integer Pflegearbeit_Minuten;

    public Integer Treppensteigen;
    public Integer Treppensteigen_Tag;
    public Integer Treppensteigen_Stockwerke;

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
