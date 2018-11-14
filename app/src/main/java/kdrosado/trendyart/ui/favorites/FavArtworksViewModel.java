package kdrosado.trendyart.ui.favorites;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.util.Log;

import java.util.List;

import kdrosado.trendyart.callbacks.ResultFromDbCallback;
import kdrosado.trendyart.database.entity.FavoriteArtworks;
import kdrosado.trendyart.repository.FavArtRepository;

public class FavArtworksViewModel extends AndroidViewModel {

    private static final String TAG = FavArtworksViewModel.class.getSimpleName();
    private static final int PAGE_SIZE = 20;

    private LiveData<PagedList<FavoriteArtworks>> mFavArtworkList;
    private FavArtRepository mRepository;


    public FavArtworksViewModel(Application application) {
        super(application);

        mRepository = FavArtRepository.getInstance(application);

        init(application);
        Log.d(TAG, "FavArtworksViewModel called");
    }

    /*
     Method for initializing the DataSourceFactory and for building the LiveData
    */
    private void init(Application application) {
        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(PAGE_SIZE)
                .setPageSize(PAGE_SIZE)
                .build();

        mFavArtworkList = new LivePagedListBuilder<>(FavArtRepository.getInstance(application)
                .getAllFavArtworks(),
                pagedListConfig).build();
    }

    public LiveData<PagedList<FavoriteArtworks>> getFavArtworkList() {
        return mFavArtworkList;
    }

    public List<FavoriteArtworks> getFavListForWidget() {
        return mRepository.getFavArtworksList();
    }

    public LiveData<PagedList<FavoriteArtworks>> refreshFavArtworkList(Application application) {

        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(PAGE_SIZE)
                .setPageSize(PAGE_SIZE)
                .build();

        mFavArtworkList = new LivePagedListBuilder<>(FavArtRepository.getInstance(application)
                .getAllFavArtworks(),
                pagedListConfig).build();

        Log.d(TAG, "refreshFavArtworkList is called.");
        return mFavArtworkList;
    }

    // TODO: Remove this method, because we don't need to call it from here
    public void insertItem(FavoriteArtworks favArtwork) {
        mRepository.insertItem(favArtwork);
    }

    // TODO: Remove this method, because we don't need to call it from here
    public void getItemById(String artworkId, ResultFromDbCallback resultFromDbCallback) {
        mRepository.executeGetItemById(artworkId, resultFromDbCallback);
    }

    public void deleteItem(String artworkId) {
        mRepository.deleteItem(artworkId);
    }

    public void deleteAllItems() {
        mRepository.deleteAllItems();
    }

    @Override
    protected void onCleared() {
        // Destroy the database instance
        //ArtworksDatabase.destroyInstance();
        super.onCleared();
    }
}
