package com.example.spin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spin.AppDatabase;
import com.example.spin.R;
import com.example.spin.adapters.ExerciseAdapter;
import com.example.spin.entities.Exercise;
import com.example.spin.entities.Workout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Экран создания тренировки
 */
public class CreateWorkoutActivity extends AppCompatActivity {
    //окно ввода названия тренировки
    private EditText etWorkoutName;
    //вью для отображения упражнений тренировки
    private RecyclerView recyclerView;
    //адаптер для отображения упражнений
    private ExerciseAdapter adapter;
    //список упражнений
    private List<Exercise> exerciseList = new ArrayList<>();
    //кнопка "Добавить упражнение", "Сохранить тренировку"
    private Button btnAddExercise, btnSaveWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);   // Установка UI-макета

        etWorkoutName = findViewById(R.id.etWorkoutName);
        recyclerView = findViewById(R.id.recyclerViewExercises);
        btnAddExercise = findViewById(R.id.btnAddExercise);
        btnSaveWorkout = findViewById(R.id.btnSaveWorkout);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ExerciseAdapter(exerciseList, position -> {   //создание адаптера с функцией удаления
            exerciseList.remove(position);
            adapter.notifyDataSetChanged();
        });
        recyclerView.setAdapter(adapter);

        btnAddExercise.setOnClickListener(v -> {    //при нажатии на "Добавить упражнение" - запуск экрана по добавлению
            Intent intent = new Intent(CreateWorkoutActivity.this, AddExerciseActivity.class);
            startActivityForResult(intent, 1);
        });
        //при нажатии на "Сохранить тренировку" - вызов метода сохранения
        btnSaveWorkout.setOnClickListener(v -> saveWorkout());
    }

    /**
     * Сохранение тренировки
     */
    private void saveWorkout() {
        String name = etWorkoutName.getText().toString().trim();
        if (name.isEmpty() || exerciseList.isEmpty()) { //проверка заполнения названия
            Toast.makeText(this, "Заполните название и добавьте упражнения", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            // Создаем тренировку
            Workout workout = new Workout(name);
            long workoutId = AppDatabase.getInstance(this).workoutDao().insert(workout);    //сохранение в БД

            // Добавляем упражнения
            for (int i = 0; i < exerciseList.size(); i++) {
                Exercise ex = exerciseList.get(i);
                ex.setWorkoutId((int) workoutId);
                ex.setOrderNumber(i+1);
                AppDatabase.getInstance(this).exerciseDao().insert(ex);
            }

            runOnUiThread(() -> {   //обновление UI с сообщением об успешном создании
                Toast.makeText(this, "Тренировка сохранена!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    /**
     * Получение результата добавления упражнения
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Exercise exercise = (Exercise) data.getSerializableExtra("exercise");
            if (exercise != null) {
                exerciseList.add(exercise);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
