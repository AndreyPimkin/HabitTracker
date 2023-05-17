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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;

public class MainFragment extends Fragment {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Cursor cursor;
    private HabitAdapter habitAdapter;
    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<String> iconList = new ArrayList<>();
    private ArrayList<String> colorList = new ArrayList<>();
    private ArrayList<String> timeStartList = new ArrayList<>();
    private ArrayList<String> periodList = new ArrayList<>();


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
        habitAdapter = new HabitAdapter(getActivity());
        listView.setAdapter(habitAdapter);
        return view;
    }

    private class HabitAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;

        HabitAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return nameList.size();
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
                convertView = mLayoutInflater.inflate(R.layout.list_design_two, null);

            TextView signTextView = (TextView) convertView.findViewById(R.id.nameHabitTwo);
            signTextView.setText(nameList.get(position));

            ImageView image = (ImageView) convertView.findViewById(R.id.imageHabit);
            image.setImageResource(getResources().getIdentifier(iconList.get(position), "drawable", requireActivity().getPackageName()));

            int iColor = Integer.parseInt(colorList.get(position));
            String sColor = String.format("#%06X", (0xFFFFFF & iColor));
            RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.layoutHabit);
            relativeLayout.setBackgroundColor(iColor);

            return convertView;
        }
    }


    private void initList() {
        cursor = mDb.rawQuery("SELECT * FROM list WHERE id_user = ?", new String[]{idUser});
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                nameList.add(cursor.getString(1));
                iconList.add(cursor.getString(3));
                nameList.add(cursor.getString(2));
                timeStartList.add(cursor.getString(3));
                periodList.add(cursor.getString(3));
                cursor.moveToNext();
            }
        }
        cursor.close();
    }



}