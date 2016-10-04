package powerprogress.powerprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static powerprogress.powerprogress.MagicStringsAreEvil.FireBaseProfile_KEY;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;

    EditText ett_age_profileActivity;

    TextView ttv_name_profileActivity;
    TextView ttv_email_profileActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Firebase services
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        //Finding UI
        Button btn_ok_profileActivity = (Button) findViewById(R.id.btn_ok_profileActivity);
        ttv_name_profileActivity = (TextView) findViewById(R.id.ttv_name_profileActivity);
        ttv_email_profileActivity = (TextView) findViewById(R.id.ttv_email_profileActivity);

        ett_age_profileActivity = (EditText) findViewById(R.id.ett_age_profileActivity);

        //query for existing user
        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if ( dataSnapshot.child(FireBaseProfile_KEY).child(firebaseAuth.getCurrentUser().getEmail().replace(".",",")).exists() ) {

                    //Load Profile
                    Profile userProfile = dataSnapshot.child(FireBaseProfile_KEY).child(firebaseAuth.getCurrentUser().getEmail().replace(".",",")).getValue(Profile.class);

/*                    ttv_name_profileActivity.setText(dataSnapshot.child(FireBaseProfile_KEY).child(firebaseAuth.getCurrentUser().getEmail().replace(".",",")).child("name").getValue().toString());
                    ttv_name_profileActivity.setText(dataSnapshot.child(FireBaseProfile_KEY).child(firebaseAuth.getCurrentUser().getEmail().replace(".",",")).child("age").getValue().toString());*/
                    int i = 1+1;
                }
                else {
                    ttv_name_profileActivity.setText(firebaseAuth.getCurrentUser().getDisplayName());
                    ttv_email_profileActivity.setText(firebaseAuth.getCurrentUser().getEmail());
                    ett_age_profileActivity.setText("18");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btn_ok_profileActivity.setOnClickListener(new View.OnClickListener() {
            //TODO DONT SAVE IF NO VALUES

            @Override
            public void onClick(View v) {
                //Save the profile in firebase
                Profile userProfile = new Profile();

                userProfile.setAge(ett_age_profileActivity.getText().toString());
                userProfile.setName(firebaseAuth.getCurrentUser().getDisplayName().toString());
                userProfile.setEmail(ttv_email_profileActivity.getText().toString());

                //Save To firebase
                firebaseDatabase.child(FireBaseProfile_KEY).child(firebaseAuth.getCurrentUser().getEmail().replace(".",",")).setValue(userProfile);
            }
        });
    }

    private void updateGUI(Profile profile)
    {

    }

}