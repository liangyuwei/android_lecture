package cn.zju.id21832004.liangyuwei;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.LongSparseArray;

import java.util.List;
import cn.iipc.android.tweetlib.Status;
import cn.iipc.android.tweetlib.YambaClient;
import cn.iipc.android.tweetlib.YambaClientException;

public class UpdateService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener{
    public UpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static final String TAG = "UpdateService";
    static long DELAY = 60000; // a minute
    public boolean runFlag = false;
    private Updater updater;
    static String username;
    static String password;

    @Override
    public void onCreate(){
        super.onCreate();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        DELAY = Long.parseLong(prefs.getString("interval", "60")) * 1000;
        username = prefs.getString("username", "student");
        password = prefs.getString("password", "password");

        // register Listener
        prefs.registerOnSharedPreferenceChangeListener(this);

        // Tweet update service
        YambaClient cloud = new YambaClient(username, password);
        try {
            List<Status> timeline = cloud.getTimeline(20);
            for (Status status : timeline){
                Log.d(TAG, String.format("%s: %s", status.getUser(), status.getMessage()));
            }
        } catch (YambaClientException e) {
            Log.e(TAG, "Failed to fetch the timeline", e);
            e.printStackTrace();
        }

        this.updater = new Updater();
        Log.d(TAG, "onCreated");
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId){
        if(!runFlag){
            this.runFlag = true;
            this.updater.start();
        }
        Log.d(TAG, "onStarted");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        this.runFlag = false;
        this.updater.interrupt();
        this.updater = null;
        Log.d(TAG, "onDestroyed");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals("username"))
            username = prefs.getString("username", "student");

        if (key.equals("password"))
            password = prefs.getString("password", "password");

        if (key.equals("interval"))
            DELAY = Long.parseLong(prefs.getString("interval", "60")) * 1000;

    }

    /*
     * Thread that performs the actual update from the online service
     */
    private class Updater extends Thread{

        public Updater() {
            super("UpdaterService-Thread");
        }

        @Override
        public void run(){
            while (runFlag){
                Log.d(TAG, "Running background thread");
                try{
                    // ...
                    Thread.sleep(DELAY);
                } catch (InterruptedException e){
                    runFlag = false;
                }
            } // while
        }
    } // Updater

}
