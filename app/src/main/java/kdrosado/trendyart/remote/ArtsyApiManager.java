package kdrosado.trendyart.remote;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import kdrosado.trendyart.BuildConfig;

import kdrosado.trendyart.model.CustomArtsyDeserializer;
import kdrosado.trendyart.model.artists.CustomArtistsDeserializer;
import kdrosado.trendyart.model.artists.EmbeddedArtists;
import kdrosado.trendyart.model.artworks.ArtworkWrapperResponse;
import kdrosado.trendyart.model.artworks.CustomArtworksDeserializer;
import kdrosado.trendyart.model.artworks.EmbeddedArtworks;
import kdrosado.trendyart.utils.Utils;
import kdrosado.trendyart.model.token.TypeToken;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArtsyApiManager {

    private static TokenServiceHolder tokenServiceHolder;
    private static TokenAuthenticator authenticator = new TokenAuthenticator(tokenServiceHolder);
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static retrofit2.Response<TypeToken> mAccessToken;
    private static TokenServiceHolder mTokenServiceHolder;
    private static String token;
    private static OkHttpClient.Builder client;

    private static final String TAG = ArtsyApiManager.class.getSimpleName();


    // Provide the Retrofit call
    public static ArtsyApiInterface create() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client = new OkHttpClient.Builder();

        //COMPLETED: Add the Header wit the Token here
        // source: https://stackoverflow.com/a/32282876/8132331
        client.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("X-XAPP-Token", BuildConfig.TOKEN)
                        //.addHeader("X-XAPP-Token", token)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        client.addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.BASE_ARTSY_URL)
                .client(client.build())
                //.addConverterFactory(GsonConverterFactory.create(makeGson()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ArtsyApiInterface.class);
    }

    /*
    Register the TypeAdapter here for deserializing the model
     */
    private static Gson makeGson() {

        // Register two different TypeAdapters!
        // resource: https://stackoverflow.com/a/33459073/8132331
        return new GsonBuilder()
                .registerTypeAdapter(ArtworkWrapperResponse.class, new CustomArtsyDeserializer())
                .registerTypeAdapter(EmbeddedArtworks.class, new CustomArtworksDeserializer())
                .registerTypeAdapter(EmbeddedArtists.class, new CustomArtistsDeserializer())
                .create();
    }

}
