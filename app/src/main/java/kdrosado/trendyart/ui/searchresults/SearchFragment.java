package kdrosado.trendyart.ui.searchresults;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import kdrosado.trendyart.R;
import kdrosado.trendyart.TrendyArtApp;

import butterknife.BindView;
import butterknife.ButterKnife;

import kdrosado.trendyart.callbacks.OnResultClickListener;
import kdrosado.trendyart.model.search.Result;
import kdrosado.trendyart.ui.contentdetail.SearchDetailActivity;
import kdrosado.trendyart.ui.searchresults.adapter.SearchListAdapter;
import kdrosado.trendyart.utils.NetworkState;

public class SearchFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener, OnResultClickListener {

    private static final String TAG = SearchFragment.class.getSimpleName();
    private static final String ARG_SEARCH_TITLE = "search_title";

    private static final String PREFERENCE_SEARCH_NAME = "search_prefs";
    private static final String PREFERENCE_SEARCH_KEY = "search_key";
    private static final String SEARCH_QUERY_SAVE_STATE = "search_state";
    private static final String RESULT_PARCEL_KEY = "results_key";

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.search_rv)
    RecyclerView searchResultsRv;
    @BindView(R.id.progress_bar_search)
    ProgressBar progressBar;
    @BindView(R.id.error_message_search)
    TextView errorMessage;

    private SearchFragmentViewModel mViewModel;
    private SearchListAdapter mSearchAdapter;
    private SearchView mSearchView;
    private String mQueryWordString;
    private SearchFragmentViewModelFactory mViewModelFactory;
    private String mTypeString;
    private String mTitle;

    private SharedPreferences mSharedPreferences;


    // Required empty public constructor
    public SearchFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add a menu to the current Fragment
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            mQueryWordString = savedInstanceState.getString(SEARCH_QUERY_SAVE_STATE);
            //mTitle = savedInstanceState.getString(ARG_SEARCH_TITLE);
        }

        if (getArguments() != null) {
           // mTitle = getArguments().getString(ARG_SEARCH_TITLE);
            //Objects.requireNonNull(getActivity()).setTitle(mTitle);
        }

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SEARCH_QUERY_SAVE_STATE, mQueryWordString);
       // outState.putString(ARG_SEARCH_TITLE, mTitle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, rootView);

        // Set the UI
        setupUi();

        return rootView;
    }

    private void setupRecyclerView() {
        int columnCount = getResources().getInteger(R.integer.list_column_count);

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);

        searchResultsRv.setLayoutManager(staggeredGridLayoutManager);

        mSearchAdapter = new SearchListAdapter(getContext(), this);
    }

    private void setupUi() {
        // Setup the RecyclerView first
        setupRecyclerView();

        if (mQueryWordString == null) {
            mQueryWordString = "Andy Warhol";
        }

        //getActivity().setTitle(mQueryWordString);

        Log.d(TAG, "setupUi: Query word: " + mQueryWordString);
        Log.d(TAG, "setupUi: Type word: " + mTypeString);

        mViewModelFactory = new SearchFragmentViewModelFactory(TrendyArtApp.getInstance(), mQueryWordString, mTypeString);

        // Initialize the ViewModel
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(SearchFragmentViewModel.class);

        mViewModel.getSearchResultsLiveData().observe(this, new Observer<PagedList<Result>>() {

            @Override
            public void onChanged(@Nullable PagedList<Result> results) {
                if (results != null) {
                    // Submit the list to the PagedListAdapter
                    mSearchAdapter.submitList(results);
                }
            }
        });

        mViewModel.getNetworkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                if (networkState != null) {
                    mSearchAdapter.setNetworkState(networkState);
                }
            }
        });

        mViewModel.getInitialLoading().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                if (networkState != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    errorMessage.setText(R.string.loading_results_message);
                    errorMessage.setVisibility(View.VISIBLE);

                    if (networkState.getStatus() == NetworkState.Status.SUCCESS
                            && networkState.getStatus() == NetworkState.Status.NO_RESULT) {
                        Log.d(TAG, "Network Status: " + networkState.getStatus());
                        progressBar.setVisibility(View.GONE);
                        errorMessage.setText(R.string.no_data_found);
                        errorMessage.setVisibility(View.VISIBLE);
                        Snackbar.make(coordinatorLayout, "Please search for another word.",
                                Snackbar.LENGTH_LONG).show();
                    }

                    if (networkState.getStatus() == NetworkState.Status.SUCCESS) {
                        Log.d(TAG, "Network Status: " + networkState.getStatus());
                        progressBar.setVisibility(View.INVISIBLE);
                        errorMessage.setVisibility(View.INVISIBLE);
                    }

                    if (networkState.getStatus() == NetworkState.Status.FAILED) {
                        Log.d(TAG, "Network Status: " + networkState.getStatus());
                        progressBar.setVisibility(View.GONE);
                        // TODO: Hide this message when no connection but some cache results still visible
                        errorMessage.setVisibility(View.VISIBLE);
                        Snackbar.make(coordinatorLayout, R.string.snackbar_no_network_connection,
                                Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });

        // Set the Adapter on the RecyclerView
        searchResultsRv.setAdapter(mSearchAdapter);
    }

    public synchronized void requestNewCall(String typeWord) {

        // Setup the RecyclerView first
        setupRecyclerView();

        mSharedPreferences = getContext().getSharedPreferences(PREFERENCE_SEARCH_NAME, Context.MODE_PRIVATE);
        mQueryWordString = mSharedPreferences.getString(PREFERENCE_SEARCH_KEY, null);

        if (mQueryWordString == null) {
            mQueryWordString = "Andy Warhol";
        }

        Log.d(TAG, "requestNewCall: Query word: " + mQueryWordString);
        Log.d(TAG, "requestNewCall: Type word: " + typeWord);

        mViewModelFactory = new SearchFragmentViewModelFactory(TrendyArtApp.getInstance(), mQueryWordString, typeWord);

        // Initialize the ViewModel
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(SearchFragmentViewModel.class);

        mViewModel.refreshSearchLiveData(TrendyArtApp.getInstance(), mQueryWordString, typeWord).observe(this, new Observer<PagedList<Result>>() {

            @Override
            public void onChanged(@Nullable PagedList<Result> results) {
                if (results != null) {
                    Log.d(TAG, "Size of the result: " + results.size());
                    // Submit the list to the PagedListAdapter
                    mSearchAdapter.submitList(results);
                }
            }
        });

        // Set the Adapter on the RecyclerView
        searchResultsRv.setAdapter(mSearchAdapter);
    }

    private void saveToSharedPreference(String searchQuery) {
        mSharedPreferences = getActivity().getApplicationContext()
                .getSharedPreferences(PREFERENCE_SEARCH_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(PREFERENCE_SEARCH_KEY, searchQuery);
        // Use apply() instead of commit(), because it is being saved on the background
        editor.apply();
        Log.d(TAG, "Saved into shared prefs");
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_search).setVisible(true);
        Log.d(TAG, "onPrepareOptionsMenu called");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.search_menu, menu);

        // Make the icon with a dynamic tint
        // source: https://stackoverflow.com/a/29916353/8132331
        Drawable drawable = menu.findItem(R.id.action_search).getIcon();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(getActivity(), R.color.colorText));
        menu.findItem(R.id.action_search).setIcon(drawable);

        // Set the SearchView
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        // Set the Submit Button
        mSearchView.setSubmitButtonEnabled(false);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                query = String.valueOf(mSearchView.getQuery());
                // Save the search query into SharedPreference
                saveToSharedPreference(query);
                requestNewCall(mTypeString);
                Log.d(TAG, "SearchFragment: onQueryTextSubmit called, query word: " + query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "SearchFragment: onQueryTextChange called");

                if (newText.length() > 0) {
                    requestNewCall(mTypeString);
                }

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                return true;

            case R.id.action_type_article:
                item.setChecked(true);
                // Set the type to article
                mTypeString = "article";

                requestNewCall(mTypeString);

                Log.d(TAG, "Type word: " + mTypeString);
                return true;

            case R.id.action_type_artist:
                item.setChecked(true);
                // Set the type to artist
                mTypeString = "artist";

                requestNewCall(mTypeString);

                Log.d(TAG, "Type word: " + mTypeString);
                return true;

            case R.id.action_type_artwork:
                item.setChecked(true);
                // Set the type to artwork
                mTypeString = "artwork";

                requestNewCall(mTypeString);

                Log.d(TAG, "Type word: " + mTypeString);
                return true;

            case R.id.action_type_gene:

                item.setChecked(true);
                // Set the type to gene
                mTypeString = "gene";

                requestNewCall(mTypeString);

                Log.d(TAG, "Type word: " + mTypeString);
                return true;

            case R.id.action_type_show:
                item.setChecked(true);
                // Set the type to show
                mTypeString = "show";

                requestNewCall(mTypeString);

                Log.d(TAG, "Type word: " + mTypeString);
                return true;

            case R.id.action_type_none:
                item.setChecked(true);
                requestNewCall(null);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(PREFERENCE_SEARCH_KEY)) {
            mSharedPreferences = getActivity()
                    .getSharedPreferences(PREFERENCE_SEARCH_NAME, Context.MODE_PRIVATE);

            if (mSharedPreferences.contains(PREFERENCE_SEARCH_KEY)) {
                mQueryWordString = mSharedPreferences.getString(PREFERENCE_SEARCH_KEY, null);

                Log.d(TAG, "onSharedPreferenceChanged: Saved search query: " + mQueryWordString);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "SearchFragment: onPause called" );
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "SearchFragment: onResume called" );
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResultClick(Result result) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RESULT_PARCEL_KEY, result);

        Intent intent = new Intent(getActivity(), SearchDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
