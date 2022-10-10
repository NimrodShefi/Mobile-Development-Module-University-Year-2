package cm6226.c1989618.multipletimers.appPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import android.app.Notification;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cm6226.c1989618.multipletimers.notificationsManager.Notifications;
import cm6226.c1989618.multipletimers.R;
import cm6226.c1989618.multipletimers.TimerData;
import cm6226.c1989618.multipletimers.repositories.TimerDatabase;

public class MainActivity extends AppCompatActivity {

    private TimerDatabase timerDatabase;
    private Handler handler;
    private List<TimerData> timersList;
    private HashMap<Integer, CountDownTimer> countdownTimers;
    private Toolbar toolbar;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = NotificationManagerCompat.from(this);

        // creating my own menu for the user to use
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // this will be where teh countdown timers are created and every time it is used, the data will be stored/retrieved from here, making it easy to share data across the app
        countdownTimers = new HashMap<>();

        // initialising the DB
        timerDatabase = Room.databaseBuilder(
                this,
                TimerDatabase.class,
                "TimerDatabase"
        ).fallbackToDestructiveMigration().build();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        handler = HandlerCompat.createAsync(Looper.getMainLooper());

        // this will store the timers in the db in a local variable so that when getting data from the DB is not good, it will be retrieved from here
        timersList = new ArrayList<>();
        executorService.execute(() -> {
            timersList = timerDatabase.timerRepository().getAllTimers();

            handler.post(() -> {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment_container, new TimerList())
                        .commit();
            });
        });
    }

    // used: http://android-doc.github.io/training/wearables/notifications/stacks.html
    // https://www.youtube.com/watch?v=ZC4x4eEBU9U&ab_channel=CodinginFlow and other videos from his channel
    /**
     * Here I am creating notification for each timer, and this will be called every time the method updateTimerView is called,
     * this means that the time in the notification gets updated as well every second. In addition to that, once there is more than 1 timer running
     * the notifications will collapse onto each other making them one that can be expanded to view them
     *
     * @param timerName - let me put the name of the timer in the notification
     * @param time - the tme remaining in String format
     * @param timerId - to allow me to easily update each notification, I use their unique value to make each notification unique and easily updatable
     */
    public void createNotification(String timerName, String time, int timerId) {
        Notification notification = new NotificationCompat.Builder(this, Notifications.CHANNEL_ID)
                .setSmallIcon(R.drawable.timer_notification)
                .setContentTitle(timerName)
                .setContentText(time)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setGroup("timer_group")
                .build();

        Notification summaryNotification = new NotificationCompat.Builder(this, Notifications.CHANNEL_ID)
                .setSmallIcon(R.drawable.timer_notification)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(timerName + " " + time)
                        .setSummaryText("Timers Running")
                )
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setGroup("timer_group")
                .setGroupSummary(true)
                .build();

        notificationManager.notify(timerId, notification);
        notificationManager.notify(0, summaryNotification);

        if (time.equals("00:00:00")) {
            notificationManager.cancel(timerId);
        }
    }

    // these methods is for when the menu I created is used
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.about_us:
                changeFragment(R.id.main_fragment_container, new AboutUs(), "timer page");
                return true;
            case R.id.timer_list:
                changeFragment(R.id.main_fragment_container, new TimerList(), "timer page");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * this is to allow me to change fragments in the app with the ability to go back to the previous page
     *
     * @param layoutContainer the id of fragment container that will show the rest of the pages of the app
     * @param fragment        the object that will put the data in the layoutContainer
     * @param addToBackStack  for when the back button is pressed
     */
    public void changeFragment(int layoutContainer, Fragment fragment, String addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager(); // this will give us access to the fragment above it
        fragmentManager.beginTransaction()
                .replace(layoutContainer, fragment)
                .addToBackStack(addToBackStack) // this allows to go back after clicking on the button using the back button of Android
                .commit();
    }

    /**
     * this is to allow me to change fragments in the app without the ability to go back to the previous page
     *
     * @param layoutContainer the id of fragment container that will show the rest of the pages of the app
     * @param fragment        the object that will put the data in the layoutContainer
     */
    public void changeFragment(int layoutContainer, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(layoutContainer, fragment)
                .commit();
    }

    /**
     * this is to know whether the TimerRunning or the TimerList fragments are visible
     *
     * @return whether or not the fragments looked for are found
     */
    public boolean getTimerRunningOrTimerListFragment() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof TimerRunning || fragment instanceof TimerList) {
                return true;
            }
        }
        return false;
    }


    public TimerDatabase getTimerDatabase() {
        return timerDatabase;
    }

    public List<TimerData> getTimersList() {
        return timersList;
    }

    public void setTimersList(List<TimerData> timersList) {
        this.timersList = timersList;
    }

    public HashMap<Integer, CountDownTimer> getCountdownTimers() {
        return countdownTimers;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

}
