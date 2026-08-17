package com.example.spin.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import com.example.spin.entities.Workout;
import java.util.List;

/**
 * DAO для тренировки
 */
@Dao
public interface WorkoutDao {
    @Insert
    long insert(Workout workout);

    @Query("SELECT * FROM workout ORDER BY createdAt DESC")
    List<Workout> getAllWorkouts();

    @Delete
    void delete(Workout workout);

    @Query("SELECT * FROM workout WHERE id = :id")
    Workout getWorkoutById(int id);
}
