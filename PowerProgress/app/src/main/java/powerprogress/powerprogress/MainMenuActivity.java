package powerprogress.powerprogress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ImageView imageViewUpload;
    ImageView imageViewProfile;
    ImageView imageViewBrowse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        imageViewUpload = (ImageView)findViewById(R.id.imv_upload_mainmenuActivity);
        imageViewProfile = (ImageView)findViewById(R.id.imv_profile_mainmenuActivity);
        imageViewBrowse = (ImageView)findViewById(R.id.imv_search_mainmenuActivity);

        imageViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadIntent = new Intent(MainMenuActivity.this, UploadDataActivity.class);
                startActivity(uploadIntent);
            }
        });
        imageViewBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BrowsingIntent = new Intent(MainMenuActivity.this, BrowsingActivity.class);
                startActivity(BrowsingIntent);
            }
        });
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ProfileIntent = new Intent(MainMenuActivity.this, ProfileActivity.class);
                startActivity(ProfileIntent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {

            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }



/*        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        Profile profile = new Profile();
        profile.setName("ole");
        profile.setAge("32");
        profile.setEmail("ole@ole.dk");

        profile.setOptions( new ArrayList<String>() {{
            add("Lift");
            add("HeavyRock");
        }});

        firebaseDatabase.child("Profile").setValue(profile);*/



    }
}
