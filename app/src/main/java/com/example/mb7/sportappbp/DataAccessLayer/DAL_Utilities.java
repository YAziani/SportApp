package com.example.mb7.sportappbp.DataAccessLayer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MB7 on 31.01.2017.
 */

public class DAL_Utilities {

    public static String DatabaseURL ;
    public static String ConvertDateToFirebaseDate(Date date)
    {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("yyyyMMdd");
        try{
            dateString = sdfr.format( date );
        }catch (Exception ex ){
            System.out.println(ex);
        }
        return dateString;
    }

}
