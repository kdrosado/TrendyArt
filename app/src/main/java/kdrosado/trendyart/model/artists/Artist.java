package kdrosado.trendyart.model.artists;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import kdrosado.trendyart.model.ImageLinks;

public class Artist implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("slug")
    @Expose
    private String slug;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("sortable_name")
    @Expose
    private String sortableName;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("biography")
    @Expose
    private String biography;

    @SerializedName("birthday")
    @Expose
    private String birthday;

    @SerializedName("deathday")
    @Expose
    private String deathday;

    @SerializedName("hometown")
    @Expose
    private String hometown;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("nationality")
    @Expose
    private String nationality;

    @SerializedName("image_versions")
    @Expose
    private List<String> imageVersions = null;

    @SerializedName("_links")
    @Expose
    private ImageLinks links;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortableName() {
        return sortableName;
    }

    public void setSortableName(String sortableName) {
        this.sortableName = sortableName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public List<String> getImageVersions() {
        return imageVersions;
    }

    public void setImageVersions(List<String> imageVersions) {
        this.imageVersions = imageVersions;
    }

    public ImageLinks getLinks() {
        return links;
    }

    public void setLinks(ImageLinks links) {
        this.links = links;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.slug);
        dest.writeString(this.name);
        dest.writeString(this.sortableName);
        dest.writeString(this.gender);
        dest.writeString(this.biography);
        dest.writeString(this.birthday);
        dest.writeString(this.deathday);
        dest.writeString(this.hometown);
        dest.writeString(this.location);
        dest.writeString(this.nationality);
        dest.writeStringList(this.imageVersions);
        dest.writeParcelable(this.links, flags);
    }

    public Artist() {
    }

    protected Artist(Parcel in) {
        this.id = in.readString();
        this.slug = in.readString();
        this.name = in.readString();
        this.sortableName = in.readString();
        this.gender = in.readString();
        this.biography = in.readString();
        this.birthday = in.readString();
        this.deathday = in.readString();
        this.hometown = in.readString();
        this.location = in.readString();
        this.nationality = in.readString();
        this.imageVersions = in.createStringArrayList();
        this.links = in.readParcelable(ImageLinks.class.getClassLoader());
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel source) {
            return new Artist(source);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}
