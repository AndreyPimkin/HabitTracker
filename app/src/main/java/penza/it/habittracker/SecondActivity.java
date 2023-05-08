package penza.it.habittracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class SecondActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    HabitFragment habitFragment =  new HabitFragment();
    HistoryFragment historyFragment = new HistoryFragment();

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
        frameLayout = findViewById(R.id.frameMain);

    }

    @Override
    public void onBackPressed() {
    }

    public void openHabit(View v) {
        textViewHabit.setTextColor(Color.parseColor("#c8c8c8"));
        textViewHistory.setTextColor(Color.parseColor("#ffffff"));
        textViewPop.setTextColor(Color.parseColor("#ffffff"));
        textViewPerson.setTextColor(Color.parseColor("#ffffff"));

        habitFragment =  new HabitFragment();
        setNewFragment(habitFragment);
    }

    private void setNewFragment(Fragment fragment) {
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


        habitFragment =  new HabitFragment();
        setNewFragment(historyFragment);

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

    public void openCreateWindow(View view) {
        Intent intent = new Intent(this, ChoiceCategoryActivity.class);
        startActivity(intent);
        finish();
    }


}