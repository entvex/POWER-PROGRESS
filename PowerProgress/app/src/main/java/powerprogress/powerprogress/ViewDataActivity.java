package powerprogress.powerprogress;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
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


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static powerprogress.powerprogress.MagicStringsAreEvil.FireBaseProfile_KEY;
import static powerprogress.powerprogress.MagicStringsAreEvil.option_BenchPress;
import static powerprogress.powerprogress.MagicStringsAreEvil.option_Deadlift;
import static powerprogress.powerprogress.MagicStringsAreEvil.option_OverheadPress;
import static powerprogress.powerprogress.MagicStringsAreEvil.option_Squat;
import static powerprogress.powerprogress.MagicStringsAreEvil.FireBaseSubmissions_KEY;
import static powerprogress.powerprogress.MagicStringsAreEvil.FireBaseStorage_URL;


public class ViewDataActivity extends AppCompatActivity {

    static final int ADD_COMMENT_REQUEST = 999;

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

    FloatingActionButton FABbuttonOptionAdd;
    FloatingActionButton FABbuttonOptionRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        description = (TextView) findViewById(R.id.txt_description_ViewDataActivity);
        titel = (TextView) findViewById(R.id.txt_titel_ViewDataActivity);
        videoView = (VideoView) findViewById(R.id.vvw_displayVideo_ViewDataActivity);
        FABbutton = (FloatingActionButton) findViewById(R.id.fab_option_ViewDataActivity);
        FABbuttonOptionAdd = (FloatingActionButton) findViewById(R.id.fab_addComment_ViewDataActivity);
        FABbuttonOptionRead = (FloatingActionButton) findViewById(R.id.fab_seeComment_ViewDataActivity);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        Intent intent = getIntent();
        videoKey = intent.getStringExtra("Submission");

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
                        Log.d("ViewDataActivity", "Couldn't get the video URL: " + exception);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        FABbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FABbuttonOptionAdd.getVisibility() == View.GONE)
                {

                    FABbuttonOptionAdd.setVisibility(View.VISIBLE);
                    FABbuttonOptionRead.setVisibility(View.VISIBLE);

                }
                else
                {
                    FABbuttonOptionAdd.setVisibility(View.GONE);
                    FABbuttonOptionRead.setVisibility(View.GONE);
                }
            }
        });



        FABbuttonOptionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Onclick","ADDCOMMENT");
                Intent addCommentIntent = new Intent(v.getContext(),AddCommentActivity.class);
                startActivityForResult(addCommentIntent,ADD_COMMENT_REQUEST);

            }
        });

        FABbuttonOptionRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Onclick","READCOMMENTS");
                Intent ReadIntent = new Intent(v.getContext(),ReadCommentActivity.class);
                ReadIntent.putExtra("Submission", submission.getName());
                startActivity(ReadIntent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {

        if (requestCode == ADD_COMMENT_REQUEST) {

            if (resultCode == RESULT_OK) {




                CommentDTO commentDTO = new CommentDTO();
                commentDTO.setAuthor(firebaseAuth.getCurrentUser().getDisplayName());
                commentDTO.setVotes(0);
                commentDTO.setTimestamp(new SimpleDateFormat("dd-MM-yy HH:mm").format(Calendar.getInstance().getTime()));
                commentDTO.setComment(resultIntent.getStringExtra("comment"));

                List<CommentDTO> commentDTOs = submission.getComments();
                if (commentDTOs == null) {
                    commentDTOs = new ArrayList<>();
                }
                commentDTOs.add(commentDTO);

                submission.setComments(commentDTOs);

                firebaseDatabase.child(FireBaseSubmissions_KEY).child(submission.getName()).setValue(submission);


                Toast.makeText(this, getString(R.string.comment_hintSucess),Toast.LENGTH_LONG).show();
                Log.d("Intent result","got result OK from Intent ADD_COMMENT");
            }
        }
    }
}
