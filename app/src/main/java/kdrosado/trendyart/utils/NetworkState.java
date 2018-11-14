package kdrosado.trendyart.utils;

// Help from this tutorial: https://proandroiddev.com/8-steps-to-implement-paging-library-in-android-d02500f7fffe
public class NetworkState {

    public enum Status {
        RUNNING,
        SUCCESS,
        FAILED,
        NO_RESULT
    }

    private final Status mStatus;

    public static final NetworkState LOADED;
    public static final NetworkState LOADING;

    public NetworkState(Status status) {
        mStatus = status;
    }

    static {
        LOADED = new NetworkState(Status.SUCCESS);
        LOADING = new NetworkState(Status.RUNNING);
    }

    public Status getStatus() {
        return mStatus;
    }

}

