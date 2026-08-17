package com.example.spin.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность упражнение
 */
@Entity(tableName = "exercise",
        foreignKeys = @ForeignKey(
                entity = Workout.class, // Родительская таблица
                parentColumns = "id",   // Первичный ключ в Workout
                childColumns = "workoutId", // Внешний ключ в Exercise
                onDelete = ForeignKey.CASCADE // При удалении тренировки удалить и ее упражнения
        ))
public class Exercise {
    //Идентификатор
    @PrimaryKey(autoGenerate = true)
    private int id;
    //Идентификатор тренировки, которой принадлежит упражнение
    private int workoutId;
    //Положение
    private String position;
    //Продолжительность
    private int duration; // в секундах
    //Сопротивление
    private int resistance; // 1-10
    //Номер упражнения в тренировке
    private int orderNumber;

    public int getId() {
        return id;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public String getPosition() {
        return position;
    }

    public int getDuration() {
        return duration;
    }

    public int getResistance() {
        return resistance;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Exercise(int workoutId, String position, int duration, int resistance, int orderNumber) {
        this.workoutId = workoutId;
        this.position = position;
        this.duration = duration;
        this.resistance = resistance;
        this.orderNumber = orderNumber;
    }
}
