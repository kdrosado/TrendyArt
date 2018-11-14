package kdrosado.trendyart.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import kdrosado.trendyart.database.dao.FavArtworksDao;
import kdrosado.trendyart.database.entity.FavoriteArtworks;

// Helper tutorial: https://medium.com/@ajaysaini.official/building-database-with-room-persistence-library-ecf7d0b8f3e9
@Database(entities = {FavoriteArtworks.class}, version = 1, exportSchema = false)
public abstract class ArtworksDatabase extends RoomDatabase {

    private static final String TAG = ArtworksDatabase.class.getSimpleName();
    private static ArtworksDatabase INSTANCE;
    private static final Object LOCK = new Object();
    private static final String TRENDYART_DB_NAME = "trendyart.db";

    // Reference the DAO from the database class
    public abstract FavArtworksDao favArtworksDao();

    public static ArtworksDatabase getInstance(Context context) {
        if (INSTANCE == null) {

            synchronized (LOCK) {
                INSTANCE = create(context);
            }
        }

        Log.d(TAG, "Getting the database instance");
        return INSTANCE;
    }

    private static ArtworksDatabase create(Context context) {
        Log.d(TAG, "Creating new database instance");
        Builder<ArtworksDatabase> databaseBuilder =
                Room.databaseBuilder(context.getApplicationContext(),
                        ArtworksDatabase.class, TRENDYART_DB_NAME);

        return (databaseBuilder.build());
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
