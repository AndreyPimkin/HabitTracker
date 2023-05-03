package penza.it.habittracker;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class SecondActivity extends AppCompatActivity {

    private FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Bundle arguments = getIntent().getExtras();
         if(arguments != null){
             String mail = arguments.getString("mail");
             String password = arguments.getString("password");
         }

         frameLayout = findViewById(R.id.frameMain);

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Переход назад невозможен", Toast.LENGTH_SHORT).show();
    }

    public void openHabit(View v) {
        HabitFragment habitFragment =  new HabitFragment();
        setNewFragment(habitFragment);
    }

    private void setNewFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameMain, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void openHistory(View v) {

    }

    public void openPop(View v) {

    }

    public void openPerson(View v) {

    }


}