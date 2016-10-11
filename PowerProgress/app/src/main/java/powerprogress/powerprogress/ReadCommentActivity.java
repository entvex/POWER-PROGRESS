package powerprogress.powerprogress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static powerprogress.powerprogress.MagicStringsAreEvil.FireBaseSubmissions_KEY;
import static powerprogress.powerprogress.ProfileActivity.userProfileDTO;

public class ReadCommentActivity extends AppCompatActivity {


    //Firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;

    ListView ltv_listOfComments_CommentActivity;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_comment);

        //Firebase services
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();



        //Find UI
        ltv_listOfComments_CommentActivity = (ListView) findViewById(R.id.ltv_listOfComments_CommentActivity);




        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Intent intent = getIntent();
                String submissionIntentName = intent.getStringExtra("Submission");
                //TODO GET COMMENTS FROM FIREBASE and put them into the commentAdapters contructor.

                UploadDTO submission  = dataSnapshot.child(FireBaseSubmissions_KEY).child(submissionIntentName).getValue(UploadDTO.class);
                List<CommentDTO> commentDTOs = submission.getComments();
                if(commentDTOs != null) {
                    commentAdapter commentAdapter = new commentAdapter(getApplicationContext(), commentDTOs);
                    ltv_listOfComments_CommentActivity.setAdapter(commentAdapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
