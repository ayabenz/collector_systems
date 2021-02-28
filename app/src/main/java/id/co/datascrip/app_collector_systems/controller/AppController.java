package id.co.datascrip.app_collector_systems.controller;

import android.app.Application;

/**
 * Created by alamsyah_putra on 3/29/2017.
 */
public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
