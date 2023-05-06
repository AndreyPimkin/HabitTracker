package penza.it.habittracker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class SecondActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    HabitFragment habitFragment =  new HabitFragment();
    private TextView textViewHabit, textViewHistory, textViewPop, textViewPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textViewHabit = findViewById(R.id.textHabit);
        textViewHistory = findViewById(R.id.textHistory);
        textViewPop = findViewById(R.id.textPop);
        textViewPerson = findViewById(R.id.textPerson);

        textViewHabit.setTextColor(Color.parseColor("#c8c8c8"));

        habitFragment =  new HabitFragment();
        setNewFragment(habitFragment);

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
        habitFragment =  new HabitFragment();
        setNewFragment(habitFragment);
    }

    private void setNewFragment(Fragment fragment) {
        textViewHabit.setTextColor(Color.parseColor("#c8c8c8"));
        textViewHistory.setTextColor(Color.parseColor("#ffffff"));
        textViewPop.setTextColor(Color.parseColor("#ffffff"));
        textViewPerson.setTextColor(Color.parseColor("#ffffff"));


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameMain, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void openHistory(View v) {
        textViewHabit.setTextColor(Color.parseColor("#ffffff"));
        textViewHistory.setTextColor(Color.parseColor("#c8c8c8"));
        textViewPop.setTextColor(Color.parseColor("#ffffff"));
        textViewPerson.setTextColor(Color.parseColor("#ffffff"));

    }

    public void openPop(View v) {

        textViewHabit.setTextColor(Color.parseColor("#ffffff"));
        textViewHistory.setTextColor(Color.parseColor("#ffffff"));
        textViewPop.setTextColor(Color.parseColor("#c8c8c8"));
        textViewPerson.setTextColor(Color.parseColor("#ffffff"));
    }

    public void openPerson(View v) {

        textViewHabit.setTextColor(Color.parseColor("#ffffff"));
        textViewHistory.setTextColor(Color.parseColor("#ffffff"));
        textViewPop.setTextColor(Color.parseColor("#ffffff"));
        textViewPerson.setTextColor(Color.parseColor("#c8c8c8"));

    }


}