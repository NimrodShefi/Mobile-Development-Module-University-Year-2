package cm6226.c1989618.multipletimers.appPages;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.Fragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cm6226.c1989618.multipletimers.MinMaxFilter;
import cm6226.c1989618.multipletimers.R;
import cm6226.c1989618.multipletimers.TimerData;

public class NewTimer extends Fragment {

    private AppCompatEditText hours;
    private AppCompatEditText minutes;
    private AppCompatEditText seconds;
    private AppCompatEditText timerName;
    private ExecutorService executorService;
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.new_timer_fragment, container, false);
        executorService = Executors.newSingleThreadExecutor();
        handler = HandlerCompat.createAsync(Looper.getMainLooper());
        final MainActivity mainActivity = (MainActivity) getActivity();

        hours = view.findViewById(R.id.hours);
        minutes = view.findViewById(R.id.minutes);
        seconds = view.findViewById(R.id.seconds);
        timerName = view.findViewById(R.id.timer_name);
        AppCompatButton resetButton = view.findViewById(R.id.reset_button);
        AppCompatButton startButton = view.findViewById(R.id.start_button);
        AppCompatButton goBack = view.findViewById(R.id.back_to_list_page);

        // this copied class (MinMaxFiler) makes it impossible for the user to input a number grater than 59
        // so that when the user is inputting data it will be done in the hr:min:sec format where minutes and seconds can't go above 60
        minutes.setFilters(new InputFilter[]{new MinMaxFilter(0, 59)});
        seconds.setFilters(new InputFilter[]{new MinMaxFilter(0, 59)});

        resetButton.setOnClickListener(v -> {
            hours.getText().clear();
            minutes.getText().clear();
            seconds.getText().clear();
            timerName.getText().clear();
            Toast.makeText(view.getContext(), "Timer was reset", Toast.LENGTH_SHORT).show();
        });

        // before creating the timer and sending the user to the TimerRunning fragment, the data is checked to ensure that the data is in the correct format and won't cause the program any problems
        // and if there are any problems it will make sure that either the data is being put in the correct format or prevent the user from continuing
        startButton.setOnClickListener(v -> {
            boolean hoursText = hours.getText().toString().matches("");
            boolean minutesText = minutes.getText().toString().matches("");
            boolean secondsText = seconds.getText().toString().matches("");
            boolean nameText = timerName.getText().toString().matches("");

            // making sure that the data is in the correct format before creating the timer
            if (hoursText && minutesText && secondsText) {
                Toast.makeText(view.getContext(), "please input data before creating the timer", Toast.LENGTH_SHORT).show();
            } else {
                if (hoursText) {
                    hours.setText(R.string._00);
                }
                if (minutesText) {
                    minutes.setText(R.string._00);
                }
                if (secondsText) {
                    seconds.setText(R.string._00);
                }
                if (nameText) {
                    timerName.setText("Timer " + Integer.parseInt(String.valueOf(mainActivity.getTimersList().size() + 1)));
                }
                long hour = TimeUnit.HOURS.toMillis(Integer.parseInt(hours.getText().toString()));
                long minute = TimeUnit.MINUTES.toMillis(Integer.parseInt(minutes.getText().toString()));
                long second = TimeUnit.SECONDS.toMillis(Integer.parseInt(seconds.getText().toString()));
                final long overallTime = hour + minute + second;
                // here I am adding the timer to the db and changing a fragment
                executorService.execute(() -> {
                    mainActivity.getTimerDatabase().timerRepository().insertTimers(new TimerData(timerName.getText().toString(), overallTime));
                    final TimerData timerData = mainActivity.getTimerDatabase().timerRepository().getLastTimer();
                    handler.post(() -> mainActivity.changeFragment(R.id.main_fragment_container, new TimerRunning(timerData.getId(), timerData.getName(), timerData.getTime(), timerData.isTimerRunning()), "New Timer"));
                });
            }
        });

        // this is for when the user wants to go back to the TimerList fragment
        goBack.setOnClickListener(v -> mainActivity.changeFragment(R.id.main_fragment_container, new TimerList()));

        return view;
    }
}
