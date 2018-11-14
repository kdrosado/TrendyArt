package kdrosado.trendyart.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.v7.util.DiffUtil;

@Entity(tableName = "fav_artworks")
public class FavoriteArtworks {

    public static DiffUtil.ItemCallback<FavoriteArtworks> DIFF_CALLBACK = new DiffUtil.ItemCallback<FavoriteArtworks>() {

        @Override
        public boolean areItemsTheSame(FavoriteArtworks oldItem, FavoriteArtworks newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(FavoriteArtworks oldItem, FavoriteArtworks newItem) {
            return oldItem.equals(newItem);
        }
    };

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "artwork_id")
    private String artworkId;

    @ColumnInfo(name = "title")
    private String artworkTitle;

    @ColumnInfo(name = "slug")
    private String artworkSlug;

    @ColumnInfo(name = "category")
    private String artworkCategory;

    @ColumnInfo(name = "medium")
    private String artworkMedium;

    @ColumnInfo(name = "date")
    private String artworkDate;

    @ColumnInfo(name = "museum")
    private String artworkMuseum;

    @ColumnInfo(name = "thumbnail")
    private String artworkThumbnailPath;

    @ColumnInfo(name = "image")
    private String artworkImagePath;

    @ColumnInfo(name = "inch")
    private String artworkDimensInch;

    @ColumnInfo(name = "cm")
    private String artworkDimensCm;


    public FavoriteArtworks(String artworkId, String artworkTitle, String artworkSlug, String artworkCategory,
                            String artworkMedium, String artworkDate, String artworkMuseum,
                            String artworkThumbnailPath, String artworkImagePath, String artworkDimensInch,
                            String artworkDimensCm) {
        this.artworkId = artworkId;
        this.artworkTitle = artworkTitle;
        this.artworkSlug = artworkSlug;
        this.artworkCategory = artworkCategory;
        this.artworkMedium = artworkMedium;
        this.artworkDate = artworkDate;
        this.artworkMuseum = artworkMuseum;
        this.artworkThumbnailPath = artworkThumbnailPath;
        this.artworkImagePath = artworkImagePath;
        this.artworkDimensInch = artworkDimensInch;
        this.artworkDimensCm = artworkDimensCm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtworkId() {
        return artworkId;
    }

    public void setArtworkId(String artworkId) {
        this.artworkId = artworkId;
    }

    public String getArtworkTitle() {
        return artworkTitle;
    }

    public void setArtworkTitle(String artworkTitle) {
        this.artworkTitle = artworkTitle;
    }

    public String getArtworkSlug() {
        return artworkSlug;
    }

    public void setArtworkSlug(String artworkSlug) {
        this.artworkSlug = artworkSlug;
    }

    public String getArtworkCategory() {
        return artworkCategory;
    }

    public void setArtworkCategory(String artworkCategory) {
        this.artworkCategory = artworkCategory;
    }

    public String getArtworkMedium() {
        return artworkMedium;
    }

    public void setArtworkMedium(String artworkMedium) {
        this.artworkMedium = artworkMedium;
    }

    public String getArtworkDate() {
        return artworkDate;
    }

    public void setArtworkDate(String artworkDate) {
        this.artworkDate = artworkDate;
    }

    public String getArtworkMuseum() {
        return artworkMuseum;
    }

    public void setArtworkMuseum(String artworkMuseum) {
        this.artworkMuseum = artworkMuseum;
    }

    public String getArtworkThumbnailPath() {
        return artworkThumbnailPath;
    }

    public void setArtworkThumbnailPath(String artworkThumbnailPath) {
        this.artworkThumbnailPath = artworkThumbnailPath;
    }

    public String getArtworkImagePath() {
        return artworkImagePath;
    }

    public void setArtworkImagePath(String artworkImagePath) {
        this.artworkImagePath = artworkImagePath;
    }

    public String getArtworkDimensInch() {
        return artworkDimensInch;
    }

    public void setArtworkDimensInch(String artworkDimensInch) {
        this.artworkDimensInch = artworkDimensInch;
    }

    public String getArtworkDimensCm() {
        return artworkDimensCm;
    }

    public void setArtworkDimensCm(String artworkDimensCm) {
        this.artworkDimensCm = artworkDimensCm;
    }
}
