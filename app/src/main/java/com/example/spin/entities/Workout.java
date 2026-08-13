package com.example.spin.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Сущность тренировка
 */
@Entity(tableName = "workout")
public class Workout {
    //Идентификатор
    @PrimaryKey(autoGenerate = true)
    private int id;
    //Название
    private String name;
    //Время создания
    private long createdAt;
    //Количество упражнений в тренировке
    public int totalExercises;

    public Workout(String name) {
        this.name = name;
        this.createdAt = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}