package kdrosado.trendyart.database.dao;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import kdrosado.trendyart.database.entity.FavoriteArtworks;

@Dao
public interface FavArtworksDao {

    @Insert
    void insertArtwork(FavoriteArtworks favArtwork);

    @Query("SELECT * FROM fav_artworks")
    List<FavoriteArtworks> allArtworks();

    @Query("SELECT artwork_id FROM fav_artworks WHERE artwork_id = :artworkId")
    String getItemById(String artworkId);

    @Query("SELECT * FROM fav_artworks")
    DataSource.Factory<Integer, FavoriteArtworks> getAllArtworks();

    @Query("DELETE FROM fav_artworks WHERE artwork_id = :artworkId")
    void deleteArtwork(String artworkId);

    @Query("DELETE FROM fav_artworks")
    void deleteAllData();

}
