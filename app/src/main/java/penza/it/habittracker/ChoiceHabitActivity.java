package penza.it.habittracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class ChoiceHabitActivity extends AppCompatActivity {

    private String nameCategory;
    private String descriptionCategory;

    private ArrayList<String> habitList = new ArrayList<>(10);
    private ArrayList<String> descriptionList = new ArrayList<>(10);
    private ArrayList<String> imageList = new ArrayList<>(10);
    private ListView listView;

    private HabitAdapter habitAdapter;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Cursor cursor;



    private TextView textViewName, textViewDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_habit);

        Bundle arguments = getIntent().getExtras();
        if(arguments != null){
            nameCategory = arguments.getString("name");
            descriptionCategory = arguments.getString("description");
        }
        textViewName = findViewById(R.id.text_name);
        textViewDesc = findViewById(R.id.text_description);

        textViewName.setText(nameCategory);
        textViewDesc.setText(descriptionCategory);

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

        initList();


        habitAdapter = new HabitAdapter(this);
        listView = findViewById(R.id.listHabit);
        listView.setAdapter(habitAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ChoiceHabitActivity.this, ChoiceHabitActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void backCreateWindow(View view) {
        Intent intent = new Intent(this, ChoiceCategoryActivity.class);
        startActivity(intent);
        finish();
    }

    private class HabitAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;

        HabitAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return habitList.size();
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
                convertView = mLayoutInflater.inflate(R.layout.list_design, null);

            ImageView image = (ImageView) convertView.findViewById(R.id.image_habit);
            image.setImageResource(getResources().getIdentifier(imageList.get(position), "drawable", getPackageName()));

            TextView signTextView = (TextView) convertView.findViewById(R.id.name_habit);
            signTextView.setText(habitList.get(position));

            TextView dateTextView = (TextView) convertView.findViewById(R.id.description_habit);
            dateTextView.setText(descriptionList.get(position));

            return convertView;
        }
    }


    private void initList() {
        cursor = mDb.rawQuery("SELECT * FROM habits WHERE id_category = (SELECT id_category FROM categories WHERE name = ?)",
                new String[]{nameCategory});
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                habitList.add(cursor.getString(2));
                descriptionList.add(cursor.getString(3));
                imageList.add(cursor.getString(4));
                cursor.moveToNext();
            }
        }
        cursor.close();
    }
}