package com.example.spin;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import com.example.spin.entities.Workout;
import com.example.spin.entities.Exercise;
import com.example.spin.entities.History;
import com.example.spin.dao.WorkoutDao;
import com.example.spin.dao.ExerciseDao;
import com.example.spin.dao.HistoryDao;

/**
 * База данных Room
 */
@Database(entities = {Workout.class, Exercise.class, History.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract WorkoutDao workoutDao();
    public abstract ExerciseDao exerciseDao();
    public abstract HistoryDao historyDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "cycling_database")
                    .build();
        }
        return instance;
    }
}