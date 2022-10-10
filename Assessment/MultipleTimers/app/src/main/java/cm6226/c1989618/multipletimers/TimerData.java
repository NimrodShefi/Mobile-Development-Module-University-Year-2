package cm6226.c1989618.multipletimers;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TimerData {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "time")
    private long time; // this will be in milliseconds
    @ColumnInfo(name = "timerRunning")
    private boolean timerRunning; // this is also saved in the db so that when it is paused, the next time the application is loaded, the timer will not run

    public TimerData() {
    }

    public TimerData(String name, long time) {
        this.name = name;
        this.time = time;
        this.timerRunning = true; // timer will start running the moment the timer is created
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isTimerRunning() {
        return timerRunning;
    }

    public void setTimerRunning(boolean timerRunning) {
        this.timerRunning = timerRunning;
    }

}
