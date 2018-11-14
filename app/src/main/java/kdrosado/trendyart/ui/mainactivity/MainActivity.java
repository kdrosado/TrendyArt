package kdrosado.trendyart.ui.mainactivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.ColorRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import kdrosado.trendyart.R;
import kdrosado.trendyart.TrendyArtApp;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.BindView;
import butterknife.ButterKnife;

import kdrosado.trendyart.ui.artworks.ArtworksFragment;
import kdrosado.trendyart.ui.favorites.FavoritesFragment;
import kdrosado.trendyart.ui.mainactivity.adapters.BottomNavAdapter;
import kdrosado.trendyart.ui.mainactivity.adapters.MainViewPager;
import kdrosado.trendyart.ui.searchresults.SearchFragment;
import kdrosado.trendyart.callbacks.OnRefreshListener;

public class MainActivity extends AppCompatActivity implements OnRefreshListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String THEME_PREFERENCE_KEY = "theme_prefs";
    private static final String POSITION_KEY = "position";

    @BindView(R.id.appbar_main)
    AppBarLayout appBarLayout;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;
    @BindView(R.id.view_pager_content)
    MainViewPager viewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Tracker mTracker;
    private BottomNavAdapter mPagerAdapter;
    private SharedPreferences mPreferences;
    private boolean mIsDayMode;
    private String mTitle;
    private int mPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set the theme before creating the View
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            // Get the position of the selected Fragment
            mPosition = savedInstanceState.getInt(POSITION_KEY);
        }

        Log.d(TAG, "onCreate: position: " + mPosition);
        // Set the title on the toolbar according to
        // the position of the clicked Fragment
        setToolbarTitle(mPosition);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mTitle);
        }

        // Obtain the shared Tracker instance.
        // source: https://developers.google.com/analytics/devguides/collection/android/v4/
        TrendyArtApp application = (TrendyArtApp) getApplication();
        mTracker = application.getDefaultTracker();

        setupViewPager();
        setupBottomNavStyle();

        // Add items to the Bottom Navigation
        addBottomNavigationItems();
        bottomNavigation.setCurrentItem(mPosition);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
                viewPager.setCurrentItem(position);

                mPosition = position;
                // Set the title on the toolbar according to
                // the position of the clicked Fragment
                setToolbarTitle(position);

                return true;
            }
        });

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mIsDayMode = mPreferences.getBoolean(THEME_PREFERENCE_KEY, false);
        //mPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the position of the selected Fragment
        outState.putInt(POSITION_KEY, mPosition);
    }

    /**
     * Method for setting the title on the Toolbar for each Fragment
     *
     * @param position of the current Fragment
     */
    private void setToolbarTitle(int position) {
        switch (position) {
            case 0:
                mTitle = getString(R.string.title_artworks);
                toolbar.setTitle(mTitle);
                break;
            case 1:
                mTitle = getString(R.string.title_search);
                toolbar.setTitle(mTitle);
                break;
            case 2:
                mTitle = getString(R.string.title_favorites);
                toolbar.setTitle(mTitle);
                break;
            default:
                break;
        }
    }

    private void addBottomNavigationItems() {

        AHBottomNavigationItem artworksItem = new AHBottomNavigationItem(
                getString(R.string.title_artworks), R.drawable.ic_frame);
        AHBottomNavigationItem artistsItem = new AHBottomNavigationItem(
                getString(R.string.search), R.drawable.ic_search_24dp);
        AHBottomNavigationItem favoritesItem = new AHBottomNavigationItem(
                getString(R.string.title_favorites), R.drawable.ic_favorite_24dp);

        bottomNavigation.addItem(artworksItem);
        bottomNavigation.addItem(artistsItem);
        bottomNavigation.addItem(favoritesItem);
    }

    //TODO: Make these three methods into one
    private Fragment createArtworksFragment() {
        Fragment artworksFragment = new ArtworksFragment();
        // Set arguments here
        Bundle bundle = new Bundle();
        artworksFragment.setArguments(bundle);
        return artworksFragment;
    }

    private FavoritesFragment createFavFragment() {
        FavoritesFragment favFragment = new FavoritesFragment();
        return favFragment;
    }

    private SearchFragment createSearchFragment() {
        SearchFragment searchFragment = new SearchFragment();
        return searchFragment;
    }

    private void setupBottomNavStyle() {
        bottomNavigation.setDefaultBackgroundColor(fetchColor(R.color.colorPrimary));
        bottomNavigation.setAccentColor(fetchColor(R.color.colorAccent));
        bottomNavigation.setInactiveColor(fetchColor(R.color.colorIconsInactive));

        bottomNavigation.setColoredModeColors(fetchColor(R.color.colorPrimary),
                fetchColor(R.color.colorAccent));

        bottomNavigation.setColored(false);

        // Hide the navigation when the user scroll the Rv
        //bottomNavigation.setBehaviorTranslationEnabled(true);
        bottomNavigation.setTranslucentNavigationEnabled(true);
    }

    private void setupViewPager() {
        viewPager.setPagingEnabled(false);
        // Set the offset to 3 so that the Pager Adapter keeps in memory
        // all 3 Fragments and doesn't load it
        viewPager.setOffscreenPageLimit(3);
        mPagerAdapter = new BottomNavAdapter(getSupportFragmentManager());

        mPagerAdapter.addFragments(createArtworksFragment());
        mPagerAdapter.addFragments(createSearchFragment());
        mPagerAdapter.addFragments(createFavFragment());

        viewPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onRefreshConnection() {
        Log.d(TAG, "onRefreshConnection is now triggered");
        setAppBarVisible();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume is called now!");

        mTracker.setScreenName(getString(R.string.analytics_artwork_screenname));
        // Send initial screen screen view hit.
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        // Show the AppBarLayout every time after resume
        setAppBarVisible();
    }

    private void setAppBarVisible() {
        // Set the AppBarLayout to expanded
        appBarLayout.setExpanded(true, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main_menu, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                return false;

            case R.id.action_refresh:
                return false;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void savePrefs(boolean state) {
        mPreferences = getApplicationContext()
                .getSharedPreferences(THEME_PREFERENCE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(THEME_PREFERENCE_KEY, state);
        editor.apply();
    }

    /**
     * Simple method for fetching colors
     * source: https://android.jlelse.eu/ultimate-guide-to-bottom-navigation-on-android-75e4efb8105f
     *
     * @param color to fetch
     * @return int color value
     */
    private int fetchColor(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }


}
