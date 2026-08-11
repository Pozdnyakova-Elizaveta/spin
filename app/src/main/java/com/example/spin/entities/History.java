package com.example.spin.entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
@Getter
@Setter
@AllArgsConstructor
public class History {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int workoutId;
    public long date;
    public int totalExercises;
    public int completedExercises;
    public long lengthTraveledPath;
}
