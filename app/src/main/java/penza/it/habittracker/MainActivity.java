package penza.it.habittracker;

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
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    RelativeLayout root;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Cursor cursor;

    SharedPreferences sPref;

    final String SAVED_TEXT = "saved_text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = findViewById(R.id.root_layout);

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

    }

    void saveText() {
        sPref = getSharedPreferences("Checking", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_TEXT, "two");
        ed.commit();
    }


    public void openNewWindow(View v) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
        saveText();
        finish();
    }

    public void openAuthorization(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Авторизация");
        builder.setMessage("Введите данные для авторизации");

        LayoutInflater inflater = LayoutInflater.from(this);
        View authorization_window = inflater.inflate(R.layout.authorization_window, null);

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
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    intent.putExtra("mail", email.getText().toString());
                    intent.putExtra("password", password.getText().toString());
                    intent.putExtra("checkAuthorization", true);
                    intent.putExtra("idUser", cursor.getString(0));
                    startActivity(intent);
                    finish();
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

}