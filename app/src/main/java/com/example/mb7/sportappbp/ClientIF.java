package com.example.mb7.sportappbp;

/**
 * Created by MB7 on 22.01.2017.
 */

public interface ClientIF {
    // function that is called after executing the background task in the background Thread to pass the value to the
    // caller in the UI Thread
    public void onResponseReceived(String result);
}
