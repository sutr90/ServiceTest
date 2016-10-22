package test.servicetest;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class TestService extends Service {
    public int counter;
    private ServiceThread thread;
    private boolean threadRunning;

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("SERVICE", "on bind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("SERVICE", "on unbind");
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        TestService getService() {
            return TestService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SERVICE", "on start command");
        if (!threadRunning) {
            thread.start();
            threadRunning = true;
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("SERVICE", "on create");

        thread = new ServiceThread();
        threadRunning = false;
        counter = 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("SERVICE", "on destroy");
        thread.interrupt();
        threadRunning = false;
    }

    private class ServiceThread extends Thread {
        ServiceThread() {
            Log.d("ServiceThread", "constructor");
        }

        public void run() {
            Log.i("ServiceThread", "started");
            while (true) {
                try {
                    Thread.sleep(5000);
                    TestService.this.counter++;
                    Log.i("ServiceThread", "tick");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}
