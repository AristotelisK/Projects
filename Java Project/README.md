![Projects/tree/master/Java%20Project](/brainstormLogo.png)

# Project Title
Ομάδα 5/ Brainstorm - JavaController

# Brainstorm
Έχουμε δημιουργήσει δυο Activity το MainActivity και το SettingsActivity.
Στο MainActivity φτίαξαμε δυο button και δυο imageView. Το Camera Permision δίνει άδεια για πρόσβαση στην κάμερα, το deactivation κάνει χειροκίνητη απενεργοποίηση.
Τα δύο ImageView είναι για το φακό και για την μουσική αντίστοιχα. Εχουμε τις δυο κλάσεις FlashlightControl και MediaPlayerControl για ενεργοποίηση/απενεργοποίηση.
Στην MainActivity έχουμε τις συναρτήσεις updateMusicGui/updateFlashlightGui για εναλλαγή των εικόνων.
BackpressButton με το πάτημα δυο φορές κάνει έξοδο από την εφαρμογή.
OnOptionItemSelected dropdown Menu για exit και μετάβαση στην SettingsActivity.

SettingsActivity έχουμε υλοποιήσει το πεδίο για συμπλήρωση νέας διεύθυνσης Ιp, την επιλογή ήχου και την ένταση.

# JavaController
* **MyConsole Class**

Εκτυπώνουμε το Μενού με την PrintMenu και με η askUser ζητάει και παίρνει το μήνυμα από τον χρήστη.

* **ΜyValidator Class**

Έλεγχος έγκυρης η μή εντολής της χρήσης.

* **MyExecutor Class**

Παίρνει την εντολή που δίνει ο χρήστης, εμφανίζει το ανάλογο μήνυμα και βάζει σε μία ουρά. 

* **MyProducer Class**

Παράγει τυχαίες εντολές που θα μπουν στην ουρά ανα periodtime.

* **MyConsumer Class**

Καταναλώνει τα μηνύματα που έχουν μπει στην ουρά και μέσω το Mqtt  Broke και επικοινωνεί με την Android εφαρμογή. 

* **JavaController Class**
 

Δημιουργούμε Thread, οποία λαμβάνουν μηνύματα από την ουρά. Επιλέγουμε να δημιουργήσουμε 3 consumers για να δέχεται πολλές εντολές σε σύντομο χρόνο.

# Built with
* Intelij IDEA
* Android Studio 3.0

# Tested Enviroment
* Linux Ubuntu 17.04-17.10/Mac OS
* Android Devices:Samsung s6 Edge(7.0), Samsung j5(6.0)

# Screenshots 
![Projects/tree/master/Java%20Project](/JavaController1.jpg)
![Projects/tree/master/Java%20Project](/JavaController2.jpg)
![Projects/tree/master/Java%20Project](/JavaController3.jpg)
![Projects/tree/master/Java%20Project](/MainActivity.jpg)
![Projects/tree/master/Java%20Project](/SettingsActivity.jpg)

# Links
Result Settings Activity  https://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
 
Flashlight https://www.youtube.com/watch?v=GC0hZdiDhoA
 
 https://www.ssaurel.com/blog/create-a-torch-flashlight-application-for-android/
 
 VolumeBar https://stackoverflow.com/questions/10134338/using-seekbar-to-control-volume-in-android
 
 
