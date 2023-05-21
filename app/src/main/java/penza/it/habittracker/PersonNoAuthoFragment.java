package penza.it.habittracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class PersonNoAuthoFragment extends Fragment {
    Button button;
    TextView textButton;
    RelativeLayout root;
    private SettingAdapter settingAdapter;
    private ArrayList<String> settingList = new ArrayList<>(10);
    private ArrayList<String> imageList = new ArrayList<>(10);

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Cursor cursor;
    SharedPreferences sPref;
    final String SAVED_TEXT = "setting_user";
    final String ID_TEXT = "id_user";
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

        settingList.add("Смена темы");
        settingList.add("Языковые параметры");
        settingList.add("Уведомления");

        imageList.add("change_themes");
        imageList.add("language");
        imageList.add("notification");

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
        View view = inflater.inflate(R.layout.fragment_person_no_autho, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listSettingNo);
        settingAdapter = new SettingAdapter(getActivity());
        listView.setAdapter(settingAdapter);

        root = getActivity().findViewById(R.id.two_layout);

        inflater = LayoutInflater.from(getActivity());

        button = view.findViewById(R.id.registrationButton);
        LayoutInflater finalInflater = inflater;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Регистрация");
                builder.setMessage("Введите данные для регистрации");

                View registration_window = finalInflater.inflate(R.layout.registration_window, null);

                EditText name = registration_window.findViewById(R.id.nameFieldReg);
                EditText phone = registration_window.findViewById(R.id.numberFieldReg);
                EditText mail = registration_window.findViewById(R.id.emailFieldReg);
                EditText password = registration_window.findViewById(R.id.passwordFieldReg);

                builder.setNegativeButton("Назад", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setPositiveButton("Вход", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.isEmpty(mail.getText().toString())) {
                            Snackbar.make(root, "Введите почту", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(name.getText().toString())) {
                            Snackbar.make(root, "Введите имя", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (password.getText().toString().length() < 5) {
                            Snackbar.make(root, "Быть не менее 5 символов", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (!phone.getText().toString().matches("^8\\d{10}$")) {
                            Snackbar.make(root, "Неверный номер телефона", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        mDb.execSQL("INSERT INTO users(name, phone, password, mail) VALUES(?, ?, ?, ?)",
                                new String[]{name.getText().toString(), phone.getText().toString(),
                                        password.getText().toString(), phone.getText().toString()});

                        cursor = mDb.rawQuery("SELECT id_user FROM users WHERE phone = ?", new String[]{phone.getText().toString()});
                        if (!cursor.isAfterLast()) {
                            cursor.moveToFirst();
                            saveText(SAVED_TEXT,"already_used");
                            saveText(ID_TEXT, String.valueOf(cursor.getInt(0)));
                            Intent intent = new Intent(getActivity(), SecondActivity.class);
                            intent.putExtra("checkAuthorization", true);
                            intent.putExtra("idUser", cursor.getInt(0));
                            deleteText(INTERVAL);
                            deleteText(END_TEXT);
                            deleteText(START_TEXT);
                            deleteText(COLOR_TEXT);
                            deleteText(ICON_TEXT);
                            deleteText(NAME_TEXT);
                            deleteText(BELONGING);
                            saveText(COUNT, "0");
                            startActivity(intent);
                            getActivity().finish();
                        }
                        cursor.close();
                    }
                });

                builder.setView(registration_window);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        textButton = view.findViewById(R.id.textOAutho);
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Авторизация");
                builder.setMessage("Введите данные для авторизации");


                View authorization_window = finalInflater.inflate(R.layout.authorization_window, null);

                EditText email = authorization_window.findViewById(R.id.emailField);
                EditText password = authorization_window.findViewById(R.id.passwordField);

                builder.setNegativeButton("Назад", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setPositiveButton("Вход", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.isEmpty(email.getText().toString())) {
                            Snackbar.make(root, "Введите почту", Snackbar.LENGTH_SHORT).show();
                            return;
                        }

                        if (password.getText().toString().length() < 5) {
                            Snackbar.make(root, "Быть не менее 5 символов", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        cursor = mDb.rawQuery("SELECT * FROM users WHERE mail = ? and password = ?", new String[]{email.getText().toString(), password.getText().toString()});

                        if (!cursor.isAfterLast()) {
                            cursor.moveToFirst();
                            Intent intent = new Intent(getActivity(), SecondActivity.class);
                            intent.putExtra("checkAuthorization", true);
                            intent.putExtra("idUser", cursor.getInt(0));
                            saveText(SAVED_TEXT,"already_used");
                            saveText(ID_TEXT, String.valueOf(cursor.getInt(0)));
                            deleteText(INTERVAL);
                            deleteText(END_TEXT);
                            deleteText(START_TEXT);
                            deleteText(COLOR_TEXT);
                            deleteText(ICON_TEXT);
                            deleteText(NAME_TEXT);
                            deleteText(BELONGING);
                            saveText(COUNT, "0");
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            Snackbar.make(root, "Пользователь не найден", Snackbar.LENGTH_SHORT).show();
                            return;
                        }

                        cursor.close();
                    }
                });

                builder.setView(authorization_window);
                AlertDialog dialog = builder.create();
                dialog.show();
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

    private void deleteText(String name) {
        sPref = this.getActivity().getSharedPreferences("Checking", Context.MODE_PRIVATE);
        sPref.edit().remove(name).apply();
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