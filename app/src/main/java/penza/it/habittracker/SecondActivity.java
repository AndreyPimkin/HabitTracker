package penza.it.habittracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Cursor cursor;
    ImageView historyImage, popImage;
    RelativeLayout root;
    HabitFragment habitFragment;
    HistoryFragment historyFragment;
    PopFragment popFragment;
    PersonFragment perFragment;
    PersonNoAuthoFragment personNoAuthoFragment;
    MainFragment mainFragment;
    SharedPreferences sPref;
    final String SAVED_TEXT = "setting_user";
    private TextView textViewHabit, textViewHistory, textViewPop, textViewPerson;
    private ArrayList<String> articleList = new ArrayList<>(10);
    private ArrayList<String> imageList = new ArrayList<>(10);
    private boolean checkAuthorization = false;
    private int idUser;
    private String mailUser;
    private String passwordUser;
    private int countHabit = 0;
    private boolean checklist = false, checkListTwo = false ;
    final String COUNT = "count";
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

        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }


        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
          //  mailUser = arguments.getString("mail");
          //  passwordUser = arguments.getString("password");
            checkAuthorization = arguments.getBoolean("checkAuthorization");
        }
        if (checkAuthorization) {
            idUser = arguments.getInt("idUser");
            cursor = mDb.rawQuery("SELECT * FROM list WHERE id_user = ? AND status = 'Создана'", new String[]{String.valueOf(idUser)});
            if(!cursor.isAfterLast()) {
                cursor.moveToFirst();
                checklist = true;
            }
            cursor = mDb.rawQuery("SELECT * FROM list_new WHERE id_user = ? AND status = 'Создана'", new String[]{String.valueOf(idUser)});

            if (!cursor.isAfterLast()){
                cursor.moveToFirst();
                checkListTwo = true;
            }

            cursor.close();
            historyImage.setImageResource(R.drawable.history);
            popImage.setImageResource(R.drawable.pop);
            textViewHistory.setTextColor(Color.parseColor("#ffffff"));
            textViewPop.setTextColor(Color.parseColor("#ffffff"));

            if(checklist | checkListTwo){
                Bundle bundle = new Bundle();
                bundle.putBoolean("checkAuthorization", checkAuthorization);
                bundle.putInt("idUser", idUser);
                bundle.putBoolean("checkListOne", checklist);
                bundle.putBoolean("checkListTwo", checkListTwo);
                mainFragment = new MainFragment();
                mainFragment.setArguments(bundle);
                setNewFragment(mainFragment);
            }
            else{
                habitFragment = new HabitFragment();
                setNewFragment(habitFragment);
            }
        }

        else {
            historyImage.setImageResource(R.drawable.lock);
            popImage.setImageResource(R.drawable.lock);
            textViewHistory.setTextColor(Color.parseColor("#81C8C8C8"));
            textViewPop.setTextColor(Color.parseColor("#81C8C8C8"));

            if(Integer.parseInt(loadText(COUNT)) > 0){
                Bundle bundle = new Bundle();
                bundle.putBoolean("checkAuthorization", checkAuthorization);
                mainFragment = new MainFragment();
                mainFragment.setArguments(bundle);
                setNewFragment(mainFragment);
            }
            else{
                habitFragment = new HabitFragment();
                setNewFragment(habitFragment);
            }
        }

    }

    // метод прохождения обучения
    private void training() {
        saveText("already_used");
    }

    void saveText(String check) {
        sPref = getSharedPreferences("Checking", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_TEXT, check);
        ed.apply();
    }

    private String loadText(String name) {
        String text;
        sPref = getSharedPreferences("Checking", Context.MODE_PRIVATE);
        text = sPref.getString(name, "");
        return text;
    }

    public void output(View view) {
        System.out.println(loadText("name_habit"));
    }

    public void openHabit(View v)
    {
        textViewHabit.setTextColor(Color.parseColor("#c8c8c8"));
        textViewHistory.setTextColor(Color.parseColor("#ffffff"));
        textViewPop.setTextColor(Color.parseColor("#ffffff"));
        textViewPerson.setTextColor(Color.parseColor("#ffffff"));

        if (checkAuthorization) {
            cursor = mDb.rawQuery("SELECT * FROM list WHERE id_user = ? AND status = 'Создана'", new String[]{String.valueOf(idUser)});
            if(!cursor.isAfterLast()) checklist = true;

            cursor = mDb.rawQuery("SELECT * FROM list_new WHERE id_user = ? AND status = 'Создана'", new String[]{String.valueOf(idUser)});
            if (!cursor.isAfterLast()) checkListTwo = true;

            if(checklist | checkListTwo){
                Bundle bundle = new Bundle();
                bundle.putBoolean("checkAuthorization", checkAuthorization);
                bundle.putInt("idUser", idUser);
                bundle.putBoolean("checkListOne", checklist);
                bundle.putBoolean("checkListTwo", checkListTwo);
                mainFragment = new MainFragment();
                mainFragment.setArguments(bundle);
                setNewFragment(mainFragment);
            }
            else{
                habitFragment = new HabitFragment();
                setNewFragment(habitFragment);
            }
        }

        else {

            if(Integer.parseInt(loadText(COUNT)) > 0){
                Bundle bundle = new Bundle();
                bundle.putBoolean("checkAuthorization", checkAuthorization);
                mainFragment = new MainFragment();
                mainFragment.setArguments(bundle);
                setNewFragment(mainFragment);
            }
            else{
                habitFragment = new HabitFragment();
                setNewFragment(habitFragment);
            }
        }
    }

    public void openHistory(View v)
    {
        if (checkAuthorization) {
            textViewHabit.setTextColor(Color.parseColor("#ffffff"));
            textViewHistory.setTextColor(Color.parseColor("#c8c8c8"));
            textViewPop.setTextColor(Color.parseColor("#ffffff"));
            textViewPerson.setTextColor(Color.parseColor("#ffffff"));
            historyFragment = new HistoryFragment();
            setNewFragment(historyFragment);
        } else {Snackbar.make(root, "Эта функция доступна только авторизованным пользователям", Snackbar.LENGTH_SHORT).show();}
    }

    public void openPop(View v)
    {
        if (checkAuthorization) {
            textViewHabit.setTextColor(Color.parseColor("#ffffff"));
            textViewHistory.setTextColor(Color.parseColor("#ffffff"));
            textViewPop.setTextColor(Color.parseColor("#c8c8c8"));
            textViewPerson.setTextColor(Color.parseColor("#ffffff"));
            popFragment = new PopFragment();
            setNewFragment(popFragment);
        } else {Snackbar.make(root, "Эта функция доступна только авторизованным пользователям", Snackbar.LENGTH_SHORT).show();}

    }

    public void openPerson(View v)
    {
        textViewHabit.setTextColor(Color.parseColor("#ffffff"));
        textViewHistory.setTextColor(Color.parseColor("#ffffff"));
        textViewPop.setTextColor(Color.parseColor("#ffffff"));
        textViewPerson.setTextColor(Color.parseColor("#c8c8c8"));
        if (checkAuthorization){
            Bundle bundle = new Bundle();
            bundle.putInt("idUser", idUser);
            perFragment = new PersonFragment();
            perFragment.setArguments(bundle);
            setNewFragment(perFragment);
            return;
        }
        personNoAuthoFragment = new PersonNoAuthoFragment();
        setNewFragment(personNoAuthoFragment);
    }

    public void openChoiceCategory(View view) {
        Intent intent = new Intent(this, ChoiceCategoryActivity.class);
        intent.putExtra("checkAuthorization", checkAuthorization);
        intent.putExtra("idUser", idUser);
        intent.putExtra("count", countHabit);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    private void setNewFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameMain, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

}