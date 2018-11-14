package kdrosado.trendyart.model.artworks;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

// Custom Deserializer gets the JSON and deserialize it to access the EmbeddedArtworks element
// resource: https://stackoverflow.com/a/23071080/8132331
public class CustomArtworksDeserializer implements JsonDeserializer<EmbeddedArtworks> {

    @Override
    public EmbeddedArtworks deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        // Get the "embedded" element from the parsed JSON
        JsonElement embeddedElement = json.getAsJsonObject().get("_embedded");

        // Deserialize it by using a new instance of Gson to avoid infinite recursion
        // to this deserializer
        return new Gson().fromJson(embeddedElement, EmbeddedArtworks.class);
    }
}
