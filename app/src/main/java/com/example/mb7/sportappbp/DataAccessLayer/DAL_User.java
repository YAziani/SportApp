package com.example.mb7.sportappbp.DataAccessLayer;

import android.util.Log;

import com.example.mb7.sportappbp.Activity.ActivityDiary;
import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.Activity.ActivityNewChallenge;
import com.example.mb7.sportappbp.BusinessLayer.Challenge;
import com.example.mb7.sportappbp.BusinessLayer.FitnessFragebogen;
import com.example.mb7.sportappbp.BusinessLayer.Fragebogen;
import com.example.mb7.sportappbp.BusinessLayer.StimmungsAngabe;
import com.example.mb7.sportappbp.BusinessLayer.StimmungAbfrageScore;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.example.mb7.sportappbp.Objects.AllDiaryEntries;
import com.example.mb7.sportappbp.BusinessLayer.DiaryEntry;
import com.example.mb7.sportappbp.BusinessLayer.Exercise;
import com.example.mb7.sportappbp.BusinessLayer.LeistungstestsExercise;
import com.example.mb7.sportappbp.BusinessLayer.ReinerAufenthaltExercise;
import com.example.mb7.sportappbp.BusinessLayer.TrainingExercise;
import com.example.mb7.sportappbp.BusinessLayer.WellnessExercise;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by MB7 on 31.01.2017.
 */

public class DAL_User {
    static  private long gcounter ;

    static public StimmungsAngabe GetStimmnungsabfrage(User user, String date){
    final StimmungsAngabe stimmunga=new StimmungsAngabe();

        try
        {
            final String fDate = date;/// DAL_Utilities.ConvertDateToFirebaseDate(date);
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + user.getName()+ "/Stimmungsabfrage/" + fDate);
            final Firebase root = new Firebase(url.toString());

            root.addListenerForSingleValueEvent(new ValueEventListener() {

                // Hier kriegst du den Knoten -KfNx5TBo4yQpfN07Ekh als Value
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String  strKey = dataSnapshot.getKey();// Datum

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        // Hier bekommst du den Knoten V oder N
                        strKey = child.getKey();// key -KfNx5TBo4yQpfN07Ekh
                        root.child(strKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                           @Override
                                                                           public void onDataChange(DataSnapshot dataSnapshot) {
                                                                               for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                                                   // child.key -> V or N
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


    static public void InsertStimmung(User user, StimmungsAngabe stimmungsAngabe)
    {
        try
        {


            Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/Stimmungsabfrage/" + stimmungsAngabe.Date + "/"  + DAL_Utilities.GetTimeAsString());

            String V_N = stimmungsAngabe.Vor?"/V":"/N";
            if (stimmungsAngabe.Angespannt >= 0) {
                Firebase childAngespannt = ref.child(V_N).child("Angespannt");
                childAngespannt.setValue(stimmungsAngabe.Angespannt);
            }
            if(stimmungsAngabe.Mitteilsam >=0) {
                Firebase childMitteilsam = ref.child(V_N).child("Mitteilsam");
                childMitteilsam.setValue(stimmungsAngabe.Mitteilsam);
            }
            if(stimmungsAngabe.Muede >= 0) {
                Firebase childMuede = ref.child(V_N).child("Muede");
                childMuede.setValue(stimmungsAngabe.Muede);
            }
            if(stimmungsAngabe.Selbstsicher >=0) {
                Firebase childSelbstsicher = ref.child(V_N).child("Selbstsicher");
                childSelbstsicher.setValue(stimmungsAngabe.Selbstsicher);
            }
            if(stimmungsAngabe.Tatkraeftig >= 0) {
                Firebase childTatkraeftig = ref.child(V_N).child("Tatkraeftig");
                childTatkraeftig.setValue(stimmungsAngabe.Tatkraeftig);
            }
            if(stimmungsAngabe.Traurig >=0) {
                Firebase childTraurig = ref.child(V_N).child("Traurig");
                childTraurig.setValue(stimmungsAngabe.Traurig);
            }
            if(stimmungsAngabe.Wuetend >=0) {
                Firebase childWuetend = ref.child(V_N).child("Wuetend");
                childWuetend.setValue(stimmungsAngabe.Wuetend);
            }
            if(stimmungsAngabe.Zerstreut >= 0) {
                Firebase childZerstreut = ref.child(V_N).child("Zerstreut");
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

    static public void UpdateStimmung(User user, StimmungsAngabe stimmungsAngabe)
    {
        try
        {


            Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/Stimmungsabfrage/" + stimmungsAngabe.Date + "/"  + stimmungsAngabe.Time + "/");

            String V_N = stimmungsAngabe.Vor?"/V":"/N";
            if (stimmungsAngabe.Angespannt >= 0) {
                Firebase childAngespannt = ref.child(V_N).child("Angespannt");
                childAngespannt.setValue(stimmungsAngabe.Angespannt);
            }
            if(stimmungsAngabe.Mitteilsam >=0) {
                Firebase childMitteilsam = ref.child(V_N).child("Mitteilsam");
                childMitteilsam.setValue(stimmungsAngabe.Mitteilsam);
            }
            if(stimmungsAngabe.Muede >= 0) {
                Firebase childMuede = ref.child(V_N).child("Muede");
                childMuede.setValue(stimmungsAngabe.Muede);
            }
            if(stimmungsAngabe.Selbstsicher >=0) {
                Firebase childSelbstsicher = ref.child(V_N).child("Selbstsicher");
                childSelbstsicher.setValue(stimmungsAngabe.Selbstsicher);
            }
            if(stimmungsAngabe.Tatkraeftig >= 0) {
                Firebase childTatkraeftig = ref.child(V_N).child("Tatkraeftig");
                childTatkraeftig.setValue(stimmungsAngabe.Tatkraeftig);
            }
            if(stimmungsAngabe.Traurig >=0) {
                Firebase childTraurig = ref.child(V_N).child("Traurig");
                childTraurig.setValue(stimmungsAngabe.Traurig);
            }
            if(stimmungsAngabe.Wuetend >=0) {
                Firebase childWuetend = ref.child(V_N).child("Wuetend");
                childWuetend.setValue(stimmungsAngabe.Wuetend);
            }
            if(stimmungsAngabe.Zerstreut >= 0) {
                Firebase childZerstreut = ref.child(V_N).child("Zerstreut");
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
            Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/StimmungabfrageScore/"+ stimmungAbfrageScore.Date + "/"  + DAL_Utilities.GetTimeAsString() );

            Firebase childscoreangespannt = ref.child("Score Angespannt");
            childscoreangespannt.setValue(stimmungAbfrageScore.AngespanntScore);

            Firebase childscoretraurig = ref.child("Score Traurig");
            childscoretraurig.setValue(stimmungAbfrageScore.TraurigScore);

            Firebase childscoretatkraeftig = ref.child("Score Tatkräftig");
            childscoretatkraeftig.setValue(stimmungAbfrageScore.TatkraeftigScore);

            Firebase childscorezerstreut = ref.child("Score Zerstreut");
            childscorezerstreut.setValue(stimmungAbfrageScore.ZerstreutScore);

            Firebase childscorewuetend = ref.child("Score Wütend");
            childscorewuetend.setValue(stimmungAbfrageScore.WuetendScore);

            Firebase childscoremuede = ref.child("Score Müde");
            childscoremuede.setValue(stimmungAbfrageScore.MuedeScore);

            Firebase childscoreselbstsicher = ref.child("Score Selbstsicher");
            childscoreselbstsicher.setValue(stimmungAbfrageScore.SelbstsicherScore);

            Firebase childscoremitteilsam = ref.child("Score Mitteilsam");
            childscoremitteilsam.setValue(stimmungAbfrageScore.MitteilsamScore);

            Firebase childscorebarometer = ref.child("Score Stimmungsbarometer");
            childscorebarometer.setValue(stimmungAbfrageScore.StimmungsBarometerScore);

            Firebase childscoreenergieindex = ref.child("Energieindex");
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


    static public void LoadCompleteDiary(User user)
    {

        final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd,HH:mm:ss");
        final SimpleDateFormat sdfDateDiary = new SimpleDateFormat("dd.MM.yyyy");

        try
        {
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + user.getName()+ "/Diary/");
            final Firebase root = new Firebase(url.toString());

            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        //date yyyyMMdd
                        final String strDate = child.getKey();

                        root.child(strDate).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    //unikey
                                    //time
                                    final String strTime = child.getKey();

                                    root.child(strDate).child(strTime).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            //get instance to add all diaryentries after restore
                                            AllDiaryEntries allDiaryEntries = AllDiaryEntries.getInstance();
                                            //create object
                                            DiaryEntry diaryEntry = new DiaryEntry();

                                            //restore date from diaryEntry
                                            try {
                                                Date date = sdfDate.parse(strDate + "," + strTime );
                                                diaryEntry.setDate(date);
                                                diaryEntry.sDate = sdfDateDiary.format(date);
                                                diaryEntry.sTime = strTime;
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }





                                            for (DataSnapshot child : dataSnapshot.getChildren()) {

                                                //restore totalPoints from diaryEntry
                                                if (child.getKey().equals("totalpoints"))
                                                    diaryEntry.setTotalPoints(Integer.getInteger(child.getValue().toString()));
                                                //restore exercises from diaryEntry
                                                else if(child.getKey().startsWith("exercise")){
                                                    //get the category of the stores exercise
                                                    String category = child.getChildren().iterator().next().getValue().toString();

                                                    Exercise exercise = null;
                                                    //create the exercise object from the right class and restore the attribute
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
                                                    //after restoring of the exercise, these has to be added to the diaryEntry
                                                    if(exercise != null)
                                                        diaryEntry.addExercise(exercise);

                                                }
                                            }
                                            //add every restored diaryEntry the singleton object
                                            allDiaryEntries.add(diaryEntry);
                                            //allDiaryEntries will be used by the ListViewAdapter from the ActivityDiary.
                                            //for which reason this method has to be executed
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
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        try
        {
            Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/Diary/");

            Firebase newChildDate = ref.child(sdfDate.format(diaryEntry.getDate()));

            Firebase childTime = newChildDate.child(sdfTime.format(diaryEntry.getDate()).toString());

            Firebase childPonts = childTime.child("totalPoints");
            childPonts.setValue(diaryEntry.getTotalPoints());


            int i = 0;
            for(Exercise ex : diaryEntry.getExerciseList()){
                // 1. Ebene
                Firebase exerciseChild = childTime.child("exercise " + String.valueOf(i) + " :");

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

    /**
     * Inserts challenge to the database
     * @param user the current user
     * @param challenge the challenge to add
     */
    public static void InsertChallenge(User user, Challenge challenge){

        Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/Challenges/");

        //Build a string with start and end date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        StringBuilder sb = new StringBuilder();
        sb.append(sdf.format(challenge.getStartDate())).append("_").
                append(sdf.format(challenge.getEndDate()));

        //add challenge to user challenges
        Firebase newChild = ref.child(challenge.getName());
        newChild.setValue(sdf.format(challenge.getEndDate()));

    }

    /**
     * Removes the challenge in user in the database
     *
     * @param user user that has to leave the challenge
     * @param challenge to leave
     */
    public static void RemoveChallenge(User user, Challenge challenge){

        Firebase ref = new Firebase("https://sportapp-cbd6b.firebaseio.com/" + "users/" + user.getName() + "/Challenges/" );
        ref.child(challenge.getName()).removeValue();
    }


}


