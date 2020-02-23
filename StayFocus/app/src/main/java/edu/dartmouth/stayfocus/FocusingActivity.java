package edu.dartmouth.stayfocus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dhims.timerview.TimerTextView;


public class FocusingActivity extends AppCompatActivity {
    private int hour = 0, minute = 0, second = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focusing);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            hour = bundle.getInt("hour");
            minute = bundle.getInt("minute");
            second = bundle.getInt("second");
        }
        long futureTimestamp = System.currentTimeMillis() + (hour * 60 * 60 * 1000)
                + (minute * 60 * 1000) + (second * 1000);
        TimerTextView timerText = (TimerTextView) this.findViewById(R.id.timerText);
        timerText.setEndTime(futureTimestamp);
    }

    public void onClickFocusInterrupt(View view) {
        this.finish();
    }

    public void onClickFocusTerminate(View view) {
        this.finish();
    }
}
