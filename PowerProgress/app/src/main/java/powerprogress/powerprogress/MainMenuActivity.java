package powerprogress.powerprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainMenuActivity extends AppCompatActivity {
TextView helloTextTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        helloTextTest = (TextView)findViewById(R.id.test);

        BasicDB testSubject = new BasicDB(this);
        testSubject.testInserter("folder","hey");
        
        testSubject.testReceiver();
    }
}
