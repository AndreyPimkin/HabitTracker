package penza.it.habittracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
    private Cursor cursor, cursorTwo;
    SharedPreferences sPref;
    private HabitAdapter habitAdapter;
    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<String> iconList = new ArrayList<>();
    private ArrayList<String> colorList = new ArrayList<>();
    private ArrayList<String> timeStartList = new ArrayList<>();
    private ArrayList<String> periodList = new ArrayList<>();
    private boolean checklist = false, checkListTwo = false ;
    private boolean checkAuthorization = false;
    private int idUser;

    final String BELONGING = "belonging";
    final String NAME_TEXT = "name_habit";
    final String ICON_TEXT = "icon";
    final String COLOR_TEXT = "color";
    final String START_TEXT = "start";
    final String END_TEXT = "end";
    final String INTERVAL = "interval";

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

        if(checkAuthorization){
            if(checklist){
                initList("SELECT habits.name, list.icon, list.color, list.time_start FROM list " +
                            "INNER JOIN habits ON list.id_habit = habits.id_habit " +
                            "WHERE id_user = ?");
            }
            if(checkListTwo){
                initList("SELECT name_habit, icon, color, time_start FROM list WHERE id_user = ?");
            }
        }

        else{
            initListNoAuthorization();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        assert getArguments() != null;
        checkAuthorization = getArguments().getBoolean("checkAuthorization");
        if(checkAuthorization){
            checklist = getArguments().getBoolean("checkListOne");
            checkListTwo = getArguments().getBoolean("checkListTwo");
            idUser = getArguments().getInt("idUser");
        }

        ListView listView = (ListView) view.findViewById(R.id.listHabitMain);
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

            System.out.println(nameList.get(position));
            System.out.println(iconList.get(position));
            System.out.println(colorList.get(position));
            int iColor = Integer.parseInt(colorList.get(position));
            String sColor = String.format("#%06X", (0xFFFFFF & iColor));

            RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.layoutHabit);
            relativeLayout.setBackgroundColor(iColor);

            return convertView;
        }
    }


    private String loadText(String name) {
        String text;
        sPref = this.getActivity().getSharedPreferences("Checking", Context.MODE_PRIVATE);
        text = sPref.getString(name, "");
        return text;
    }

    private void initList(String query) {
        cursor = mDb.rawQuery(query, new String[]{String.valueOf(idUser)});
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

    private void initListNoAuthorization(){
        cursor = mDb.rawQuery("SELECT name FROM habits WHERE id_habit = ?", new String[]{loadText(NAME_TEXT)});
        if(loadText(BELONGING).equals("old")){
            if(!cursor.isAfterLast()){
                cursor.moveToFirst();
                nameList.add(cursor.getString(0));
            }
        }
        else{
            nameList.add(loadText(NAME_TEXT));
        }
        iconList.add(loadText(ICON_TEXT));
        colorList.add(loadText(COLOR_TEXT));
        timeStartList.add(loadText(START_TEXT));
        periodList.add(loadText(INTERVAL));
        cursor.moveToNext();
    }

}