package kdrosado.trendyart.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import kdrosado.trendyart.model.artworks.ArtistsLink;
import kdrosado.trendyart.model.artworks.MainImage;
import kdrosado.trendyart.model.search.Permalink;

public class ImageLinks implements Parcelable {

    /*
    Default image thumbnail.
    Doesn't need a size, it's always set to "medium"
     */
    @SerializedName("thumbnail")
    @Expose
    private Thumbnail thumbnail;

    /*
    Curied image location.
     */
    @SerializedName("image")
    @Expose
    private MainImage image;

    /*
    An external location on the artsy.net website.
    Used for sharing outside the app
     */
    @SerializedName("permalink")
    @Expose
    private Permalink permalink;

    /*
    Genes are like genres in the Artsy API
     */
    @SerializedName("genes")
    @Expose
    private Genes genes;

    @SerializedName("similar_artworks")
    @Expose
    private SimilarArtworksLink similarArtworks;

    /*@SerializedName("partner")
    @Expose
    private PartnerLink partnerLink;*/

    @SerializedName("artists")
    @Expose
    private ArtistsLink artists;


    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public MainImage getImage() {
        return image;
    }

    public void setImage(MainImage image) {
        this.image = image;
    }

    public ArtistsLink getArtists() {
        return artists;
    }

    public void setArtists(ArtistsLink artists) {
        this.artists = artists;
    }

    public Permalink getPermalink() {
        return permalink;
    }

    public Genes getGenes() {
        return genes;
    }

    public SimilarArtworksLink getSimilarArtworks() {
        return similarArtworks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.thumbnail, flags);
        dest.writeParcelable(this.image, flags);
        dest.writeParcelable(this.permalink, flags);
        dest.writeParcelable(this.genes, flags);
        dest.writeParcelable(this.similarArtworks, flags);
        dest.writeParcelable(this.artists, flags);
    }

    public ImageLinks() {
    }

    protected ImageLinks(Parcel in) {
        this.thumbnail = in.readParcelable(Thumbnail.class.getClassLoader());
        this.image = in.readParcelable(MainImage.class.getClassLoader());
        this.permalink = in.readParcelable(Permalink.class.getClassLoader());
        this.genes = in.readParcelable(Genes.class.getClassLoader());
        this.similarArtworks = in.readParcelable(SimilarArtworksLink.class.getClassLoader());
        this.artists = in.readParcelable(ArtistsLink.class.getClassLoader());
    }

    public static final Creator<ImageLinks> CREATOR = new Creator<ImageLinks>() {
        @Override
        public ImageLinks createFromParcel(Parcel source) {
            return new ImageLinks(source);
        }

        @Override
        public ImageLinks[] newArray(int size) {
            return new ImageLinks[size];
        }
    };
}
