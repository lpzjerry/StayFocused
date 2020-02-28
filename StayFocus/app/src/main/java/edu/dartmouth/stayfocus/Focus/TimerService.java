package edu.dartmouth.stayfocus.Focus;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DebugUtils;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {

    private static final String DEBUG_TAG = "Timer";
    private MyBinder myBinder;
    private Handler uIMsgHandler;
    private Timer timer;
    private MyTask myTask;
    private long futureTimestamp;
    private long currentTimestamp;
    private long millisUntilFinished;
    public static final int MSG_REMAIN_TIME = 0;
    public static final String NAME_BUNDLE_REMAIN_TIME = "millisUntilFinished";
    private boolean timeout = false;


    public TimerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(DEBUG_TAG, "onCreate called");
        myBinder = new MyBinder();
        uIMsgHandler = null;

//        timer = new Timer();
//        myTask = new MyTask();
//
//        timer.scheduleAtFixedRate(myTask, 0, 1000L);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(DEBUG_TAG,"onStartCommand called");

        futureTimestamp = intent.getLongExtra("timeStamp", 0);
        currentTimestamp = System.currentTimeMillis();
        Log.d(DEBUG_TAG, "currentTime: "+currentTimestamp);
        millisUntilFinished = futureTimestamp - currentTimestamp;
        Log.d(DEBUG_TAG, "remainTime: "+millisUntilFinished);

        timer = new Timer();
        myTask = new MyTask();

        timer.scheduleAtFixedRate(myTask, 0, 1000L);

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(DEBUG_TAG, "onBind called");


        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(DEBUG_TAG, "onUnbind called");
        uIMsgHandler = null;
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myTask.cancel();
        Log.d(DEBUG_TAG, "onDestroy called");
    }

    public class MyBinder extends Binder {
        public void getUIMsgHandler(Handler msgHandler) {
            uIMsgHandler = msgHandler;
        }
    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {

            try {
                millisUntilFinished -= 1000L;
                Log.d(DEBUG_TAG, "millisUntilFinish: "+millisUntilFinished);
                if(millisUntilFinished < 0){
                    timeout = true;
                }
                if(uIMsgHandler != null && !timeout) {
                    Bundle bundle = new Bundle();
                    bundle.putLong(NAME_BUNDLE_REMAIN_TIME, millisUntilFinished);
                    Message message = uIMsgHandler.obtainMessage();
                    message.setData(bundle);
                    message.what = MSG_REMAIN_TIME;
                    uIMsgHandler.sendMessage(message);
                    Log.d(DEBUG_TAG,"message sent");
                }

                if(timeout) {
                    myTask.cancel();
                    timer.cancel();
                    timeout = false;
                }

            } catch (Throwable t) {
                Log.e(DEBUG_TAG, "run timer task failed", t);
            }
        }
    }

}
