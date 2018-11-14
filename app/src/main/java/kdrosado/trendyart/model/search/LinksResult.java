package kdrosado.trendyart.model.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import kdrosado.trendyart.model.Self;
import kdrosado.trendyart.model.Thumbnail;

public class LinksResult implements Parcelable {

    @SerializedName("self")
    @Expose
    private Self self;

    @SerializedName("permalink")
    @Expose
    private Permalink permalink;

    @SerializedName("thumbnail")
    @Expose
    private Thumbnail thumbnail;

    public Self getSelf() {
        return self;
    }

    public void setSelf(Self self) {
        this.self = self;
    }

    public Permalink getPermalink() {
        return permalink;
    }

    public void setPermalink(Permalink permalink) {
        this.permalink = permalink;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.self, flags);
        dest.writeParcelable(this.permalink, flags);
        dest.writeParcelable(this.thumbnail, flags);
    }

    public LinksResult() {
    }

    protected LinksResult(Parcel in) {
        this.self = in.readParcelable(Self.class.getClassLoader());
        this.permalink = in.readParcelable(Permalink.class.getClassLoader());
        this.thumbnail = in.readParcelable(Thumbnail.class.getClassLoader());
    }

    public static final Creator<LinksResult> CREATOR = new Creator<LinksResult>() {
        @Override
        public LinksResult createFromParcel(Parcel source) {
            return new LinksResult(source);
        }

        @Override
        public LinksResult[] newArray(int size) {
            return new LinksResult[size];
        }
    };
}
