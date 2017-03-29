package com.example.mb7.sportappbp.BusinessLayer;

import java.io.Serializable;

/**
 * Created by M.Braei on 31.01.2017.
 */

public class StimmungsAngabe implements Serializable {
    public String Date;
    public String FirebaseDate;
    public Boolean Vor;                     // Is it the Stimmungsabfrage before Training or after
    public Integer Angespannt;
    public Integer Mitteilsam;
    public Integer Muede;
    public Integer Selbstsicher;
    public Integer Tatkraeftig;
    public Integer Traurig;
    public Integer Wuetend;
    public Integer Zerstreut;
    public String Time;


}
