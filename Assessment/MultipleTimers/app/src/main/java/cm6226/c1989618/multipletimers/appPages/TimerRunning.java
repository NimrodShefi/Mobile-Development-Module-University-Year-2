package cm6226.c1989618.multipletimers.appPages;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.Fragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cm6226.c1989618.multipletimers.R;
import cm6226.c1989618.multipletimers.TimerData;

public class TimerRunning extends Fragment {

    private int id;
    private String name;
    private long time;
    private boolean timerRunning;
    private CountDownTimer countDownTimer;
    private AppCompatButton status;
    private AppCompatButton plusOne;
    private AppCompatButton minusOne;
    private AppCompatTextView hours;
    private AppCompatTextView minutes;
    private AppCompatTextView seconds;
    private ExecutorService executorService;
    private Handler handler;
    private MainActivity mainActivity;
    private MediaPlayer mediaPlayer;

    // I have 2 constructors as there are 2 different scenarios where TimerRunning is being initiated. The first one is when a brand new countdown timer is being created and doesn't need to be initialised
    public TimerRunning(int id, String timerName, long time, boolean timerRunning) {
        this.id = id;
        this.name = timerName;
        this.time = time;
        this.timerRunning = timerRunning;
    }

    // the second constructor is for when the countdown timer is already created and just needs to be connected to the fragment
    public TimerRunning(int id, String timerName, long time, boolean timerRunning, CountDownTimer countDownTimer) {
        this.id = id;
        this.name = timerName;
        this.time = time;
        this.timerRunning = timerRunning;
        this.countDownTimer = countDownTimer;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.timer_running_fragment, container, false);
        executorService = Executors.newSingleThreadExecutor();
        handler = HandlerCompat.createAsync(Looper.getMainLooper());
        mainActivity = (MainActivity) getActivity();

        // initialising the variables used
        AppCompatTextView timerName = view.findViewById(R.id.name);
        plusOne = view.findViewById(R.id.plus_one_minute);
        minusOne = view.findViewById(R.id.minus_one_minute);
        AppCompatButton goBack = view.findViewById(R.id.back_to_list_page);
        status = view.findViewById(R.id.status_button);
        hours = view.findViewById(R.id.hours);
        minutes = view.findViewById(R.id.minutes);
        seconds = view.findViewById(R.id.seconds);
        timerName.setText(name);

        // in here I am checking whether the timer was paused or not, and then if it wasn't paused (it's running) I make sure that if the countdown timer doesn't exist yet, it is created and added to teh hashmap
        // and if it already exists, I just make sure that it starts to run on the correct time and there aren't any same countdown timers that will run in the background
        if (timerRunning) {
            status.setText(R.string.pause);
            if (mainActivity.getCountdownTimers().get(id) == null) {
                startTimer();
            } else {
                updateTimer();
            }
        } else {
            status.setText(R.string.start);
        }

        updateTimerView();

        // creating all of the onClick listeners
        // this listener is for when the pause/start button is clicked
        status.setOnClickListener(v ->

        {
            if (timerRunning) {
                timerRunning = false;
                status.setText(R.string.start);
                stopTimer();
                Toast.makeText(view.getContext(), "TIMER PAUSED", Toast.LENGTH_SHORT).show();
            } else {
                timerRunning = true;
                status.setText(R.string.pause);
                startTimer();
                Toast.makeText(view.getContext(), "TIMER STARTED", Toast.LENGTH_SHORT).show();
            }
        });

        // this is when i am adding 1 minute to the timer
        plusOne.setOnClickListener(v ->

        {
            time += TimeUnit.MINUTES.toMillis(1);
            updateTimer();
            Toast.makeText(view.getContext(), "+1 minute", Toast.LENGTH_SHORT).show();
        });

        // in here I am trying to remove one minute from teh timer, but it will only run when there is more than 1 minute remaining for the timer
        minusOne.setOnClickListener(v ->

        {
            if (time < TimeUnit.MINUTES.toMillis(1)) {
                Toast.makeText(view.getContext(), "can't lower the time", Toast.LENGTH_SHORT).show();
            } else {
                time -= TimeUnit.MINUTES.toMillis(1);
                updateTimer();
                Toast.makeText(view.getContext(), "-1 minute", Toast.LENGTH_SHORT).show();
            }

        });

        // this is for when the user wants to go back to the TimerList fragment
        goBack.setOnClickListener(v -> executorService.execute(() ->

        {
            mainActivity.getTimerDatabase().timerRepository().updateTimerStatus(timerRunning);
            handler.post(() -> mainActivity.changeFragment(R.id.main_fragment_container, new TimerList(), name));
        }));

        return view;
    }

    // in here I am initialising the countdown timer. This means that when a new timer is being created or an existing timer is modified, it goes through here
    private void startTimer() {
        countDownTimer = new CountDownTimer(time, TimeUnit.SECONDS.toMillis(1)) {
            // the onTick method runs every second and updated both the db and the view of the user with the remaining time
            @Override
            public void onTick(long millisUntilFinished) {
                time = millisUntilFinished;
                executorService.execute(() -> mainActivity.getTimerDatabase().timerRepository().updateTimeRemaining(id, time));
                updateTimerView();
            }

            // this runs when the countdown timer is finished. Once the timer is finished it is removed from the db as it is no longer needed, and it will disappear from user view
            // when it is finished, depending on which fragment the user is on, it might take him to the TimerList fragment.
            @Override
            public void onFinish() {
                executorService.execute(() -> {
                    TimerData timerDataToRemove = mainActivity.getTimerDatabase().timerRepository().getTimerById(id);
                    mainActivity.getTimerDatabase().timerRepository().deleteTimers(timerDataToRemove);
                    handler.post(() -> {
                        try {
                            createAlert();
                            if (mainActivity.getTimerRunningOrTimerListFragment()) {
                                mainActivity.changeFragment(R.id.main_fragment_container, new TimerList());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                });
            }
        }.start();
        mainActivity.getCountdownTimers().put(id, countDownTimer);
        setCountDownTimer(mainActivity.getCountdownTimers().get(id));
    }

    // this is for when teh user want to pause the timer.
    // It is needed to completely stop the timer as there is not real pause function for the CountDownTimer class object and just recreate it with the updated time remaining
    private void stopTimer() {
        countDownTimer.cancel();
        mainActivity.getCountdownTimers().remove(id);
    }

    // this is ran every time there is a change in the time remaining as it stops the and then restarts it and updates the view of the user
    public void updateTimer() {
        stopTimer();
        startTimer();
        updateTimerView();
    }

    // this is where the user view is updated, by making sure that the remaining time is properly divided into seconds, minutes and hours and also put into proper form of 00:00:00
    public void updateTimerView() {
        long hr = TimeUnit.MILLISECONDS.toHours(time);
        long min = TimeUnit.MILLISECONDS.toMinutes(time) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time));
        long sec = TimeUnit.MILLISECONDS.toSeconds(time) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time));
        if (sec < 10) {
            seconds.setText("0" + sec);
        } else {
            seconds.setText(String.valueOf(sec));
        }

        if (min < 10) {
            minutes.setText("0" + min);
        } else {
            minutes.setText(String.valueOf(min));
        }

        if (hr < 10) {
            hours.setText("0" + hr);
        } else {
            hours.setText(String.valueOf(hr));
        }
        String time = String.format("%s:%s:%s", hours.getText(), minutes.getText(), seconds.getText());
        mainActivity.createNotification(name, time, id);
    }

    // this is ran when the timer is finished and creates an alert for the user by playing music and popup message that will stop the message when clicking on DISMISS
    private void createAlert() {
        mediaPlayer = MediaPlayer.create(mainActivity, R.raw.time_alarm);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setCancelable(true);
        builder.setMessage(String.format("Timer %s is finished!", name));
        builder.setPositiveButton("DISMISS", (dialog, which) -> stopMediaPlayer());
        builder.setOnCancelListener(dialog -> stopMediaPlayer());
        builder.show();
    }

    private void stopMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public String getName() {
        return name;
    }

    public void setCountDownTimer(CountDownTimer countDownTimer) {
        this.countDownTimer = countDownTimer;
    }

}
