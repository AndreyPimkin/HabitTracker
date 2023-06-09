package penza.it.habittracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class PersonFragment extends Fragment {
    TextView textView;
    Button button;
    private SettingAdapter settingAdapter;
    private ArrayList<String> settingList = new ArrayList<>(10);
    private ArrayList<String> imageList = new ArrayList<>(10);
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Cursor cursor;
    SharedPreferences sPref;
    private int idUser;

    final String SAVED_TEXT = "setting_user";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingList.add("Смена пароля");
        settingList.add("Смена номера телефона");
        settingList.add("Смена темы");
        settingList.add("Языковые параметры");
        settingList.add("Уведомления");
        settingList.add("Очистить данные");

        imageList.add("change_password");
        imageList.add("change_phone");
        imageList.add("change_themes");
        imageList.add("language");
        imageList.add("notification");
        imageList.add("clear_data");

        mDBHelper = new DatabaseHelper(getActivity());
        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listSetting);
        settingAdapter = new SettingAdapter(getActivity());
        listView.setAdapter(settingAdapter);

        assert getArguments() != null;
        idUser = getArguments().getInt("idUser");

        textView = view.findViewById(R.id.nameUser);
        cursor = mDb.rawQuery("SELECT name FROM users WHERE id_user = ?", new String[]{String.valueOf(idUser)});
        if(!cursor.isAfterLast()){
            cursor.moveToFirst();
            textView.setText("Добрый день, " + cursor.getString(0));
        }
        button = view.findViewById(R.id.buttonDeAutho);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveText(SAVED_TEXT,"used");
                Intent intent = new Intent(getActivity(), SecondActivity.class);
                intent.putExtra("checkAuthorization", false);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    void saveText(String saved, String check) {
        sPref = this.getActivity().getSharedPreferences("Checking", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(saved, check);
        ed.apply();
    }

    private class SettingAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;

        SettingAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return settingList.size();
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
                convertView = mLayoutInflater.inflate(R.layout.person_design, null);

            ImageView image = (ImageView) convertView.findViewById(R.id.imageSetting);
            image.setImageResource(getResources().getIdentifier(imageList.get(position), "drawable", requireActivity().getPackageName()));

            TextView signTextView = (TextView) convertView.findViewById(R.id.nameSetting);
            signTextView.setText(settingList.get(position));
            return convertView;
        }
    }

}