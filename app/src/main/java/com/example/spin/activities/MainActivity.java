package com.example.spin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.spin.R;

/**
 * Главный экран
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Установка UI-макета
        //Инициализация кнопок
        Button btnStartWorkout = findViewById(R.id.btnStartWorkout);
        Button btnCreateWorkout = findViewById(R.id.btnCreateWorkout);
        Button btnHistory = findViewById(R.id.btnHistory);
        Button btnExit = findViewById(R.id.btnExit);
        //переходы на экраны по нажатию кнопок
        btnStartWorkout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SelectWorkoutActivity.class);
            startActivity(intent);
        });

        btnCreateWorkout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateWorkoutActivity.class);
            startActivity(intent);
        });

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
        //полное закрытие приложения
        btnExit.setOnClickListener(v -> finishAffinity());
    }
}