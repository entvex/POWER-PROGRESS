package powerprogress.powerprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //BasicDB basicDB = new BasicDB(this);
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        Profile profile = new Profile();
        profile.setName("ole");
        profile.setAge("32");
        profile.setEmail("ole@ole.dk");

        profile.setOptions( new ArrayList<String>() {{
            add("Lift");
            add("HeavyRock");
        }});

        firebaseDatabase.child("Profile").setValue(profile);
    }
}