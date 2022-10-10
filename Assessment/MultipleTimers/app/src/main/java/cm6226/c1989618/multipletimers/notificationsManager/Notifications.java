package cm6226.c1989618.multipletimers.notificationsManager;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

// used: https://www.youtube.com/watch?v=tTbd1Mfi-Sk&ab_channel=CodinginFlow
public class Notifications extends Application {

    public static final String CHANNEL_ID = "timer";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();

    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Timer",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Timer Running");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
