package cm6226.c1989618.multipletimers.repositories;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import cm6226.c1989618.multipletimers.TimerData;

@Dao
public interface TimerRepository {

    @Query("SELECT * FROM TimerData")
    List<TimerData> getAllTimers();

    @Insert
    void insertTimers(TimerData... timerData);

    @Delete
    void deleteTimers(TimerData... timerData);

    @Query("DELETE FROM TimerData")
    void deleteAllTimers();

    @Query("SELECT * FROM TimerData WHERE id = :timerId")
    TimerData getTimerById(long timerId);

    @Query("SELECT * FROM TimerData WHERE id = (SELECT MAX(id) FROM TimerData)")
    TimerData getLastTimer();

    @Query("UPDATE TimerData SET time = :time WHERE id = :id")
    void updateTimeRemaining(int id, long time);

    @Query("UPDATE TimerData SET timerRunning = :status")
    void updateTimerStatus(boolean status);

}
