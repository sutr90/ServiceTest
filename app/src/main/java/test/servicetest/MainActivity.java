package test.servicetest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tv = (TextView) findViewById(R.id.textView);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("Counter" + testService.counter);
            }
        });
    }

    TestService testService = null;

    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                                       IBinder binder) {
            TestService.LocalBinder b = (TestService.LocalBinder) binder;
            testService = b.getService();
            Log.i("CONNECTION", "on connected");
        }

        public void onServiceDisconnected(ComponentName className) {
            testService = null;
            Log.i("CONNECTION", "on disconnected");
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
        Log.i("ACTIVITY", "on pause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = new Intent(this, TestService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Log.i("ACTIVITY", "on resume");
    }


    @Override
    public void onStart() {
        super.onStart();
        startService();
        Log.i("ACTIVITY", "on start");
    }

    @Override
    protected void onDestroy() {
        super.onStop();
        stopService(new Intent(this, TestService.class));
        Log.i("ACTIVITY", "on stop");
    }

    void startService() {
        Intent i = new Intent(this, TestService.class);
        i.putExtra("name", true);
        startService(i);
    }
}
