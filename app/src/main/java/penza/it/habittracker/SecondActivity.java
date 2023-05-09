package penza.it.habittracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    HabitFragment habitFragment =  new HabitFragment();
    HistoryFragment historyFragment = new HistoryFragment();

    private TextView textViewHabit, textViewHistory, textViewPop, textViewPerson;


    private ArrayList<String> articleList = new ArrayList<>(10);
    private ArrayList<String> linkList = new ArrayList<>(10);
    private ArrayList<String> imageList = new ArrayList<>(10);
    private ListView listView;
    private ArticleAdapter articleAdapter;

    PopFragment popFragment;

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Cursor cursor;



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

    private class ArticleAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;

        ArticleAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return articleList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("DiscouragedApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = mLayoutInflater.inflate(R.layout.pop_design, null);

            ImageView image = (ImageView) convertView.findViewById(R.id.imagePopTwo);
            image.setImageResource(getResources().getIdentifier(imageList.get(position), "drawable", getPackageName()));

            TextView signTextView = (TextView) convertView.findViewById(R.id.namePop);
            signTextView.setText(articleList.get(position));


            return convertView;
        }
    }


    private void initList() {
        cursor = mDb.rawQuery("SELECT * FROM articles", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                articleList.add(cursor.getString(1));
                linkList.add(cursor.getString(2));
                imageList.add(cursor.getString(3));
                cursor.moveToNext();
            }
        }
        cursor.close();
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

        popFragment =  new PopFragment();
        setNewFragment(popFragment);
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