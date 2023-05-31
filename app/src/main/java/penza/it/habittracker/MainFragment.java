package penza.it.habittracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class MainFragment extends Fragment{
    private ListView listView;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Cursor cursor;
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
    final String COUNT = "count";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBHelper = new DatabaseHelper(getActivity());
        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        if(checkAuthorization){
            if(checklist){
                initList("SELECT habits.name, list.icon, list.color, list.time_start FROM list " +
                        "INNER JOIN habits ON list.id_habit = habits.id_habit " +
                        "WHERE id_user = ? AND status = ?");
            }

            if(checkListTwo){
                initList("SELECT name_habit, icon, color, time_start FROM list_new WHERE id_user = ? AND status = ?");
            }
        }

        else{
            initListNoAuthorization();
        }

        listView = (ListView) view.findViewById(R.id.listHabitMain);
        habitAdapter = new HabitAdapter(getActivity());
        listView.setAdapter(habitAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

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
            ImageView image = (ImageView) convertView.findViewById(R.id.imageIconHabit);

            int iColor = Integer.parseInt(colorList.get(position));

            RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.layoutHabit);
            SomeDrawable drawable = new SomeDrawable(iColor, iColor, iColor,1,Color.BLACK, 20);
            relativeLayout.setBackgroundDrawable(drawable);
            Resources res = getActivity().getResources();
            int resID = Integer.parseInt(iconList.get(position));
            Drawable db = res.getDrawable(resID);

            cursor = mDb.rawQuery("SELECT * FROM habits WHERE name = ?", new String[]{nameList.get(position)});
            if(cursor.isAfterLast()){
                db.setTint(getResources().getColor(R.color.white));
            }
            cursor.close();
            image.setImageDrawable(db);
            ImageButton button = (ImageButton) convertView.findViewById(R.id.buttonMore);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(getActivity(), button);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            if(checkAuthorization){
                                if(menuItem.getTitle().equals("Удалить")) {
                                    deleteHabit(nameList.get(position), position);
                                }
                                if(menuItem.getTitle().equals("Завершить")){
                                    completeHabit(nameList.get(position), position);
                                }
                            }
                            else{
                                deleteText(INTERVAL);
                                deleteText(END_TEXT);
                                deleteText(START_TEXT);
                                deleteText(COLOR_TEXT);
                                deleteText(ICON_TEXT);
                                deleteText(NAME_TEXT);
                                deleteText(BELONGING);
                                saveText(COUNT, "0");
                                nameList.remove(0);
                                listView.setAdapter(habitAdapter);
                            }

                            return true;
                        }
                    });
                    // Showing the popup menu
                    popupMenu.show();
                }
            });
            return convertView;
        }
    }

    public class SomeDrawable extends GradientDrawable {

        public SomeDrawable(int pStartColor, int pCenterColor, int pEndColor, int pStrokeWidth, int pStrokeColor, float cornerRadius) {
            super(Orientation.BOTTOM_TOP,new int[]{pStartColor,pCenterColor,pEndColor});
            setStroke(pStrokeWidth,pStrokeColor);
            setShape(GradientDrawable.RECTANGLE);
            setCornerRadius(cornerRadius);

        }
    }

    private String loadText(String name) {
        String text;
        sPref = this.getActivity().getSharedPreferences("Checking", Context.MODE_PRIVATE);
        text = sPref.getString(name, "");
        return text;
    }

    private void deleteText(String name) {
        sPref = this.getActivity().getSharedPreferences("Checking", Context.MODE_PRIVATE);
        sPref.edit().remove(name).apply();
    }

    void saveText(String saved, String check) {
        sPref = this.getActivity().getSharedPreferences("Checking", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(saved, check);
        ed.apply();
    }

    private void initList(String query) {
        cursor = mDb.rawQuery(query, new String[]{String.valueOf(idUser), "Создана"});
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                nameList.add(cursor.getString(0));
                iconList.add(cursor.getString(1));
                colorList.add(cursor.getString(2));
                timeStartList.add(cursor.getString(3));
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
        cursor.close();
    }

    private void deleteHabit(String nameHabit, int position){
        cursor = mDb.rawQuery("SELECT * FROM habits WHERE name = ?", new String[]{nameHabit});
        if(cursor.isAfterLast()){
            mDb.execSQL("DELETE FROM list_new WHERE name_habit = ?", new String[]{nameHabit});
        }
        else{
            mDb.execSQL("DELETE FROM list WHERE id_habit = (SELECT id_habit FROM habits WHERE name = ?)", new String[]{nameHabit});
        }
        cursor.close();
        nameList.remove(position);
        listView.setAdapter(habitAdapter);
    }

    private void completeHabit(String nameHabit, int position){
        cursor = mDb.rawQuery("SELECT * FROM habits WHERE name = ?", new String[]{nameHabit});
        if(cursor.isAfterLast()){
            mDb.execSQL("UPDATE list_new SET status = 'Завершена' " +
                    "WHERE name_habit = ?", new String[]{nameHabit});
        }
        else{
            mDb.execSQL("UPDATE list SET status = 'Завершена' " +
                    "WHERE id_habit = (SELECT id_habit FROM habits WHERE name = ?)", new String[]{nameHabit});
        }
        cursor.close();
        nameList.remove(position);
        listView.setAdapter(habitAdapter);
    }

}