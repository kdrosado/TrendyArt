package kdrosado.trendyart.ui.searchresults;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.util.Log;

import kdrosado.trendyart.AppExecutors;
import kdrosado.trendyart.TrendyArtApp;
import kdrosado.trendyart.model.search.Result;
import kdrosado.trendyart.ui.searchresults.datasource.SearchDataSource;
import kdrosado.trendyart.ui.searchresults.datasource.SearchDataSourceFactory;
import kdrosado.trendyart.utils.NetworkState;

public class SearchFragmentViewModel extends ViewModel {

    private static final String TAG = SearchFragmentViewModel.class.getSimpleName();

    private static final int PAGE_SIZE = 10;
    private static final int INITIAL_SIZE_HINT = 10;
    private static final int PREFETCH_DISTANCE_HINT = 10;

    private SearchDataSourceFactory mSearchDataSourceFactory;

    private LiveData<NetworkState> mNetworkState;
    private LiveData<NetworkState> mInitialLoading;

    private LiveData<PagedList<Result>> mResultPagedList;
    private TrendyArtApp mApplication;
    private String mQueryWord;
    private String mTypeWord;


    public SearchFragmentViewModel(TrendyArtApp application, String queryWord, String typeWord) {
        mApplication = application;
        mQueryWord = queryWord;
        mTypeWord = typeWord;

        init(application, queryWord, typeWord);
    }

    private void init(Application application, String queryWord, String typeWord) {

        // Get an instance of the DataSourceFactory class
        mSearchDataSourceFactory = new SearchDataSourceFactory((TrendyArtApp) application, queryWord, typeWord);

        // Initialize the network state liveData
        mNetworkState = Transformations.switchMap(mSearchDataSourceFactory.getSearchDataSourceMutableLiveData(),
                new Function<SearchDataSource, LiveData<NetworkState>>() {
            @Override
            public LiveData<NetworkState> apply(SearchDataSource input) {
                return input.getNetworkState();
            }
        });

        // Initialize the Loading state liveData
        mInitialLoading = Transformations.switchMap(mSearchDataSourceFactory.getSearchDataSourceMutableLiveData(),
                new Function<SearchDataSource, LiveData<NetworkState>>() {
                    @Override
                    public LiveData<NetworkState> apply(SearchDataSource input) {
                        return input.getLoadingState();
                    }
                });


        // Configure the PagedList.Config
        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(INITIAL_SIZE_HINT)
                .setPrefetchDistance(PREFETCH_DISTANCE_HINT)
                .setPageSize(PAGE_SIZE)
                .build();

        Log.d(TAG, "SearchFragmentViewModel: PagedListConfig: " + mResultPagedList);

        mResultPagedList = new LivePagedListBuilder<>(mSearchDataSourceFactory, pagedListConfig)
                .setFetchExecutor(AppExecutors.getInstance().networkIO())
                .build();
    }

    public LiveData<NetworkState> getNetworkState() {
        return mNetworkState;
    }

    public LiveData<NetworkState> getInitialLoading() {
        return mNetworkState;
    }

    public LiveData<PagedList<Result>> getSearchResultsLiveData() {
        return mResultPagedList;
    }

    public LiveData<PagedList<Result>> refreshSearchLiveData(Application application, String queryWord, String typeWord) {
        // Get an instance of the DataSourceFactory class
        mSearchDataSourceFactory = new SearchDataSourceFactory((TrendyArtApp) application, queryWord, typeWord);

        // Configure the PagedList.Config
        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(INITIAL_SIZE_HINT)
                .setPrefetchDistance(PREFETCH_DISTANCE_HINT)
                .setPageSize(PAGE_SIZE)
                .build();

        Log.d(TAG, "SearchFragmentViewModel: PagedListConfig: " + pagedListConfig);

        mResultPagedList = new LivePagedListBuilder<>(mSearchDataSourceFactory, pagedListConfig)
                .setFetchExecutor(AppExecutors.getInstance().networkIO())
                .build();
        return mResultPagedList;
    }
}
