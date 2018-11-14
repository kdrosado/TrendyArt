package kdrosado.trendyart.model.artworks;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dimensions implements Parcelable {

    @SerializedName("in")
    @Expose
    private InSize inSize;
    @SerializedName("cm")
    @Expose
    private CmSize cmSize;

    public InSize getInSize() {
        return inSize;
    }

    public void setInSize(InSize inSize) {
        this.inSize = inSize;
    }

    public CmSize getCmSize() {
        return cmSize;
    }

    public void setCmSize(CmSize cmSize) {
        this.cmSize = cmSize;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.inSize, flags);
        dest.writeParcelable(this.cmSize, flags);
    }

    public Dimensions() {
    }

    protected Dimensions(Parcel in) {
        this.inSize = in.readParcelable(InSize.class.getClassLoader());
        this.cmSize = in.readParcelable(CmSize.class.getClassLoader());
    }

    public static final Creator<Dimensions> CREATOR = new Creator<Dimensions>() {
        @Override
        public Dimensions createFromParcel(Parcel source) {
            return new Dimensions(source);
        }

        @Override
        public Dimensions[] newArray(int size) {
            return new Dimensions[size];
        }
    };
}
