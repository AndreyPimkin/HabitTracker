package penza.it.habittracker;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {

    SharedPreferences sPref;

    final String SAVED_TEXT = "setting_user";
    final String ID_TEXT = "id_user";

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        Intent intent;

        if(loadText(SAVED_TEXT).equals("used")){
            intent = new Intent(LaunchActivity.this, SecondActivity.class);
        }
        else if(loadText(SAVED_TEXT).equals("already_used")) {
            intent = new Intent(LaunchActivity.this, SecondActivity.class);
            intent.putExtra("checkAuthorization", true);
            intent.putExtra("idUser",Integer.parseInt(loadText(ID_TEXT)));
        }
        else {
            createNotificationChannel();
            intent = new Intent(LaunchActivity.this, MainActivity.class);
            setAlarm();
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void setAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long time = System.currentTimeMillis();
        Intent intent = new Intent(LaunchActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(LaunchActivity.this, 0, intent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time,
                AlarmManager.INTERVAL_DAY, pendingIntent);

    }


    private String loadText(String name) {
        String text;
        sPref = getSharedPreferences("Checking", MODE_PRIVATE);
        text = sPref.getString(name, "");
        return text;
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ITHabit";
            String description = "Chanel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("oneChanel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }

    }
}