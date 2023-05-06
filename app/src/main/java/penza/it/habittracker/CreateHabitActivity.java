package penza.it.habittracker;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateHabitActivity extends AppCompatActivity {

    private String[] namesArr = new String[] {"1", "2", "3", "4", "5", "6", "7", "8"};

    private String[] descArr = new String[] {"11", "22", "33", "44", "5", "6", "7", "8"};
    private ListView listView;

    private HabitAdapter habitAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);

        habitAdapter = new HabitAdapter(this);

        listView = findViewById(R.id.listHabit);
        listView.setAdapter(habitAdapter);

    }


    protected void onListItemClick(ListView l, View v, int position, long id) {
        // super.onListItemClick(l, v, position, id);
        String selection = l.getItemAtPosition(position).toString();
        Toast.makeText(this, selection, Toast.LENGTH_LONG).show();
    }

    // Пишем свой класс-адаптер
    private class HabitAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;

        HabitAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return namesArr.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = mLayoutInflater.inflate(R.layout.list_design, null);


            TextView signTextView = (TextView) convertView.findViewById(R.id.name_habit);
            signTextView.setText(namesArr[position]);

            TextView dateTextView = (TextView) convertView.findViewById(R.id.description_habit);
            dateTextView.setText(descArr[position]);

            return convertView;
        }

        String getString(int position) {
            return namesArr[position] + " (" + descArr[position] + ")";
        }
    }



}
