package penza.it.habittracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {

    SharedPreferences sPref;

    final String SAVED_TEXT = "setting_user";
    private String checkOpen = "no_used";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        loadText();

        Intent intent;


        if(checkOpen.equals("no_used")) {   // Если пользователь не использовал приложение
            intent = new Intent(this, MainActivity.class);
        }
        else {       // Если пользователь уже использовал приложение
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