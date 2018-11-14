package kdrosado.trendyart.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Genes implements Parcelable {

    @SerializedName("self")
    @Expose
    private Self self;

    public Self getSelf() {
        return self;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.self, flags);
    }

    public Genes() {
    }

    protected Genes(Parcel in) {
        this.self = in.readParcelable(Self.class.getClassLoader());
    }

    public static final Creator<Genes> CREATOR = new Creator<Genes>() {
        @Override
        public Genes createFromParcel(Parcel source) {
            return new Genes(source);
        }

        @Override
        public Genes[] newArray(int size) {
            return new Genes[size];
        }
    };
}
