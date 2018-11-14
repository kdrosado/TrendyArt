package kdrosado.trendyart.utils;

import android.os.AsyncTask;
import android.util.Log;

import kdrosado.trendyart.R;
import kdrosado.trendyart.TrendyArtApp;
import kdrosado.trendyart.callbacks.SnackMessageListener;
import kdrosado.trendyart.ui.artworks.ArtworksFragment;

import java.lang.ref.WeakReference;

/**
 * AsyncTask that is used for requesting a network connection upon a Refresh button click from the user
 *
 * Note: It's not ideal in this case, because it's being destroyed with the Lifecylce of the Activity,
 * but it demonstrate a use case of AsyncTask that is needed for passing the Rubrics fro Capstone Stage 2
 */
public class RetrieveNetworkConnectivity extends AsyncTask<String, Void, String> {

    private static final String TAG = RetrieveNetworkConnectivity.class.getSimpleName();

    private WeakReference<ArtworksFragment> mContext;
    private SnackMessageListener mListener;

    boolean flag = false;
    private Exception mException;


    public RetrieveNetworkConnectivity(ArtworksFragment context, SnackMessageListener snackMessageListener) {
        mContext = new WeakReference<>(context);
        mListener = snackMessageListener;
    }

    @Override
    protected String doInBackground(String... result) {

        String snackMessage;
        try {
            boolean isConnected = ConnectivityUtils.isConnected();
            if (isConnected) {
                flag = true;
                Log.d(TAG, "doInBackground, network=true ");

                // Cannot extract the string here, because it's a static method
                snackMessage = TrendyArtApp.getInstance().getString(R.string.network_ok);

                return snackMessage;
            } else {
                flag = false;
                Log.d(TAG, "doInBackground, network= false");

                // Cannot extract the string here, because it's a static method
                snackMessage = TrendyArtApp.getInstance().getString(R.string.no_network);

                return snackMessage;
            }
        } catch (Exception e) {
            this.mException = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (mListener != null) {
            if (flag) {
                // Show a message to the user there is an internet connection
                mListener.showSnackMessage(result);

                Log.d(TAG, "onPostExecute called with connectivity ON");

            } else {
                // Show a message to the user there is No internet connection
                mListener.showSnackMessage(result);

                Log.d(TAG, "onPostExecute called with NO connectivity");
            }
        }
    }
}
