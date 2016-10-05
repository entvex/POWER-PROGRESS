package powerprogress.powerprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static powerprogress.powerprogress.MagicStringsAreEvil.FireBaseProfile_KEY;

public class ProfileActivity extends AppCompatActivity {

    //Firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;

    //UI
    EditText ett_age_profileActivity;
    TextView ttv_name_profileActivity;
    TextView ttv_email_profileActivity;
    Button btn_ok_profileActivity;
    CheckBox ckb_deadlift_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Firebase services
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        //Finding UI
        btn_ok_profileActivity = (Button) findViewById(R.id.btn_ok_profileActivity);
        ttv_name_profileActivity = (TextView) findViewById(R.id.ttv_name_profileActivity);
        ttv_email_profileActivity = (TextView) findViewById(R.id.ttv_email_profileActivity);
        ett_age_profileActivity = (EditText) findViewById(R.id.ett_age_profileActivity);
        ckb_deadlift_profile = (CheckBox) findViewById(R.id.ckb_deadlift_profile);

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

        btn_ok_profileActivity.setOnClickListener(new View.OnClickListener() {
            //TODO DONT SAVE IF NO VALUES

            @Override
            public void onClick(View v) {
                //Save the profile in firebase


                if ( !ttv_email_profileActivity.getText().equals("") && !ett_age_profileActivity.getText().equals("") ) {
                    Profile userProfile = new Profile();

                    //User info
                    userProfile.setAge(ett_age_profileActivity.getText().toString());
                    userProfile.setName(firebaseAuth.getCurrentUser().getDisplayName().toString());
                    userProfile.setEmail(ttv_email_profileActivity.getText().toString());

                    //User options
                    if (ckb_deadlift_profile.isChecked())
                    {
                        //List<String> options = userProfile.getOptions();
                        //List<String> options = userProfile.getOptions().isEmpty() ? null : new ArrayList<String>();

                        //options.add("deadlift");
                        //userProfile.setOptions(options);
                    }

                    //Save To firebase
                    firebaseDatabase.child(FireBaseProfile_KEY).child(firebaseAuth.getCurrentUser().getEmail().replace(".", ",")).setValue(userProfile);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Fill out empty fields or wait for data to load",Toast.LENGTH_LONG);
                }
            }
        });
    }

    private void updateUI(DataSnapshot dataSnapshot)
    {
        String userEmail = firebaseAuth.getCurrentUser().getEmail().replace(".",",");
        Profile userProfile = dataSnapshot.child(FireBaseProfile_KEY).child(userEmail).getValue(Profile.class);
        ttv_name_profileActivity.setText(dataSnapshot.child(FireBaseProfile_KEY).child(userEmail).child("name").getValue().toString());
        ttv_email_profileActivity.setText(dataSnapshot.child(FireBaseProfile_KEY).child(userEmail).child("email").getValue().toString());

        if( dataSnapshot.child(FireBaseProfile_KEY).child(userEmail).child("age").exists())
        {
            ett_age_profileActivity.setText(dataSnapshot.child(FireBaseProfile_KEY).child(userEmail).child("age").getValue().toString());
        }
        else
        {
            ett_age_profileActivity.setText("18");
        }
    }

}