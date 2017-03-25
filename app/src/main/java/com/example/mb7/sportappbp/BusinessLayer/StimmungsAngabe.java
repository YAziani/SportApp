package com.example.mb7.sportappbp.BusinessLayer;

import java.io.Serializable;

/**
 * Created by MB7 on 31.01.2017.
 */

public class StimmungsAngabe implements Serializable {
    public String Date;
    public String FirebaseDate;
    // Is it the Stimmungsabfrage before Training or after
    public Boolean Vor;
    public Integer Angespannt ;
    public Integer Mitteilsam;
    public Integer Muede;
    public Integer Selbstsicher;
    public Integer Tatkraeftig;
    public Integer Traurig;
    public Integer Wuetend;
    public Integer Zerstreut;
    public  String Time;


    }
