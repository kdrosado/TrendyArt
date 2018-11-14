package kdrosado.trendyart.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import kdrosado.trendyart.TrendyArtApp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

// Help from this SO port: https://stackoverflow.com/a/25816086/8132331
public class ConnectivityUtils {

    private static final String TAG = ConnectivityUtils.class.getSimpleName();

    // Empty private constructor for preventing creation of instances of this class
    private ConnectivityUtils() {
    }

    // Always called on a background thread, otherwise will produce a NetworkOnMainThreadException
    private static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                TrendyArtApp.getInstance().getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static boolean isConnected() {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlConnection = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlConnection.setRequestProperty("User/Agent", "Android");
                urlConnection.setRequestProperty("Connection", "close");
                urlConnection.setConnectTimeout(1500);
                urlConnection.connect();
                return (urlConnection.getResponseCode() == 204 &&
                        urlConnection.getContentLength() == 0);
            } catch (IOException e) {
                Log.e(TAG, "Error checking internet connection", e) ;
            }
        } else {
            Log.d(TAG, "No network available!");
        }

        return false;
    }

}
