package powerprogress.powerprogress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {
    TextView helloTextTest;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {

            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
        else
        {
            Intent TestIntent = new Intent(MainMenuActivity.this,UploadDataActivity.class);
            startActivity(TestIntent);
        }


        helloTextTest = (TextView)findViewById(R.id.test);


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
