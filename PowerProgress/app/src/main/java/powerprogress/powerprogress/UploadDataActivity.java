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
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadDataActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO = 1;
    VideoView videoView;
    Button getVideoBtn;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);
        videoView = (VideoView) findViewById(R.id.vvw_displayVideo_UploadDataActivity);

        getVideoBtn = (Button)findViewById(R.id.btn_getVideo_UploadDataActivity);

        getVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //getVideoFromCamera();
                StreamVideo();
            }

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
            Uri video = intent.getData();
            //here we handle what to do with the video.

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://power-progress.appspot.com");

            StorageReference videoUpload = storageReference.child("video.mp4");
            UploadTask uploadTask = videoUpload.putFile(video);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("upload", "onFailure: ",e );
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("upload", "onSuccess: " );
                }
            });


            //temp ----------------------
            videoView.setVideoURI(video);
            videoView.start();
            //temp end ---------------------
        }
    }
}
