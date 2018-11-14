package kdrosado.trendyart.model.artworks;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainImage implements Parcelable {

    /*
    Link to the image
    TODO: needs {image_versions} always to be "large"
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

    public MainImage() {
    }

    protected MainImage(Parcel in) {
        this.href = in.readString();
    }

    public static final Creator<MainImage> CREATOR = new Creator<MainImage>() {
        @Override
        public MainImage createFromParcel(Parcel source) {
            return new MainImage(source);
        }

        @Override
        public MainImage[] newArray(int size) {
            return new MainImage[size];
        }
    };
}
