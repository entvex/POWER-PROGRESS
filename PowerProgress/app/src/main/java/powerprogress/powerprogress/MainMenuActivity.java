package powerprogress.powerprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {
TextView helloTextTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        helloTextTest = (TextView)findViewById(R.id.test);

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
