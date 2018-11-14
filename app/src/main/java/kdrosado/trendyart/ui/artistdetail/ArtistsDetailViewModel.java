package kdrosado.trendyart.ui.artistdetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import kdrosado.trendyart.model.artists.Artist;
import kdrosado.trendyart.model.artworks.Artwork;
import kdrosado.trendyart.repository.ArtsyRepository;

public class ArtistsDetailViewModel extends ViewModel {

    private LiveData<List<Artist>> mArtistLink;
    private LiveData<List<Artwork>> mSimilarArtworkLink;

    public ArtistsDetailViewModel() {
    }

    public void initArtistLink(String artistUrl) {

        if (mArtistLink != null) {
            return;
        }

        mArtistLink = ArtsyRepository.getInstance().getArtistFromLink(artistUrl);
    }

    public void initSimilarArtworksLink(String similarArtUrl) {
        if (mSimilarArtworkLink != null) {
            return;
        }

        mSimilarArtworkLink = ArtsyRepository.getInstance().getSimilarArtFromLink(similarArtUrl);
    }


    public LiveData<List<Artist>> getArtistFromLink() {
        return mArtistLink;
    }

    public LiveData<List<Artwork>> getSimilarArtworksLink() {
        return mSimilarArtworkLink;
    }

}
