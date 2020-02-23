package edu.dartmouth.stayfocus.Focus;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.DebugUtils;
import android.util.Log;

import java.util.TimerTask;

public class TimerService extends Service {

    private static final String DEBUG_TAG = "Timer Service";
    private MyBinder myBinder;
    private Handler uIMsgHander;
    private int counter = 0;


    public TimerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(DEBUG_TAG, "onCreate called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(DEBUG_TAG,"onStartCommand called");

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
        uIMsgHander = null;
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(DEBUG_TAG, "onDestroy called");
    }

    public class MyBinder extends Binder {
        public void getUIMsgHandler(Handler msgHandler) {
            uIMsgHander = msgHandler;
        }
    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            try {
                counter += 1;

                if(uIMsgHander != null) {

                }
            } catch (Throwable t) {
                Log.e(DEBUG_TAG, "run timer task failed", t);
            }
        }
    }
}
