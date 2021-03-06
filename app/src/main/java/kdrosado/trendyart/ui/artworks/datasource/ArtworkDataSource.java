package kdrosado.trendyart.ui.artworks.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import kdrosado.trendyart.TrendyArtApp;
import kdrosado.trendyart.model.Links;
import kdrosado.trendyart.model.Next;
import kdrosado.trendyart.model.artworks.Artwork;
import kdrosado.trendyart.model.artworks.ArtworkWrapperResponse;
import kdrosado.trendyart.model.artworks.EmbeddedArtworks;
import kdrosado.trendyart.utils.NetworkState;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtworkDataSource extends PageKeyedDataSource<Long, Artwork> {

    private static final String TAG = ArtworkDataSource.class.getSimpleName();

    private TrendyArtApp mAppController;

    private final MutableLiveData<NetworkState> mNetworkState;
    private final MutableLiveData<NetworkState> mInitialLoading;

    private String mNextUrl;


    public ArtworkDataSource(TrendyArtApp appController) {
        mAppController = appController;

        mNetworkState = new MutableLiveData();
        mInitialLoading = new MutableLiveData();
    }

    public MutableLiveData getNetworkState() {
        return mNetworkState;
    }

    public MutableLiveData getInitialLoading() {
        return mInitialLoading;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, Artwork> callback) {

        // Update NetworkState
        mInitialLoading.postValue(NetworkState.LOADING);
        mNetworkState.postValue(NetworkState.LOADING);

        mAppController.getArtsyApi().getArtsyResponse(params.requestedLoadSize).enqueue(new Callback<ArtworkWrapperResponse>() {
            ArtworkWrapperResponse artworkWrapperResponse = new ArtworkWrapperResponse();
            List<Artwork> artworkList = new ArrayList<>();

            Links links = new Links();
            Next next = new Next();

            @Override
            public void onResponse(@NonNull Call<ArtworkWrapperResponse> call, @NonNull Response<ArtworkWrapperResponse> response) {
                if (response.isSuccessful()) {
                    artworkWrapperResponse = response.body();
                    if (artworkWrapperResponse != null) {
                        EmbeddedArtworks embeddedArtworks = artworkWrapperResponse.getEmbeddedArtworks();

                        links = artworkWrapperResponse.getLinks();
                        if (links != null) {
                            next = links.getNext();
                            mNextUrl = next.getHref();
                            Log.d(TAG, "Link to next (loadInitial): " + mNextUrl);
                        }

                        callback.onResult(artworkList = embeddedArtworks.getArtworks(), null, 2L);

                        Log.d(TAG, "List of Artworks loadInitial : " + artworkList.size());

                        mNetworkState.postValue(NetworkState.LOADED);
                        mInitialLoading.postValue(NetworkState.LOADED);
                    }

                    Log.d(TAG, "Response code from initial load, onSuccess: " + response.code());
                } else {

                    mInitialLoading.postValue(new NetworkState(NetworkState.Status.FAILED));
                    mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED));

                    Log.d(TAG, "Response code from initial load: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArtworkWrapperResponse> call, @NonNull Throwable t) {

                mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED));
                Log.d(TAG, "Response code from initial load, onFailure: " + t.getMessage());
            }
        });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Artwork> callback) {

        // Ignore this, because we don't need to load anything before the initial load of data
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Artwork> callback) {

        Log.i(TAG, "Loading: " + params.key + " Count: " + params.requestedLoadSize);

        // Set Network State to Loading
        mNetworkState.postValue(NetworkState.LOADING);

            mAppController.getArtsyApi().getNextLink(mNextUrl, params.requestedLoadSize).enqueue(new Callback<ArtworkWrapperResponse>() {
                ArtworkWrapperResponse artworkWrapperResponse = new ArtworkWrapperResponse();
                List<Artwork> artworkList = new ArrayList<>();

                Links links = new Links();
                Next next = new Next();

                @Override
                public void onResponse(@NonNull Call<ArtworkWrapperResponse> call, @NonNull Response<ArtworkWrapperResponse> response) {

                    if (response.isSuccessful()) {
                        artworkWrapperResponse = response.body();
                        if (artworkWrapperResponse != null) {
                            EmbeddedArtworks embeddedArtworks = artworkWrapperResponse.getEmbeddedArtworks();

                            links = artworkWrapperResponse.getLinks();
                            if (links != null) {
                                next = links.getNext();

                                // Try and catch block doesn't crash the app,
                                // and stops when there is no more next urls for next page
                                try {
                                    mNextUrl = next.getHref();
                                } catch (NullPointerException e) {
                                    Log.e(TAG, "The next.getHref() is null: " + e);
                                    // Return so that it stops repeating the same call
                                    return;
                                } catch (Exception e) {
                                    Log.e(TAG, "The general exception is: " + e);
                                    // Return so that it stops repeating the same call
                                    return;
                                }

                                Log.d(TAG, "Link to next: " + mNextUrl);
                            }

                            if (embeddedArtworks.getArtworks() != null) {
                                //long nextKey = params.key == 100 ? null: params.key + 30;
                                long nextKey;

                                if (params.key == embeddedArtworks.getArtworks().size()) {
                                    nextKey = 0;
                                } else {
                                    nextKey = params.key + 100;
                                }

                                Log.d(TAG, "Next key : " + nextKey);

                                artworkList = embeddedArtworks.getArtworks();

                                callback.onResult(artworkList, nextKey);

                                Log.d(TAG, "List of Artworks loadAfter : " + artworkList.size());
                            }

                            Log.d(TAG, "List of Artworks loadInitial : " + artworkList.size());

                            mNetworkState.postValue(NetworkState.LOADED);
                            mInitialLoading.postValue(NetworkState.LOADED);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArtworkWrapperResponse> call, @NonNull Throwable t) {

                    mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED));
                    Log.d(TAG, "Response code from initial load, onFailure: " + t.getMessage());
                }
            });

    }
}
