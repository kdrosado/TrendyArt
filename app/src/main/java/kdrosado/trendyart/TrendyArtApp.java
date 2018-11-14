package kdrosado.trendyart;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import kdrosado.trendyart.remote.ArtsyApiInterface;
import kdrosado.trendyart.remote.ArtsyApiManager;

// Singleton class that extends the Application.
// Singleton pattern, explained here: https://medium.com/exploring-code/how-to-make-the-perfect-singleton-de6b951dfdb0
public class TrendyArtApp extends Application {

    private ArtsyApiInterface mArtsyApi;
    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    // With volatile variable all the write will happen on volatile sInstance
    // before any read of sInstance variable
    private static volatile TrendyArtApp sInstance;

    // Set the theme to the whole app
    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        sAnalytics = GoogleAnalytics.getInstance(this);
    }

    // Source: https://developers.google.com/analytics/devguides/collection/android/v4/
    synchronized public Tracker getDefaultTracker() {
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }

        return sTracker;
    }

    public static TrendyArtApp getInstance() {
        // Double check locking pattern
        if (sInstance == null) {

            synchronized (TrendyArtApp.class) {
                // If there is no instance available, create one
                if (sInstance == null) {
                    sInstance = new TrendyArtApp();
                }
            }
        }

        return sInstance;
    }

    /*
    Helper method that gets the context of the application
     */
    private static TrendyArtApp get(Context context) {
        return (TrendyArtApp) context.getApplicationContext();
    }

    /*
    Method that is used for initializing  the ViewModel,
    because the ViewModel class has a parameter of ArtPlaceApp
     */
    public static TrendyArtApp create(Context context) {
        return TrendyArtApp.get(context);
    }


    /**
     * Method that creates a Retrofit instance from the ArtsyApiManager
     */
    public ArtsyApiInterface getArtsyApi() {
        if (mArtsyApi == null) {
            mArtsyApi = ArtsyApiManager.create();
        }
        return mArtsyApi;
    }

}
