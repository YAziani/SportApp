package com.example.mb7.sportappbp.MotivationMethods;

/**
 * Created by Aziani on 23.12.2016.
 *
 * abstract class for methods with which the user gets motivational help
 */

public abstract class MotivationMethod {

    /**
     * runs the motivation method
     * has to be individually implemented by each concrete motivation method
     */
    public abstract void run();

    /**
     * initiates the rating of a motivation method
     * collects data about the efficiency of the used method
     */
    public abstract void rate();

    /**
     * evaluate the results of possible permissionRequests, leave empty if motivation method doesn't request permissions
     * @param requestCode code of the requested permission
     * @param permissions array of all requested permissions
     * @param grantResults permissions which have been granted
     */
    public void evaluatePermissionResults(int requestCode, String permissions[], int[] grantResults) {
    }
}
