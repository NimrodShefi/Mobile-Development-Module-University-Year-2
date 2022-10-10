package cm6226.c1989618.multipletimers.repositories;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import cm6226.c1989618.multipletimers.TimerData;

@Database(entities = {TimerData.class}, version = 5)
public abstract class TimerDatabase extends RoomDatabase {

    public abstract TimerRepository timerRepository();

}
