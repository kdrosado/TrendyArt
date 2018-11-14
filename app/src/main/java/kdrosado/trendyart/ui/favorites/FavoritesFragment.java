package kdrosado.trendyart.ui.favorites;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import kdrosado.trendyart.callbacks.OnFavItemClickListener;
import kdrosado.trendyart.database.entity.FavoriteArtworks;
import kdrosado.trendyart.ui.FavDetailActivity;
import kdrosado.trendyart.ui.favorites.adapter.FavArtworkListAdapter;

public class FavoritesFragment extends Fragment implements OnFavItemClickListener {

    private static final String TAG = FavoritesFragment.class.getSimpleName();
    private static final String ARG_FAV_TITLE = "fav_title";

    public static final String ARTWORK_ID_KEY = "artwork_id";
    private static final String ARTWORK_TITLE_KEY = "artwork_title";
    private static final String ARTWORK_ARTIST_KEY = "artwork_artist";
    private static final String ARTWORK_CATEGORY_KEY = "artwork_category";
    private static final String ARTWORK_MEDIUM_KEY = "artwork_medium";
    private static final String ARTWORK_DATE_KEY = "artwork_date";
    private static final String ARTWORK_MUSEUM_KEY = "artwork_museum";
    private static final String ARTWORK_IMAGE_KEY = "artwork_image";
    private static final String ARTWORK_DIMENS_CM_KEY = "artwork_dimens_cm";
    private static final String ARTWORK_DIMENS_INCH_KEY = "artwork_dimens_inch";

    @BindView(R.id.fav_artworks_rv)
    RecyclerView favArtworksRv;
    @BindView(R.id.fav_empty_text)
    TextView emptyText;
    @BindView(R.id.fav_progress_bar)
    ProgressBar favProgressBar;
    @BindView(R.id.fav_coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    private FavArtworkListAdapter mAdapter;
    private PagedList<FavoriteArtworks> mFavoriteArtworksList;
    private Tracker mTracker;
    private FavArtworksViewModel mFavArtworksViewModel;
    private String mTitle;

    // Required empty public constructor
    public FavoritesFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add a menu to the current Fragment
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString(ARG_FAV_TITLE);
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ARG_FAV_TITLE, mTitle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        ButterKnife.bind(this, rootView);

        // Obtain the shared Tracker instance.
        // source: https://developers.google.com/analytics/devguides/collection/android/v4/
        TrendyArtApp application = (TrendyArtApp) Objects.requireNonNull(getActivity()).getApplication();
        mTracker = application.getDefaultTracker();

        mFavArtworksViewModel = ViewModelProviders.of(this).get(FavArtworksViewModel.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        favArtworksRv.setLayoutManager(layoutManager);

        mAdapter = new FavArtworkListAdapter(getContext(), this);
        emptyText.setVisibility(View.INVISIBLE);

        // Show the whole list of Favorite Artworks via the ViewModel
        getFavArtworks();

        favArtworksRv.setAdapter(mAdapter);

        // Delete a single item by swiping to left or right
        deleteItemBySwiping();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mTracker.setScreenName(TAG);
        // Send initial screen screen view hit.
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        Log.d(TAG, "FavoritesFragment: onResume called");

        refreshFavList();
    }

    private void getFavArtworks() {
        mFavArtworksViewModel.getFavArtworkList().observe(this, new Observer<PagedList<FavoriteArtworks>>() {

            @Override
            public void onChanged(@Nullable PagedList<FavoriteArtworks> favoriteArtworks) {

                if (favoriteArtworks != null && favoriteArtworks.size() > 0) {
                    // Hide the Progress Bar and the Empty Text View
                    favProgressBar.setVisibility(View.INVISIBLE);
                    emptyText.setVisibility(View.INVISIBLE);

                    Log.d(TAG, "Submit artworks to the db " + favoriteArtworks.size());
                    mAdapter.submitList(favoriteArtworks);
                } else {
                    // Show the empty message when there is no items added to favorites
                    emptyText.setVisibility(View.VISIBLE);

                    // Hide the Progress Bar
                    favProgressBar.setVisibility(View.INVISIBLE);
                    Log.e(TAG, "No artworks added to the database");
                }

                mFavoriteArtworksList = favoriteArtworks;
                Log.d(TAG, "Number of fav artworks: " + favoriteArtworks);
            }
        });
    }

    private void deleteItemBySwiping() {

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when the user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                FavoriteArtworks favArtwork = mAdapter.getFavoriteAtPosition(position);
                String itemId = favArtwork.getArtworkId();
                // Delete the item by swiping it
                mFavArtworksViewModel.deleteItem(itemId);
                Snackbar.make(coordinatorLayout, R.string.snackbar_deleted_item, Snackbar.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(favArtworksRv);
    }

    private void refreshFavList() {

        mFavArtworksViewModel.refreshFavArtworkList(Objects.requireNonNull(getActivity()).getApplication()).observe(this, new Observer<PagedList<FavoriteArtworks>>() {
            @Override
            public void onChanged(@Nullable PagedList<FavoriteArtworks> favoriteArtworks) {
                mAdapter.submitList(favoriteArtworks);
                mFavoriteArtworksList = favoriteArtworks;
            }
        });
    }

    @Override
    public void onFavItemClick(FavoriteArtworks favArtworks) {

        // Set Intent to the DetailActivity
        Intent favIntent = new Intent(getActivity(), FavDetailActivity.class);
        favIntent.putExtra(ARTWORK_ID_KEY, favArtworks.getId());
        favIntent.putExtra(ARTWORK_IMAGE_KEY, favArtworks.getArtworkImagePath());
        favIntent.putExtra(ARTWORK_TITLE_KEY, favArtworks.getArtworkTitle());
        favIntent.putExtra(ARTWORK_ARTIST_KEY, favArtworks.getArtworkSlug());
        favIntent.putExtra(ARTWORK_CATEGORY_KEY, favArtworks.getArtworkCategory());
        favIntent.putExtra(ARTWORK_DATE_KEY, favArtworks.getArtworkDate());
        favIntent.putExtra(ARTWORK_MEDIUM_KEY, favArtworks.getArtworkMedium());
        favIntent.putExtra(ARTWORK_MUSEUM_KEY, favArtworks.getArtworkMuseum());
        favIntent.putExtra(ARTWORK_DIMENS_INCH_KEY, favArtworks.getArtworkDimensInch());
        favIntent.putExtra(ARTWORK_DIMENS_CM_KEY, favArtworks.getArtworkDimensCm());

        startActivity(favIntent);
    }

    private void deleteItemsWithConfirmation() {

        if (mFavoriteArtworksList.size() > 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            builder.setMessage(R.string.delete_all_message);
            builder.setTitle(R.string.delete_all_title);
            builder.setIcon(R.drawable.ic_delete_24dp);

            builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mFavArtworksViewModel.deleteAllItems();
                    // Swapping the data doesn't refresh the list?
                    mAdapter.swapData(mFavoriteArtworksList);
                    // Instead - Refresh the list
                    refreshFavList();
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fav_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all:
                // Show warning dialog to the user before deleting all data from the db
                deleteItemsWithConfirmation();
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "FavoritesFragment: onPause called");
    }


}
