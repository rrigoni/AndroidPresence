package com.github.rrigoni.presencesample;

import android.app.Application;
import android.widget.Toast;

import com.github.rrigoni.presence.Presence;

/**
 * Created by ronaldo on 2/1/17.
 */

public class SampleApp extends Application implements Presence.PresenceChangeListener {

    @Override
    public void onCreate() {
        super.onCreate();
        //Presence.init(this);
        Presence.init(this, new Presence.PresenceChangeListener() {
            @Override
            public void onBecameOnline() {
                Toast.makeText(SampleApp.this, "We are Online", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBecameOffline() {
                Toast.makeText(SampleApp.this, "We are Offline", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBecameOnline() {
        Toast.makeText(this, "We are Online", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBecameOffline() {
        Toast.makeText(this, "We are Offline", Toast.LENGTH_SHORT).show();
    }
}
