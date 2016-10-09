package powerprogress.powerprogress;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import static powerprogress.powerprogress.MagicStringsAreEvil.FireBaseProfile_KEY;
import static powerprogress.powerprogress.MagicStringsAreEvil.option_BenchPress;
import static powerprogress.powerprogress.MagicStringsAreEvil.option_Deadlift;
import static powerprogress.powerprogress.MagicStringsAreEvil.option_OverheadPress;
import static powerprogress.powerprogress.MagicStringsAreEvil.option_Squat;
import static powerprogress.powerprogress.MagicStringsAreEvil.FireBaseSubmissions_KEY;
import static powerprogress.powerprogress.MagicStringsAreEvil.FireBaseStorage_URL;


public class ViewDataActivity extends AppCompatActivity {


    Boolean videoPlaying;
    TextView description;
    TextView titel;
    VideoView videoView;
    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;
    Uri currentVideo;
    UploadDTO submission;
    String videoKey;
    FirebaseStorage firebaseStorage;

    FloatingActionButton FABbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        description = (TextView) findViewById(R.id.txt_description_ViewDataActivity);
        titel = (TextView) findViewById(R.id.txt_titel_ViewDataActivity);
        videoView = (VideoView) findViewById(R.id.vvw_displayVideo_ViewDataActivity);
        FABbutton = (FloatingActionButton) findViewById(R.id.fab_option_ViewDataActivity);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        Intent intent = getIntent();
        videoKey = intent.getStringExtra("Submission");
        videoPlaying = false;


        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //this might need to sub . for , for name to work properly
                submission = dataSnapshot.child(FireBaseSubmissions_KEY).child(videoKey).getValue(UploadDTO.class);

                titel.setText(submission.getTitel());
                description.setText(submission.getDescription());

                //Time to Get the Video from the Database
                StorageReference storageReference = firebaseStorage.getReferenceFromUrl(FireBaseStorage_URL);

                storageReference.child(submission.getName().replace(",",".")).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        currentVideo = uri;

                        videoView.setVideoURI(currentVideo);
                        videoView.start();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });



            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                videoPlaying = !videoPlaying;

                if(videoPlaying) {
                    videoView.setVideoURI(currentVideo);
                    videoView.start();
                }else{
                    videoView.stopPlayback();
                }
            }
        });

    }
}
