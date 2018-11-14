package kdrosado.trendyart.model.artists;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import kdrosado.trendyart.model.Links;

public class ArtistWrapperResponse implements Parcelable {

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;

    @SerializedName("_links")
    @Expose
    private Links links;

    @SerializedName("_embedded")
    @Expose
    private EmbeddedArtists embeddedArtist;

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

    public EmbeddedArtists getEmbeddedArtist() {
        return embeddedArtist;
    }

    public void setEmbedded(EmbeddedArtists embeddedArtist) {
        this.embeddedArtist = embeddedArtist;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.totalCount);
        dest.writeParcelable(this.links, flags);
        dest.writeParcelable(this.embeddedArtist, flags);
    }

    public ArtistWrapperResponse() {
    }

    protected ArtistWrapperResponse(Parcel in) {
        this.totalCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.links = in.readParcelable(Links.class.getClassLoader());
        this.embeddedArtist = in.readParcelable(EmbeddedArtists.class.getClassLoader());
    }

    public static final Creator<ArtistWrapperResponse> CREATOR = new Creator<ArtistWrapperResponse>() {
        @Override
        public ArtistWrapperResponse createFromParcel(Parcel source) {
            return new ArtistWrapperResponse(source);
        }

        @Override
        public ArtistWrapperResponse[] newArray(int size) {
            return new ArtistWrapperResponse[size];
        }
    };
}
