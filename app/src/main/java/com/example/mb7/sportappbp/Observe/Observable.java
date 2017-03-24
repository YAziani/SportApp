package com.example.mb7.sportappbp.Observe;

/**
 * Created by M.Braei on 24.03.2017.
 */

public interface Observable {
    public void registerObserver(Observer observer);
    public void removeObserver(Observer observer);
    public void notifyObserver(Observer observer);
}
