package powerprogress.powerprogress;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

import static powerprogress.powerprogress.MagicStringsAreEvil.FireBaseProfile_KEY;

public class BackgroundNotificationService extends Service {

    private final IBinder iNotificationBinder = new NotificationBinder();

    ProfileDTO userProfile;
    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;

    Timer serviceTimer;
    int interval = 1; // minutes waiting between datachecks
    int timerInterval = 60*1000*interval;

    public BackgroundNotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userEmail = firebaseAuth.getCurrentUser().getEmail().replace(".",",");
                userProfile = dataSnapshot.child(FireBaseProfile_KEY).child(userEmail).getValue(ProfileDTO.class);
                Log.d("NotificationService",userProfile.getName() + " : " + userProfile.getEmail() );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        serviceTimer = new Timer();
        serviceTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("NotificationService", " run: getting data");
                new CheckForNewData().execute();
            }
        }, timerInterval);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        Log.d("NotificationService", "Binding");

        throw new UnsupportedOperationException("Not yet implemented");
    }


    public class NotificationBinder extends Binder {
        BackgroundNotificationService getService() {
            // Returns the instance of service so clients can call it's public methods
            Log.d("NotificationService ", "getService: return Service");
            return BackgroundNotificationService.this;
        }
    }

    private class CheckForNewData extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            Log.d("NotificationService", "CheckForNewData executed");

            return null;
        }

        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);
        }
    }

}
