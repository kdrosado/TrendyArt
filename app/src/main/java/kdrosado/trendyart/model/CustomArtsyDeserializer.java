package kdrosado.trendyart.model;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import kdrosado.trendyart.model.artworks.ArtworkWrapperResponse;

public class CustomArtsyDeserializer implements JsonDeserializer<ArtworkWrapperResponse> {

    @Override
    public ArtworkWrapperResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        // Get the "_links" element from the parsed JSON
        JsonElement linksElement = json.getAsJsonObject().get("_links");

        // Deserialize it by using a new instance of Gson to avoid infinite recursion
        // to this deserializer
        return new Gson().fromJson(linksElement, ArtworkWrapperResponse.class);
    }
}
