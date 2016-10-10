package powerprogress.powerprogress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static powerprogress.powerprogress.MagicStringsAreEvil.FireBaseProfile_KEY;
import static powerprogress.powerprogress.MagicStringsAreEvil.FireBaseSubmissions_KEY;
import static powerprogress.powerprogress.ProfileActivity.userProfileDTO;

public class BrowsingActivity extends AppCompatActivity {

    //Firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;

    List<UploadDTO> uploadDTOs;
    ListView ltv_listOfSubmitsions_BrowsingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing);

        //Firebase services
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        //Find UI
        ltv_listOfSubmitsions_BrowsingActivity = (ListView) findViewById(R.id.ltv_listOfSubmitsions_BrowsingActivity);

        //query for existing submissions
        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //FindUsers preferences
                userProfileDTO = dataSnapshot.child(FireBaseProfile_KEY).child(firebaseAuth.getCurrentUser().getEmail().replace(".",",")).getValue(ProfileDTO.class);

                uploadDTOs = new ArrayList<UploadDTO>();

                //Loop tho all submissions and only keep the once that are interresting for the user
                for (DataSnapshot submission: dataSnapshot.child(FireBaseSubmissions_KEY).getChildren()) {

                    UploadDTO uploadDTO = submission.getValue(UploadDTO.class);

                    if (userProfileDTO.getOptions() != null) {

                        for (String option : userProfileDTO.getOptions()) {
                            if (uploadDTO.getOptions().contains(option)) {
                                uploadDTOs.add(uploadDTO);
                                break;
                            }
                        }
                    }
                }
                SubmissionsAdapter submissionsAdapter = new SubmissionsAdapter(getApplicationContext(),uploadDTOs);
                ltv_listOfSubmitsions_BrowsingActivity.setAdapter(submissionsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        ltv_listOfSubmitsions_BrowsingActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                UploadDTO uploadDTO = uploadDTOs.get(position);
                Intent viewDataIntent = new Intent(view.getContext(), ViewDataActivity.class);
                viewDataIntent.putExtra("Submission", uploadDTO.getName());
                Log.d("Intent","Start new Intent for showing submission");
                startActivity(viewDataIntent);
            }
        });
    }
}
