package edu.dartmouth.stayfocus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dhims.timerview.TimerTextView;

import edu.dartmouth.stayfocus.ui.countdowntimer.CountdownTimerFragment;

public class CountdownTimerActivity extends AppCompatActivity {
    public static int hour = 0, minute = 0, second = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countdown_timer_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CountdownTimerFragment.newInstance())
                    .commitNow();
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            hour = bundle.getInt("hour");
            minute = bundle.getInt("minute");
            second = bundle.getInt("second");
        }
    }

    public void onClickFocusInterrupt(View view) {
        this.finish();
    }

    public void onClickFocusTerminate(View view) {
        this.finish();
    }
}