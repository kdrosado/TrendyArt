package kdrosado.trendyart.model.token;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TypeToken {

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("expires_at")
    @Expose
    private String expiresAt;

    public String getType() {
        return type;
    }

    public String getToken() {
        return token;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public TypeToken() { }
}
