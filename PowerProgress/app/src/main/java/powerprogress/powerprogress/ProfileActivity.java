package powerprogress.powerprogress;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static powerprogress.powerprogress.MagicStringsAreEvil.FireBaseProfile_KEY;
import static powerprogress.powerprogress.MagicStringsAreEvil.option_BenchPress;
import static powerprogress.powerprogress.MagicStringsAreEvil.option_Deadlift;
import static powerprogress.powerprogress.MagicStringsAreEvil.option_OverheadPress;
import static powerprogress.powerprogress.MagicStringsAreEvil.option_Squat;

public class ProfileActivity extends AppCompatActivity {

    //Firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;

    //UI
    EditText ett_age_profileActivity;
    TextView ttv_name_profileActivity;
    TextView ttv_email_profileActivity;
    Button btn_ok_profileActivity;

    CheckBox ckb_deadlift_profileActivity;
    CheckBox ckb_BenchPress_profileActivity;
    CheckBox ckb_overheadpress_profileActivity;
    CheckBox ckb_Squat_profileActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Firebase services
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        //Finding UI
        btn_ok_profileActivity = (Button) findViewById(R.id.btn_ok_profileActivity);

        ImageView imv_profilePhoto_ProfileActivity = (ImageView) findViewById(R.id.imv_profilePhoto_ProfileActivity);

        ttv_name_profileActivity = (TextView) findViewById(R.id.ttv_name_profileActivity);
        ttv_email_profileActivity = (TextView) findViewById(R.id.ttv_email_profileActivity);
        ett_age_profileActivity = (EditText) findViewById(R.id.ett_age_profileActivity);

        ckb_deadlift_profileActivity = (CheckBox) findViewById(R.id.ckb_deadlift_profileActivity);
        ckb_BenchPress_profileActivity = (CheckBox) findViewById(R.id.ckb_BenchPress_profileActivity);
        ckb_overheadpress_profileActivity = (CheckBox) findViewById(R.id.ckb_overheadpress_profileActivity);
        ckb_Squat_profileActivity = (CheckBox) findViewById(R.id.ckb_Squat_profileActivity);


        //query for existing user
        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateUI(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"databaseError",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        //Load image using Picasso
        if (firebaseAuth.getCurrentUser().getPhotoUrl() != null)
        {
            Picasso.with(getApplicationContext())
                    .load(firebaseAuth.getCurrentUser().getPhotoUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imv_profilePhoto_ProfileActivity);
        }

        btn_ok_profileActivity.setOnClickListener(new View.OnClickListener() {
            //TODO DONT SAVE IF NO VALUES

            @Override
            public void onClick(View v) {
                //Save the profile in firebase

                if ( !ttv_email_profileActivity.getText().equals("") && !ett_age_profileActivity.getText().equals("") ) {
                    ProfileDTO userProfileDTO = getFilledProfile();

                    //Save To firebase
                    firebaseDatabase.child(FireBaseProfile_KEY).child(firebaseAuth.getCurrentUser().getEmail().replace(".", ",")).setValue(userProfileDTO);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.profileWaitWhileLoadingOrHaveNoEmptyFields,Toast.LENGTH_LONG);
                }
            }
        });
    }

    @NonNull
    private ProfileDTO getFilledProfile() {
        ProfileDTO userProfileDTO = new ProfileDTO();

        //User info
        userProfileDTO.setAge(ett_age_profileActivity.getText().toString());
        userProfileDTO.setName(firebaseAuth.getCurrentUser().getDisplayName().toString());
        userProfileDTO.setEmail(ttv_email_profileActivity.getText().toString());

        List<String> options = (userProfileDTO.getOptions() == null ? new ArrayList<String>() : userProfileDTO.getOptions());
        //User options
        if (ckb_deadlift_profileActivity.isChecked()) {
            options.add(option_Deadlift);
            userProfileDTO.setOptions(options);
        }
        else {
            options.remove(option_Deadlift);
        }

        if (ckb_BenchPress_profileActivity.isChecked()) {
            options.add(option_BenchPress);
            userProfileDTO.setOptions(options);
        }
        else {
            options.remove(option_BenchPress);
        }

        if (ckb_overheadpress_profileActivity.isChecked()) {
            options.add(option_OverheadPress);
            userProfileDTO.setOptions(options);
        }
        else {
            options.remove(option_OverheadPress);
        }

        if (ckb_Squat_profileActivity.isChecked()) {
            options.add(option_Squat);
            userProfileDTO.setOptions(options);
        }
        else {
            options.remove(option_Squat);
        }

        return userProfileDTO;
    }

    private void updateUI(DataSnapshot dataSnapshot)
    {
        String userEmail = firebaseAuth.getCurrentUser().getEmail().replace(".",",");
        ProfileDTO userProfileDTO = dataSnapshot.child(FireBaseProfile_KEY).child(userEmail).getValue(ProfileDTO.class);
        ttv_name_profileActivity.setText(dataSnapshot.child(FireBaseProfile_KEY).child(userEmail).child("name").getValue().toString());
        ttv_email_profileActivity.setText(dataSnapshot.child(FireBaseProfile_KEY).child(userEmail).child("email").getValue().toString());

        if( dataSnapshot.child(FireBaseProfile_KEY).child(userEmail).child("age").exists()) {
            ett_age_profileActivity.setText(dataSnapshot.child(FireBaseProfile_KEY).child(userEmail).child("age").getValue().toString());
        }
        else {
            ett_age_profileActivity.setText("18");
        }

        if (userProfileDTO.getOptions() != null) {
            if (userProfileDTO.getOptions().contains(option_Deadlift))
                ckb_deadlift_profileActivity.setChecked(true);

            if (userProfileDTO.getOptions().contains(option_BenchPress))
                ckb_BenchPress_profileActivity.setChecked(true);

            if (userProfileDTO.getOptions().contains(option_OverheadPress))
                ckb_overheadpress_profileActivity.setChecked(true);

            if (userProfileDTO.getOptions().contains(option_Squat))
                ckb_Squat_profileActivity.setChecked(true);
        }
    }

}