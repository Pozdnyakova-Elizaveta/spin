package com.example.spin.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercise")
@Getter
@Setter
@AllArgsConstructor
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int workoutId;
    private String position;
    private int duration; // в секундах
    private int resistance; // 1-10
    private int orderNumber;
}
