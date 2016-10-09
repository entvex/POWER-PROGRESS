package powerprogress.powerprogress;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static powerprogress.powerprogress.MagicStringsAreEvil.FireBaseProfile_KEY;

public class BackgroundNotificationService extends Service {

    private final IBinder iNotificationBinder = new NotificationBinder();

    ProfileDTO userProfile;
    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;

    List<String> oldUploads, newUploads;

    Timer serviceTimer;
    int interval = 1; // minutes waiting between datachecks
    int timerInterval = 60 * 1000 * interval;

    NotificationManager commentManager;
    boolean commentManagerActive = false;
    int commentManagerID = 10;

    public BackgroundNotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Firebase setup
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        // Setup the timer to check for new comments
        serviceTimer = new Timer();
        serviceTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("NotificationService", "Started Notification Timer");
                new CheckForNewData().execute();
            }
        }, timerInterval);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        Log.d("NotificationService", "Binding");
        return iNotificationBinder;
    }


    public class NotificationBinder extends Binder {
        BackgroundNotificationService getService() {
            // Returns the instance of service so clients can call it's public methods
            Log.d("NotificationService ", "getService: return Service");
            return BackgroundNotificationService.this;
        }
    }

    // Check for new comments
    private class CheckForNewData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.d("NotificationService", "CheckForNewData executed");

            firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String userEmail = firebaseAuth.getCurrentUser().getEmail().replace(".", ",");
                    userProfile = dataSnapshot.child(FireBaseProfile_KEY).child(userEmail).getValue(ProfileDTO.class);
                    Log.d("NotificationService", userProfile.getName() + " : " + userProfile.getEmail());

                    newUploads = userProfile.getUploads();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            if (oldUploads != null) {
                for (int i = 0; i < newUploads.size(); i++) {

                    // TODO Iterate through uploads and check if there is a new comment

                    int newComments = 2; //TODO remove when tested

                    if (newComments > 0) {

                    }
                }
            }
            oldUploads = newUploads;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

    }
}
