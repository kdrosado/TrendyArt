package kdrosado.trendyart.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import kdrosado.trendyart.TrendyArtApp;

import java.util.ArrayList;
import java.util.List;

import kdrosado.trendyart.model.artists.Artist;
import kdrosado.trendyart.model.artists.ArtistWrapperResponse;
import kdrosado.trendyart.model.artists.EmbeddedArtists;
import kdrosado.trendyart.model.artworks.Artwork;
import kdrosado.trendyart.model.artworks.ArtworkWrapperResponse;
import kdrosado.trendyart.model.artworks.EmbeddedArtworks;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Singleton pattern for the Repository class,
// best explained here: https://medium.com/exploring-code/how-to-make-the-perfect-singleton-de6b951dfdb0
public class ArtsyRepository {

    private static final String TAG = ArtsyRepository.class.getSimpleName();

    // With volatile variable all the write will happen on volatile sInstance
    // before any read of sInstance variable
    private static volatile ArtsyRepository INSTANCE;


    // Private constructor of the Repository class
    private ArtsyRepository() {

        // Prevent from the reflection api
        if (INSTANCE != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static ArtsyRepository getInstance() {
        // Double check locking pattern
        if (INSTANCE == null) {

            // if there is no instance available, create a new one
            synchronized (ArtsyRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ArtsyRepository();
                }
            }
        }

        return INSTANCE;
    }

    public LiveData<List<Artwork>> getSimilarArtFromLink(String similarArtUrl) {
        return loadSimilarArtworks(similarArtUrl);
    }

    private LiveData<List<Artwork>> loadSimilarArtworks(String similarArtUrl) {

        MutableLiveData<List<Artwork>> similarArtData = new MutableLiveData<>();

        TrendyArtApp.getInstance().getArtsyApi().getSimilarArtLink(similarArtUrl)
                .enqueue(new Callback<ArtworkWrapperResponse>() {

                    ArtworkWrapperResponse artworkWrapper = new ArtworkWrapperResponse();
                    EmbeddedArtworks embeddedArtworks = new EmbeddedArtworks();
                    List<Artwork> similarArtList = new ArrayList<>();

                    @Override
                    public void onResponse(@NonNull Call<ArtworkWrapperResponse> call,
                                           @NonNull Response<ArtworkWrapperResponse> response) {

                        if (response.isSuccessful()) {
                            artworkWrapper = response.body();
                            if (artworkWrapper != null) {
                                embeddedArtworks = artworkWrapper.getEmbeddedArtworks();

                                similarArtList = embeddedArtworks.getArtworks();
                                similarArtData.setValue(similarArtList);
                            }
                            Log.d(TAG, "Similar artworks loaded successfully! " + response.code());
                        } else {
                            similarArtData.setValue(null);
                            Log.d(TAG, "Similar artworks loaded NOT successfully! " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ArtworkWrapperResponse> call, @NonNull Throwable t) {
                        Log.d(TAG, "OnFailure! " + t.getMessage());
                    }
                });

        return similarArtData;
    }

    public LiveData<List<Artist>> getArtistFromLink(String artistUrl) {
        return loadArtistFromLink(artistUrl);
    }

    private LiveData<List<Artist>> loadArtistFromLink(String artistUrl) {

        MutableLiveData<List<Artist>> artistLiveData = new MutableLiveData<>();

        TrendyArtApp.getInstance().getArtsyApi().getArtistLink(artistUrl)
                .enqueue(new Callback<ArtistWrapperResponse>() {
                    ArtistWrapperResponse artistWrapperResponse = new ArtistWrapperResponse();
                    EmbeddedArtists embeddedArtists = new EmbeddedArtists();
                    List<Artist> artistList = new ArrayList<>();

                    @Override
                    public void onResponse(@NonNull Call<ArtistWrapperResponse> call,
                                           @NonNull Response<ArtistWrapperResponse> response) {
                        if (response.isSuccessful()) {

                            artistWrapperResponse = response.body();

                            if (artistWrapperResponse != null) {
                                embeddedArtists = artistWrapperResponse.getEmbeddedArtist();

                                if (embeddedArtists != null) {
                                    artistList = embeddedArtists.getArtists();

                                    artistLiveData.setValue(artistList);
                                }
                            }
                            Log.d(TAG, "Loaded successfully! " + response.code());
                        } else {
                            artistLiveData.setValue(null);
                            Log.d(TAG, "Loaded NOT successfully! " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ArtistWrapperResponse> call, @NonNull Throwable t) {
                        Log.d(TAG, "OnFailure! " + t.getMessage());
                    }
                });

        return artistLiveData;
    }

    /**
     * Fetch data from the Artists endpoint with selected artwork ID
     * Not in use now
     *
     * @param artworkId is the current artwork ID
     */
   /* private LiveData<List<Artist>> loadArtist(String artworkId) {

        MutableLiveData<List<Artist>> artistLiveData = new MutableLiveData<>();

        // No need of a networkIO Executors here, as Retrofit is doing its call asynchronously
        // Get the Instance of the App to create the Retrofit call
        TrendyArtApp.getInstance().getArtsyApi().getArtist(artworkId).enqueue(new Callback<ArtistWrapperResponse>() {
            ArtistWrapperResponse artistWrapperResponse = new ArtistWrapperResponse();
            EmbeddedArtists embeddedArtists = new EmbeddedArtists();
            List<Artist> artistList = new ArrayList<>();

            @Override
            public void onResponse(@NonNull Call<ArtistWrapperResponse> call, @NonNull Response<ArtistWrapperResponse> response) {
                if (response.isSuccessful()) {

                    artistWrapperResponse = response.body();

                    if (artistWrapperResponse != null) {
                        embeddedArtists = artistWrapperResponse.getEmbeddedArtist();

                        artistList = embeddedArtists.getArtists();

                        artistLiveData.setValue(artistList);
                    }
                    Log.d(TAG, "Loaded successfully! " + response.code());

                } else {
                    artistLiveData.setValue(null);
                    Log.d(TAG, "Loaded NOT successfully! " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArtistWrapperResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "OnFailure! " + t.getMessage());
            }
        });

        // Return LiveData<Artist>
        return artistLiveData;
    }*/
}
