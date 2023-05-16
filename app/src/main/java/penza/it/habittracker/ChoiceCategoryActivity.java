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

public class ChoiceCategoryActivity extends AppCompatActivity {

    private ArrayList<String> categoryList = new ArrayList<>(10);
    private ArrayList<String> descriptionList = new ArrayList<>(10);
    private ArrayList<String> imageList = new ArrayList<>(10);
    private ListView listView;
    private CategoryAdapter categoryAdapter;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_category);
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

        categoryAdapter = new CategoryAdapter(this);
        listView = findViewById(R.id.listCategory);
        listView.setAdapter(categoryAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ChoiceCategoryActivity.this, ChoiceHabitActivity.class);
                intent.putExtra("name", categoryList.get(position));
                intent.putExtra("description", descriptionList.get(position));
                startActivity(intent);
            }
        });

    }

    public void backWindow(View view) {
        finish();
    }

    public void openCreateHabitActivity(View view) {
        Intent intent = new Intent(this, CreateHabitActivity.class);
        startActivity(intent);

    }

    // Пишем свой класс-адаптер
    private class CategoryAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;

        CategoryAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return categoryList.size();
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
            signTextView.setText(categoryList.get(position));

            TextView dateTextView = (TextView) convertView.findViewById(R.id.description_habit);
            dateTextView.setText(descriptionList.get(position));

            return convertView;
        }
    }

    private void initList() {
        cursor = mDb.rawQuery("SELECT * FROM categories", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                categoryList.add(cursor.getString(1));
                descriptionList.add(cursor.getString(2));
                imageList.add(cursor.getString(3));
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

}
