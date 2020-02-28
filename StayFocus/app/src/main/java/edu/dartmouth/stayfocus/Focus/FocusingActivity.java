package edu.dartmouth.stayfocus.Focus;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import com.dhims.timerview.TimerTextView;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

import edu.dartmouth.stayfocus.Entry;
import edu.dartmouth.stayfocus.R;

import static java.lang.String.*;


public class FocusingActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "pengze";
    private int hour = 0, minute = 0, second = 0;
    private long futureTimestamp;
    private long remainTimestamp;
    private String startTime, endTime, duration, success;
    public HomeWatcher mHomeWatcher;

    ServiceConnection timerServiceConnection;
    private boolean isBind = false;
    TimerHandler timerHandler;
    TextView timerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(DEBUG_TAG, "onCreate called");
        // Hide the bottom bar with back button, home button and recent apps button.
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(params);
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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            hour = bundle.getInt("hour");
            minute = bundle.getInt("minute");
            second = bundle.getInt("second");
        }

        futureTimestamp = System.currentTimeMillis() + (hour * 60 * 60 * 1000)
               + (minute * 60 * 1000) + (second * 1000);
        Log.d(DEBUG_TAG, "futureTimeStamp: " + futureTimestamp);
        // TimerTextView timerText = (TimerTextView) this.findViewById(R.id.timerText);
        // timerText.setEndTime(futureTimestamp);

        timerTextView = (TextView) findViewById(R.id.tv_countdown_timer);

        timerHandler = new TimerHandler();
        timerServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(DEBUG_TAG, "onServiceConnected");
                TimerService.MyBinder myBinder = (TimerService.MyBinder)service;
                myBinder.getUIMsgHandler(timerHandler);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        startAndBindService();

    }


    public void startAndBindService() {
        Intent intent = new Intent(this, TimerService.class);
        intent.putExtra("timeStamp", futureTimestamp);
        startService(intent);

        Log.d(DEBUG_TAG, "start service called");

        if(!isBind) {
            this.getApplicationContext().bindService(intent, timerServiceConnection, Context.BIND_AUTO_CREATE);
            isBind = true;
            Log.d(DEBUG_TAG, "Bind Service");
        }
    }

    public void unBindService() {
        this.getApplicationContext().unbindService(timerServiceConnection);
        isBind = false;
    }

    public class TimerHandler extends Handler {
        public void handleMessage(Message msg) {
            if(msg.what == TimerService.MSG_REMAIN_TIME) {
                Bundle bundle = msg.getData();
                remainTimestamp = bundle.getLong(TimerService.NAME_BUNDLE_REMAIN_TIME);
                // Log.d(DEBUG_TAG, "remainTimestamp: " + remainTimestamp);
                int seconds = (int) (remainTimestamp / 1000) % 60 ;
                int minutes = (int) ((remainTimestamp / (1000*60)) % 60);
                int hours   = (int) ((remainTimestamp / (1000*60*60)) % 24);
                // Log.d(DEBUG_TAG, hours + "h " + minutes + "min " + seconds +"s");
                // Toast.makeText(getApplicationContext(),hours + "h " + minutes + "min " + seconds +"s" , Toast.LENGTH_SHORT).show();
                timerTextView.setText(format("%02d:%02d:%02d", hours, minutes, seconds));
                if (hours <= 0 && minutes <= 0 && seconds <= 0) {
                    FocusingActivity.this.finish();
                }
            }
        }
    }

    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert activityManager != null;
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

    @Override
    public void onBackPressed() {} // DO NOTHING

    private void distracted() {
        // Todo: pause_timer.start();
        // Todo: send notification;
        Log.d("pengze", "YOU ARE DISTRACTED");
        Intent intent = new Intent(FocusingActivity.this, NotifyService.class);
        FocusingActivity.this.startService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Todo: timer < threshold ? continue : break;
        Log.d("pengze", "YOU ARE FOCUSED");
        Intent intent = new Intent();
        intent.setAction(NotifyService.ACTION);
        intent.putExtra(NotifyService.STOP_SERVICE_BROADCAST_KEY,
                NotifyService.RQS_STOP_SERVICE);
        sendBroadcast(intent);
    }

    @Override
    public void finish() {
        super.finish();

        Intent intent = new Intent();
        intent.setAction(NotifyService.ACTION);
        intent.putExtra(NotifyService.STOP_SERVICE_BROADCAST_KEY,
                NotifyService.RQS_STOP_SERVICE);
        sendBroadcast(intent);

        // TODO return Entry
        Entry entry = new Entry();
    }
}
