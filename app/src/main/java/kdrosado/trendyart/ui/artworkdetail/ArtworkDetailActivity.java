package kdrosado.trendyart.ui.artworkdetail;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import kdrosado.trendyart.R;
import kdrosado.trendyart.TrendyArtApp;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.tiagohm.markdownview.MarkdownView;
import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.fresco.processors.BlurPostprocessor;

import kdrosado.trendyart.callbacks.ResultFromDbCallback;
import kdrosado.trendyart.database.entity.FavoriteArtworks;
import kdrosado.trendyart.model.ImageLinks;
import kdrosado.trendyart.model.SimilarArtworksLink;
import kdrosado.trendyart.model.Thumbnail;
import kdrosado.trendyart.model.artists.Artist;
import kdrosado.trendyart.model.artworks.ArtistsLink;
import kdrosado.trendyart.model.artworks.Artwork;
import kdrosado.trendyart.model.artworks.CmSize;
import kdrosado.trendyart.model.artworks.Dimensions;
import kdrosado.trendyart.model.artworks.InSize;
import kdrosado.trendyart.model.artworks.MainImage;
import kdrosado.trendyart.model.search.Permalink;
import kdrosado.trendyart.repository.FavArtRepository;
import kdrosado.trendyart.ui.artistdetail.ArtistDetailActivity;
import kdrosado.trendyart.ui.artistdetail.ArtistsDetailViewModel;
import kdrosado.trendyart.ui.artworkdetail.adapter.SimilarArtworksAdapter;
import kdrosado.trendyart.utils.StringUtils;

public class ArtworkDetailActivity extends AppCompatActivity {

    private static final String TAG = ArtworkDetailActivity.class.getSimpleName();
    private static final String ARTWORK_PARCEL_KEY = "artwork_key";
    private static final String ARTWORK_ID_KEY = "artwork_id";
    private static final String ARTWORK_TITLE_KEY = "artwork_title";
    private static final String ARTIST_URL_KEY = "artist_url";
    private static final String IS_FAV_SAVED_STATE = "is_fav";
    private static final int FAV_TAG = 0;
    private static final int NON_FAV_TAG = 1;

    @BindView(R.id.coordinator_artwork)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar_detail)
    Toolbar toolbar;

    // Views of the Artwork
    @BindView(R.id.artwork_title)
    TextView artworkName;
    @BindView(R.id.artwork_artist_button)
    Button artistNameButton;
    @BindView(R.id.artwork_medium)
    TextView artworkMedium;
    @BindView(R.id.artwork_category)
    TextView artworkCategory;
    @BindView(R.id.artwork_date)
    TextView artworkDate;
    @BindView(R.id.artwork_museum)
    TextView artworkMuseum;
    @BindView(R.id.museum_label)
    TextView artworkMuseumLabel;
    @BindView(R.id.artwork_image)
    ImageView artworkImage;
    @BindView(R.id.artwork_dimens_cm)
    TextView dimensCm;
    @BindView(R.id.artwork_dimens_in)
    TextView dimensIn;
    @BindView(R.id.blurry_image)
    SimpleDraweeView blurryImage;
    @BindView(R.id.artwork_info_markdown)
    MarkdownView artworkInfoMarkdown;
    @BindView(R.id.info_label)
    TextView artworkInfoLabel;

    // Views of the artist
    @BindView(R.id.artist_cardview)
    CardView artistCard;
    @BindView(R.id.artist_name)
    TextView artistName;
    @BindView(R.id.artist_home)
    TextView artistHomeTown;
    //    @BindView(R.id.artist_image)
//    ImageView artistImage;
    @BindView(R.id.artist_lifespan)
    TextView artistLifespan;
    @BindView(R.id.artist_location)
    TextView artistLocation;
    @BindView(R.id.artist_location_label)
    TextView artistLocationLabel;
    @BindView(R.id.artist_nationality)
    TextView artistNationality;

    // Similar Artworks Views
    @BindView(R.id.similar_artworks_rv)
    RecyclerView similarArtworksRv;
    private SimilarArtworksAdapter mSimilarArtAdapter;

    @BindView(R.id.fav_button)
    FloatingActionButton mFavButton;
    @BindView(R.id.adView)
    AdView adView;

    private Artwork mArtworkObject;
    private String mArtworkIdString;
    private String mTitleString;
    private String mMediumString;
    private String mCategoryString;
    private String mDateString;
    private String mMuseumString;
    private String mNewArtworkLinkString;
    private String mNewSquareArtworkLinkString;
    private String mArtistNameFromSlug;
    private String mArtistNameString;
    private String mArtworkThumbnailString;
    private String mDimensInCmString;
    private String mDimensInInchString;
    private String mArtistUrl;
    private String mPermalinkForShare;
    private String mSimilarArtworksLink;

    private ArtistsDetailViewModel mArtistViewModel;

    private boolean mIsFavorite;
    private Tracker mTracker;

    // Variables for Fresco Library
    private Postprocessor mPostprocessor;
    private ImageRequest mImageRequest;
    private PipelineDraweeController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Fresco
        Fresco.initialize(this);
        setContentView(R.layout.activity_artwork_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Obtain the shared Tracker instance.
        // source: https://developers.google.com/analytics/devguides/collection/android/v4/
        TrendyArtApp application = (TrendyArtApp) getApplication();
        mTracker = application.getDefaultTracker();

        // Initialize Ads
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        // TODO: Add my own ID
        MobileAds.initialize(this, getString(R.string.admob_id));

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);

        if (savedInstanceState != null) {
            mIsFavorite = savedInstanceState.getBoolean(IS_FAV_SAVED_STATE);
        }

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();

            mArtworkObject = bundle.getParcelable(ARTWORK_PARCEL_KEY);

            if (mArtworkObject != null) {
                setupUi(mArtworkObject);
            }
        }

        // Check if the item exists in the db already or not!!!
        FavArtRepository.getInstance(getApplication()).executeGetItemById(mArtworkIdString, new ResultFromDbCallback() {
            @Override
            public void setResult(boolean isFav) {
                if (isFav) {
                    mIsFavorite = true;
                    mFavButton.setTag(FAV_TAG);
                    // Set the button to display it's already added
                    Log.d(TAG, "Item already exists in the db.");
                    mFavButton.setImageResource(R.drawable.ic_favorite_24dp);
                } else {
                    // Add to the db
                    mIsFavorite = false;
                    mFavButton.setTag(NON_FAV_TAG);
                    Log.d(TAG, "Insert a new item into the db");
                    mFavButton.setImageResource(R.drawable.ic_favorite_border_24dp);
                }
            }
        });

        clickFab();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_FAV_SAVED_STATE, mIsFavorite);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTracker.setScreenName(TAG);
        // Send initial screen screen view hit.
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void setupUi(Artwork currentArtwork) {

        mArtworkIdString = currentArtwork.getId();

        // Set the Up Button Navigation to another color
        // source: https://stackoverflow.com/a/26837072/8132331
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorText),
                PorterDuff.Mode.SRC_ATOP);

        if (currentArtwork.getTitle() != null) {
            mTitleString = currentArtwork.getTitle();
            artworkName.setText(mTitleString);
            collapsingToolbarLayout.setTitle(mTitleString);
            collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorText));
            Log.d(TAG, "Title of the artwork: " + mTitleString);

        } else {
            artworkName.setText(R.string.not_applicable);
        }

        if (currentArtwork.getMedium() != null) {
            mMediumString = currentArtwork.getMedium();
            artworkMedium.setText(mMediumString);
        } else {
            artworkMedium.setText(R.string.not_applicable);
        }

        if (currentArtwork.getCategory() != null) {
            mCategoryString = currentArtwork.getCategory();
            artworkCategory.setText(mCategoryString);
        } else {
            artworkCategory.setText(R.string.not_applicable);
        }

        if (currentArtwork.getDate() != null) {
            mDateString = currentArtwork.getDate();
            artworkDate.setText(mDateString);
        } else {
            artworkDate.setText(R.string.not_applicable);
        }

        if (currentArtwork.getCollectingInstitution() != null) {
            mMuseumString = currentArtwork.getCollectingInstitution();
            // Hide the Museum if the field is empty
            if (TextUtils.isEmpty(mMuseumString)) {
                artworkMuseum.setVisibility(View.GONE);
                artworkMuseumLabel.setVisibility(View.GONE);
            }
            artworkMuseum.setText(mMuseumString);
        } else {
            artworkMuseum.setText(R.string.not_applicable);
        }

        if (currentArtwork.getDimensions() != null) {
            Dimensions dimensionObject = currentArtwork.getDimensions();

            if (dimensionObject.getCmSize() != null) {
                CmSize cmSizeObject = dimensionObject.getCmSize();
                mDimensInCmString = cmSizeObject.getText();
                dimensCm.setText(mDimensInCmString);
            } else {
                dimensCm.setText(R.string.not_applicable);
            }

            if (dimensionObject.getInSize() != null) {
                InSize inSizeObject = dimensionObject.getInSize();
                mDimensInInchString = inSizeObject.getText();
                dimensIn.setText(mDimensInInchString);
            } else {
                dimensIn.setText(R.string.not_applicable);
            }
        }

        if (currentArtwork.getAdditionalInformation() != null) {
            String addInfo = currentArtwork.getAdditionalInformation();
            // Hide the Additional Information if the field is empty
            if (TextUtils.isEmpty(addInfo)) {
                artworkInfoMarkdown.setVisibility(View.GONE);
                artworkInfoLabel.setVisibility(View.GONE);
            }
            // Load the markdown text
            artworkInfoMarkdown.loadMarkdown(addInfo);
        }

        ImageLinks imageLinksObject = currentArtwork.getLinks();
        MainImage mainImageObject = imageLinksObject.getImage();

        if (currentArtwork.getImageVersions() != null) {

            List<String> imageVersionList = currentArtwork.getImageVersions();

            String versionLargeString;
            // Check if the list with image version contains "large"
            if (imageVersionList.contains("large")) {

                // Set the version to "large"
                versionLargeString = "large";
                Log.d(TAG, "Version of the image: " + versionLargeString);
            } else {

                // Set the first String from the version list
                versionLargeString = imageVersionList.get(0);
                Log.d(TAG, "Version of the image: " + versionLargeString);
            }

            // Get the sixth entry from this list, which corresponds to "square"
            String versionSquareString;
            if (imageVersionList.contains("square")) {

                versionSquareString = "square";
                Log.d(TAG, "Version of the square image: " + versionSquareString);
            } else {

                versionSquareString = imageVersionList.get(0);
                Log.d(TAG, "Version of the square image: " + versionSquareString);
            }


            // Get the link for the current artwork,
            // e.g.: "https://d32dm0rphc51dk.cloudfront.net/rqoQ0ln0TqFAf7GcVwBtTw/{image_version}.jpg"
            String artworkImgLinkString = mainImageObject.getHref();

            // Replace the {image_version} from the artworkImgLinkString with
            // the wanted version, e.g. "large"
            mNewArtworkLinkString = artworkImgLinkString
                    .replaceAll("\\{.*?\\}", versionLargeString);
            Log.d(TAG, "New link to the image: " + mNewArtworkLinkString);

            // Get the first entry from this list, which corresponds to "large"
            mNewSquareArtworkLinkString = artworkImgLinkString.replaceAll("\\{.*?\\}", versionSquareString);
            Log.d(TAG, "New link to the square image: " + mNewSquareArtworkLinkString);

            // Extract the string to thumbnail so that it is saved in favorites
            Thumbnail thumbnail = imageLinksObject.getThumbnail();
            mArtworkThumbnailString = thumbnail.getHref();

            // Initialize Blur Post Processor
            // Tutorial:https://android.jlelse.eu/android-image-blur-using-fresco-vs-picasso-ea095264abbf
            mPostprocessor = new BlurPostprocessor(this, 20);

            // Instantiate Image Request using Post Processor as parameter
            mImageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(mNewArtworkLinkString))
                    .setPostprocessor(mPostprocessor)
                    .build();

            // Instantiate Controller
            mController = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    .setImageRequest(mImageRequest)
                    .setOldController(blurryImage.getController())
                    .build();

            // Load the blurred image
            blurryImage.setController(mController);

        }

        // Set the square image with Picasso
        if (mNewSquareArtworkLinkString == null || mNewSquareArtworkLinkString.isEmpty()) {
            Picasso.get()
                    .load(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(artworkImage);
        } else {
            Picasso.get()
                    .load(Uri.parse(mNewSquareArtworkLinkString))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(artworkImage);
        }

        if (imageLinksObject.getArtists() != null) {

            ArtistsLink artistsLinkObject = imageLinksObject.getArtists();
            mArtistUrl = artistsLinkObject.getHref(); // This link needs a token

            // Initialize the artist ViewModel
            initArtistViewModel(mArtistUrl);
            Log.d(TAG, "Link to the artist: " + mArtistUrl);

            String artworkId = currentArtwork.getId();
            Log.d(TAG, "Artwork id: " + artworkId);

            mArtistNameFromSlug = StringUtils.getArtistNameFromSlug(currentArtwork);
            Log.d(TAG, "Name of Artist after extraction: " + mArtistNameFromSlug);

            //artistNameLink.setText(mArtistNameFromSlug);

            String finalTitleString = mTitleString;

            // Check first if the artist name is not null or "N/A"
            if ((mArtistNameFromSlug == null) || (mArtistNameFromSlug.equals("N/A"))) {
                // Hide the Artist CardView if there is no info about the Artist
                artistCard.setVisibility(View.GONE);
            }
        }

        // Get the Permalink for sharing it outside the app
        Permalink permalinkForShare = imageLinksObject.getPermalink();
        mPermalinkForShare = permalinkForShare.getHref();

        SimilarArtworksLink similarArtworksLink = imageLinksObject.getSimilarArtworks();
        mSimilarArtworksLink = similarArtworksLink.getHref();
        Log.d(TAG, "Similar Artworks link: " + mSimilarArtworksLink);
        initSimilarViewModel(mSimilarArtworksLink);
    }

    /**
     * Method for initializing the ViewModel of the Artist
     *
     * @param artistLink is the given link to the artist
     */
    private void initArtistViewModel(String artistLink) {
        // Initialize the ViewModel
        mArtistViewModel = ViewModelProviders.of(this).get(ArtistsDetailViewModel.class);
        mArtistViewModel.initArtistLink(artistLink);

        mArtistViewModel.getArtistFromLink().observe(this, new Observer<List<Artist>>() {
            @Override
            public void onChanged(@Nullable List<Artist> artists) {
                if (artists != null) {

                    for (int i = 0; i < artists.size(); i++) {
                        Artist artistCurrent = artists.get(i);
                        setupArtistUI(artistCurrent);
                    }
                }
            }
        });
    }

    private void setupArtistUI(Artist currentArtist) {

        if (currentArtist == null) {
            // Hide the Artist CardView if there is no info about the Artist
            artistCard.setVisibility(View.GONE);
        }

        // Get the name of the artist
        if (currentArtist != null) {

            if (currentArtist.getName() != null) {
                mArtistNameString = currentArtist.getName();
                artistName.setText(mArtistNameString);

                // Check first if the artist name is not null or empty
                if ((mArtistNameString == null) || (mArtistNameString.isEmpty())) {
                    // Hide the Artist CardView if there is no info about the Artist
                    artistCard.setVisibility(View.GONE);
                    artistNameButton.setVisibility(View.GONE);
                }

                // Set the name of the Artist to the Button
                artistNameButton.setText(mArtistNameString);

                artistNameButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Check first if the artist name is not null or "N/A"
                        if ((mArtistNameString == null) || (mArtistNameString.equals("N/A")) || TextUtils.isEmpty(mArtistNameString)) {
                            // Show a message to the user that there is no artist for the selected artwork
                            Snackbar.make(coordinatorLayout, R.string.snackbar_no_data_artist, Snackbar.LENGTH_LONG).show();
                            return;
                        }

                        Intent intent = new Intent(ArtworkDetailActivity.this, ArtistDetailActivity.class);
                        // Send the name of the artwork as extra
                        //intent.putExtra(ARTWORK_TITLE_KEY, finalTitleString);
                        //intent.putExtra(ARTWORK_ID_KEY, artworkId);
                        intent.putExtra(ARTIST_URL_KEY, mArtistUrl);

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(ArtworkDetailActivity.this).toBundle();
                            startActivity(intent, bundle);
                        } else {
                            startActivity(intent);
                        }
                    }
                });

            } else {
                artistName.setText(getString(R.string.not_applicable));
            }

            // Get the Home town of the artist
            if (currentArtist.getHometown() != null) {
                String artistHomeTownString = currentArtist.getHometown();
                artistHomeTown.setText(artistHomeTownString);
            } else {
                artistHomeTown.setText(getString(R.string.not_applicable));
            }

            // Get the date of the birth and dead of the artist
            String artistBirthString;
            String artistDeathString;
            if (currentArtist.getBirthday() != null || currentArtist.getDeathday() != null) {
                artistBirthString = currentArtist.getBirthday();
                artistDeathString = currentArtist.getDeathday();

                String lifespanConcatString = artistBirthString + " - " + artistDeathString;
                artistLifespan.setText(lifespanConcatString);
            } else {
                artistLifespan.setText(getString(R.string.not_applicable));
            }

            // Get the location of the artist
            if (currentArtist.getLocation() != null) {
                String artistLocationString = currentArtist.getLocation();
                // Hide the location field if it's empty
                if (TextUtils.isEmpty(artistLocationString)) {
                    artistLocation.setVisibility(View.GONE);
                    artistLocationLabel.setVisibility(View.GONE);
                }
                artistLocation.setText(artistLocationString);
            } else {
                artistLocation.setText(getString(R.string.not_applicable));
            }

            if (currentArtist.getNationality() != null) {
                String artistNationalityString = currentArtist.getNationality();
                artistNationality.setText(artistNationalityString);
            } else {
                artistNationality.setText(getString(R.string.not_applicable));
            }

            // Get the list of image versions first
            List<String> imageVersionList = currentArtist.getImageVersions();
            // Get the first entry from this list, which corresponds to "large"
            String versionString = imageVersionList.get(0);

            ImageLinks imageLinksObject = currentArtist.getLinks();
            MainImage mainImageObject = imageLinksObject.getImage();
            // Get the link for the current artist,
            // e.g.: "https://d32dm0rphc51dk.cloudfront.net/rqoQ0ln0TqFAf7GcVwBtTw/{image_version}.jpg"
            String artistImgLinkString = mainImageObject.getHref();
            // Replace the {image_version} from the artworkImgLinkString with
            // the wanted version, e.g. "large"
            String newArtistLinkString = artistImgLinkString
                    .replaceAll("\\{.*?\\}", versionString);

            // Handle no image cases with placeholders
        /*Picasso.get()
                .load(Uri.parse(newArtistLinkString))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(artistImage);*/

        }
    }

    /**
     * Method for initializing the ViewModel of the Similar Artworks
     *
     * @param similarArtLink is the given link to the similar artworks
     */
    private void initSimilarViewModel(String similarArtLink) {
        // Initialize the ViewModel
        mArtistViewModel = ViewModelProviders.of(this).get(ArtistsDetailViewModel.class);
        mArtistViewModel.initSimilarArtworksLink(similarArtLink);

        mArtistViewModel.getSimilarArtworksLink().observe(this, new Observer<List<Artwork>>() {
            @Override
            public void onChanged(@Nullable List<Artwork> artworkList) {
                if (artworkList != null) {
                    setupSimilarArtworksUI(artworkList);
                }
            }
        });
    }

    private void setupSimilarArtworksUI(List<Artwork> artworkList) {

        mSimilarArtAdapter = new SimilarArtworksAdapter(this, artworkList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        similarArtworksRv.setLayoutManager(layoutManager);
        similarArtworksRv.setAdapter(mSimilarArtAdapter);
    }

    private void clickFab() {
        mFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIconOnFab(v);
            }
        });
    }

    /**
     * Set the icons on the FAB according to the clicks
     *
     * @param view the returned view of the button
     */
    private void setIconOnFab(View view) {
        int tagValue = (int) view.getTag();

        switch (tagValue) {
            case (FAV_TAG):
                // Delete from the db
                deleteItemFromFav();
                mFavButton.setTag(NON_FAV_TAG);
                mFavButton.setImageResource(R.drawable.ic_favorite_border_24dp);
                break;
            case (NON_FAV_TAG):
                // Add an item to the db
                addArtworkToFavorites();
                mFavButton.setTag(FAV_TAG);
                mFavButton.setImageResource(R.drawable.ic_favorite_24dp);
                break;
            default:
                mFavButton.setTag(NON_FAV_TAG);
                mFavButton.setImageResource(R.drawable.ic_favorite_border_24dp);
                break;
        }
    }

    /**
     * Method for deleting an item from the database
     */
    private void deleteItemFromFav() {
        FavArtRepository.getInstance(getApplication()).deleteItem(mArtworkIdString);
        Snackbar.make(coordinatorLayout, R.string.snackbar_item_removed, Snackbar.LENGTH_SHORT).show();
        Log.d(TAG, "Delete the item from the db");
    }

    /**
     * Method for adding an item to the database
     */
    private void addArtworkToFavorites() {

        String artworkId = mArtworkIdString;
        String artworkTitle = mTitleString;
        String artworkArtist = mArtistNameString;
        String artworkCategory = mCategoryString;
        String artworkMedium = mMediumString;
        String artworkDate = mDateString;
        String artworkMuseum = mMuseumString;
        String artworkThumbnail = mArtworkThumbnailString;
        String artworkImage = mNewArtworkLinkString;
        String artworkDimensInch = mDimensInInchString;
        String artworkDimensCm = mDimensInCmString;

        FavoriteArtworks favArtwork = new FavoriteArtworks(artworkId, artworkTitle, artworkArtist,
                artworkCategory, artworkMedium, artworkDate, artworkMuseum, artworkThumbnail, artworkImage, artworkDimensInch, artworkDimensCm);

        FavArtRepository.getInstance(getApplication()).insertItem(favArtwork);
        Snackbar.make(coordinatorLayout, R.string.snackbar_item_added, Snackbar.LENGTH_SHORT).show();
        Log.d(TAG, "Insert a new item into the db");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.artwork_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                // Share the Permalink here
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                if (mPermalinkForShare != null || !TextUtils.isEmpty(mPermalinkForShare)) {
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, mPermalinkForShare);
                    startActivity(shareIntent);
                    Log.d(TAG, "Shared permalink: " + mPermalinkForShare);
                } else {
                    Toast.makeText(this, "Nothing to share.", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

