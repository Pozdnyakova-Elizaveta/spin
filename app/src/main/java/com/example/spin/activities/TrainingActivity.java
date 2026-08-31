package com.example.spin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
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
    private TextView tvPositionName, tvResistance, tvTimer;
    private ImageView ivPositionImage;
    private ImageButton btnPause;

    private int workoutId;
    private List<Exercise> exercises;
    private int currentExerciseIndex = 0;
    private CountDownTimer timer;
    private boolean isPaused = false;
    private boolean isRunning = false;
    private long remainingTimeInMillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        tvPositionName = findViewById(R.id.tvPositionName);
        tvResistance = findViewById(R.id.tvResistance);
        tvTimer = findViewById(R.id.tvTimer);
        ivPositionImage = findViewById(R.id.ivPositionImage);
        Button btnSkip = findViewById(R.id.btnSkip);
        btnPause = findViewById(R.id.btnPause);

        workoutId = getIntent().getIntExtra("workoutId", -1);
        if (workoutId == -1) {
            finish();
            return;
        }

        loadExercises();

        btnSkip.setOnClickListener(v -> {
            if (timer != null) {
                timer.cancel();
                timer = null; // Важно: обнуляем ссылку
            }
            isRunning = false;
            isPaused = false;
            btnPause.setImageResource(R.drawable.ic_pause);
            btnPause.setEnabled(true);
            btnPause.setBackgroundResource(R.drawable.btn_circle_white);
            nextExercise();
        });

        btnPause.setOnClickListener(v -> togglePause());
    }

    private void loadExercises() {
        Executors.newSingleThreadExecutor().execute(() -> {
            exercises = AppDatabase.getInstance(this).exerciseDao().getExercisesForWorkout(workoutId);
            runOnUiThread(() -> {
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

    private void showExercise(int index) {
        if (index >= exercises.size()) {
            finishTraining();
            return;
        }

        Exercise exercise = exercises.get(index);
        tvPositionName.setText(getPositionName(exercise.getPosition()));
        tvResistance.setText("Сопротивление: " + exercise.getResistance());
        setPositionImage(exercise.getPosition());

        // Сброс состояния паузы
        isPaused = false;
        isRunning = false;
        btnPause.setImageResource(R.drawable.ic_pause);
        btnPause.setEnabled(true);
        btnPause.setBackgroundResource(R.drawable.btn_circle_white);

        // Остановка старого таймера
        if (timer != null) {
            timer.cancel();
            timer = null; // Обнуляем ссылку
        }

        // Запуск нового таймера
        long durationInSeconds = exercise.getDuration();
        long durationInMillis = durationInSeconds * 1000;
        remainingTimeInMillis = durationInMillis;
        startTimer(durationInMillis);
    }

    /**
     * Запуск таймера
     * @param durationMillis продолжительность упражнения в МИЛЛИСЕКУНДАХ
     */
    private void startTimer(long durationMillis) {
        // Отменяем старый таймер, если он существует
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        isRunning = true;
        isPaused = false;
        btnPause.setImageResource(R.drawable.ic_pause);
        btnPause.setBackgroundResource(R.drawable.btn_circle_white);

        timer = new CountDownTimer(durationMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTimeInMillis = millisUntilFinished;
                long seconds = millisUntilFinished / 1000;
                tvTimer.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
            }

            @Override
            public void onFinish() {
                isRunning = false;
                isPaused = false;
                btnPause.setImageResource(R.drawable.ic_pause);
                btnPause.setEnabled(false);
                btnPause.setBackgroundResource(R.drawable.btn_circle_white);
                tvTimer.setText("00:00");
                timer = null; // Обнуляем ссылку
                nextExercise();
            }
        }.start();
    }

    private void togglePause() {
        if (!isRunning) {
            // Если таймер не запущен, пробуем запустить
            if (remainingTimeInMillis > 0) {
                startTimer(remainingTimeInMillis);
            }
            return;
        }

        if (isPaused) {
            resumeTimer();
        } else {
            pauseTimer();
        }
    }

    private void pauseTimer() {
        if (timer != null && isRunning) {
            timer.cancel();
            timer = null; // Обнуляем ссылку
            isPaused = true;
            isRunning = false;
            btnPause.setImageResource(R.drawable.ic_play);
            btnPause.setBackgroundResource(R.drawable.btn_circle_green);
        }
    }

    private void resumeTimer() {
        if (remainingTimeInMillis > 0) {
            // Отменяем старый таймер, если он еще существует
            if (timer != null) {
                timer.cancel();
                timer = null;
            }

            isPaused = false;
            isRunning = true;
            btnPause.setImageResource(R.drawable.ic_pause);
            btnPause.setBackgroundResource(R.drawable.btn_circle_white);

            // Создаем новый таймер с оставшимся временем
            timer = new CountDownTimer(remainingTimeInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    remainingTimeInMillis = millisUntilFinished;
                    long seconds = millisUntilFinished / 1000;
                    tvTimer.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
                }

                @Override
                public void onFinish() {
                    isRunning = false;
                    isPaused = false;
                    btnPause.setImageResource(R.drawable.ic_pause);
                    btnPause.setEnabled(false);
                    btnPause.setBackgroundResource(R.drawable.btn_circle_white);
                    tvTimer.setText("00:00");
                    timer = null;
                    nextExercise();
                }
            }.start();
        }
    }

    private void nextExercise() {
        currentExerciseIndex++;
        if (currentExerciseIndex < exercises.size()) {
            showExercise(currentExerciseIndex);
        } else {
            finishTraining();
        }
    }

    private void finishTraining() {
        Executors.newSingleThreadExecutor().execute(() -> {
            Workout workout = AppDatabase.getInstance(this).workoutDao().getWorkoutById(workoutId);
            if (workout != null) {
                History history = new History(workout.getId(), workout.getName(),
                        System.currentTimeMillis(), exercises.size(), exercises.size(), 0L);
                AppDatabase.getInstance(this).historyDao().insert(history);
            }
            runOnUiThread(() -> {
                Intent intent = new Intent(TrainingActivity.this, TrainingCompleteActivity.class);
                startActivity(intent);
                finish();
            });
        });
    }

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

    private void setPositionImage(String position) {
        int drawableId = R.drawable.sitting;
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
            timer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Если приложение уходит в фон, автоматически ставим на паузу
        if (isRunning && !isPaused) {
            pauseTimer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновляем состояние кнопки при возврате
        if (isPaused) {
            btnPause.setImageResource(R.drawable.ic_play);
            btnPause.setBackgroundResource(R.drawable.btn_circle_green);
        } else if (isRunning) {
            btnPause.setImageResource(R.drawable.ic_pause);
            btnPause.setBackgroundResource(R.drawable.btn_circle_white);
        }
    }
}
