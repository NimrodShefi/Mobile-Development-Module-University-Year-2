package cm6226.c1989618.multipletimers.appPages;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.view.ActionMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cm6226.c1989618.multipletimers.ListViewAdapter;
import cm6226.c1989618.multipletimers.R;
import cm6226.c1989618.multipletimers.TimerData;

public class TimerList extends Fragment {

    private List<TimerData> data;
    private Handler handler;
    private boolean running;
    private Runnable listUpdater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_list_fragment, container, false);
        final MainActivity mainActivity = (MainActivity) getActivity();
        final ListView listView = view.findViewById(R.id.timer_list);
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        handler = HandlerCompat.createAsync(Looper.getMainLooper());

        // where the timers are stored for the ListView object
        data = new ArrayList<>();

        // creating my own adapter to allow me to show the data that I want in a certain format on the listView variable
        final ListViewAdapter listViewAdapter = new ListViewAdapter(
                mainActivity,
                R.layout.timer_list_format,
                data
        );

        // giving the user the ability to choose multiple items in listView
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setAdapter(listViewAdapter);

        // getting data from the DB of all timers so that they can be shown in the listView object
        executorService.execute(() -> {
            final List<TimerData> l = mainActivity.getTimerDatabase().timerRepository().getAllTimers();
            mainActivity.setTimersList(l);

            handler.post(() -> {
                data.addAll(l);
                listViewAdapter.notifyDataSetChanged();
            });
        });

        // taken from: https://stackoverflow.com/questions/14295150/how-to-update-a-textview-in-an-activity-constantly-in-an-infinite-loop
        // this Runnable object allows each timer in the list to update it's time every second so that the user can see the remaining time also on the overall page and not just when clicking on a timer
        listUpdater = new Runnable() {
            @Override
            public void run() {
                if (!running){
                    return;
                } else {
                    executorService.execute(() -> {
                        data.clear();
                        data.addAll(mainActivity.getTimerDatabase().timerRepository().getAllTimers());
                        handler.post(listViewAdapter::notifyDataSetChanged);
                    });
                    handler.postDelayed(this, 1000);
                }
            }
        };

        final FloatingActionButton addTimer = view.findViewById(R.id.add_timer);
        addTimer.setOnClickListener(view12 -> mainActivity.changeFragment(R.id.main_fragment_container, new NewTimer(), "Timer List"));

        // when clicking on an item in the list, it will take the user to the TimerRunning page where the user will be able to control the timer by stopping the timer, adding/removing 1 minute
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            final TimerData[] timerData = new TimerData[1];
            executorService.execute(() -> {
                int id1 = data.get(position).getId();
                timerData[0] = mainActivity.getTimerDatabase().timerRepository().getTimerById(id1);
                handler.post(() -> mainActivity.changeFragment(R.id.main_fragment_container, new TimerRunning(timerData[0].getId(), timerData[0].getName(), timerData[0].getTime(), timerData[0].isTimerRunning(), mainActivity.getCountdownTimers().get(timerData[0].getId())), "Timer List"));
            });
        });

        final List<Integer> selectedItemsPosition = new ArrayList<>();
        // https://stackoverflow.com/questions/37696980/get-the-position-of-selected-item-in-action-mode
        // creating the ability to long click on the app, which will use teh ability to select multiple items in the list to delete the selected items
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // this is ran every time an item is selected/deselected
                int checkedCount = listView.getCheckedItemCount();
                mode.setTitle(checkedCount + " selected");
                listViewAdapter.toggleSelection(position);
                if (checked) {
                    selectedItemsPosition.add(position);
                } else {
                    selectedItemsPosition.remove(selectedItemsPosition.indexOf(position));
                }
            }

            @SuppressLint("RestrictedApi")
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // this is ran when I enter the listener
                mainActivity.getToolbar().setVisibility(View.GONE);
                mode.getMenuInflater().inflate(R.menu.delete_menu, menu);
                selectedItemsPosition.clear();
                addTimer.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // this is ran when I click on the delete button that was created in the menu I created
                // I get all of the chosen items and then I get their id so that I can delete them from the DB and stop the countdown and then update the listView
                if (item.getItemId() == R.id.delete_mode) {
                    SparseBooleanArray selected = listViewAdapter.getSelectedIds();
                    for (int i = selected.size() - 1; i >= 0; i--) {
                        if (selected.valueAt(i)) {
                            TimerData selectedItem = listViewAdapter.getItem(selected.keyAt(i));
                            listViewAdapter.remove(selectedItem);
                        }
                    }
                    executorService.execute(() -> {
                        TimerData[] timersToDelete = new TimerData[selectedItemsPosition.size()];
                        int i = 0;
                        for (Integer timerPos : selectedItemsPosition) {
                            timersToDelete[i] = mainActivity.getTimersList().get(timerPos);
                            CountDownTimer countDownTimer = mainActivity.getCountdownTimers().get(timersToDelete[i].getId());
                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                                mainActivity.getCountdownTimers().remove(timersToDelete[i].getId());
                            }
                            i++;
                        }
                        mainActivity.getTimerDatabase().timerRepository().deleteTimers(timersToDelete);
                        data = mainActivity.getTimerDatabase().timerRepository().getAllTimers();
                        mainActivity.setTimersList(data);
                    });
                    mode.finish();
                    return true;
                }
                return false;
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // this is ran when I exit the listener
                listViewAdapter.removeSelection();
                addTimer.setVisibility(View.VISIBLE);
                mainActivity.getToolbar().setVisibility(View.VISIBLE);
                listViewAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    // these 2 methods run when the user goes to and from the fragment, which also initiates the updating of the viewing of the time remaining while on this page
    @Override
    public void onResume() {
        super.onResume();
        running = true;
        handler.post(listUpdater);
    }

    @Override
    public void onPause() {
        super.onPause();
        running = false;
    }
}
