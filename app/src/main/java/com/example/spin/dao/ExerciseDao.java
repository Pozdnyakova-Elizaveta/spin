package com.example.spin.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.spin.entities.Exercise;
import java.util.List;

@Dao
public interface ExerciseDao {
    @Insert
    long insert(Exercise exercise);

    @Query("SELECT * FROM exercises WHERE workoutId = :workoutId ORDER BY orderNumber")
    List<Exercise> getExercisesForWorkout(int workoutId);

    @Query("DELETE FROM exercises WHERE workoutId = :workoutId")
    void deleteExercisesForWorkout(int workoutId);

    @Query("DELETE FROM exercises WHERE id = :id")
    void deleteExerciseById(int id);
}