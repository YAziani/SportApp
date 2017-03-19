package com.example.mb7.sportappbp.DataAccessLayer;

import android.util.Log;

import com.example.mb7.sportappbp.BusinessLayer.FitnessFragebogen;
import com.example.mb7.sportappbp.BusinessLayer.Fragebogen;
import com.example.mb7.sportappbp.BusinessLayer.StimmungAbfrage;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.example.mb7.sportappbp.Objects.DiaryEntry;
import com.example.mb7.sportappbp.Objects.Exercise;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created by MB7 on 31.01.2017.
 */

public class DAL_User {
    static  private long gcounter ;

    static public void GetStimmnungsabfrage(User user, Date date)
    {
        try
        {
            final String fDate = DAL_Utilities.ConvertDateToFirebaseDate(date);
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + user.getName()+ "/Stimmungsabfrage/" + fDate);
            Firebase root = new Firebase(url.toString());
            root.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // ((ChildrenNode) dataSnapshot.node.node).children.iterator().next().getValue()
                    //

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


        }
        catch (Exception e)
        {
            Log.d("ERROR", e.getMessage());
        }
    }

    static public void GetLastTodayStimmungsabfrage(User user, Date date)
    {
        try {
            final String sDate = DAL_Utilities.ConvertDateToFirebaseDate(date);
            URL url = new URL(DAL_Utilities.DatabaseURL  +  "players/" + user.getName() + "/Stimmungsabfrage/" + sDate + "/");
            Firebase root = new Firebase(url.toString());
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    gcounter = dataSnapshot.getChildrenCount();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.d("DAL_User.GetLTSabfrage",firebaseError.getMessage());
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    static public void InsertStimmung(User user, StimmungAbfrage stimmungAbfrage, Date date)
    {
        try
        {


            Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/Stimmungsabfrage/" + stimmungAbfrage.Date + "/");

            String V_N = stimmungAbfrage.Vor?"/V":"/N";
            Firebase newChildRef = ref.push();
            if (stimmungAbfrage.Angespannt >= 0) {
                Firebase childAngespannt = newChildRef.child(V_N).child("Angespannt");
                childAngespannt.setValue(stimmungAbfrage.Angespannt);
            }
            if(stimmungAbfrage.Mitteilsam >=0) {
                Firebase childMitteilsam = newChildRef.child(V_N).child("Mitteilsam");
                childMitteilsam.setValue(stimmungAbfrage.Mitteilsam);
            }
            if(stimmungAbfrage.Muede >= 0) {
                Firebase childMuede = newChildRef.child(V_N).child("Muede");
                childMuede.setValue(stimmungAbfrage.Muede);
            }
            if(stimmungAbfrage.Selbstsicher >=0) {
                Firebase childSelbstsicher = newChildRef.child(V_N).child("Selbstsicher");
                childSelbstsicher.setValue(stimmungAbfrage.Selbstsicher);
            }
            if(stimmungAbfrage.Tatkraeftig >= 0) {
                Firebase childTatkraeftig = newChildRef.child(V_N).child("Tatkraeftig");
                childTatkraeftig.setValue(stimmungAbfrage.Tatkraeftig);
            }
            if(stimmungAbfrage.Traurig >=0) {
                Firebase childTraurig = newChildRef.child(V_N).child("Traurig");
                childTraurig.setValue(stimmungAbfrage.Traurig);
            }
            if(stimmungAbfrage.Wuetend >=0) {
                Firebase childWuetend = newChildRef.child(V_N).child("Wuetend");
                childWuetend.setValue(stimmungAbfrage.Wuetend);
            }
            if(stimmungAbfrage.Zerstreut >= 0) {
                Firebase childZerstreut = newChildRef.child(V_N).child("Zerstreut");
                childZerstreut.setValue(stimmungAbfrage.Zerstreut);
            }
        }
        catch (Exception ex)
        {
            String s = ex.getMessage();
        }
            finally
        {

        }

    }


    static public void GetLastTodayDiaryEntry(User user, Date date)
    {
        try {
            final String sDate = DAL_Utilities.ConvertDateToFirebaseDate(date);
            URL url = new URL(DAL_Utilities.DatabaseURL  +  "players/" + user.getName() + "/DiaryEntry/" + sDate + "/");
            Firebase root = new Firebase(url.toString());
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    gcounter = dataSnapshot.getChildrenCount();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.d("DAL_User.GetLTSabfrage",firebaseError.getMessage());
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    static public void InsertDiaryEntry(User user, DiaryEntry diaryEntry)
    {
        try
        {
            Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/DiaryEntry/" + diaryEntry.getID() + "/");
            System.out.println("war hier2!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            //String V_N = stimmungAbfrage.Vor?"/V":"/N";
            Firebase newChildRef = ref.push();

            Firebase childDate = newChildRef.child("Date");
            childDate.setValue(diaryEntry.getDate());

            Firebase childTime = newChildRef.child("Time");
            childTime.setValue(diaryEntry.getTime());


            int i = 0;
            for(Exercise ex : diaryEntry.getExerciseList()){

                Firebase exerciseChild = newChildRef.child("Exercise " + String.valueOf(i) + " :");
                Firebase childExercise = exerciseChild.child("Exercise");
                childExercise.setValue(ex.getExercise());
                Firebase childMinutes = exerciseChild.child("Minutes");
                childMinutes.setValue(ex.getTimeMunites());
                Firebase childHours = exerciseChild.child("Hours");
                childHours.setValue(ex.getTimeHours());
                i++;
            }

        }
        catch (Exception ex)
        {
            String s = ex.getMessage();
            System.out.println(s);
            System.out.println("war hier2!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        finally
        {

        }

    }

    /**
     * Speichert die Scorings des Fitnessfragbogens in der Datenbank.
     * @param user
     * @param finessfragebogen
     */
    static public void InsertFitnessFragebogen(User user, FitnessFragebogen finessfragebogen)
    {
        try
        {
            Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/FitnessFragebogen/" );
            Firebase newChildRef = ref.push();

                Firebase childscorekraft = newChildRef.child("Score Kraft");
                childscorekraft.setValue(finessfragebogen.scorekraft);

                Firebase childscoreausdauer = newChildRef.child("Score Ausdauer");
                childscoreausdauer.setValue(finessfragebogen.scoreausdauer);

                Firebase childscorekoordination = newChildRef.child("Score Koordination");
                childscorekoordination.setValue(finessfragebogen.scorekoordination);

                Firebase childscorebewglichkeit = newChildRef.child("Score Beweglichkeit");
                childscorebewglichkeit.setValue(finessfragebogen.scorebeweglichkeit);

                Firebase childscoregesamt = newChildRef.child("Gesamtscore");
                childscoregesamt.setValue(finessfragebogen.scoregesamt);

        }
        catch (Exception exception)
        {
            String s = exception.getMessage();
            System.out.println(s);
        }
        finally
        {

        }

    }

    /**
     * Speichert Antworten und Scoring des BSA Fragebogens.
     * @param user
     * @param fragebogen
     */
    static public void InsertFragebogen(User user, Fragebogen fragebogen)
    {
        try
        {


            Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/BSAFragebogen/" );

            Firebase newChildRef = ref.push();

            Firebase childberufstätig = newChildRef.child("Berufstätig");
            childberufstätig.setValue(fragebogen.berufstätig);

            Firebase childsitzendetätigkeiten = newChildRef.child("Sitzende Tätigkeiten");
            childsitzendetätigkeiten.setValue(fragebogen.sitzendetätigkeiten);

            Firebase childmäßigebewegung = newChildRef.child("Mäßige Bewegung");
            childmäßigebewegung.setValue(fragebogen.mäßigebewegung);

            Firebase childintensivebewegung = newChildRef.child("Intensive Bewegung");
            childintensivebewegung.setValue(fragebogen.intensivebewegung);

            Firebase childsportlichaktiv = newChildRef.child("Sportlich Aktiv");
            childsportlichaktiv.setValue(fragebogen.sportlichaktiv);

            Firebase childzufußzurarbeit = newChildRef.child("Zu Fuß zur Arbeit");
            childzufußzurarbeit.setValue(fragebogen.zufußzurarbeit);

            Firebase childzufußeinkaufen = newChildRef.child("Zu Fuß einkaufen");
            childzufußeinkaufen.setValue(fragebogen.zufußeinkaufen);

            Firebase childradzurarbeit = newChildRef.child("Mit dem Rad zur Arbeit");
            childradzurarbeit.setValue(fragebogen.radzurarbeit);

            Firebase childradfahren = newChildRef.child("Radfahren");
            childradfahren.setValue(fragebogen.radfahren);

            Firebase childspazieren = newChildRef.child("Spazieren");
            childspazieren.setValue(fragebogen.spazieren);

            Firebase childgartenarbeit = newChildRef.child("Gartenarbeit");
            childgartenarbeit.setValue(fragebogen.gartenarbeit);

            Firebase childhausarbeit = newChildRef.child("Hausarbeit");
            childhausarbeit.setValue(fragebogen.hausarbeit);

            Firebase childpflegearbeit = newChildRef.child("Pflegearbeit");
            childpflegearbeit.setValue(fragebogen.pflegearbeit);

            Firebase childtreppensteigen = newChildRef.child("Treppensteigen");
            childtreppensteigen.setValue(fragebogen.treppensteigen);

            Firebase childaktaname = newChildRef.child("Aktivität A Name");
            childaktaname.setValue(fragebogen.aktivitätaname);

            Firebase childaktaanzahl = newChildRef.child("Aktivität A Zeit");
            childaktaanzahl.setValue(fragebogen.aktivitäta);

            Firebase childaktbname = newChildRef.child("Aktivität B Name");
            childaktbname.setValue(fragebogen.aktivitätbname);

            Firebase childaktbanzahl = newChildRef.child("Aktivität B Zeit");
            childaktbanzahl.setValue(fragebogen.aktivitätb);

            Firebase childaktcname = newChildRef.child("Aktivität C Name");
            childaktcname.setValue(fragebogen.aktivitätcname);

            Firebase childaktcanzahl = newChildRef.child("Aktivität C Zeit");
            childaktcanzahl.setValue(fragebogen.aktivitätc);

            Firebase childbewegungscore = newChildRef.child("Score Bewegung");
            childbewegungscore.setValue(fragebogen.bewegungscoring);

            Firebase childsportscore = newChildRef.child("Score Sport");
            childsportscore.setValue(fragebogen.sportscoring);

            Firebase childscore = newChildRef.child("Gesamtscore");
            childscore.setValue(fragebogen.gesamtscoring);

        }
        catch (Exception exception)
        {
            String s = exception.getMessage();
            System.out.println(s);
        }
        finally
        {

        }

    }





    /**
     * inserts ratings into the database
     * @param user the current user
     * @param listMethod list containing the rated methods
     * @param listRating list containing the ratings
     */
    static public void insertRating(User user, List<String> listMethod, List<String> listRating) {
        try
        {
            // setting up url for the database
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/methodRatings");
            Firebase root = new Firebase(url.toString());
            Firebase child;
            // insert ratings for each method
            for(int i = 0; i < Math.min(listMethod.size(),listRating.size()); i++) {
                child = root.child(listMethod.get(i));
                child.setValue(listRating.get(i));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * update groups of alternating group assignment
     * @param user the active user
     * @param currentActiveGroup the currently active group
     * @param nextActiveGroup the next group to be active
     * @param alternGroup the set of groups currently used
     */
    static public void insertAlternGroupUpdate(User user, String currentActiveGroup, String nextActiveGroup, String alternGroup) {
        try
        {
            // setting up url for the database
            URL url = new URL(DAL_Utilities.DatabaseURL + "/Administration/assignment/altern/" + alternGroup);
            Firebase root = new Firebase(url.toString());
            // update group values
            root.child(currentActiveGroup).child("groupactive").setValue(false);
            root.child(nextActiveGroup).child("groupactive").setValue(true);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}


