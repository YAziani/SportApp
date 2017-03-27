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

import static com.example.mb7.sportappbp.R.string.fitnessfragebogen;

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
     * Speichert die Scorings und Indexe der gewählten Listviews des Fitnessfragbogens in der Datenbank.
     * @param user
     * @param finessfragebogen
     */
    static public void InsertFitnessFragebogen(User user, FitnessFragebogen finessfragebogen)

    {
        try
        {
            Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/FitnessFragebogen/" + finessfragebogen.Date );




            if(finessfragebogen.stuhlaufstehen>=0){
                Firebase childstuhlaufstehen = ref.child("auf einem Stuhl sitzend ohne Hilfe der Arme aufstehen");
                childstuhlaufstehen.setValue(finessfragebogen.stuhlaufstehen);}

            if(finessfragebogen.einkaufskorb>=0){
                Firebase childeinkaufskorb = ref.child("einen schweren Einkaufskorb (8 kg) über mehrere Etagen tragen");
                childeinkaufskorb.setValue(finessfragebogen.einkaufskorb);}

            if(finessfragebogen.kistetragen>=0){
                Firebase childkistetragen = ref.child("eine volle Bierkiste in den Keller tragen");
                childkistetragen.setValue(finessfragebogen.kistetragen);}

            if(finessfragebogen.situp>=0){
                Firebase childsitup = ref.child("aus der Rückenlage ohne Hilfe der Arme den Oberkörper aufrichten (Situp)");
                childsitup.setValue(finessfragebogen.situp);}

            if(finessfragebogen.kofferheben>=0){
                Firebase childkofferheben = ref.child("einen schweren Koffer über Kopfhöhe heben");
                childkofferheben.setValue(finessfragebogen.kofferheben);}

            if(finessfragebogen.koffertragen>=0){
                Firebase childkoffertragen = ref.child("2 schwere Koffer über mehrere Etagen tragen");
                childkoffertragen.setValue(finessfragebogen.koffertragen);}

            if(finessfragebogen.hantelstemmen>=0){
                Firebase childhantelstemmen = ref.child("eine Hantel mit mehr als Ihrem Körpergewicht hochstemmen");
                childhantelstemmen.setValue(finessfragebogen.hantelstemmen);}

            if(finessfragebogen.flottgehen>=0){
                Firebase childflottgehen = ref.child("um mehrere Blocks flott gehen");
                childflottgehen.setValue(finessfragebogen.flottgehen);}

            if(finessfragebogen.treppengehen>=0){
                Firebase childtreppengehen = ref.child("mehrere Treppen hochgehen ohne auszuruhen");
                childtreppengehen.setValue(finessfragebogen.treppengehen);}

            if(finessfragebogen.zweikmgehen>=0){
                Firebase childzweikmgehen = ref.child("zwei Kilometer schnell gehen („walken“) ohne auszuruhen");
                childzweikmgehen.setValue(finessfragebogen.zweikmgehen);}

            if(finessfragebogen.einkmjoggen>=0){
                Firebase childeinkmjoggen = ref.child("Einen Kilometer ohne Pause joggen");
                childeinkmjoggen.setValue(finessfragebogen.einkmjoggen);}

            if(finessfragebogen.dreißigminjoggen>=0){
                Firebase childdreißigminjoggen = ref.child("30 min ohne Pause joggen");
                childdreißigminjoggen.setValue(finessfragebogen.dreißigminjoggen);}

            if(finessfragebogen.sechzigminjoggen>=0){
                Firebase childsechzigminjoggen = ref.child("Eine Stunde ohne Pause joggen (ca. 10 km)");
                childsechzigminjoggen.setValue(finessfragebogen.sechzigminjoggen);}

            if(finessfragebogen.marathon>=0){
                Firebase childmarathon = ref.child("einen Marathonlauf (42 km) laufen");
                childmarathon.setValue(finessfragebogen.marathon);}

            if(finessfragebogen.anziehen>=0){
                Firebase childanziehen = ref.child("einen engen Pulli und Socken alleine aus- und anziehen");
                childanziehen.setValue(finessfragebogen.anziehen);}

            if(finessfragebogen.sitzendboden>=0){
                Firebase childsitzendboden = ref.child("auf einem Stuhl sitzend mit den Händen den Boden erreichen");
                childsitzendboden.setValue(finessfragebogen.sitzendboden);}

            if(finessfragebogen.schuhebinden>=0){
                Firebase childschuhebinden = ref.child("im Stehen Schuhe binden");
                childschuhebinden.setValue(finessfragebogen.schuhebinden);}

            if(finessfragebogen.rueckenberuehren>=0){
                Firebase childrueckenberuehren =ref.child("mit der Hand von unten auf dem Rücken ein Schulterblatt berühren");
                childrueckenberuehren.setValue(finessfragebogen.rueckenberuehren);}

            if(finessfragebogen.stehendboden>=0){
                Firebase childstehendboden = ref.child("aus dem Stand (Knie gestr.) mit den Händen den Boden erreichen");
                childstehendboden.setValue(finessfragebogen.stehendboden);}

            if(finessfragebogen.kopfknie>=0){
                Firebase childkopfknie = ref.child("im Stehen mit dem Kopf die gestreckten Knie berühren");
                childkopfknie.setValue(finessfragebogen.kopfknie);}

            if(finessfragebogen.bruecke>=0){
                Firebase childbruecke = ref.child("rückwärts bis in die Brücke abbeugen");
                childbruecke.setValue(finessfragebogen.bruecke);}

            if(finessfragebogen.trepperunter>=0){
                Firebase childtrepperunter =ref.child("eine Treppe hinab gehen, ohne sich festzuhalten");
                childtrepperunter.setValue(finessfragebogen.trepperunter);}

            if(finessfragebogen.einbeinstand>=0){
                Firebase childeinbeinstand =ref.child("auf einem Bein stehen, ohne sich festzuhalten (mind. 15 sec.)");
                childeinbeinstand.setValue(finessfragebogen.einbeinstand);}

            if(finessfragebogen.purzelbaum>=0){
                Firebase childpurzelbaum = ref.child("einen Purzelbaum");
                childpurzelbaum.setValue(finessfragebogen.purzelbaum);}

            if(finessfragebogen.ballprellen>=0){
                Firebase childballprellen = ref.child("im schnellen Gehen einen Ball prellen");
                childballprellen.setValue(finessfragebogen.ballprellen);}

            if(finessfragebogen.zaunsprung>=0){
                Firebase childzaunsprung = ref.child("mit Abstützen über einen 1m hohen Zaun springen");
                childzaunsprung.setValue(finessfragebogen.zaunsprung);}

            if(finessfragebogen.kurveohnehand>=0){
                Firebase childkurveohnehand = ref.child("freihändig mit dem Fahrrad um eine Kurve fahren");
                childkurveohnehand.setValue(finessfragebogen.kurveohnehand);}

            if(finessfragebogen.radschlagen>=0){
                Firebase childradschlagen = ref.child("ein Rad schlagen");
                childradschlagen.setValue(finessfragebogen.radschlagen);}


            Firebase childscorekraft = ref.child("Score Kraft");
            childscorekraft.setValue(finessfragebogen.scorekraft);

            Firebase childscoreausdauer = ref.child("Score Ausdauer");
            childscoreausdauer.setValue(finessfragebogen.scoreausdauer);

            Firebase childscorekoordination = ref.child("Score Koordination");
            childscorekoordination.setValue(finessfragebogen.scorekoordination);

            Firebase childscorebewglichkeit = ref.child("Score Beweglichkeit");
            childscorebewglichkeit.setValue(finessfragebogen.scorebeweglichkeit);

            Firebase childscoregesamt = ref.child("Gesamtscore");
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
     * Updatet die Daten aus fitnessfragebogen
     * @param user
     * @param finessfragebogen
     */
    static public void UpdateFitnessFragebogen(User user, FitnessFragebogen finessfragebogen)
    {
        try
        {
            Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/FitnessFragebogen/" + finessfragebogen.Date );




            if(finessfragebogen.stuhlaufstehen>=0){
                Firebase childstuhlaufstehen = ref.child("auf einem Stuhl sitzend ohne Hilfe der Arme aufstehen");
                childstuhlaufstehen.setValue(finessfragebogen.stuhlaufstehen);}

            if(finessfragebogen.einkaufskorb>=0){
                Firebase childeinkaufskorb = ref.child("einen schweren Einkaufskorb (8 kg) über mehrere Etagen tragen");
                childeinkaufskorb.setValue(finessfragebogen.einkaufskorb);}

            if(finessfragebogen.kistetragen>=0){
                Firebase childkistetragen = ref.child("eine volle Bierkiste in den Keller tragen");
                childkistetragen.setValue(finessfragebogen.kistetragen);}

            if(finessfragebogen.situp>=0){
                Firebase childsitup = ref.child("aus der Rückenlage ohne Hilfe der Arme den Oberkörper aufrichten (Situp)");
                childsitup.setValue(finessfragebogen.situp);}

            if(finessfragebogen.kofferheben>=0){
                Firebase childkofferheben = ref.child("einen schweren Koffer über Kopfhöhe heben");
                childkofferheben.setValue(finessfragebogen.kofferheben);}

            if(finessfragebogen.koffertragen>=0){
                Firebase childkoffertragen = ref.child("2 schwere Koffer über mehrere Etagen tragen");
                childkoffertragen.setValue(finessfragebogen.koffertragen);}

            if(finessfragebogen.hantelstemmen>=0){
                Firebase childhantelstemmen = ref.child("eine Hantel mit mehr als Ihrem Körpergewicht hochstemmen");
                childhantelstemmen.setValue(finessfragebogen.hantelstemmen);}

            if(finessfragebogen.flottgehen>=0){
                Firebase childflottgehen = ref.child("um mehrere Blocks flott gehen");
                childflottgehen.setValue(finessfragebogen.flottgehen);}

            if(finessfragebogen.treppengehen>=0){
                Firebase childtreppengehen = ref.child("mehrere Treppen hochgehen ohne auszuruhen");
                childtreppengehen.setValue(finessfragebogen.treppengehen);}

            if(finessfragebogen.zweikmgehen>=0){
                Firebase childzweikmgehen = ref.child("zwei Kilometer schnell gehen („walken“) ohne auszuruhen");
                childzweikmgehen.setValue(finessfragebogen.zweikmgehen);}

            if(finessfragebogen.einkmjoggen>=0){
                Firebase childeinkmjoggen = ref.child("Einen Kilometer ohne Pause joggen");
                childeinkmjoggen.setValue(finessfragebogen.einkmjoggen);}

            if(finessfragebogen.dreißigminjoggen>=0){
                Firebase childdreißigminjoggen = ref.child("30 min ohne Pause joggen");
                childdreißigminjoggen.setValue(finessfragebogen.dreißigminjoggen);}

            if(finessfragebogen.sechzigminjoggen>=0){
                Firebase childsechzigminjoggen = ref.child("Eine Stunde ohne Pause joggen (ca. 10 km)");
                childsechzigminjoggen.setValue(finessfragebogen.sechzigminjoggen);}

            if(finessfragebogen.marathon>=0){
                Firebase childmarathon = ref.child("einen Marathonlauf (42 km) laufen");
                childmarathon.setValue(finessfragebogen.marathon);}

            if(finessfragebogen.anziehen>=0){
                Firebase childanziehen = ref.child("einen engen Pulli und Socken alleine aus- und anziehen");
                childanziehen.setValue(finessfragebogen.anziehen);}

            if(finessfragebogen.sitzendboden>=0){
                Firebase childsitzendboden = ref.child("auf einem Stuhl sitzend mit den Händen den Boden erreichen");
                childsitzendboden.setValue(finessfragebogen.sitzendboden);}

            if(finessfragebogen.schuhebinden>=0){
                Firebase childschuhebinden = ref.child("im Stehen Schuhe binden");
                childschuhebinden.setValue(finessfragebogen.schuhebinden);}

            if(finessfragebogen.rueckenberuehren>=0){
                Firebase childrueckenberuehren =ref.child("mit der Hand von unten auf dem Rücken ein Schulterblatt berühren");
                childrueckenberuehren.setValue(finessfragebogen.rueckenberuehren);}

            if(finessfragebogen.stehendboden>=0){
                Firebase childstehendboden = ref.child("aus dem Stand (Knie gestr.) mit den Händen den Boden erreichen");
                childstehendboden.setValue(finessfragebogen.stehendboden);}

            if(finessfragebogen.kopfknie>=0){
                Firebase childkopfknie = ref.child("im Stehen mit dem Kopf die gestreckten Knie berühren");
                childkopfknie.setValue(finessfragebogen.kopfknie);}

            if(finessfragebogen.bruecke>=0){
                Firebase childbruecke = ref.child("rückwärts bis in die Brücke abbeugen");
                childbruecke.setValue(finessfragebogen.bruecke);}

            if(finessfragebogen.trepperunter>=0){
                Firebase childtrepperunter =ref.child("eine Treppe hinab gehen, ohne sich festzuhalten");
                childtrepperunter.setValue(finessfragebogen.trepperunter);}

            if(finessfragebogen.einbeinstand>=0){
                Firebase childeinbeinstand =ref.child("auf einem Bein stehen, ohne sich festzuhalten (mind. 15 sec.)");
                childeinbeinstand.setValue(finessfragebogen.einbeinstand);}

            if(finessfragebogen.purzelbaum>=0){
                Firebase childpurzelbaum = ref.child("einen Purzelbaum");
                childpurzelbaum.setValue(finessfragebogen.purzelbaum);}

            if(finessfragebogen.ballprellen>=0){
                Firebase childballprellen = ref.child("im schnellen Gehen einen Ball prellen");
                childballprellen.setValue(finessfragebogen.ballprellen);}

            if(finessfragebogen.zaunsprung>=0){
                Firebase childzaunsprung = ref.child("mit Abstützen über einen 1m hohen Zaun springen");
                childzaunsprung.setValue(finessfragebogen.zaunsprung);}

            if(finessfragebogen.kurveohnehand>=0){
                Firebase childkurveohnehand = ref.child("freihändig mit dem Fahrrad um eine Kurve fahren");
                childkurveohnehand.setValue(finessfragebogen.kurveohnehand);}

            if(finessfragebogen.radschlagen>=0){
                Firebase childradschlagen = ref.child("ein Rad schlagen");
                childradschlagen.setValue(finessfragebogen.radschlagen);}


            Firebase childscorekraft = ref.child("Score Kraft");
            childscorekraft.setValue(finessfragebogen.scorekraft);

            Firebase childscoreausdauer = ref.child("Score Ausdauer");
            childscoreausdauer.setValue(finessfragebogen.scoreausdauer);

            Firebase childscorekoordination = ref.child("Score Koordination");
            childscorekoordination.setValue(finessfragebogen.scorekoordination);

            Firebase childscorebewglichkeit = ref.child("Score Beweglichkeit");
            childscorebewglichkeit.setValue(finessfragebogen.scorebeweglichkeit);

            Firebase childscoregesamt = ref.child("Gesamtscore");
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
            Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/BSAFragebogen/" +fragebogen.Date  );


            if(fragebogen.berufstätig>=0){
            Firebase childberufstätig = ref.child("Berufstätig");
            childberufstätig.setValue(fragebogen.berufstätig);}

            if (fragebogen.berufstätig==0 && fragebogen.sitzendetätigkeiten>=0)
            {Firebase childsitzendetätigkeiten = ref.child("Sitzende Tätigkeiten");
            childsitzendetätigkeiten.setValue(fragebogen.sitzendetätigkeiten);}

            if (fragebogen.berufstätig==0 && fragebogen.mäßigebewegung>=0){
            Firebase childmäßigebewegung = ref.child("Mäßige Bewegung");
            childmäßigebewegung.setValue(fragebogen.mäßigebewegung);}

            if (fragebogen.berufstätig==0 && fragebogen.intensivebewegung>=0){
            Firebase childintensivebewegung = ref.child("Intensive Bewegung");
            childintensivebewegung.setValue(fragebogen.intensivebewegung);}

            if (fragebogen.sportlichaktiv>=0){
            Firebase childsportlichaktiv =ref.child("Sportlich Aktiv");
            childsportlichaktiv.setValue(fragebogen.sportlichaktiv);}

            if(fragebogen.zufußzurarbeit>0){
            Firebase childzufußzurarbeit = ref.child("Zu Fuß zur Arbeit");
            childzufußzurarbeit.setValue(fragebogen.zufußzurarbeit);}

            if(fragebogen.zufußeinkaufen>0){
            Firebase childzufußeinkaufen = ref.child("Zu Fuß einkaufen");
            childzufußeinkaufen.setValue(fragebogen.zufußeinkaufen);}

            if (fragebogen.radzurarbeit>0){
            Firebase childradzurarbeit = ref.child("Mit dem Rad zur Arbeit");
            childradzurarbeit.setValue(fragebogen.radzurarbeit);}

            if (fragebogen.radfahren>0){
            Firebase childradfahren = ref.child("Radfahren");
            childradfahren.setValue(fragebogen.radfahren);}

            if(fragebogen.spazieren>0){
            Firebase childspazieren = ref.child("Spazieren");
            childspazieren.setValue(fragebogen.spazieren);}

            if(fragebogen.gartenarbeit>0){
            Firebase childgartenarbeit = ref.child("Gartenarbeit");
            childgartenarbeit.setValue(fragebogen.gartenarbeit);}

            if(fragebogen.hausarbeit>0){
            Firebase childhausarbeit = ref.child("Hausarbeit");
            childhausarbeit.setValue(fragebogen.hausarbeit);}

            if(fragebogen.pflegearbeit>0){
            Firebase childpflegearbeit = ref.child("Pflegearbeit");
            childpflegearbeit.setValue(fragebogen.pflegearbeit);}

            if(fragebogen.treppensteigen>0){
            Firebase childtreppensteigen = ref.child("Treppensteigen");
            childtreppensteigen.setValue(fragebogen.treppensteigen);}

            if (fragebogen.aktivitätaname.isEmpty()==false){
            Firebase childaktaname = ref.child("Aktivität A Name");
            childaktaname.setValue(fragebogen.aktivitätaname);}

            if (fragebogen.aktivitätaname.isEmpty()==false && fragebogen.aktivitäta>0){
            Firebase childaktaanzahl = ref.child("Aktivität A Zeit");
            childaktaanzahl.setValue(fragebogen.aktivitäta);}

            if (fragebogen.aktivitätbname.isEmpty()==false){
            Firebase childaktbname = ref.child("Aktivität B Name");
            childaktbname.setValue(fragebogen.aktivitätbname);}

            if (fragebogen.aktivitätaname.isEmpty()==false && fragebogen.aktivitätb>0){
            Firebase childaktbanzahl = ref.child("Aktivität B Zeit");
            childaktbanzahl.setValue(fragebogen.aktivitätb);}

            if (fragebogen.aktivitätcname.isEmpty()==false){
            Firebase childaktcname = ref.child("Aktivität C Name");
            childaktcname.setValue(fragebogen.aktivitätcname);}

            if (fragebogen.aktivitätaname.isEmpty()==false && fragebogen.aktivitätc>0){
            Firebase childaktcanzahl =ref.child("Aktivität C Zeit");
            childaktcanzahl.setValue(fragebogen.aktivitätc);}

            Firebase childbewegungscore = ref.child("Score Bewegung");
            childbewegungscore.setValue(fragebogen.bewegungscoring);

            Firebase childsportscore = ref.child("Score Sport");
            childsportscore.setValue(fragebogen.sportscoring);

            Firebase childscore = ref.child("Gesamtscore");
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
     * Speichert Antworten und Scoring des BSA Fragebogens.
     * @param user
     * @param fragebogen
     */
    static public void UpdateFragebogen(User user, Fragebogen fragebogen)
    {
        try
        {


            Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/BSAFragebogen/" +fragebogen.Date );


            if(fragebogen.berufstätig>=0){
                Firebase childberufstätig = ref.child("Berufstätig");
                childberufstätig.setValue(fragebogen.berufstätig);}

            if (fragebogen.berufstätig==0 && fragebogen.sitzendetätigkeiten>=0)
            {Firebase childsitzendetätigkeiten = ref.child("Sitzende Tätigkeiten");
                childsitzendetätigkeiten.setValue(fragebogen.sitzendetätigkeiten);}

            if (fragebogen.berufstätig==0 && fragebogen.mäßigebewegung>=0){
                Firebase childmäßigebewegung = ref.child("Mäßige Bewegung");
                childmäßigebewegung.setValue(fragebogen.mäßigebewegung);}

            if (fragebogen.berufstätig==0 && fragebogen.intensivebewegung>=0){
                Firebase childintensivebewegung = ref.child("Intensive Bewegung");
                childintensivebewegung.setValue(fragebogen.intensivebewegung);}

            if (fragebogen.sportlichaktiv>=0){
                Firebase childsportlichaktiv =ref.child("Sportlich Aktiv");
                childsportlichaktiv.setValue(fragebogen.sportlichaktiv);}

            if(fragebogen.zufußzurarbeit>0){
                Firebase childzufußzurarbeit = ref.child("Zu Fuß zur Arbeit");
                childzufußzurarbeit.setValue(fragebogen.zufußzurarbeit);}

            if(fragebogen.zufußeinkaufen>0){
                Firebase childzufußeinkaufen = ref.child("Zu Fuß einkaufen");
                childzufußeinkaufen.setValue(fragebogen.zufußeinkaufen);}

            if (fragebogen.radzurarbeit>0){
                Firebase childradzurarbeit = ref.child("Mit dem Rad zur Arbeit");
                childradzurarbeit.setValue(fragebogen.radzurarbeit);}

            if (fragebogen.radfahren>0){
                Firebase childradfahren = ref.child("Radfahren");
                childradfahren.setValue(fragebogen.radfahren);}

            if(fragebogen.spazieren>0){
                Firebase childspazieren = ref.child("Spazieren");
                childspazieren.setValue(fragebogen.spazieren);}

            if(fragebogen.gartenarbeit>0){
                Firebase childgartenarbeit = ref.child("Gartenarbeit");
                childgartenarbeit.setValue(fragebogen.gartenarbeit);}

            if(fragebogen.hausarbeit>0){
                Firebase childhausarbeit = ref.child("Hausarbeit");
                childhausarbeit.setValue(fragebogen.hausarbeit);}

            if(fragebogen.pflegearbeit>0){
                Firebase childpflegearbeit = ref.child("Pflegearbeit");
                childpflegearbeit.setValue(fragebogen.pflegearbeit);}

            if(fragebogen.treppensteigen>0){
                Firebase childtreppensteigen = ref.child("Treppensteigen");
                childtreppensteigen.setValue(fragebogen.treppensteigen);}

            if (fragebogen.aktivitätaname.isEmpty()==false){
                Firebase childaktaname = ref.child("Aktivität A Name");
                childaktaname.setValue(fragebogen.aktivitätaname);}

            if (fragebogen.aktivitätaname.isEmpty()==false && fragebogen.aktivitäta>0){
                Firebase childaktaanzahl = ref.child("Aktivität A Zeit");
                childaktaanzahl.setValue(fragebogen.aktivitäta);}

            if (fragebogen.aktivitätbname.isEmpty()==false){
                Firebase childaktbname = ref.child("Aktivität B Name");
                childaktbname.setValue(fragebogen.aktivitätbname);}

            if (fragebogen.aktivitätaname.isEmpty()==false && fragebogen.aktivitätb>0){
                Firebase childaktbanzahl = ref.child("Aktivität B Zeit");
                childaktbanzahl.setValue(fragebogen.aktivitätb);}

            if (fragebogen.aktivitätcname.isEmpty()==false){
                Firebase childaktcname = ref.child("Aktivität C Name");
                childaktcname.setValue(fragebogen.aktivitätcname);}

            if (fragebogen.aktivitätaname.isEmpty()==false && fragebogen.aktivitätc>0){
                Firebase childaktcanzahl =ref.child("Aktivität C Zeit");
                childaktcanzahl.setValue(fragebogen.aktivitätc);}

            Firebase childbewegungscore = ref.child("Score Bewegung");
            childbewegungscore.setValue(fragebogen.bewegungscoring);

            Firebase childsportscore = ref.child("Score Sport");
            childsportscore.setValue(fragebogen.sportscoring);

            Firebase childscore = ref.child("Gesamtscore");
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
        Firebase name = ref.child(challenge.getName());

        Firebase nameChildStart =  name.child("startDate");
        nameChildStart.setValue(sdf.format(challenge.getStartDate()));

        Firebase nameChildEnd =  name.child("endDate");
        nameChildEnd.setValue(sdf.format(challenge.getStartDate()));
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


