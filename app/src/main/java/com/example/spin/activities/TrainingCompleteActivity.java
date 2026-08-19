package com.example.spin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spin.R;

/**
 * Экран завершения тренировки
 */
public class TrainingCompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_complete);    // Установка UI-макета

        Button btnGoToMenu = findViewById(R.id.btnGoToMenu);
        btnGoToMenu.setOnClickListener(v -> {
            Intent intent = new Intent(TrainingCompleteActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);    //очистка стека Activity
            startActivity(intent);
            finish();
        });
    }
}
