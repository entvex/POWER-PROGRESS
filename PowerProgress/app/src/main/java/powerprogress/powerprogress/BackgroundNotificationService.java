package powerprogress.powerprogress;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static powerprogress.powerprogress.MagicStringsAreEvil.FireBaseProfile_KEY;
import static powerprogress.powerprogress.MagicStringsAreEvil.FireBaseSubmissions_KEY;
import static powerprogress.powerprogress.MagicStringsAreEvil.sharedPreferences_Notifications;

public class BackgroundNotificationService extends Service {

    private final IBinder iServiceBinder = new ServiceBinder();

    List<String> uploads;
    List<UploadDTO> oldUploadDTOs;
    List<UploadDTO> newUploadDTOs;
    ProfileDTO userProfile;
    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;

    TimerTask serviceTimerTask;
    Timer serviceTimer;
    int interval = 1; // minutes waiting between datachecks
    int timerInterval = 60 * 1000 * interval;

    NotificationManager commentManager;
    boolean commentManagerActive = false;
    int commentManagerID = 10;

    SharedPreferences sharedPreferences;

    public BackgroundNotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("NotificationService", "Service Created");

        sharedPreferences = getSharedPreferences(sharedPreferences_Notifications, MODE_PRIVATE);

        uploads = new ArrayList<String>();
        newUploadDTOs = new ArrayList<UploadDTO>();
        oldUploadDTOs = new ArrayList<UploadDTO>();

        // Firebase setup
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        StartTimer(timerInterval);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        Log.d("NotificationService", "Binding");
        return iServiceBinder;
    }

    public class ServiceBinder extends Binder {
        BackgroundNotificationService getService() {
            // Returns the instance of service so clients can call it's public methods
            Log.d("NotificationService ", "getService: return Service");
            return BackgroundNotificationService.this;
        }
    }

    private void StartTimer(int intervalUpdate) {
        if(serviceTimer != null){
            serviceTimer.cancel();
        }
        // Setup the timer to check for new comments
        final Handler handler = new Handler();
        serviceTimer = new Timer();
        serviceTimerTask = new TimerTask() {
            @Override
            public void run() {

                handler.post(new Runnable() {

                    public void run() {

                        try {
                            CheckForNewData performBackgroundTask = new CheckForNewData();
                            performBackgroundTask.execute();
                        }
                        catch (Exception e) {

                        }
                    }
                });
            }
        };
        serviceTimer.schedule(serviceTimerTask, 0, intervalUpdate); //execute in every 50000 ms

    }

    private class CheckForNewData extends AsyncTask<Void, Void, Void> {
        // Check for new comments

        @Override
        protected Void doInBackground(Void... params) {
            Log.d("NotificationService", "CheckForNewData executing");
            if (firebaseAuth.getCurrentUser() != null) {
                firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userEmail = firebaseAuth.getCurrentUser().getEmail().replace(".", ",");

                        userProfile = dataSnapshot.child(FireBaseProfile_KEY).child(userEmail).getValue(ProfileDTO.class);

                        uploads = userProfile.getUploads();

                        if (uploads != null && uploads.size() > 0) {
                            newUploadDTOs = new ArrayList<UploadDTO>();
                            for (int i = 0; i < uploads.size(); i++) {

                                String uploadName = uploads.get(i);
                                newUploadDTOs.add(dataSnapshot.child(FireBaseSubmissions_KEY).child(uploadName).getValue(UploadDTO.class));
                            }

                            List<String> uploadNames = new ArrayList<String>();

                            if (!oldUploadDTOs.isEmpty() && !newUploadDTOs.isEmpty() && oldUploadDTOs != null && newUploadDTOs != null) {

                                for (int i = 0; i < newUploadDTOs.size(); i++) {

                                    if (oldUploadDTOs.size() > i) {

                                        if (newUploadDTOs.get(i).getComments() != null) {

                                            if (oldUploadDTOs.get(i).getComments() == null)
                                            {
                                                oldUploadDTOs.get(i).setComments(new ArrayList<CommentDTO>());
                                            }
                                            if (newUploadDTOs.get(i).getComments().size() > oldUploadDTOs.get(i).getComments().size()) {

                                                uploadNames.add(newUploadDTOs.get(i).getTitel());
                                            }
                                        }
                                    }
                                }
                            }

                            if (uploadNames.size() > 0) {
                                StartNotification(uploadNames);
                            }

                            oldUploadDTOs = newUploadDTOs;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Log.d("NotificationService", "CheckForNewData executed");
                return null;
            }
            else{

                Log.d("NotificationService", "User not logged in");
                return null;
            }
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    public void StartNotification(List<String> uploadNames){

        if(sharedPreferences.getBoolean(sharedPreferences_Notifications,true)) {
            Log.d("NotificationService", "StartNotification executing");
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this)
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText(uploadNames + getString(R.string.Notification_text))
                            .setTicker(getString(R.string.Notification_newComment))
                            .setSmallIcon(R.drawable.ppicon)
                            .setAutoCancel(true)
                            .setVibrate(new long[]{500, 500});

            Intent mainMenuIntent = new Intent(this, MainMenuActivity.class);

            TaskStackBuilder tStackBuilder = TaskStackBuilder.create(this);
            tStackBuilder.addParentStack(MainMenuActivity.class);
            tStackBuilder.addNextIntent(mainMenuIntent);

            PendingIntent pendingIntent = tStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);

            commentManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            commentManager.notify(commentManagerID, notificationBuilder.build());
            commentManagerActive = true;
        }
        else{
            Log.d("NotificationService", "StartNotification disabled");
        }
    }

    // Not in use
    public void StopNotification(){
        if (commentManagerActive){

            commentManager.cancel(commentManagerID);
        }
    }
}
