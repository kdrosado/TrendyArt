package kdrosado.trendyart.model.artworks;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArtistsLink implements Parcelable {

    /*
    Link to the artists endpoint, e.g.:
    "https://api.artsy.net/api/artists?artwork_id="
    Note: needs a token added to the Header!
     */
    @SerializedName("href")
    @Expose
    private String href;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.href);
    }

    public ArtistsLink() {
    }

    protected ArtistsLink(Parcel in) {
        this.href = in.readString();
    }

    public static final Creator<ArtistsLink> CREATOR = new Creator<ArtistsLink>() {
        @Override
        public ArtistsLink createFromParcel(Parcel source) {
            return new ArtistsLink(source);
        }

        @Override
        public ArtistsLink[] newArray(int size) {
            return new ArtistsLink[size];
        }
    };
}
