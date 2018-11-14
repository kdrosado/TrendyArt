package kdrosado.trendyart.model.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Permalink implements Parcelable {

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

    public Permalink() {
    }

    protected Permalink(Parcel in) {
        this.href = in.readString();
    }

    public static final Creator<Permalink> CREATOR = new Creator<Permalink>() {
        @Override
        public Permalink createFromParcel(Parcel source) {
            return new Permalink(source);
        }

        @Override
        public Permalink[] newArray(int size) {
            return new Permalink[size];
        }
    };
}
