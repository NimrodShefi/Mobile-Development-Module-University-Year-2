package cm6226.c1989618.multipletimers.appPages;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import cm6226.c1989618.multipletimers.R;

public class AboutUs extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.about_us_fragment, container, false);

        AppCompatButton goBack = view.findViewById(R.id.back_to_list_page);
        AppCompatTextView textView = view.findViewById(R.id.about_app);
        final AppCompatTextView seekBarValue = view.findViewById(R.id.rating_value);
        final AppCompatSeekBar seekBar = view.findViewById(R.id.rating);
        AppCompatButton submit = view.findViewById(R.id.submit_review);
        final MainActivity mainActivity = (MainActivity) getActivity();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // creating a Toast telling the user the data from the seekbar above is saved, when in reality nothing happens as even if it was saved, there would be no use for it as no one checks the data for this
        submit.setOnClickListener(v -> {
            Toast.makeText(view.getContext(), "Review submitted!", Toast.LENGTH_SHORT).show();
            seekBar.setProgress(0);
            seekBarValue.setText(String.valueOf(0));
        });

        goBack.setOnClickListener(v -> mainActivity.changeFragment(R.id.main_fragment_container, new TimerList(), "about us"));


        return view;
    }
}
