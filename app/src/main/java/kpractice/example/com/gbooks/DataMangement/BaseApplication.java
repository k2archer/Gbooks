package kpractice.example.com.gbooks.DataMangement;

import android.app.Application;
import android.content.Context;


public class BaseApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
    }

    public static BaseApplication getBaseApplication() {
        return (BaseApplication) BaseApplication.getContext().getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

}
