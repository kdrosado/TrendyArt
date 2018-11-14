package kdrosado.trendyart.model.artworks;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InSize implements Parcelable {

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

    public InSize() {
    }

    protected InSize(Parcel in) {
        this.text = in.readString();
    }

    public static final Creator<InSize> CREATOR = new Creator<InSize>() {
        @Override
        public InSize createFromParcel(Parcel source) {
            return new InSize(source);
        }

        @Override
        public InSize[] newArray(int size) {
            return new InSize[size];
        }
    };
}
