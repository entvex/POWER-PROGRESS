package powerprogress.powerprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;

    EditText ett_name_profileActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Firebase services
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        //Finding UI
        Button btn_ok_profileActivity =(Button)findViewById(R.id.btn_ok_profileActivity);

        ett_name_profileActivity = (EditText)findViewById(R.id.ett_name_profileActivity);
        EditText ett_email_profileActivity;
        ett_email_profileActivity = (EditText)findViewById(R.id.ett_email_profileActivity);
        EditText ett_age_profileActivity;
        ett_age_profileActivity = (EditText)findViewById(R.id.ett_age_profileActivity);

        ett_name_profileActivity.setText("Wait for it");

        //query for existing user
        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if ( dataSnapshot.child("Profile").child("entvex@gmail,com").exists() ) {
                    ett_name_profileActivity.setText(dataSnapshot.child("Profile").child(firebaseAuth.getCurrentUser().getEmail().replace(".",",")).child("name").getValue().toString());
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}