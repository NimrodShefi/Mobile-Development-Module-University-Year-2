package cm6226.c1989618.multipletimers;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ListViewAdapter extends ArrayAdapter<TimerData> {

    LayoutInflater inflater;
    private int resId;
    private SparseBooleanArray mSelectedItemsIds;
    private List<TimerData> timerDataList;

    public ListViewAdapter(Context context, int resource, List<TimerData> timerDataList) {
        super(context, resource, timerDataList);
        this.resId = resource;
        this.inflater = LayoutInflater.from(context);
        this.timerDataList = timerDataList;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(resId, parent, false);
        }

        TimerData timerData = this.getItem(position);

        AppCompatTextView topText = convertView.findViewById(R.id.timer_name);
        topText.setText(timerData.getName());

        AppCompatTextView bottomText = convertView.findViewById(R.id.timer_time);
        long hr = TimeUnit.MILLISECONDS.toHours(timerData.getTime());
        long min = TimeUnit.MILLISECONDS.toMinutes(timerData.getTime()) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timerData.getTime()));
        long sec = TimeUnit.MILLISECONDS.toSeconds(timerData.getTime()) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timerData.getTime()));
        String hours;
        String minutes;
        String seconds;
        if (sec < 10) {
            seconds = "0" + sec;
        } else {
            seconds = String.valueOf(sec);
        }

        if (min < 10) {
            minutes = "0" + min;
        } else {
            minutes = String.valueOf(min);
        }

        if (hr < 10) {
            hours = "0" + hr;
        } else {
            hours = String.valueOf(hr);
        }
        bottomText.setText(String.format("%s:%s:%s", hours, minutes, seconds));
        return convertView;
    }

    @Override
    public void remove(@Nullable TimerData object) {
        super.remove(object);
    }

    public void toggleSelection(int position){
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    private void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();

    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;

    }
}
