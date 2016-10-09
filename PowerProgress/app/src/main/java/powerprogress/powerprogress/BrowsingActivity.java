package powerprogress.powerprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static powerprogress.powerprogress.MagicStringsAreEvil.FireBaseSubmissions_KEY;

public class BrowsingActivity extends AppCompatActivity {

    //Firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing);

        //Firebase services
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        //Find UI
        final ListView ltv_listOfSubmitsions_BrowsingActivity = (ListView) findViewById(R.id.ltv_listOfSubmitsions_BrowsingActivity);

        //query for existing submissions
        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<UploadDTO> uploadDTOs = new ArrayList<UploadDTO>();

                for (DataSnapshot submission: dataSnapshot.child(FireBaseSubmissions_KEY).getChildren()) {
                    UploadDTO uploadDTO = submission.getValue(UploadDTO.class);
                    uploadDTOs.add(uploadDTO);
                }

                SubmissionsAdapter submissionsAdapter = new SubmissionsAdapter(getApplicationContext(),uploadDTOs);
                ltv_listOfSubmitsions_BrowsingActivity.setAdapter(submissionsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
