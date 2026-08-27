package com.example.spin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spin.AppDatabase;
import com.example.spin.R;
import com.example.spin.entities.Exercise;
import com.example.spin.entities.History;
import com.example.spin.entities.Workout;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Экран процесса тренировки
 */
public class TrainingActivity extends AppCompatActivity {
    private TextView tvPositionName, tvResistance, tvTimer; //отображение данных упражнения
    private ImageView ivPositionImage;  //изображение для положения

    private int workoutId;  //id выполняемой тренировки
    private List<Exercise> exercises;   //список упражнений
    private int currentExerciseIndex = 0;   //индекс текущего упражнения
    private CountDownTimer timer;   //таймер

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training); // Установка UI-макета

        tvPositionName = findViewById(R.id.tvPositionName);
        tvResistance = findViewById(R.id.tvResistance);
        tvTimer = findViewById(R.id.tvTimer);
        ivPositionImage = findViewById(R.id.ivPositionImage);
        //кнопка пропуска упражнения
        Button btnSkip = findViewById(R.id.btnSkip);
        //получение ID тренировки из Intent
        workoutId = getIntent().getIntExtra("workoutId", -1);
        if (workoutId == -1) {
            finish();
            return;
        }
        //загрузка упражнений
        loadExercises();
        //пропуск упражнения
        btnSkip.setOnClickListener(v -> {
            if (timer != null) {
                timer.cancel();
            }
            nextExercise();
        });
    }

    /**
     * Загрузка упражнений из БД
     */
    private void loadExercises() {
        Executors.newSingleThreadExecutor().execute(() -> {
            exercises = AppDatabase.getInstance(this).exerciseDao().getExercisesForWorkout(workoutId);
            runOnUiThread(() -> {   //обновление UI
                if (exercises.isEmpty()) {
                    Toast.makeText(this, "Нет упражнений в тренировке", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    currentExerciseIndex = 0;
                    showExercise(currentExerciseIndex);
                }
            });
        });
    }

    /**
     * Отображение упражнения
     * @param index индекс упражнения
     */
    private void showExercise(int index) {
        //проверка, выполнены ли все упражнения
        if (index >= exercises.size()) {
            finishTraining();
            return;
        }
        //вывод данных упражнения
        Exercise exercise = exercises.get(index);
        tvPositionName.setText(getPositionName(exercise.getPosition()));
        tvResistance.setText("Сопротивление: " + exercise.getResistance());
        setPositionImage(exercise.getPosition());
        //остановка старого таймера
        if (timer != null) {
            timer.cancel();
        }
        //запуск нового таймера
        long remainingTime = exercise.getDuration();
        startTimer(remainingTime);
    }

    /**
     * Запуск таймера
     * @param duration продолжительность упражнения
     */
    private void startTimer(long duration) {
        timer = new CountDownTimer(duration * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {  //обновление значения таймера каждую секунду
                long seconds = millisUntilFinished / 1000;
                tvTimer.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
            }

            @Override
            public void onFinish() {
                nextExercise(); //переход к следующему упражнению
            }
        }.start();
    }

    /**
     * Переход к следующему упражнению
     */
    private void nextExercise() {
        currentExerciseIndex++;
        if (currentExerciseIndex < exercises.size()) {
            showExercise(currentExerciseIndex);
        } else {
            finishTraining();
        }
    }

    /**
     * Завершение тренировки
     */
    private void finishTraining() {
        // Сохраняем в историю
        Executors.newSingleThreadExecutor().execute(() -> {
            Workout workout = AppDatabase.getInstance(this).workoutDao().getWorkoutById(workoutId);
            if (workout != null) {
                History history = new History(workout.getId(), workout.getName(),
                        System.currentTimeMillis(), exercises.size(), exercises.size(), 0L);
                AppDatabase.getInstance(this).historyDao().insert(history);
            }
            runOnUiThread(() -> {   //переход к экрану завершения тренировки
                Intent intent = new Intent(TrainingActivity.this, TrainingCompleteActivity.class);
                startActivity(intent);
                finish();
            });
        });
    }

    /**
     * Локализация названий позиций
     */
    private String getPositionName(String position) {
        switch (position) {
            case "sitting": return "Сидя";
            case "sitting_tilt": return "Сидя в наклоне";
            case "standing": return "Стоя";
            case "standing_tilt": return "Стоя в наклоне";
            case "sitting_standing": return "Переход сидя-стоя";
            default: return position;
        }
    }

    /**
     * Получение соответствующего изображения для позиции
     */
    private void setPositionImage(String position) {
        int drawableId = R.drawable.sitting; // default
        switch (position) {
            case "sitting": drawableId = R.drawable.sitting; break;
            case "sitting_tilt": drawableId = R.drawable.sitting_tilt; break;
            case "standing": drawableId = R.drawable.standing; break;
            case "standing_tilt": drawableId = R.drawable.standing_tilt; break;
            case "sitting_standing": drawableId = R.drawable.sitting_standing; break;
        }
        ivPositionImage.setImageResource(drawableId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}