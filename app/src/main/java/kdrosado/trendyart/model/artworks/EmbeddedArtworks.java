package kdrosado.trendyart.model.artworks;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmbeddedArtworks implements Parcelable {

    @SerializedName("artworks")
    @Expose
    private List<Artwork> artworks;

    public List<Artwork> getArtworks() {
        return artworks;
    }

    public void setArtworks(List<Artwork> artworks) {
        this.artworks = artworks;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.artworks);
    }

    public EmbeddedArtworks() {
    }

    protected EmbeddedArtworks(Parcel in) {
        this.artworks = in.createTypedArrayList(Artwork.CREATOR);
    }

    public static final Creator<EmbeddedArtworks> CREATOR = new Creator<EmbeddedArtworks>() {
        @Override
        public EmbeddedArtworks createFromParcel(Parcel source) {
            return new EmbeddedArtworks(source);
        }

        @Override
        public EmbeddedArtworks[] newArray(int size) {
            return new EmbeddedArtworks[size];
        }
    };
}
