package com.example.spin.entities;
import androidx.room.Entity;
import androidx.room.ForeignKey;
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
    private int id;
    //Идентификатор проведенной тернировки
    private int workoutId;
    //Название тренировки
    private String workoutName;
    //Дата проведение
    private long date;
    //Число завершенных упражнений
    private int completedExercises;
    //Всего упражнений
    private int totalExercises;
    //Пройденный путь за тренировку (в м)
    private long lengthTraveledPath;

    public String getWorkoutName() {
        return workoutName;
    }

    public int getTotalExercises() {
        return totalExercises;
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public History(int workoutId, String workoutName, long date, int completedExercises, int totalExercises, long lengthTraveledPath) {
        this.workoutId = workoutId;
        this.workoutName = workoutName;
        this.date = date;
        this.completedExercises = completedExercises;
        this.totalExercises = totalExercises;
        this.lengthTraveledPath = lengthTraveledPath;
    }
}
