package com.example.spin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spin.R;
import com.example.spin.entities.Exercise;

/**
 * Экран создания упражнения
 */
public class AddExerciseActivity extends AppCompatActivity {
    // Окна для ввода времени выполнения и сопротивления
    private EditText etMinutes, etSeconds, etResistance;
    //Шкалы для ввода времени выполнения и сопротивления
    private SeekBar seekBarMinutes, seekBarResistance;
    //вью для информации об общей продолжительности + дублирование информации с SeekBar
    private TextView tvTotalDuration, tvSeekBarMinutesLabel, tvSeekBarResistanceLabel;
    //радиокнопки выбора положения
    private RadioGroup radioGroupPosition;
    //кнопка добавления упражнения
    private Button btnAdd;

    //переменные для хранения значений
    private int duration = 0; // в секундах
    private int resistance = 5;
    private String position = "sitting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise); // Установка UI-макета
        // Инициализация UI элементов
        initViews();
        // Настройка слушателей
        setupListeners();
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        btnAdd.setOnClickListener(v -> addExercise());
    }

    /**
     * Инициализация всех элементов интерфейса
     */
    private void initViews() {
        etMinutes = findViewById(R.id.etMinutes);
        etSeconds = findViewById(R.id.etSeconds);
        etResistance = findViewById(R.id.etResistance);
        seekBarMinutes = findViewById(R.id.seekBarMinutes);
        seekBarResistance = findViewById(R.id.seekBarResistance);
        tvTotalDuration = findViewById(R.id.tvTotalDuration);
        tvSeekBarMinutesLabel = findViewById(R.id.tvSeekBarMinutesLabel);
        tvSeekBarResistanceLabel = findViewById(R.id.tvSeekBarResistanceLabel);
        radioGroupPosition = findViewById(R.id.radioGroupPosition);
        btnAdd = findViewById(R.id.btnAdd);
    }

    /**
     * Настройка слушателей
     */
    private void setupListeners() {
        // Слушатель для минут
        seekBarMinutes.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                etMinutes.setText(String.valueOf(progress));    //обновление текстового поля
                tvSeekBarMinutesLabel.setText(progress + " минут"); //обновление подписи
                updateTotalDuration();  //обновление общей длительности
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Слушатель для сопротивления
        seekBarResistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress + 1; // 0-9 -> 1-10
                etResistance.setText(String.valueOf(value));    //обновление текстового поля
                tvSeekBarResistanceLabel.setText("Сопротивление: " + value); //обновление подписи
                resistance = value; //обновление самого значения сопротивления
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Слушатель для ручного ввода минут
        etMinutes.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                updateMinutesFromInput();
            }
        });

        // Слушатель для ручного ввода секунд
        etSeconds.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                updateTotalDuration();
            }
        });

        // Слушатель для ручного ввода сопротивления
        etResistance.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                updateResistanceFromInput();
            }
        });

        // Слушатель для радиокнопок
        radioGroupPosition.setOnCheckedChangeListener((group, checkedId) -> {
            position = getPositionFromRadioId(checkedId);
        });
    }

    /**
     * Обновление общей длительности упражнения
     */
    private void updateTotalDuration() {
        //Получение данных из EditText
        String minutesStr = etMinutes.getText().toString().trim();
        String secondsStr = etSeconds.getText().toString().trim();
        //Приведение к числам
        int minutes = TextUtils.isEmpty(minutesStr) ? 0 : Integer.parseInt(minutesStr);
        int seconds = TextUtils.isEmpty(secondsStr) ? 0 : Integer.parseInt(secondsStr);

        // Ограничиваем минуты 0-30
        if (minutes > 30) {
            minutes = 30;
            etMinutes.setText("30");
            seekBarMinutes.setProgress(30);
        }

        // Ограничиваем секунды 0-59
        if (seconds > 59) {
            seconds = 59;
            etSeconds.setText("59");
        }

        duration = minutes * 60 + seconds;
        tvTotalDuration.setText("= " + duration + " сек");
    }

    /**
     * Обновление при ручном вводе минут
     */
    private void updateMinutesFromInput() {
        String minutesStr = etMinutes.getText().toString().trim();
        //Пустое поле - сброс
        if (TextUtils.isEmpty(minutesStr)) {
            etMinutes.setText("0");
            seekBarMinutes.setProgress(0);
            return;
        }

        int minutes = Integer.parseInt(minutesStr);
        //Ограничения на число 0-30
        if (minutes < 0) {
            minutes = 0;
            etMinutes.setText("0");
        } else if (minutes > 30) {
            minutes = 30;
            etMinutes.setText("30");
        }

        seekBarMinutes.setProgress(minutes);
        tvSeekBarMinutesLabel.setText(minutes + " минут");
        updateTotalDuration();
    }

    /**
     * Обновление при ручном вводе сопротивления
     */
    private void updateResistanceFromInput() {
        String resistanceStr = etResistance.getText().toString().trim();
        //Пустое поле - сброс
        if (TextUtils.isEmpty(resistanceStr)) {
            etResistance.setText("1");
            seekBarResistance.setProgress(0);
            resistance = 1;
            return;
        }
        //Ограничение на число 1-10
        int value = Integer.parseInt(resistanceStr);
        if (value < 1) {
            value = 1;
            etResistance.setText("1");
        } else if (value > 10) {
            value = 10;
            etResistance.setText("10");
        }

        seekBarResistance.setProgress(value - 1);
        tvSeekBarResistanceLabel.setText("Сопротивление: " + value);
        resistance = value;
    }

    /**
     * Возврат соответствующей позиции упражнения в соответствии с радиокнопками
     */
    private String getPositionFromRadioId(int checkedId) {
        if (checkedId == R.id.radioSitting) {
            return "sitting";
        } else if (checkedId == R.id.radioLeaningSitting) {
            return "sitting_tilt";
        } else if (checkedId == R.id.radioStanding) {
            return "standing";
        } else if (checkedId == R.id.radioLeaningStanding) {
            return "standing_tilt";
        } else if (checkedId == R.id.radiositting_standing) {
            return "sitting_standing";
        }
        return "sitting"; // по умолчанию
    }

    /**
     * Добавление упражнения
     */
    private void addExercise() {
        // Проверяем продолжительность
        updateTotalDuration();
        if (duration <= 0) {
            Toast.makeText(this, "Продолжительность должна быть больше 0 секунд", Toast.LENGTH_SHORT).show();
            return;
        }

        // Проверяем сопротивление
        String resistanceStr = etResistance.getText().toString().trim();
        if (TextUtils.isEmpty(resistanceStr)) {
            Toast.makeText(this, "Введите сопротивление (1-10)", Toast.LENGTH_SHORT).show();
            return;
        }

        int res = Integer.parseInt(resistanceStr);
        if (res < 1 || res > 10) {
            Toast.makeText(this, "Сопротивление должно быть от 1 до 10", Toast.LENGTH_SHORT).show();
            return;
        }

        // Создаем объект Exercise
        Exercise exercise = new Exercise(
                0, // workoutId будет установлен позже
                position,
                duration,
                resistance,
                0 // orderNumber будет установлен позже
        );

        // Возвращаем результат
        Intent intent = new Intent();
        intent.putExtra("exercise", exercise);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Сохранение состояние при повороте экрана
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("duration", duration);
        outState.putInt("resistance", resistance);
        outState.putString("position", position);
    }

    /**
     * Восстановление состояния при пересоздании Activity при повороте
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        duration = savedInstanceState.getInt("duration", 0);
        resistance = savedInstanceState.getInt("resistance", 5);
        position = savedInstanceState.getString("position", "sitting");
    }
}
