package kdrosado.trendyart.repository;

import android.app.Application;
import android.arch.paging.DataSource;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import kdrosado.trendyart.AppExecutors;
import kdrosado.trendyart.callbacks.ResultFromDbCallback;
import kdrosado.trendyart.database.ArtworksDatabase;
import kdrosado.trendyart.database.dao.FavArtworksDao;
import kdrosado.trendyart.database.entity.FavoriteArtworks;


public class FavArtRepository {

    private static final String TAG = FavArtRepository.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static FavArtRepository INSTANCE;
    private FavArtworksDao mFavArtworksDao;


    private FavArtRepository(Application application) {
        ArtworksDatabase artworksDatabase = ArtworksDatabase.getInstance(application);
        mFavArtworksDao = artworksDatabase.favArtworksDao();
    }

    public static FavArtRepository getInstance(Application application) {
        if (INSTANCE == null) {

            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new FavArtRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    public List<FavoriteArtworks> getFavArtworksList() {
        return mFavArtworksDao.allArtworks();
    }

    public DataSource.Factory<Integer, FavoriteArtworks> getAllFavArtworks() {

        return mFavArtworksDao.getAllArtworks();
    }

    // Delete a single item from the database
    public void deleteItem(final String artworkId) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Item is deleted from the db!");
                mFavArtworksDao.deleteArtwork(artworkId);
            }
        });
    }

    // Delete all list of favorite artworks
    // Create a warning dialog for the user before allowing them to delete all data
    public void deleteAllItems() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mFavArtworksDao.deleteAllData();
            }
        });
    }

    // Insert a new item into the database
    public void insertItem(FavoriteArtworks favArtwork) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Item is added to the db!");
                mFavArtworksDao.insertArtwork(favArtwork);
            }
        });
    }


    // Get an item by Id from the database
    public void executeGetItemById(String artworkId, ResultFromDbCallback resultFromDbCallback) {
        new getItemById(artworkId, mFavArtworksDao, resultFromDbCallback).execute();
    }


    // Query the item on a background thread via AsyncTask
    private static class getItemById extends AsyncTask<Void, Void, Boolean> {
        private String artworkId;
        private FavArtworksDao favArtworksDao;
        private ResultFromDbCallback resultFromDbCallback;

        private getItemById(String artworkId, FavArtworksDao favArtworksDao, ResultFromDbCallback resultFromDbCallback) {
            this.artworkId = artworkId;
            this.favArtworksDao = favArtworksDao;
            this.resultFromDbCallback = resultFromDbCallback;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean isFav = artworkId.equals(favArtworksDao.getItemById(artworkId));
            Log.d(TAG, "From doInBackground: Item exists in the db: " + isFav);

            return isFav;
        }

        @Override
        protected void onPostExecute(Boolean isFav) {
            resultFromDbCallback.setResult(isFav);
        }
    }

}
