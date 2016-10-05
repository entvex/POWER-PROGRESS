package powerprogress.powerprogress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static powerprogress.powerprogress.MagicStringsAreEvil.FireBaseProfile_KEY;
import static powerprogress.powerprogress.R.id.ttv_email_profileActivity;

public class MainMenuActivity extends AppCompatActivity {

    static final int LOGIN_REQUEST_CODE = 100;  // The request code

    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;
    ImageView imageViewUpload;
    ImageView imageViewProfile;
    ImageView imageViewBrowse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

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

        if (firebaseAuth.getCurrentUser() == null) {

            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent,LOGIN_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //The users should now be logined in therefor get getCurrentUser.
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        if (requestCode == LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            createProfile();
        }
    }

    private void createProfile()
    {
        //Save the profile in firebase
        Profile userProfile = new Profile();

        userProfile.setName(firebaseAuth.getCurrentUser().getDisplayName());
        userProfile.setEmail(firebaseAuth.getCurrentUser().getEmail());

        firebaseDatabase.child(FireBaseProfile_KEY).child(firebaseAuth.getCurrentUser().getEmail().replace(".",",")).setValue(userProfile);
    }
}