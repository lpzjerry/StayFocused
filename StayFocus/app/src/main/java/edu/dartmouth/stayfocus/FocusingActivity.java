package edu.dartmouth.stayfocus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dhims.timerview.TimerTextView;

import java.util.List;


public class FocusingActivity extends AppCompatActivity {
    public HomeWatcher mHomeWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focusing);

        // Ref: https://stackoverflow.com/questions/8881951/detect-home-button-press-in-android
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                distracted();
            }
            @Override
            public void onRecentAppsPressed() {
                distracted();
            }
        });
        mHomeWatcher.startWatch();

        // initialize hour, minute, second
        int hour = 0, minute = 0, second = 0;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            hour = bundle.getInt("hour");
            minute = bundle.getInt("minute");
            second = bundle.getInt("second");
        }
        // set TimerTextView
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

    @Override
    public void onBackPressed() {
        Log.d("pengze", "onBackPressed Called");
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    private void distracted() {
        // Todo: pause_timer.start();
        // Todo: send notification;
        Log.d("pengze", "YOU ARE DISTRACTED");
    }
}
