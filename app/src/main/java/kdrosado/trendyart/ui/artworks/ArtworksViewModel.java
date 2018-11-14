package kdrosado.trendyart.ui.artworks;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import kdrosado.trendyart.TrendyArtApp;
import kdrosado.trendyart.AppExecutors;
import kdrosado.trendyart.model.artworks.Artwork;
import kdrosado.trendyart.ui.artworks.datasource.ArtworkDataSource;
import kdrosado.trendyart.ui.artworks.datasource.ArtworkDataSourceFactory;
import kdrosado.trendyart.utils.NetworkState;

public class ArtworksViewModel extends AndroidViewModel {

    private LiveData<NetworkState> mNetworkState;
    private LiveData<NetworkState> mInitialLoading;

    public LiveData<PagedList<Artwork>> mArtworkLiveData;
    private ArtworkDataSourceFactory mArtworkDataSourceFactory;

    private static final int PAGE_SIZE = 80;
    private static final int INITIAL_SIZE_HINT = 50;
    private static final int PREFETCH_DISTANCE_HINT = 20;


    public ArtworksViewModel(Application application) {
        super(application);

        init(application);
    }

    /*
     Method for initializing the DataSourceFactory and for building the LiveData
    */
    private void init(Application application) {

        // Get an instance of the DataSourceFactory class
        mArtworkDataSourceFactory = new ArtworkDataSourceFactory((TrendyArtApp) application);

        // Initialize the network state liveData
        mNetworkState = Transformations.switchMap(mArtworkDataSourceFactory.getArtworksDataSourceLiveData(),
                new Function<ArtworkDataSource, LiveData<NetworkState>>() {
            @Override
            public LiveData<NetworkState> apply(ArtworkDataSource input) {
                return input.getNetworkState();
            }
        });

        // Initialize the Loading state liveData
        mInitialLoading = Transformations.switchMap(mArtworkDataSourceFactory.getArtworksDataSourceLiveData(),
                new Function<ArtworkDataSource, LiveData<NetworkState>>() {
                    @Override
                    public LiveData<NetworkState> apply(ArtworkDataSource input) {
                        return input.getInitialLoading();
                    }
                });

        // Configure the PagedList.Config
        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(INITIAL_SIZE_HINT)
                        // If not set, defaults to page size.
                        //A value of 0 indicates that no list items
                        // will be loaded until they are specifically requested
                        .setPrefetchDistance(PREFETCH_DISTANCE_HINT)
                        .setPageSize(PAGE_SIZE)
                        .build();


        mArtworkLiveData = new LivePagedListBuilder<>(mArtworkDataSourceFactory, pagedListConfig)
                //.setInitialLoadKey()
                .setFetchExecutor(AppExecutors.getInstance().networkIO())
                .build();
    }

    public LiveData<NetworkState> getNetworkState() {
        return mNetworkState;
    }

    public LiveData<NetworkState> getInitialLoading() {
        return mNetworkState;
    }

    public LiveData<PagedList<Artwork>> getArtworkLiveData() {
        return mArtworkLiveData;
    }

    public LiveData<PagedList<Artwork>> refreshArtworkLiveData() {
        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(INITIAL_SIZE_HINT)
                // If not set, defaults to page size.
                //A value of 0 indicates that no list items
                // will be loaded until they are specifically requested
                .setPrefetchDistance(PREFETCH_DISTANCE_HINT)
                .setPageSize(PAGE_SIZE)
                .build();


        mArtworkLiveData = new LivePagedListBuilder<>(mArtworkDataSourceFactory, pagedListConfig)
                .setFetchExecutor(AppExecutors.getInstance().networkIO())
                .build();

        return mArtworkLiveData;
    }
}
