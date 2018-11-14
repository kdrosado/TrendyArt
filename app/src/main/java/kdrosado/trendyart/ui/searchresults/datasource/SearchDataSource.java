package kdrosado.trendyart.ui.searchresults.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import kdrosado.trendyart.TrendyArtApp;
import kdrosado.trendyart.model.Links;
import kdrosado.trendyart.model.Next;
import kdrosado.trendyart.model.search.EmbeddedResults;
import kdrosado.trendyart.model.search.Result;
import kdrosado.trendyart.model.search.SearchWrapperResponse;
import kdrosado.trendyart.utils.NetworkState;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchDataSource extends PageKeyedDataSource<Long, Result> {

    private static final String LOG_TAG = SearchDataSource.class.getSimpleName();
    private TrendyArtApp mAppController;

    private final MutableLiveData<NetworkState> mNetworkState;
    private final MutableLiveData<NetworkState> mInitialLoading;

    private String mQueryString;
    private String mTypeString;
    private String mNextUrl;


    public SearchDataSource(TrendyArtApp appController, String queryWord, String typeWord) {
        mAppController = appController;
        mQueryString = queryWord;
        mTypeString = typeWord;

        mNetworkState = new MutableLiveData<>();
        mInitialLoading = new MutableLiveData<>();
    }

    public MutableLiveData getNetworkState() {
        return mNetworkState;
    }

    public MutableLiveData getLoadingState() {
        return mInitialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, Result> callback) {

        // Update NetworkState
        mInitialLoading.postValue(NetworkState.LOADING);
        mNetworkState.postValue(NetworkState.LOADING);

        Log.d(LOG_TAG, "loadInitial: query word: " + mQueryString);
        Log.d(LOG_TAG, "loadInitial: type word: " + mTypeString);

        mAppController.getArtsyApi().getSearchResults(mQueryString, params.requestedLoadSize, mTypeString).enqueue(
                new Callback<SearchWrapperResponse>() {

            SearchWrapperResponse searchResponse = new SearchWrapperResponse();
            List<Result> resultList = new ArrayList<>();

            Links links = new Links();
            Next next = new Next();

            @Override
            public void onResponse(@NonNull Call<SearchWrapperResponse> call, @NonNull Response<SearchWrapperResponse> response) {

                if (response.isSuccessful()) {
                    searchResponse = response.body();

                    if (searchResponse != null) {

                        mNetworkState.postValue(NetworkState.LOADED);
                        mInitialLoading.postValue(NetworkState.LOADED);

                        // Get the next link for paging the results
                        links = searchResponse.getLinks();
                        if (links != null) {
                            next = links.getNext();

                            if (next != null) {
                                mNextUrl = next.getHref();
                            }

                            Log.d(LOG_TAG, "loadInitial: Next page link: " + mNextUrl);
                        }

                        String receivedQuery = searchResponse.getQ();
                        Log.d(LOG_TAG, "Query word: " + receivedQuery);

                        EmbeddedResults embeddedResults = searchResponse.getEmbedded();
                        resultList = embeddedResults.getResults();

                        Log.d(LOG_TAG, "List of results: " + resultList.size());

                        // The response code is 200, but because of a typo, the API returns an empty list
                        if (resultList.size() == 0) {
                            // TODO: Show a message there is no data for this query
                            mInitialLoading.postValue(new NetworkState(NetworkState.Status.NO_RESULT));
                            mNetworkState.postValue(new NetworkState(NetworkState.Status.NO_RESULT));
                        }

                        callback.onResult(resultList, null, 2L);
                    }

                    Log.d(LOG_TAG, "Response code from initial load, onSuccess: " + response.code());

                } else {

                    mInitialLoading.postValue(new NetworkState(NetworkState.Status.FAILED));
                    mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED));

                    switch (response.code()) {
                        case 400:
                            //TODO: Make display the following error message:
                            // "Invalid type article,artist,artwork,city,fair,feature,gene,show,profile,sale,tag,page"
                            mNetworkState.postValue(new NetworkState(NetworkState.Status.NO_RESULT));
                            break;
                        case 404:

                            break;
                    }

                    Log.d(LOG_TAG, "Response code from initial load: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchWrapperResponse> call, @NonNull Throwable t) {

                mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED));
                Log.d(LOG_TAG, "Response code from initial load, onFailure: " + t.getMessage());
            }
        });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Result> callback) {

        // Ignore this, because we don't need to load anything before the initial load of data
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Result> callback) {

        Log.i(LOG_TAG, "Loading: " + params.key + " Count: " + params.requestedLoadSize);
        Log.d(LOG_TAG, "loadAfter: query word: " + mQueryString);
        Log.d(LOG_TAG, "loadAfter: type word: " + mTypeString);

        // Set Network State to Loading
        mNetworkState.postValue(NetworkState.LOADING);

        mAppController.getArtsyApi().getNextLinkForSearch(mNextUrl, params.requestedLoadSize, mTypeString).enqueue(new Callback<SearchWrapperResponse>() {

            SearchWrapperResponse searchResponse = new SearchWrapperResponse();
            List<Result> resultList = new ArrayList<>();

            Links links = new Links();
            Next next = new Next();

            @Override
            public void onResponse(@NonNull Call<SearchWrapperResponse> call, @NonNull Response<SearchWrapperResponse> response) {
                if (response.isSuccessful()) {

                    searchResponse = response.body();

                    if (searchResponse != null) {

                        EmbeddedResults embeddedResults = searchResponse.getEmbedded();

                        int totalCount = searchResponse.getTotalCount();
                        Log.d(LOG_TAG, "loadAfter: Total count: " + totalCount);

                        if (totalCount != 0) {
                            links = searchResponse.getLinks();
                            if (links != null) {
                                next = links.getNext();

                                // Try and catch block doesn't crash the app,
                                // and stops when there is no more next urls for next page
                                try {
                                    mNextUrl = next.getHref();
                                } catch (NullPointerException e) {
                                    Log.e(LOG_TAG, "The next.getHref() is null: " + e);
                                    // Return so that it stops repeating the same call
                                    return;
                                } catch (Exception e) {
                                    Log.e(LOG_TAG, "The general exception is: " + e);
                                    // Return so that it stops repeating the same call
                                    return;
                                }

                                Log.d(LOG_TAG, "loadAfter: Next page link: " + mNextUrl);
                            }

                        } else {
                            mInitialLoading.postValue(new NetworkState(NetworkState.Status.FAILED));
                        }

                        String receivedQuery = searchResponse.getQ();
                        Log.d(LOG_TAG, "Query word: " + receivedQuery);

                        if (embeddedResults.getResults() != null) {
                            long nextKey;

                            if (params.key == embeddedResults.getResults().size()) {
                                nextKey = 0;
                            } else {
                                nextKey = params.key + 100;
                            }

                            Log.d(LOG_TAG, "Next key : " + nextKey);

                            resultList = embeddedResults.getResults();

                            callback.onResult(resultList, nextKey);

                            Log.d(LOG_TAG, "List of Search Result loadAfter : " + resultList.size());
                        }

                        mNetworkState.postValue(NetworkState.LOADED);
                        mInitialLoading.postValue(NetworkState.LOADED);
                    }

                    Log.d(LOG_TAG, "Response code from initial load, onSuccess: " + response.code());

                } else {

                    mInitialLoading.postValue(new NetworkState(NetworkState.Status.FAILED));
                    mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED));

                    Log.d(LOG_TAG, "Response code from initial load: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchWrapperResponse> call, @NonNull Throwable t) {
                mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED));
                Log.d(LOG_TAG, "Response code from initial load, onFailure: " + t.getMessage());
            }
        });

    }
}
