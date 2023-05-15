package penza.it.habittracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import yuku.ambilwarna.AmbilWarnaDialog;

public class CreateHabitActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton, timeButton, colorButton;

    int hour, minute;

    int mDefaultColor;

    RadioButton noMatter, morning, day, evening;

    EditText inputNameHabit;

    ImageView oldImage;

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Cursor cursor;

    private String dateForDB;
    private String selectedPeriod;

    private String selectedTime;

    private String nameHabit;

    private String nameImage;

    private String belonging = "new";

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

        final List<String> pictures = Arrays.asList("time_vector", "body_vector", "baby_vector", "cake_vector", "corona_vector",
                "moon_vector", "emoji_vector", "hiking_vector", "ice_vector", "icecream_vector", "kitchen_vector",
                "wash_vector", "alt_vector", "self_vector", "sick_vector", "tennis_vector");
        final Spinner spinner = findViewById(R.id.spinner);

        // Our custom Adapter class that we created
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

        Bundle arguments = getIntent().getExtras();

        inputNameHabit = findViewById(R.id.inputName);
        oldImage = findViewById(R.id.imageOld);

        if(arguments != null){
            nameHabit = arguments.getString("name");
            nameImage = arguments.getString("image");
            inputNameHabit.setText(nameHabit);
            oldImage.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            colorButton.setEnabled(false);
            oldImage.setImageResource(getResources().getIdentifier(nameImage, "drawable", getPackageName()));
            belonging = "old";
        }





    }

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

    private void initWidgets() {
        noMatter = findViewById(R.id.noMatterButton);
        morning = findViewById(R.id.morningButton);
        day = findViewById(R.id.dayButton);
        evening = findViewById(R.id.eveningButton);
    }

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
                dateForDB = makeDateStringForDB(day, month, year);
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

        if(month <= 10){
            return year + "-0" + month + "-" + day;
        }
        else {
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

    public void createHabit(View view) {
        if (TextUtils.isEmpty(inputNameHabit.getText().toString())) {
            Snackbar.make(view, "Введите название привычки", Snackbar.LENGTH_SHORT).show();
            return;
        }

        else if (TextUtils.isEmpty(inputNameHabit.getText().toString())) {
            Snackbar.make(view, "Выберите картинку", Snackbar.LENGTH_SHORT).show();
            return;
        }







        cursor = mDb.rawQuery(
                "INSERT INTO categories()" +
                        ",INSERT INTO list(id_user, id_habit, icon, color, time_start, time_end, time_interval, reminder, beloning) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                new String[]{"1" , ""});
        cursor.close();

        System.out.println(String.format("#%06X", (0xFFFFFF & mDefaultColor)));
        System.out.println(dateForDB);
        System.out.println(selectedPeriod);
        System.out.println(selectedTime);
        System.out.println(resId);

    }

    public void openDatePicker(View view) {

        datePickerDialog.show();
    }

    public void backChoiceWindow(View view) {
    }

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

    private int resId;

    private class SpinnerAdapter extends ArrayAdapter<String> {

        Context context;
        List<String> picturesList;

        private SpinnerAdapter(Context context, List<String> pictures) {
            super(context, R.layout.dropdown_item, pictures);
            this.context = context;
            this.picturesList = pictures;

        }

        // Override these methods and instead return our custom view (with image and text)
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

