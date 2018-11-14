package kdrosado.trendyart.model.artists;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmbeddedArtists implements Parcelable {

    @SerializedName("artists")
    @Expose
    private List<Artist> artists = null;

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.artists);
    }

    public EmbeddedArtists() {
    }

    protected EmbeddedArtists(Parcel in) {
        this.artists = in.createTypedArrayList(Artist.CREATOR);
    }

    public static final Creator<EmbeddedArtists> CREATOR = new Creator<EmbeddedArtists>() {
        @Override
        public EmbeddedArtists createFromParcel(Parcel source) {
            return new EmbeddedArtists(source);
        }

        @Override
        public EmbeddedArtists[] newArray(int size) {
            return new EmbeddedArtists[size];
        }
    };
}
