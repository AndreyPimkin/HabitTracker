package penza.it.habittracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {

    SharedPreferences sPref;
    final String SAVED_TEXT = "saved_text";

    private String checkOpen = "one";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        //loadText();


        Intent intent;
        if(checkOpen.equals("one")) {
            intent = new Intent(this, MainActivity.class);
        }
        else {
            intent = new Intent(this, SecondActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    void loadText() {
        sPref = getSharedPreferences("Checking", MODE_PRIVATE);
        checkOpen = sPref.getString(SAVED_TEXT, "");
    }
}