package penza.it.habittracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    RelativeLayout root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = findViewById(R.id.root_layout);

    }

    public void openNewWindow(View v){
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
        finish();
    }

    public void openAuthorization(View v){
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
                if(TextUtils.isEmpty(email.getText().toString())){
                    Snackbar.make(root, "Введите почту", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(password.getText().toString().length() < 5){
                    Snackbar.make(root, "Введите пароль длинее 5 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // открытие авторизации

            }
        });


        builder.setView(authorization_window);

        AlertDialog dialog = builder.create();
        dialog.show();







    }

}