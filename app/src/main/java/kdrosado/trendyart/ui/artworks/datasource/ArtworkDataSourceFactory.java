package kdrosado.trendyart.ui.artworks.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import kdrosado.trendyart.TrendyArtApp;
import kdrosado.trendyart.model.artworks.Artwork;

public class ArtworkDataSourceFactory extends DataSource.Factory<Long, Artwork> {

    private MutableLiveData<ArtworkDataSource> mArtworksDataSourceLiveData;
    private ArtworkDataSource mDataSource;
    private TrendyArtApp mTrendyArtApp;


    public ArtworkDataSourceFactory(TrendyArtApp artPlaceApp) {
        mTrendyArtApp = artPlaceApp;
        mArtworksDataSourceLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource<Long, Artwork> create() {
        mDataSource = new ArtworkDataSource(mTrendyArtApp);
        mArtworksDataSourceLiveData.postValue(mDataSource);

        return mDataSource;
    }

    public MutableLiveData<ArtworkDataSource> getArtworksDataSourceLiveData() {
        return mArtworksDataSourceLiveData;
    }
}
