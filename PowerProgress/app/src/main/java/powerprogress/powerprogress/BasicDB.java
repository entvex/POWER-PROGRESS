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

    public void testReceiver(){

        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.child("Person").getChildren()) {
                    Log.d(TAG, "onDataChange: " + child.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        firebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        })

    }
}
