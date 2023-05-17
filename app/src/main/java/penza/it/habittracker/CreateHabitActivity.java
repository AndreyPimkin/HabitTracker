package penza.it.habittracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import yuku.ambilwarna.AmbilWarnaDialog;

public class CreateHabitActivity extends AppCompatActivity {
    RadioButton noMatter, morning, day, evening;
    Button dateButton, timeButton, colorButton;
    EditText inputNameHabit;
    ImageView oldImage;
    SwitchMaterial switchMaterial;
    Spinner spinner;
    SharedPreferences sPref;
    private DatePickerDialog datePickerDialog;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private String timeEnd;
    private String selectedPeriod = "Неважно";
    private String selectedTime;
    private String nameHabit, nameImage;
    private String belonging = "new";
    private int idUser, idHabit, resId;
    private int countHabit = 0;
    int hour, minute, mDefaultColor;
    private boolean checkAuthorization = false;
    private boolean checkSwitch = false;
    private final List<String> pictures = Arrays.asList("time_vector", "body_vector", "baby_vector", "cake_vector", "corona_vector",
            "moon_vector", "emoji_vector", "hiking_vector", "ice_vector", "icecream_vector", "kitchen_vector",
            "wash_vector", "alt_vector", "self_vector", "sick_vector", "tennis_vector");

    final String BELONGING = "belonging";
    final String NAME_TEXT = "name_habit";
    final String ICON_TEXT = "icon";
    final String COLOR_TEXT = "color";
    final String START_TEXT = "start";
    final String END_TEXT = "end";
    final String INTERVAL = "interval";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);

        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodayDate());

        timeButton = findViewById(R.id.timeButton);

        initWidgets();
        updateRadioGroup(noMatter);

        spinner = findViewById(R.id.spinner);
        SpinnerAdapter adapter = new SpinnerAdapter(getApplicationContext(), pictures);
        adapter.setDropDownViewResource(R.layout.dropdown_item);
        spinner.setAdapter(adapter);

        mDefaultColor = ContextCompat.getColor(CreateHabitActivity.this, R.color.black);
        colorButton = findViewById(R.id.buttonColor);
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });

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

        inputNameHabit = findViewById(R.id.inputName);
        oldImage = findViewById(R.id.imageOld);
        Context context = null;
        resId = context.getResources().getIdentifier("time_vector", "drawable", context.getPackageName());

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            belonging = arguments.getString("belonging");
            checkAuthorization = arguments.getBoolean("checkAuthorization");
            countHabit = arguments.getInt("count");
            if (checkAuthorization) {
                idUser = arguments.getInt("idUser");
            }
            if (belonging.equals("old")) {
                nameHabit = arguments.getString("name");
                nameImage = arguments.getString("image");
                idHabit = arguments.getInt("idHabit");
                inputNameHabit.setText(nameHabit);
                oldImage.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                inputNameHabit.setEnabled(false);
                oldImage.setImageResource(getResources().getIdentifier(nameImage, "drawable", getPackageName()));
            } else {
                oldImage.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
                inputNameHabit.setEnabled(true);
            }
        }

        switchMaterial = findViewById(R.id.switchReminder);
        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    checkSwitch = true;
                }
            }
        });
    }

    // методы ColorPicker
    private void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
            }
        });
        colorPicker.show();
    }

    // методы DatePicker
    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                timeEnd = makeDateStringForDB(day, month, year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {

        return " Окончание:  " + getMonthFormat(month) + " " + day + ", " + year;
    }

    private String makeDateStringForDB(int day, int month, int year) {
        if (month <= 10) {
            return year + "-0" + month + "-" + day;
        } else {
            return year + "-" + month + "-" + day;
        }
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "янв";
        if (month == 2)
            return "февр";
        if (month == 3)
            return "март";
        if (month == 4)
            return "апр";
        if (month == 5)
            return "май";
        if (month == 6)
            return "июнь";
        if (month == 7)
            return "июль";
        if (month == 8)
            return "авг";
        if (month == 9)
            return "сент";
        if (month == 10)
            return "окт";
        if (month == 11)
            return "нояб";
        if (month == 12)
            return "дек";
        return "ЯНВ";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    // методы timePicker
    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Выберите время");
        timePickerDialog.show();
    }

    // методы radioButton
    private void initWidgets() {
        noMatter = findViewById(R.id.noMatterButton);
        morning = findViewById(R.id.morningButton);
        day = findViewById(R.id.dayButton);
        evening = findViewById(R.id.eveningButton);
    }

    public void radioTapped(View view) {
        int selectedID = view.getId();
        if (selectedID == R.id.noMatterButton) {
            updateRadioGroup(noMatter);
        } else if (selectedID == R.id.morningButton) {
            updateRadioGroup(morning);
        } else if (selectedID == R.id.dayButton) {
            updateRadioGroup(day);
        } else if (selectedID == R.id.eveningButton) {
            updateRadioGroup(evening);
        }
    }

    private void updateRadioGroup(RadioButton selected) {
        noMatter.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_off));
        morning.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_off));
        day.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_off));
        evening.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_off));
        selected.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_on));
        selectedPeriod = String.valueOf(selected.getText());
    }

    public void backChoiceWindow(View view) {
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createHabit(View view) {
        LocalDate ld = LocalDate.now();
        String timeStart = ld.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (TextUtils.isEmpty(inputNameHabit.getText().toString())) {
            Snackbar.make(view, "Введите название привычки", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(timeEnd)) {
            Snackbar.make(view, "Выберите дату окончания", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(selectedTime) && checkSwitch) {
            Snackbar.make(view, "Выберите время уведомлений", Snackbar.LENGTH_SHORT).show();
        } else {
            if (checkAuthorization) {
                if (!checkSwitch) {
                    selectedTime = "none";
                }
                if (belonging.equals("old")) {
                    mDb.execSQL("INSERT INTO " +
                                    "list (id_user, id_habit, icon, color, time_start, time_end, reminder, time_interval,  belonging) " +
                                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)",
                            new String[]{String.valueOf(idUser), String.valueOf(idHabit), nameImage, String.valueOf(mDefaultColor),
                                    timeStart, timeEnd, selectedPeriod, selectedTime, belonging});

                } else {
                    String newName = inputNameHabit.getText().toString();

                    mDb.execSQL("INSERT INTO " +
                                    "list(id_user, name_habit, icon, color, time_start, time_end, reminder, time_interval, belonging) " +
                                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                            new String[]{String.valueOf(idUser), newName, String.valueOf(resId), String.valueOf(mDefaultColor),
                                    timeStart, timeEnd, selectedPeriod, selectedTime, belonging});
                }

                Snackbar.make(view, "Успешно", Snackbar.LENGTH_SHORT).show();
            }


            else {
                if (countHabit > 0) {
                    if (belonging.equals("new")) {
                        ++countHabit;
                        saveText(BELONGING, "new");
                        saveText(NAME_TEXT, inputNameHabit.getText().toString());
                        saveText(ICON_TEXT, String.valueOf(resId));
                        saveText(COLOR_TEXT, String.valueOf(mDefaultColor));
                        saveText(START_TEXT, timeStart);
                        saveText(END_TEXT, timeEnd);
                        saveText(INTERVAL, selectedPeriod);
                    } else {
                        ++countHabit;
                        saveText(BELONGING, "old");
                        saveText(NAME_TEXT, String.valueOf(idHabit));
                        saveText(ICON_TEXT, nameImage);
                        saveText(COLOR_TEXT, String.valueOf(mDefaultColor));
                        saveText(START_TEXT, timeStart);
                        saveText(END_TEXT, timeEnd);
                        saveText(INTERVAL, selectedPeriod);
                    }
                } else {
                    Snackbar.make(view, "Вы достигли лимита, пожалуйста, авторизуйтесь", Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }

    void saveText(String saved, String check) {
        sPref = getSharedPreferences("Checking", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(saved, check);
        ed.apply();
    }

    private class SpinnerAdapter extends ArrayAdapter<String> {
        Context context;
        List<String> picturesList;

        private SpinnerAdapter(Context context, List<String> pictures) {
            super(context, R.layout.dropdown_item, pictures);
            this.context = context;
            this.picturesList = pictures;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.dropdown_item, parent, false);
            ImageView picture = row.findViewById(R.id.img);
            Resources res = context.getResources();
            String drawableName = picturesList.get(position).toLowerCase(); // tx
            resId = res.getIdentifier(drawableName, "drawable", context.getPackageName());
            Drawable drawable = res.getDrawable(resId);
            drawable.setTint(mDefaultColor);
            picture.setImageDrawable(drawable);
            return row;
        }
    }
}

