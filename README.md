# README #

This repository contains an Android app that served as a bachelor internship.
The app uses the webapp "Kompass", which is available through this repository <https://bitbucket.org/Alfhir/kompass>

### What is this repository for? ###
* Aim of the bachelor internship was to create an app that provides motivational help for the user by using various scientificly designed motivation methods, with the goal to provide aid in retaining their motivations regarding sporting activities. The app shal furthermore collect psychologically interesting informations about the users behavior. The app is subject of interest regarding further development by the TU Darmstadt, Germany. For further development we suggest Android Studio.
* Version 1.0

### How do I get set up? ###
* Install the newest version of Android Studio
* Clone the project via VCS -> Git -> Clone
* Sync the project with gradle (open "build.gradle" and click on the message "Sync now" in the top right corner)
* Create a Firebase account and a Retrieving Database on Firebase
* Set it's security rules to public
* Change to the DAL_Utilities class in the project (inside the DataAccessLayer package) and set the DatabaseURL to your database's URL
* Build the application via Build -> Make Projekt
* Switch to the main activity "ActivityMain"
* Run the project, using the Run option of your current Android Studio version

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

* Writing code
Felix Götz
Sebastian Jestädt
Mohammad Braei
Yassine Aziani
Zamir Wayandou

* Writing tests
Felix Götz
Yassine Aziani

* Code review
Felix Götz
Sebastian Jestädt
Mohammad Braei
Yassine Aziani
Zamir Wayandou

### Who do I talk to? ###

* Send us a mail at sportappbachelor@gmail.com
