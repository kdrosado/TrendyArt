package kdrosado.trendyart.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimilarArtworksLink implements Parcelable {

    @SerializedName("href")
    @Expose
    private String href;

    public String getHref() {
        return href;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.href);
    }

    public SimilarArtworksLink() {
    }

    protected SimilarArtworksLink(Parcel in) {
        this.href = in.readString();
    }

    public static final Creator<SimilarArtworksLink> CREATOR = new Creator<SimilarArtworksLink>() {
        @Override
        public SimilarArtworksLink createFromParcel(Parcel source) {
            return new SimilarArtworksLink(source);
        }

        @Override
        public SimilarArtworksLink[] newArray(int size) {
            return new SimilarArtworksLink[size];
        }
    };
}
