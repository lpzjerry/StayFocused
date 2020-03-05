package edu.dartmouth.stayfocus.Focus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.dartmouth.stayfocus.Entry;
import edu.dartmouth.stayfocus.FirebaseHelper;
import edu.dartmouth.stayfocus.MainActivity;
import edu.dartmouth.stayfocus.R;

import static java.lang.Math.min;
import static java.lang.String.*;


public class FocusingActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "Timer";
    private int hour = 0, minute = 0, second = 0;
    private int init_hour, init_minute;
    private long futureTimestamp;
    private long remainTimestamp;
    private String startTime, endTime, duration, success;
    private boolean finished = false;
    public HomeWatcher mHomeWatcher;

    ServiceConnection timerServiceConnection = null;
    private boolean isBind = false;
    TimerHandler timerHandler;
    TextView timerTextView;

    Application appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(DEBUG_TAG, "onCreate called");
        //kxx: add for UI
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

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
            init_hour = hour;
            minute = bundle.getInt("minute");
            init_minute = minute;
            second = bundle.getInt("second");
        }
        if (init_hour == 0)
            duration = init_minute + " min";
        else
            duration = init_hour + " hrs "+init_minute + " min";

        futureTimestamp = System.currentTimeMillis() + (hour * 60 * 60 * 1000)
               + (minute * 60 * 1000) + (second * 1000);
        Log.d(DEBUG_TAG, "futureTimeStamp: " + futureTimestamp);
        // TimerTextView timerText = (TimerTextView) this.findViewById(R.id.timerText);
        // timerText.setEndTime(futureTimestamp);

        timerTextView = (TextView) findViewById(R.id.tv_countdown_timer);

        appContext = (Application) this.getApplicationContext();
        timerHandler = new TimerHandler();
        if (timerServiceConnection == null) {
            timerServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    Log.d(DEBUG_TAG, "onServiceConnected");
                    TimerService.MyBinder myBinder = (TimerService.MyBinder) service;
                    myBinder.getUIMsgHandler(timerHandler);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
            startAndBindService();
        }
    }


    public void startAndBindService() {
        Intent intent = new Intent(this, TimerService.class);
        intent.putExtra("timeStamp", futureTimestamp);
        startService(intent);

        Log.d(DEBUG_TAG, "start service called");

        if(!isBind) {
            appContext.bindService(intent, timerServiceConnection, Context.BIND_AUTO_CREATE);
            isBind = true;
            Log.d(DEBUG_TAG, "Bind Service");
        }
    }

    public void unBindService() {
        appContext.unbindService(timerServiceConnection);
        isBind = false;
    }

    public class TimerHandler extends Handler {
        public void handleMessage(Message msg) {
            if(msg.what == TimerService.MSG_REMAIN_TIME) {
                Bundle bundle = msg.getData();
                remainTimestamp = bundle.getLong(TimerService.NAME_BUNDLE_REMAIN_TIME);
                // Log.d(DEBUG_TAG, "remainTimestamp: " + remainTimestamp);
                int seconds = (int)Math.ceil( (remainTimestamp / 1000) % 60);
                int minutes = (int)Math.ceil( ((remainTimestamp / (1000*60)) % 60));
                int hours   = (int)Math.ceil( ((remainTimestamp / (1000*60*60)) % 24));
                // Log.d(DEBUG_TAG, hours + "h " + minutes + "min " + seconds +"s");
                // Toast.makeText(getApplicationContext(),hours + "h " + minutes + "min " + seconds +"s" , Toast.LENGTH_SHORT).show();
                timerTextView.setText(format("%02d:%02d:%02d", hours, minutes, seconds));
                if (hours <= 0 && minutes <= 0 && seconds <= 0) {
                    finished = true;
                    mHomeWatcher.stopWatch();
                    unBindService();
                    Intent timerService = new Intent(FocusingActivity.this.getApplicationContext(), TimerService.class);
                    stopService(timerService);
                    Intent notifyService = new Intent(FocusingActivity.this, NotifyService.class);
                    stopService(notifyService);
                    showResult();
                    //FocusingActivity.this.finish();
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
    public void onBackPressed() {
        if (finished) {
            this.finish();
        }
    } // DO NOTHING

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

//    @Override
//    public void finish() {
//        super.finish();
//        Log.d(DEBUG_TAG, "finish");
//
//        Intent intent = new Intent();
//        intent.setAction(NotifyService.ACTION);
//        intent.putExtra(NotifyService.STOP_SERVICE_BROADCAST_KEY,
//                NotifyService.RQS_STOP_SERVICE);
//        //sendBroadcast(intent);
//        appContext.stopService(intent);
//
//        // TODO return Entry
//        Entry entry = new Entry();
//        new FirebaseHelper().addEntry(entry);
//    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(DEBUG_TAG, "finish");

        Intent intent = new Intent(this, NotifyService.class);
        //intent.setAction(NotifyService.ACTION);
        //intent.putExtra(NotifyService.STOP_SERVICE_BROADCAST_KEY,
                //NotifyService.RQS_STOP_SERVICE);
        //sendBroadcast(intent);
        appContext.stopService(intent);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        startTime = dateFormat.format(date);
        Entry entry = new Entry(startTime, "", duration, "success");
        if (!finished) {
            entry.setSuccess("failed");
        }
        new FirebaseHelper().addEntry(entry);
    }

    private void showResult() {
        // TODO share
        View dialog_view = getLayoutInflater().inflate(R.layout.dialog_focus_result, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this,R.style.DialogTheme)
                .setView(dialog_view)
                .create();
        TextView resultMessage = dialog_view.findViewById(R.id.tvFocusResult);
        ImageView shareButton = dialog_view.findViewById(R.id.ivDialogResult);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "StayFocused! APP";
                String shareSub = "I have focused for " + duration + " in StayFocused! Come on and check out this amazing app! \n https://home.cs.dartmouth.edu/~pengze/stayfocused/";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareSub);
                startActivity(Intent.createChooser(myIntent, "Share using"));
                //FocusingActivity.this.finish();
            }
        });
        //alertDialog.setMessage("You stayed focused for " + duration);
        resultMessage.setText("You stayed focused for " + duration);

        /*alertDialog.setButton(Dialog.BUTTON_NEGATIVE,"Cool",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FocusingActivity.this.finish();
            }
        });
        alertDialog.setButton(Dialog.BUTTON_POSITIVE,"Share",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Stay Focus APP";
                String shareSub = "https://home.cs.dartmouth.edu/~pengze/stayfocus/";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareSub);
                startActivity(Intent.createChooser(myIntent, "Share using"));
                FocusingActivity.this.finish();
            }
        });*/
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                FocusingActivity.this.finish();
            }
        });
        alertDialog.show();


    }

}
