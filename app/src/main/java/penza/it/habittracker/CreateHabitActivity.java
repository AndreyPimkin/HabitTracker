package penza.it.habittracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class CreateHabitActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton, timeButton;

    int hour, minute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);
        
        initDatePicker();

        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodayDate());

        timeButton = findViewById(R.id.timeButton);
    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return  makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month + 1;
                String date = makeDateString(day, month, year);
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

        return  getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {

        if(month == 1)
            return "ЯНВ";
        if(month == 2)
            return "ФЕВР";
        if(month == 3)
            return "МАРТ";
        if(month == 4)
            return "АПР";
        if(month == 5)
            return "МАЙ";
        if(month == 6)
            return "ИЮНЬ";
        if(month == 7)
            return "ИЮЛЬ";
        if(month == 8)
            return "АВГ";
        if(month == 9)
            return "СЕНТ";
        if(month == 10)
            return "ОКТ";
        if(month == 11)
            return "НОЯБ";
        if(month == 12)
            return "ДЕК";

        return "ЯНВ";
    }

    public void matterChoice(View view) {
    }

    public void createHabit(View view) {
    }

    public void openDatePicker(View view) {

        datePickerDialog.show();
    }

    public void eveningChoice(View view) {
    }

    public void backChoiceWindow(View view) {
    }

    public void popTimePicker(View view) {

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Выберите время");
        timePickerDialog.show();

    }
}