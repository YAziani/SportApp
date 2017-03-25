package com.example.mb7.sportappbp.DataAccessLayer;

import android.provider.ContactsContract;
import android.util.Log;

import com.example.mb7.sportappbp.Activity.ActivityDiary;
import com.example.mb7.sportappbp.BusinessLayer.FitnessFragebogen;
import com.example.mb7.sportappbp.BusinessLayer.Fragebogen;
import com.example.mb7.sportappbp.BusinessLayer.StimmungsAngabe;
import com.example.mb7.sportappbp.BusinessLayer.StimmungAbfrageScore;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.example.mb7.sportappbp.Objects.AllDiaryEntries;
import com.example.mb7.sportappbp.Objects.DiaryEntry;
import com.example.mb7.sportappbp.Objects.Exercise;
import com.example.mb7.sportappbp.Objects.LeistungstestsExercise;
import com.example.mb7.sportappbp.Objects.ReinerAufenthaltExercise;
import com.example.mb7.sportappbp.Objects.TrainingExercise;
import com.example.mb7.sportappbp.Objects.WellnessExercise;
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

    static public StimmungsAngabe GetStimmnungsabfrage(User user, Date date){
    final StimmungsAngabe stimmunga=new StimmungsAngabe();

        try
        {
            final String fDate = DAL_Utilities.ConvertDateToFirebaseDate(date);
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + user.getName()+ "/Stimmungsabfrage/" + fDate);
            final Firebase root = new Firebase(url.toString());

            root.addListenerForSingleValueEvent(new ValueEventListener() {

                // Hier kriegst du den Knoten -KfNx5TBo4yQpfN07Ekh
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String  strKey = "";

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        // Hier bekommst du den Knoten V oder N
                        strKey = child.getKey();
                        root.child(strKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                           @Override
                                                                           public void onDataChange(DataSnapshot dataSnapshot) {
                                                                               for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                                                   // Hier bekommst du dann letztlich die Stimmungsabfrage
                                                                                   StimmungsAngabe stimmungsAngabe = child.getValue(StimmungsAngabe.class);
                                                                                   stimmungsAngabe =stimmunga;

                                                                               }
                                                                          }

                                                                          @Override
                                                                          public void onCancelled(FirebaseError firebaseError) {

                                                                          }
                                                                      });
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            return stimmunga;

        }
        catch (Exception e)
        {
            Log.d("ERROR", e.getMessage());
            return stimmunga;
        }
    }

    static public void GetLastTodayStimmungsabfrage(User user, Date date)
    {
        try {
            final String sDate = DAL_Utilities.ConvertDateToFirebaseDate(date);
            URL url = new URL(DAL_Utilities.DatabaseURL  +  "users/" + user.getName() + "/Stimmungsabfrage/" + sDate + "/");
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


    static public void InsertStimmung(User user, StimmungsAngabe stimmungsAngabe, Date date)
    {
        try
        {


            Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/Stimmungsabfrage/" + stimmungsAngabe.Date + "/");

            String V_N = stimmungsAngabe.Vor?"/V":"/N";
            Firebase newChildRef = ref.push();
            if (stimmungsAngabe.Angespannt >= 0) {
                Firebase childAngespannt = newChildRef.child(V_N).child("Angespannt");
                childAngespannt.setValue(stimmungsAngabe.Angespannt);
            }
            if(stimmungsAngabe.Mitteilsam >=0) {
                Firebase childMitteilsam = newChildRef.child(V_N).child("Mitteilsam");
                childMitteilsam.setValue(stimmungsAngabe.Mitteilsam);
            }
            if(stimmungsAngabe.Muede >= 0) {
                Firebase childMuede = newChildRef.child(V_N).child("Muede");
                childMuede.setValue(stimmungsAngabe.Muede);
            }
            if(stimmungsAngabe.Selbstsicher >=0) {
                Firebase childSelbstsicher = newChildRef.child(V_N).child("Selbstsicher");
                childSelbstsicher.setValue(stimmungsAngabe.Selbstsicher);
            }
            if(stimmungsAngabe.Tatkraeftig >= 0) {
                Firebase childTatkraeftig = newChildRef.child(V_N).child("Tatkraeftig");
                childTatkraeftig.setValue(stimmungsAngabe.Tatkraeftig);
            }
            if(stimmungsAngabe.Traurig >=0) {
                Firebase childTraurig = newChildRef.child(V_N).child("Traurig");
                childTraurig.setValue(stimmungsAngabe.Traurig);
            }
            if(stimmungsAngabe.Wuetend >=0) {
                Firebase childWuetend = newChildRef.child(V_N).child("Wuetend");
                childWuetend.setValue(stimmungsAngabe.Wuetend);
            }
            if(stimmungsAngabe.Zerstreut >= 0) {
                Firebase childZerstreut = newChildRef.child(V_N).child("Zerstreut");
                childZerstreut.setValue(stimmungsAngabe.Zerstreut);
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

    static public void InsertStimmungScore(User user, StimmungAbfrageScore stimmungAbfrageScore, Date date)
    {
        try
        {
            Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/StimmungabfrageScore/"+ stimmungAbfrageScore.Date + "/" );
            Firebase newChildRef = ref.push();

            Firebase childscoreangespannt = newChildRef.child("Score Angespannt");
            childscoreangespannt.setValue(stimmungAbfrageScore.AngespanntScore);

            Firebase childscoretraurig = newChildRef.child("Score Traurig");
            childscoretraurig.setValue(stimmungAbfrageScore.TraurigScore);

            Firebase childscoretatkraeftig = newChildRef.child("Score Tatkräftig");
            childscoretatkraeftig.setValue(stimmungAbfrageScore.TatkraeftigScore);

            Firebase childscorezerstreut = newChildRef.child("Score Zerstreut");
            childscorezerstreut.setValue(stimmungAbfrageScore.ZerstreutScore);

            Firebase childscorewuetend = newChildRef.child("Score Wütend");
            childscorewuetend.setValue(stimmungAbfrageScore.WuetendScore);

            Firebase childscoremuede = newChildRef.child("Score Müde");
            childscoremuede.setValue(stimmungAbfrageScore.MuedeScore);

            Firebase childscoreselbstsicher = newChildRef.child("Score Selbstsicher");
            childscoreselbstsicher.setValue(stimmungAbfrageScore.SelbstsicherScore);

            Firebase childscoremitteilsam = newChildRef.child("Score Mitteilsam");
            childscoremitteilsam.setValue(stimmungAbfrageScore.MitteilsamScore);

            Firebase childscorebarometer = newChildRef.child("Score Stimmungsbarometer");
            childscorebarometer.setValue(stimmungAbfrageScore.StimmungsBarometerScore);

            Firebase childscoreenergieindex = newChildRef.child("Energieindex");
            childscoreenergieindex.setValue(stimmungAbfrageScore.EnergieIndexScore);



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


    static public void GetDiaryEntry(User user, Date date)
    {
        try
        {
            final String fDate = DAL_Utilities.ConvertDateToFirebaseDate(date);
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + user.getName()+ "/Diary/" + "20170322");
            final Firebase root = new Firebase(url.toString());

            root.addListenerForSingleValueEvent(new ValueEventListener() {

                // Hier kriegst du den Knoten -KfNx5TBo4yQpfN07Ekh
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String  strKey = "";

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        //Key
                        strKey = child.getKey();

                            root.child(strKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    AllDiaryEntries allDiaryEntries = AllDiaryEntries.getInstance();
                                    //clear list to delete duplicates
                                    //allDiaryEntries.getDiaryList().clear();

                                    DiaryEntry diaryEntry = new DiaryEntry();
                                    String strExercise = "";

                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        if (child.getKey().equals("date"))
                                            diaryEntry.setDate(child.getValue().toString());
                                        else if (child.getKey().equals("time"))
                                            diaryEntry.setTime(child.getValue().toString());
                                        else if (child.getKey().equals("totalpoints"))
                                            diaryEntry.setTotalpoints(Integer.getInteger(child.getValue().toString()));

                                        else if(child.getKey().startsWith("exercise")){

                                            strExercise = child.getKey();
                                            String category = child.getChildren().iterator().next().getValue().toString();

                                            Exercise exercise = null;
                                            //String category = child.getValue().toString();

                                                if (category.equals("Training")) {
                                                    exercise = new TrainingExercise();
                                                    exercise = child.getValue(TrainingExercise.class);
                                                } else if (category.equals("Leistungstests")) {
                                                    exercise = new LeistungstestsExercise();
                                                    exercise = child.getValue(LeistungstestsExercise.class);
                                                } else if (category.equals("ReinerAufenthalt")) {
                                                    exercise = new ReinerAufenthaltExercise();
                                                    exercise = child.getValue(ReinerAufenthaltExercise.class);
                                                } else if (category.equals("Wellness")) {
                                                    exercise = new WellnessExercise();
                                                    exercise = child.getValue(WellnessExercise.class);
                                                }

                                            if(exercise != null)
                                                diaryEntry.addExercise(exercise);

                                        }
                                    }
                                    allDiaryEntries.add(diaryEntry);
                                    ActivityDiary.notifyDataChanged();
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                    }


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


    static public void GetLastTodayDiaryEntry(User user, Date date)
    {
        try {
            final String sDate = DAL_Utilities.ConvertDateToFirebaseDate(date);
            URL url = new URL(DAL_Utilities.DatabaseURL  +  "users/" + user.getName() + "/Diary/" + sDate + "/");
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
            Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/Diary/" + diaryEntry.getID() + "/");
            Firebase newChildRef = ref.push();

            Firebase childDate = newChildRef.child("date");
            childDate.setValue(diaryEntry.getDate());

            Firebase childTime = newChildRef.child("time");
            childTime.setValue(diaryEntry.getTime());

            Firebase childPonts = newChildRef.child("totalPoints");
            childPonts.setValue(diaryEntry.getTotalpoints());


            int i = 0;
            for(Exercise ex : diaryEntry.getExerciseList()){
                // 1. Ebene
                Firebase exerciseChild = newChildRef.child("exercise " + String.valueOf(i) + " :");

                // 2. Ebene
                Firebase categoryChild = exerciseChild.child("category");
                categoryChild.setValue(ex.getCategory());
                Firebase childExercise = exerciseChild.child("name");
                childExercise.setValue(ex.getName());
                Firebase childMinutes = exerciseChild.child("timeMinutes");
                childMinutes.setValue(ex.getTimeMunites());
                Firebase childHours = exerciseChild.child("timeHours");
                childHours.setValue(ex.getTimeHours());

                i++;
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

    /**
     * Speichert die Scorings des Fitnessfragbogens in der Datenbank.
     * @param user
     * @param finessfragebogen
     */
    static public void InsertFitnessFragebogen(User user, FitnessFragebogen finessfragebogen, Date date)
    {
        try
        {
            Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/FitnessFragebogen/" + finessfragebogen.Date + "/"  );
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
    static public void InsertFragebogen(User user, Fragebogen fragebogen, Date date)
    {
        try
        {


            Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/BSAFragebogen/" +fragebogen.Date + "/"  );

            Firebase newChildRef = ref.push();

            if(fragebogen.berufstätig>=0){
            Firebase childberufstätig = newChildRef.child("Berufstätig");
            childberufstätig.setValue(fragebogen.berufstätig);}

            if (fragebogen.berufstätig==0 && fragebogen.sitzendetätigkeiten>=0)
            {Firebase childsitzendetätigkeiten = newChildRef.child("Sitzende Tätigkeiten");
            childsitzendetätigkeiten.setValue(fragebogen.sitzendetätigkeiten);}

            if (fragebogen.berufstätig==0 && fragebogen.mäßigebewegung>=0){
            Firebase childmäßigebewegung = newChildRef.child("Mäßige Bewegung");
            childmäßigebewegung.setValue(fragebogen.mäßigebewegung);}

            if (fragebogen.berufstätig==0 && fragebogen.intensivebewegung>=0){
            Firebase childintensivebewegung = newChildRef.child("Intensive Bewegung");
            childintensivebewegung.setValue(fragebogen.intensivebewegung);}

            if (fragebogen.sportlichaktiv>=0){
            Firebase childsportlichaktiv = newChildRef.child("Sportlich Aktiv");
            childsportlichaktiv.setValue(fragebogen.sportlichaktiv);}

            if(fragebogen.zufußzurarbeit>0){
            Firebase childzufußzurarbeit = newChildRef.child("Zu Fuß zur Arbeit");
            childzufußzurarbeit.setValue(fragebogen.zufußzurarbeit);}

            if(fragebogen.zufußeinkaufen>0){
            Firebase childzufußeinkaufen = newChildRef.child("Zu Fuß einkaufen");
            childzufußeinkaufen.setValue(fragebogen.zufußeinkaufen);}

            if (fragebogen.radzurarbeit>0){
            Firebase childradzurarbeit = newChildRef.child("Mit dem Rad zur Arbeit");
            childradzurarbeit.setValue(fragebogen.radzurarbeit);}

            if (fragebogen.radfahren>0){
            Firebase childradfahren = newChildRef.child("Radfahren");
            childradfahren.setValue(fragebogen.radfahren);}

            if(fragebogen.spazieren>0){
            Firebase childspazieren = newChildRef.child("Spazieren");
            childspazieren.setValue(fragebogen.spazieren);}

            if(fragebogen.gartenarbeit>0){
            Firebase childgartenarbeit = newChildRef.child("Gartenarbeit");
            childgartenarbeit.setValue(fragebogen.gartenarbeit);}

            if(fragebogen.hausarbeit>0){
            Firebase childhausarbeit = newChildRef.child("Hausarbeit");
            childhausarbeit.setValue(fragebogen.hausarbeit);}

            if(fragebogen.pflegearbeit>0){
            Firebase childpflegearbeit = newChildRef.child("Pflegearbeit");
            childpflegearbeit.setValue(fragebogen.pflegearbeit);}

            if(fragebogen.treppensteigen>0){
            Firebase childtreppensteigen = newChildRef.child("Treppensteigen");
            childtreppensteigen.setValue(fragebogen.treppensteigen);}

            if (fragebogen.aktivitätaname.isEmpty()==false){
            Firebase childaktaname = newChildRef.child("Aktivität A Name");
            childaktaname.setValue(fragebogen.aktivitätaname);}

            if (fragebogen.aktivitätaname.isEmpty()==false && fragebogen.aktivitäta>0){
            Firebase childaktaanzahl = newChildRef.child("Aktivität A Zeit");
            childaktaanzahl.setValue(fragebogen.aktivitäta);}

            if (fragebogen.aktivitätbname.isEmpty()==false){
            Firebase childaktbname = newChildRef.child("Aktivität B Name");
            childaktbname.setValue(fragebogen.aktivitätbname);}

            if (fragebogen.aktivitätaname.isEmpty()==false && fragebogen.aktivitätb>0){
            Firebase childaktbanzahl = newChildRef.child("Aktivität B Zeit");
            childaktbanzahl.setValue(fragebogen.aktivitätb);}

            if (fragebogen.aktivitätcname.isEmpty()==false){
            Firebase childaktcname = newChildRef.child("Aktivität C Name");
            childaktcname.setValue(fragebogen.aktivitätcname);}

            if (fragebogen.aktivitätaname.isEmpty()==false && fragebogen.aktivitätc>0){
            Firebase childaktcanzahl = newChildRef.child("Aktivität C Zeit");
            childaktcanzahl.setValue(fragebogen.aktivitätc);}

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
            URL url = new URL("https://sportapp-cbd6b.firebaseio.com/" + "users/" + user.getName() + "/methodRatings");
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
     * @param currentActiveGroup the currently active group
     * @param nextActiveGroup the next group to be active
     * @param alternGroup the set of groups currently used
     */
    static public void insertAlternGroupUpdate(String currentActiveGroup, String nextActiveGroup, String alternGroup) {
        try
        {
            // setting up url for the database
            URL url = new URL("https://sportapp-cbd6b.firebaseio.com/" + "/Administration/assignment/altern/" + alternGroup + "/groups/");
            Firebase root = new Firebase(url.toString());
            // update group values
            root.child(currentActiveGroup).child("groupactive").setValue(false);
            root.child(nextActiveGroup).child("groupactive").setValue(true);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}


