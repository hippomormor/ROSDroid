package com.app.org;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button startButton, quitButton;
    boolean musicIsBound = false;

    ServiceConnection serviceConnection = new ServiceConnection() {
        MusicService musicService;

        public void onServiceConnected(ComponentName name, IBinder binder) {
            musicService = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    void doBindService() {
        bindService(new Intent(this,MusicService.class),
                serviceConnection, Context.BIND_AUTO_CREATE);
        musicIsBound = true;
    }

    void doUnbindService() {
        if(musicIsBound) {
            unbindService(serviceConnection);
            musicIsBound = false;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.startButton);
        quitButton = (Button) findViewById(R.id.quitButton);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!preferences.contains("music")) {
            preferences.edit()
                    .putBoolean("music", true)
                    .putBoolean("sound", true)
                    .putBoolean("online", true)
                    .apply();
        }
        startButton.setOnClickListener(this);
        quitButton.setOnClickListener(this);

        doBindService();
    }

    @Override
    protected void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.options) {
            Intent start = new Intent(this, OptionsActivity.class);
            startActivity(start);
        } else if (item.getItemId() == R.id.quit)
            System.exit(0);
        else
            return super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view == startButton) {
            Intent start = new Intent(this, GameActivity.class);
            startActivity(start);
        } else if (view == quitButton)
            System.exit(0);
    }

}
