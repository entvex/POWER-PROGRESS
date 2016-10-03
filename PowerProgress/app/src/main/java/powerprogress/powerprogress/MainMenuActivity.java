package powerprogress.powerprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {
TextView helloTextTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        helloTextTest = (TextView)findViewById(R.id.test);

        BasicDB basicDB = new BasicDB(this);

/*        Profile profile = new Profile("ss@ss.dk","Ole","21");
        profile.setOptions( new ArrayList<String>() {{
            add("Lift");
            add("HeavyRock");
        }});

        basicDB.insertUserProfile(profile);
        //testSubject.insertUserProfile(profile);

        profile = new Profile("ss@kk.dk", "jens", "41");
        profile.setOptions( new ArrayList<String>() {{
            add("Lift");
            add("HeavyRock");
        }});*/

        //testSubject.insertUserProfile(profile);

        //basicDB.insertUserProfile(profile);



        Profile profile = basicDB.getUserProfile("ss@kk.dk");


    }
}
