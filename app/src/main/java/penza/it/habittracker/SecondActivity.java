package penza.it.habittracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private FrameLayout frameLayout;

    RelativeLayout root;
    HabitFragment habitFragment = new HabitFragment();
    HistoryFragment historyFragment = new HistoryFragment();

    private TextView textViewHabit, textViewHistory, textViewPop, textViewPerson;


    private ArrayList<String> articleList = new ArrayList<>(10);
    private ArrayList<String> imageList = new ArrayList<>(10);


    PopFragment popFragment;
    PersonFragment perFragment;

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Cursor cursor;

    ImageView historyImage, popImage;

    private boolean checkAuthorization = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textViewHabit = findViewById(R.id.textHabit);
        textViewHistory = findViewById(R.id.textHistory);
        textViewPop = findViewById(R.id.textPop);
        textViewPerson = findViewById(R.id.textPerson);
        historyImage = findViewById(R.id.imageHistory);
        popImage = findViewById(R.id.imagePop);


        textViewHabit.setTextColor(Color.parseColor("#c8c8c8"));

        root = findViewById(R.id.two_layout);

        habitFragment = new HabitFragment();
        setNewFragment(habitFragment);
        frameLayout = findViewById(R.id.frameMain);

        if(checkAuthorization){
            historyImage.setImageResource(R.drawable.history);
            popImage.setImageResource(R.drawable.pop);
            textViewHistory.setTextColor(Color.parseColor("#ffffff"));
            textViewPop.setTextColor(Color.parseColor("#ffffff"));
        }

        else{
            historyImage.setImageResource(R.drawable.lock);
            popImage.setImageResource(R.drawable.lock);
            textViewHistory.setTextColor(Color.parseColor("#81C8C8C8"));
            textViewPop.setTextColor(Color.parseColor("#81C8C8C8"));
        }

    }


    @Override
    public void onBackPressed() {
    }

    public void openHabit(View v) {
        textViewHabit.setTextColor(Color.parseColor("#c8c8c8"));
        textViewHistory.setTextColor(Color.parseColor("#ffffff"));
        textViewPop.setTextColor(Color.parseColor("#ffffff"));
        textViewPerson.setTextColor(Color.parseColor("#ffffff"));

        habitFragment = new HabitFragment();
        setNewFragment(habitFragment);
    }

    private void setNewFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameMain, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void openHistory(View v) {
        if(checkAuthorization){
            textViewHabit.setTextColor(Color.parseColor("#ffffff"));
            textViewHistory.setTextColor(Color.parseColor("#c8c8c8"));
            textViewPop.setTextColor(Color.parseColor("#ffffff"));
            textViewPerson.setTextColor(Color.parseColor("#ffffff"));
            habitFragment = new HabitFragment();
            setNewFragment(historyFragment);

        }
        else {
            Snackbar.make(root, "Эта функция доступна только авторизованным пользователям", Snackbar.LENGTH_SHORT).show();
        }



    }

    public void openPop(View v) {

        if(checkAuthorization){
            textViewHabit.setTextColor(Color.parseColor("#ffffff"));
            textViewHistory.setTextColor(Color.parseColor("#ffffff"));
            textViewPop.setTextColor(Color.parseColor("#c8c8c8"));
            textViewPerson.setTextColor(Color.parseColor("#ffffff"));

            popFragment = new PopFragment();
            setNewFragment(popFragment);
        }
        else{
            Snackbar.make(root, "Эта функция доступна только авторизованным пользователям", Snackbar.LENGTH_SHORT).show();
        }

    }

    public void openPerson(View v) {

        textViewHabit.setTextColor(Color.parseColor("#ffffff"));
        textViewHistory.setTextColor(Color.parseColor("#ffffff"));
        textViewPop.setTextColor(Color.parseColor("#ffffff"));
        textViewPerson.setTextColor(Color.parseColor("#c8c8c8"));

        perFragment = new PersonFragment();
        setNewFragment(perFragment);

    }

    public void openCreateWindow(View view) {
        Intent intent = new Intent(this, ChoiceCategoryActivity.class);
        startActivity(intent);
        finish();
    }


}