package kdrosado.trendyart.ui.artworks.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import kdrosado.trendyart.R;
import kdrosado.trendyart.callbacks.OnArtworkClickListener;
import kdrosado.trendyart.model.ImageLinks;
import kdrosado.trendyart.model.Thumbnail;
import kdrosado.trendyart.model.artworks.Artwork;
import kdrosado.trendyart.model.artworks.MainImage;
import kdrosado.trendyart.utils.NetworkState;
import kdrosado.trendyart.utils.StringUtils;
import kdrosado.trendyart.callbacks.OnRefreshListener;

// Help from tutorial: https://proandroiddev.com/8-steps-to-implement-paging-library-in-android-d02500f7fffe
public class ArtworkListAdapter extends PagedListAdapter<Artwork, RecyclerView.ViewHolder> {

    private static final String TAG = ArtworkListAdapter.class.getSimpleName();

    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    private Context mContext;
    private NetworkState mNetworkState;

    private OnArtworkClickListener mClickHandler;
    private OnRefreshListener mRefreshHandler;

    private int mMutedColor = 0xFF333333;
    private int mLightMutedColor = 0xFFAAAAAA;
    private int mLastPosition = -1;


    public ArtworkListAdapter(Context context, OnArtworkClickListener clickHandler, OnRefreshListener onRefreshListener) {
        super(Artwork.DIFF_CALLBACK);
        mContext = context;
        mClickHandler = clickHandler;
        mRefreshHandler = onRefreshListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_PROGRESS) {
            View view = layoutInflater.inflate(R.layout.network_state_item, parent, false);
            return new NetworkStateItemViewHolder(view);

        } else {
            View view = layoutInflater.inflate(R.layout.artwork_item, parent, false);
            return new ArtworkItemViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ArtworkItemViewHolder) {
            // This gets the Item from the PagedList
            if (getItem(position) != null) {
                ((ArtworkItemViewHolder)holder).bindTo(getItem(position), position);
            }
        } else {
            ((NetworkStateItemViewHolder) holder).bindView(mNetworkState);
        }
    }


    private boolean hasExtraRow() {
        return mNetworkState != null && mNetworkState != NetworkState.LOADED;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() -1) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = mNetworkState;
        boolean previousExtraRow = hasExtraRow();
        mNetworkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();

        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState){
            notifyItemChanged(getItemCount() - 1);
        }
    }


    public class ArtworkItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.artwork_cardview)
        CardView artworkCard;
        @BindView(R.id.artwork_thumbnail)
        ImageView artworkThumbnail;
        @BindView(R.id.artwork_title)
        TextView artworkTitle;
        @BindView(R.id.artwork_artist)
        TextView artworkArtist;

        private ArtworkItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        private void bindTo(Artwork artwork, int position) {

            // Get the thumbnail from the json tree
            if (artwork.getLinks() != null) {

                ImageLinks currentImageLink = artwork.getLinks();

                String artworkThumbnailString = null;
                // Handle case where no thumbnail is provided!!!
                try {
                    Thumbnail currentThumbnail = currentImageLink.getThumbnail();
                    artworkThumbnailString = currentThumbnail.getHref();

                    Log.d(TAG, "Link to the thumbnail: " + artworkThumbnailString);

                } catch (Exception e) {
                    Log.e(TAG, "Error obtaining thumbnail from the JSON: " + e.getMessage());
                }

                if (artworkThumbnailString == null) {

                    // TODO: Handle case where no "image_version" is provided by the JSON
                    if (artwork.getImageVersions() != null) {

                        List<String> imageVersionList = artwork.getImageVersions();

                        String versionMediumString;
                        // Check if the list with image version contains "medium"
                        if (imageVersionList.contains("medium") || imageVersionList.contains("medium_rectangle")) {

                            // Set the version to "medium"
                            versionMediumString = "medium";
                            Log.d(TAG, "Version of the image: " + versionMediumString);
                        } else {

                            // Set the sixth String from the version list
                            versionMediumString = imageVersionList.get(0);
                            Log.d(TAG, "Version of the image: " + versionMediumString);
                        }

                        MainImage mainImage = currentImageLink.getImage();
                        String artworkImgLinkString = mainImage.getHref();

                        // Replace the {image_version} from the artworkImgLinkString with
                        // the wanted version, e.g. "large"
                        artworkThumbnailString = artworkImgLinkString
                                .replaceAll("\\{.*?\\}", versionMediumString);

                        Log.d(TAG, "New link to the image: " + artworkThumbnailString);
                    }
                }

                Log.d(TAG, "artworkThumbnailString: " + artworkThumbnailString);

                // Get the title from the response
                String artworkTitleString = artwork.getTitle();
                artworkTitle.setText(artworkTitleString);

                // Extract the Artist name from the Slug
                String artistNameString = StringUtils.getArtistNameFromSlug(artwork);
                Log.d(TAG, "Name of the artist: " + artistNameString);
                artworkArtist.setText(artistNameString);

                // Set the image with Picasso
                // Check first if the url in null or empty
                if (artworkThumbnailString == null || artworkThumbnailString.isEmpty()) {
                    // If it's empty or null -> set the placeholder
                    Picasso.get()
                            .load(R.drawable.placeholder)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(artworkThumbnail);
                } else {
                    // If it's not empty -> load the image
                    Picasso.get()
                            .load(Uri.parse(artworkThumbnailString))
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(artworkThumbnail, new Callback() {
                                @Override
                                public void onSuccess() {

                                    Bitmap bitmap = ((BitmapDrawable)
                                            artworkThumbnail.getDrawable()).getBitmap();
                                    artworkThumbnail.setImageBitmap(bitmap);

                                    Palette palette = Palette.from(bitmap).generate();
                                    int generatedMutedColor = palette.getMutedColor(mMutedColor);
                                    int generatedLightColor = palette.getLightVibrantColor(mLightMutedColor);

                                    //artworkCard.setCardBackgroundColor(generatedMutedColor);
                                    //artworkArtist.setTextColor(generatedLightColor);

                                    // Set the item anim here
                                    //setItemAnimator(itemView, position);
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                }
            }
        }

        @Override
        public void onClick(View v) {
            // Get the item position from the PagedList!!!
            Artwork currentArtwork = getItem(getAdapterPosition());
            mClickHandler.onArtworkClick(currentArtwork);
        }
    }

    private void setItemAnimator(View view, int position) {
        if (position > mLastPosition) {
            Animation animation = AnimationUtils
                    .loadAnimation(mContext, R.anim.item_animation_from_bottom);
            view.startAnimation(animation);
            mLastPosition = position;
        }
    }

    public class NetworkStateItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.network_state_layout)
        LinearLayout networkLayout;
        @BindView(R.id.network_state_pb)
        ProgressBar progressBar;
        @BindView(R.id.network_state_error_msg)
        TextView errorMessage;
        @BindView(R.id.network_state_refresh_bt)
        ImageButton refreshButton;

        private NetworkStateItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        private void bindView(NetworkState networkState) {

            if (networkState != null &&
                    networkState.getStatus() == NetworkState.Status.RUNNING) {
                networkLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                errorMessage.setVisibility(View.GONE);
                refreshButton.setVisibility(View.GONE);
            } else if (networkState != null &&
                    networkState.getStatus() == NetworkState.Status.SUCCESS) {
                networkLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                errorMessage.setVisibility(View.GONE);
                refreshButton.setVisibility(View.GONE);
            } else if (networkState != null &&
                    networkState.getStatus() == NetworkState.Status.FAILED) {
                networkLayout.setVisibility(View.VISIBLE);

                progressBar.setVisibility(View.GONE);
                errorMessage.setVisibility(View.VISIBLE);
                refreshButton.setVisibility(View.VISIBLE);
                // Set the click listener here
                refreshButton.setOnClickListener(this::onClick);
            } else {
                networkLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                errorMessage.setVisibility(View.GONE);
                refreshButton.setVisibility(View.GONE);
            }

        }

        @Override
        public void onClick(View v) {
            mRefreshHandler.onRefreshConnection();
        }
    }

}
