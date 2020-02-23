package edu.dartmouth.stayfocus.ui.countdowntimer;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhims.timerview.TimerTextView;

import edu.dartmouth.stayfocus.CountdownTimerActivity;
import edu.dartmouth.stayfocus.R;

public class CountdownTimerFragment extends Fragment {

    private CountdownTimerViewModel mViewModel;

    public static CountdownTimerFragment newInstance() {
        return new CountdownTimerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.countdown_timer_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CountdownTimerViewModel.class);
        // TODO: Use the ViewModel

        long futureTimestamp = System.currentTimeMillis() + (CountdownTimerActivity.hour * 60 * 60 * 1000)
                + (CountdownTimerActivity.minute * 60 * 1000) + (CountdownTimerActivity.second * 1000);
        Log.d("pengze", futureTimestamp+"");
        TimerTextView timerText = (TimerTextView) getActivity().findViewById(R.id.timerText);
        timerText.setEndTime(futureTimestamp);
    }

    public void onClickFocusInterrupt(View view) {
    }

    public void onClickFocusTerminate(View view) {
    }
}
