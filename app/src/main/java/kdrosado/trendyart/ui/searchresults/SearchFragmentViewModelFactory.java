package kdrosado.trendyart.ui.searchresults;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import kdrosado.trendyart.TrendyArtApp;

public class SearchFragmentViewModelFactory implements ViewModelProvider.Factory {

    private TrendyArtApp mApplication;
    private String mQueryWord;
    private String mTypeWord;

    public SearchFragmentViewModelFactory(TrendyArtApp application, String queryWord, String typeWord) {

        mApplication = application;
        mQueryWord = queryWord;
        mTypeWord = typeWord;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SearchFragmentViewModel.class)) {
            return (T) new SearchFragmentViewModel(mApplication, mQueryWord, mTypeWord);
        }
        throw new IllegalArgumentException("Unknown ViewModel class.");
    }
}
