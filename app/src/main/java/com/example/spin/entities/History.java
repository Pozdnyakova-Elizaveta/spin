package com.example.spin.entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность история тренировок
 */
@Entity(tableName = "history")
@Setter
public class History {
    //Идентификатор
    @PrimaryKey(autoGenerate = true)
    public int id;
    //Идентификатор проведенной тернировки
    public int workoutId;
    //Дата проведение
    public long date;
    //Число завершенных упражнений
    public int completedExercises;
    //Пройденный путь за тренировку (в м)
    public long lengthTraveledPath;

    public int getId() {
        return id;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public long getDate() {
        return date;
    }
    public int getCompletedExercises() {
        return completedExercises;
    }

    public long getLengthTraveledPath() {
        return lengthTraveledPath;
    }

    public History(int workoutId, long date, int totalExercises, int completedExercises, long lengthTraveledPath) {
        this.workoutId = workoutId;
        this.date = date;
        this.completedExercises = completedExercises;
        this.lengthTraveledPath = lengthTraveledPath;
    }
}
