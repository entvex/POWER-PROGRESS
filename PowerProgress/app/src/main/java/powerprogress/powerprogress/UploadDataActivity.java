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
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadDataActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO = 1;
    VideoView videoView;

    Button getVideoBtn;
    Button submitDataBtn;

    ProgressBar progressBar;
    ProgressBar progressBarHor;

    FirebaseAuth firebaseAuth;
    Uri currentVideo;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);
        videoView = (VideoView) findViewById(R.id.vvw_displayVideo_UploadDataActivity);

        getVideoBtn = (Button)findViewById(R.id.btn_getVideo_UploadDataActivity);
        submitDataBtn = (Button)findViewById(R.id.btn_submit_UploadDataActivity);

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

                UploadData();
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

    public void UploadData() {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://power-progress.appspot.com");

        //fetch instance of firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //set child document to users name + identifier
        StorageReference videoUpload = storageReference.child(firebaseAuth.getCurrentUser().getEmail() + "-0001");
        UploadTask uploadTask = videoUpload.putFile(currentVideo);

        Toast.makeText(this, "Submitting... We'll be done in a jiffy!", Toast.LENGTH_LONG).show();

        getVideoBtn.setVisibility(View.GONE);
        submitDataBtn.setVisibility(View.GONE);
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
