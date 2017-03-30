# README #

This repository contains an Android app that served as a bachelor internship.
The app uses the webapp "Kompass", which is available through this repository <https://bitbucket.org/Alfhir/kompass>

### What is this repository for? ###
* Aim of the bachelor internship was to create an app that provides motivational help for the user by using various scientificly designed motivation methods, with the goal to provide aid in retaining their motivations regarding sporting activities. The app shal furthermore collect psychologically interesting informations about the users behavior. The app is subject of interest regarding further development by the TU Darmstadt, Germany. For further development we suggest Android Studio.
* Version 1.0

### How do I get set up? ###
The setup is done in two main steps:  
I. Setup of the Kompass web app:  
  * Download the project from https://bitbucket.org/Alfhir/kompass
  * Change the connection string which is located in src/app/app.module.ts as "export const firebaseConfig" to the connection of your webhost in firebase. 
  * Setup the project as described in the readme of https://bitbucket.org/Alfhir/kompass
  * Host the web site (in the www folder) in firebase 
  * To create the database, two shell script files are located in Kompass_Shell_Script.zip
  * Replace the used URL in the two files with the URL of your database in that firebase account
  * Run the shell scripts. We recommend using Git Bash.
  * Set your rules in the database as public 
  * Enable the Email/Password Sign-In Method
  * Finally change the DAL_Utilities class in the DataAccessLayer package and set KompassURL to the URL of the database of Kompass.
  
 II. Setup of the native App:  
* download Android Studio		https://developer.android.com/studio/index.html
* run the installer and install Android Studio with the default/standard settings (Android SDK included)
* start Android Studio
* download the repository and unzip it		github.com/mbprog/fitup
* on the welcome screen choose "Open an existing Android Studio Project" and choose the directory of the unziped project
* on Gradle's notice (if the initial project sync fails) "Install missing platforms and sync project"
* on Gradle's notice "Install Build Tools 25.0.1 and sync project"
* log into Firebase with your Google account 	https://console.firebase.google.com/
* add an project to you Firebase account (so you have two)
* switch in the left side bar to "Database"
* change the security rules to public by setting ".read" : true	and  ".write" : true
* copy the database URL (https://example-12345.firebaseio.com)
* inside your projects directory go to "org/tud/bp/fitup/DataAccessLayer/DAL_Utilities"  
* edit the value DatabaseURL to your database's URL
* should you use an Android Device to debug, make sure that the USB debugging option in the developer options is enabled
* choose an emulator to run the program on and run it

### Configuration ###
The app has been configured for android use.

### Tests ###
This prototype has been tested manually and with help of the JUnit framework.

### Deployment instructions ###
* install [Android Studio](https://developer.android.com/studio/index.html)
* update to the newest version
* import the project via VCS -> Git -> Clone
* syncronize the project's gradle files (open "build.gradle" and click on the message "Sync now" in the top right corner)
* set your database URL, as explained above
* build the APK via Build -> Build APk or Build -> Generate Signed APK
* load the APK onto an Android Smartphone with Android Version 7.0 or newer
* install the application and run it

### Contribution guidelines ###

* Writing code: 
Felix Götz, 
Sebastian Jestädt, 
Mohammad Braei, 
Yassine Aziani, 
Zamir Wayandou

* Writing tests: 
Felix Götz, 
Yassine Aziani

* Code review: 
Felix Götz, 
Sebastian Jestädt, 
Mohammad Braei, 
Yassine Aziani, 
Zamir Wayandou

### Who do I talk to? ###

* Send us a mail at sportappbachelor@gmail.com
