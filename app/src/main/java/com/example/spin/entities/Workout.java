package com.example.spin.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout")
@Getter
@Setter
public class Workout {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private long createdAt;

    public Workout(String name) {
        this.name = name;
        this.createdAt = System.currentTimeMillis();
    }
}