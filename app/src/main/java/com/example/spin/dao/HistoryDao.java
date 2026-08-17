package com.example.spin.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.spin.entities.History;
import java.util.List;

/**
 * DAO для историй тренировок
 */
@Dao
public interface HistoryDao {
    @Insert
    long insert(History history);

    @Query("SELECT * FROM history ORDER BY date DESC")
    List<History> getAllHistory();
}
