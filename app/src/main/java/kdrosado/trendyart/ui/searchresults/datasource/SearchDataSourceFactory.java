package kdrosado.trendyart.ui.searchresults.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import kdrosado.trendyart.TrendyArtApp;
import kdrosado.trendyart.model.search.Result;

public class SearchDataSourceFactory extends DataSource.Factory<Long, Result> {

    private MutableLiveData<SearchDataSource> mSearchDataSourceMutableLiveData;
    private SearchDataSource mSearchDataSource;
    private TrendyArtApp mTrendyArtApp;
    private String mArtQuery;
    private String mTypeString;

    public SearchDataSourceFactory(TrendyArtApp trendyArtApp, String queryWord, String typeWord) {
        mTrendyArtApp = trendyArtApp;
        mArtQuery = queryWord;
        mTypeString = typeWord;

        mSearchDataSourceMutableLiveData = new MutableLiveData<>();
    }


    @Override
    public DataSource<Long, Result> create() {
        mSearchDataSource = new SearchDataSource(mTrendyArtApp, mArtQuery, mTypeString);
        mSearchDataSourceMutableLiveData.postValue(mSearchDataSource);

        return mSearchDataSource;
    }

    public MutableLiveData<SearchDataSource> getSearchDataSourceMutableLiveData() {
        return mSearchDataSourceMutableLiveData;
    }
}
