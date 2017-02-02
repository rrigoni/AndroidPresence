package com.github.rrigoni.presence;

/**
 * Created by ronaldo on 2/1/17.
 */

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ronaldo on 3/7/16.
 */
public class Presence implements Application.ActivityLifecycleCallbacks {

    private static final long CHECK_DELAY = 500;

    private static final String TAG = Presence.class.getName();

    public interface PresenceChangeListener {

        /**
         * Method called when the app became online. At least one screen is visible to the user.
         */
        public void onBecameOnline();

        /**
         * Method called when the app became offline. No screen is visible to the user.
         */
        public void onBecameOffline();
    }


    private static Presence instance;

    private boolean foreground = false, paused = true;
    private Handler handler = new Handler();
    private List<PresenceChangeListener> listeners = new CopyOnWriteArrayList<PresenceChangeListener>();
    private Runnable check;


    /**
     * Init the presence check.
     * @param application
     * @return
     */
    public static Presence init(Application application){
        if(!(application instanceof PresenceChangeListener)){
            throw new IllegalStateException("Application must implements Presence.PresenceChangeListener");
        }
        checkAndInit(application);
        instance.listeners.add((PresenceChangeListener) application);
        return instance;
    }


    /**
     * Init the presence check.
     * @param application
     * @param listener the listener
     * @return
     */
    public static Presence init(Application application, PresenceChangeListener listener){
        checkAndInit(application);
        instance.listeners.add(listener);
        return instance;
    }

    private static void checkAndInit(final Application application) {
        if (instance == null) {
            instance = new Presence();
            application.registerActivityLifecycleCallbacks(instance);
        }
    }

    public boolean isForeground(){
        return foreground;
    }

    public boolean isBackground(){
        return !foreground;
    }

    public void removeListener(PresenceChangeListener listener){
        listeners.remove(listener);
    }

    public boolean containsListener(PresenceChangeListener listener){
        return listeners.contains(listener);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        paused = false;
        boolean wasBackground = !foreground;
        foreground = true;

        if (check != null)
            handler.removeCallbacks(check);

        if (wasBackground){
            Log.i(TAG, "went foreground");
            for (PresenceChangeListener l : listeners) {
                try {
                    l.onBecameOnline();
                } catch (Exception exc) {
                    Log.e(TAG, "Listener threw exception!", exc);
                }
            }
        } else {
            Log.i(TAG, "still foreground");
        }
    }



    @Override
    public void onActivityPaused(Activity activity) {
        paused = true;

        if (check != null)
            handler.removeCallbacks(check);

        handler.postDelayed(check = new Runnable(){
            @Override
            public void run() {
                if (foreground && paused) {
                    foreground = false;
                    Log.i(TAG, "went background");
                    for (PresenceChangeListener l : listeners) {
                        try {
                            l.onBecameOffline();
                        } catch (Exception exc) {
                            Log.e(TAG, "Listener threw exception!", exc);
                        }
                    }
                } else {
                    Log.i(TAG, "still foreground");
                }
            }
        }, CHECK_DELAY);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

    @Override
    public void onActivityStarted(Activity activity) {}

    @Override
    public void onActivityStopped(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}
}
