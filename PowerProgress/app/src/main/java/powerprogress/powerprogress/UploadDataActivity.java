package powerprogress.powerprogress;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;
import static powerprogress.powerprogress.MagicStringsAreEvil.FireBaseProfile_KEY;
import static powerprogress.powerprogress.MagicStringsAreEvil.option_BenchPress;
import static powerprogress.powerprogress.MagicStringsAreEvil.option_Deadlift;
import static powerprogress.powerprogress.MagicStringsAreEvil.option_OverheadPress;
import static powerprogress.powerprogress.MagicStringsAreEvil.option_Squat;

public class UploadDataActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO = 1;
    VideoView videoView;

    ProfileDTO userProfile;
    Button getVideoBtn;
    Button submitDataBtn;
    Button replayVideoBtn;
    EditText editComment;

    ProgressBar progressBar;
    ProgressBar progressBarHor;

    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;
    Uri currentVideo;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);
        videoView = (VideoView) findViewById(R.id.vvw_displayVideo_UploadDataActivity);

        getVideoBtn = (Button)findViewById(R.id.btn_getVideo_UploadDataActivity);
        editComment = (EditText)findViewById(R.id.edt_comment_UploadDataActivity);
        submitDataBtn = (Button)findViewById(R.id.btn_submit_UploadDataActivity);
        replayVideoBtn = (Button)findViewById(R.id.btn_replayVideo_UploadDataActivity);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        progressBar = (ProgressBar) findViewById(R.id.pgb_upload_UploadDataActivty);
        progressBar.setVisibility(View.GONE);
        progressBarHor = (ProgressBar) findViewById(R.id.pgb_uploadH_UploadDataActivity);
        progressBarHor.setVisibility(View.GONE);


        getVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //getVideoFromCamera();
                getVideoFromCamera();
            }




            /*
            private void StreamVideo() {
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://power-progress.appspot.com");

                storageReference.child("video.mp4").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        videoView.setVideoURI(uri);
                        videoView.start();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }*/
        });



        submitDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentVideo != null) {
                    UploadData();
                }

            }
        });

        replayVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replayVideo();
            }
        });

        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userEmail = firebaseAuth.getCurrentUser().getEmail().replace(".",",");
                userProfile = dataSnapshot.child(FireBaseProfile_KEY).child(userEmail).getValue(ProfileDTO.class);
                Log.d("Profile",userProfile.getName() + " : " + userProfile.getEmail() );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





    private void getVideoFromCamera() {

        Intent recordVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        recordVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,30);
        startActivityForResult(recordVideoIntent, REQUEST_VIDEO);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO && resultCode == RESULT_OK) {

            //here we handle what to do with the video.
            currentVideo = intent.getData();


            //start the video in the view
            videoView.setVideoURI(currentVideo);
            videoView.start();

        }
    }

    private void replayVideo()
    {
        if(currentVideo != null)
        videoView.start();
    }

    public void UploadData() {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://power-progress.appspot.com");

        //fetch instance of firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        int nextUploadNumber = 0;

        List<UploadDTO> uploads = userProfile.getUploads();
        if(uploads != null) {
            Log.d("Upload amount", " found" + uploads.size() + "previous uploads");
            nextUploadNumber = uploads.size();
        }
        else {
            Log.d("Upload amount", " no previous uploads");
            uploads = new ArrayList<UploadDTO>();
        }
        String uploadname= firebaseAuth.getCurrentUser().getEmail().replace(".", ",") + "-"+ nextUploadNumber;
        UploadDTO newSubmission = new UploadDTO();
        newSubmission.setName(uploadname);
        newSubmission.setDescription(editComment.getText().toString());
        newSubmission.setTitel("this is the titel");
        Toast.makeText(this,newSubmission.getDate(),Toast.LENGTH_LONG);

        firebaseDatabase.child("Submissions").child(uploadname).setValue(newSubmission);
        uploads.add(newSubmission);
        userProfile.setUploads(uploads);

        //set child document to users name + identifier
        StorageReference videoUpload = storageReference.child(firebaseAuth.getCurrentUser().getEmail() + "-"+ nextUploadNumber);
        UploadTask uploadTask = videoUpload.putFile(currentVideo);

        Toast.makeText(this, "Submitting... We'll be done in a jiffy!", Toast.LENGTH_LONG).show();

        getVideoBtn.setVisibility(View.GONE);
        submitDataBtn.setVisibility(View.GONE);
        replayVideoBtn.setVisibility(View.GONE);
        editComment.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        progressBarHor.setVisibility(View.VISIBLE);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("upload", "onFailure: ", e);
            }
        });

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("upload", "onSuccess: ");

                if ( !firebaseAuth.getCurrentUser().equals(null) ) {


                    //Save To firebase
                    firebaseDatabase.child("Profile").child(firebaseAuth.getCurrentUser().getEmail().replace(".", ",")).setValue(userProfile);

                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.profileWaitWhileLoadingOrHaveNoEmptyFields,Toast.LENGTH_LONG);
                }

                finish();
            }
        });

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int)progress);
                progressBarHor.setProgress((int)progress);
                Log.d("Upload status", progress + "%");
            }

        });
    }






    }
