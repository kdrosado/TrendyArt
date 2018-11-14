package kdrosado.trendyart.model.artworks;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import kdrosado.trendyart.model.Links;

// Model class for the response of the Artsy API
public class ArtworkWrapperResponse implements Parcelable {

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;

    @SerializedName("_links")
    @Expose
    private Links links;

    @SerializedName("_embedded")
    @Expose
    private EmbeddedArtworks embeddedArtworks;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public EmbeddedArtworks getEmbeddedArtworks() {
        return embeddedArtworks;
    }

    public void setEmbeddedArtworks(EmbeddedArtworks embeddedArtworks) {
        this.embeddedArtworks = embeddedArtworks;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.totalCount);
        dest.writeParcelable(this.links, flags);
        dest.writeParcelable(this.embeddedArtworks, flags);
    }

    public ArtworkWrapperResponse() {
    }

    protected ArtworkWrapperResponse(Parcel in) {
        this.totalCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.links = in.readParcelable(Links.class.getClassLoader());
        this.embeddedArtworks = in.readParcelable(EmbeddedArtworks.class.getClassLoader());
    }

    public static final Creator<ArtworkWrapperResponse> CREATOR = new Creator<ArtworkWrapperResponse>() {
        @Override
        public ArtworkWrapperResponse createFromParcel(Parcel source) {
            return new ArtworkWrapperResponse(source);
        }

        @Override
        public ArtworkWrapperResponse[] newArray(int size) {
            return new ArtworkWrapperResponse[size];
        }
    };
}
