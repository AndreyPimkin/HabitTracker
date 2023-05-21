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

import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class PopFragment extends Fragment {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Cursor cursor;
    private ArticleAdapter articleAdapter;
    private ArrayList<String> articleList = new ArrayList<>(10);
    private ArrayList<String> linkList = new ArrayList<>(10);
    private ArrayList<String> imageList = new ArrayList<>(10);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBHelper = new DatabaseHelper(getActivity());
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("link", linkList.get(position));
                startActivity(intent);
            }
        });

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
                imageList.add(cursor.getString(2));
                linkList.add(cursor.getString(3));
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

}