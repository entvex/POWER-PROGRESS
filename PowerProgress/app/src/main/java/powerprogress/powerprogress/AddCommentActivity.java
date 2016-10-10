package powerprogress.powerprogress;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCommentActivity extends AppCompatActivity {

    Button submitBtn;
    EditText comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        submitBtn = (Button)findViewById(R.id.btn_submitComment_AddCommentActivity);
        comment = (EditText)findViewById(R.id.edt_comment_AddCommentActivity);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(comment.getText().toString().length() > 10) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("comment", comment.getText().toString());
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
                else
                {
                    Toast.makeText(v.getContext(),"Please Type more than 10 characters", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}
