package edu.dartmouth.stayfocus.Focus;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import com.dhims.timerview.TimerTextView;

import java.util.concurrent.TimeUnit;

import edu.dartmouth.stayfocus.R;


public class FocusingActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "FocusingActivity";
    private int hour = 0, minute = 0, second = 0;
    private long futureTimestamp;
    private long remainTimestamp;

    ServiceConnection timerServiceConnection;
    private boolean isBind = false;
    TimerHandler timerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(DEBUG_TAG, "onCreate called");
        setContentView(R.layout.activity_focusing);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            hour = bundle.getInt("hour");
            minute = bundle.getInt("minute");
            second = bundle.getInt("second");
        }
        futureTimestamp = System.currentTimeMillis() + (hour * 60 * 60 * 1000)
                + (minute * 60 * 1000) + (second * 1000);


        //Log.d(DEBUG_TAG, "futureTimeStamp: " + futureTimestamp);
        TimerTextView timerText = (TimerTextView) this.findViewById(R.id.timerText);
        timerText.setEndTime(futureTimestamp);

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
                Log.d(DEBUG_TAG, "remainTimestamp: " + remainTimestamp);
                int seconds = (int) (remainTimestamp / 1000) % 60 ;
                int minutes = (int) ((remainTimestamp / (1000*60)) % 60);
                int hours   = (int) ((remainTimestamp / (1000*60*60)) % 24);
                Log.d(DEBUG_TAG, hours + "h " + minutes + "min " + seconds +"s");
                Toast.makeText(getApplicationContext(),hours + "h " + minutes + "min " + seconds +"s" , Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClickFocusInterrupt(View view) {
        this.finish();
    }

    public void onClickFocusTerminate(View view) {
        this.finish();
    }
}
