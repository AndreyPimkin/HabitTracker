package penza.it.habittracker;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HistoryFragment extends Fragment {
    Button button;
    CalendarView calendarView;
    TextView textComplete;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Cursor cursor;
    private int countComplete = 0;


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
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        LocalDate ld = LocalDate.now();
        String timeStart = ld.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        textComplete = view.findViewById(R.id.textCompleted);

        button = (Button) view.findViewById(R.id.clearButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor = mDb.rawQuery("SELECT * FROM list WHERE (time_start BETWEEN ? AND ?) AND status = 'Завершена'",
                        new String[]{"2023-01-01", timeStart});
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        countComplete++;
                    }
                }
                cursor = mDb.rawQuery("SELECT * FROM list_new WHERE (time_start BETWEEN ? AND ?) AND status = 'Завершена'",
                        new String[]{"2023-01-01", timeStart});
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        countComplete++;
                    }
                }
                cursor.close();
                textComplete.setText(String.valueOf(countComplete));
            }
        });

        calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                cursor = mDb.rawQuery("SELECT * FROM list WHERE (time_start BETWEEN ? AND ?) AND status = 'Завершена'",
                        new String[]{year + "-" + month + "-"+ day, timeStart});
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        countComplete++;
                    }
                }
                cursor = mDb.rawQuery("SELECT * FROM list_new WHERE (time_start BETWEEN ? AND ?) AND status = 'Завершена'",
                        new String[]{year + "-" + month + "-"+ day, timeStart});
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        countComplete++;
                    }
                }
                cursor.close();
                textComplete.setText(String.valueOf(countComplete));
            }
        });

        return view;
    }

}