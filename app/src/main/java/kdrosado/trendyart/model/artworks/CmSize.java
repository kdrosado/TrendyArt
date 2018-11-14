package kdrosado.trendyart.model.artworks;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CmSize implements Parcelable {

    @SerializedName("text")
    @Expose
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
    }

    public CmSize() {
    }

    protected CmSize(Parcel in) {
        this.text = in.readString();
    }

    public static final Creator<CmSize> CREATOR = new Creator<CmSize>() {
        @Override
        public CmSize createFromParcel(Parcel source) {
            return new CmSize(source);
        }

        @Override
        public CmSize[] newArray(int size) {
            return new CmSize[size];
        }
    };
}
