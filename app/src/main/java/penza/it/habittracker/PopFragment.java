package penza.it.habittracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;


public class PopFragment extends Fragment {

    private ArrayList<String> articleList = new ArrayList<>(10);
    private ArrayList<String> linkList = new ArrayList<>(10);
    private ArrayList<String> imageList = new ArrayList<>(10);

    private ListView listView;
    private ArticleAdapter articleAdapter;

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Cursor cursor;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBHelper = new DatabaseHelper(getActivity());

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


    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pop, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listPop);
        articleAdapter = new ArticleAdapter(getActivity());
        listView.setAdapter(articleAdapter);
        return view;
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
            image.setImageResource(getResources().getIdentifier(imageList.get(position), "drawable", requireActivity().getPackageName()));

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
                linkList.add(cursor.getString(3));
                imageList.add(cursor.getString(2));
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

}