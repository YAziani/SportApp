package com.example.mb7.sportappbp.DataAccessLayer;


import java.text.ParseException;
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
    public static String ConvertDateTimeToString(Date date)
    {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        try{
            dateString = sdfr.format( date );
        }catch (Exception ex ){
            System.out.println(ex);
        }
        return dateString;
    }
    public static String ConvertDateTimeToFirebaseString(Date date)
    {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd_MM_yyyy HH_mm_ss");
        try{
            dateString = sdfr.format( date );
        }catch (Exception ex ){
            System.out.println(ex);
        }
        return dateString;
    }
    public  static Date ConvertFirebaseStringToDateTime(String date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy HH_mm_ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }
    public  static Date ConvertStringToDateTime(String date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }
}
