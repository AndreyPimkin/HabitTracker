package penza.it.habittracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Bundle arguments = getIntent().getExtras();
         if(arguments != null){
             String mail = arguments.getString("mail");
             String password = arguments.getString("password");
         }


    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Переход назад невозможен", Toast.LENGTH_SHORT).show();
    }

    public void openHabit(View v) {

    }

    public void openHistory(View v) {

    }

    public void openPop(View v) {

    }

    public void openPerson(View v) {

    }


}