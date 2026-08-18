package com.example.spin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spin.AppDatabase;
import com.example.spin.R;
import com.example.spin.adapters.WorkoutAdapter;
import com.example.spin.entities.Workout;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Экран выбора тренировки
 */
public class SelectWorkoutActivity extends AppCompatActivity {
    //вью для отображения списка тренировок
    private RecyclerView recyclerView;
    //адаптер для отображения тренировок в recyclerView
    private WorkoutAdapter adapter;
    //кнопка старта тренировки
    private Button btnStart;
    //id выбранной тренировки
    private int selectedWorkoutId = -1;
    //список тренировок из БД
    private List<Workout> workouts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_workout);   // Установка UI-макета

        recyclerView = findViewById(R.id.recyclerViewWorkouts);
        btnStart = findViewById(R.id.btnStart);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadWorkouts();

        btnStart.setOnClickListener(v -> {  //при выборе тренировки и нажатии на "Старт" - начало тренировки
            if (selectedWorkoutId != -1) {
                Intent intent = new Intent(SelectWorkoutActivity.this, TrainingActivity.class);
                intent.putExtra("workoutId", selectedWorkoutId);
                startActivity(intent);
            }
        });
    }

    private void loadWorkouts() {
        Executors.newSingleThreadExecutor().execute(() -> {  //запрос в фоновом потоке
            workouts = AppDatabase.getInstance(this).workoutDao().getAllWorkouts();
            runOnUiThread(() -> {   //обновление ui
                adapter = new WorkoutAdapter(workouts, workout -> {
                    selectedWorkoutId = workout.getId();    //сохранение id
                    btnStart.setEnabled(true);  //активация кнопки
                    btnStart.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary)); //изменение цвета кнопки
                    adapter.setSelectedId(workout.getId()); //подсветка элемента
                });
                recyclerView.setAdapter(adapter);
            });
        });
    }
}
