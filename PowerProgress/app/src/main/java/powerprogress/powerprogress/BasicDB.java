package powerprogress.powerprogress;

import android.content.Context;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.FileReader;

import static android.content.ContentValues.TAG;

/**
 * Created by bagge on 03-10-2016.
 */

public class BasicDB {

    private static final String FIREBASE_URL = "https://power-progress.firebaseio.com/";

    private Firebase firebaseRef;

    public BasicDB(Context context)
    {
        Firebase.setAndroidContext(context);
        firebaseRef = new Firebase(FIREBASE_URL);
    }

    public void testInserter(String inputFolder,String inputText){
        firebaseRef.child(inputFolder).setValue(inputText);


    }

    public void insertUserProfile(Profile profile )
    {
        firebaseRef.child("Profile").child(profile.escapeEmail()).setValue(profile);
    }

    public Profile getUserProfile(final String email)
    {
        final Profile[] profile = new Profile[1];

        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.child("Profile").getChildren()) {
                    Profile post = postSnapshot.getValue(Profile.class);
                    if (post.getEmail().equals(email)){
                        System.out.println(post.getEmail() + " - " + post.getAge());
                        profile[0] = postSnapshot.getValue(Profile.class);
                        return;
                    }
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return profile[0];
    }
}