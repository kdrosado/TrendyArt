package kdrosado.trendyart.ui.artworkdetail.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import kdrosado.trendyart.R;
import kdrosado.trendyart.model.ImageLinks;
import kdrosado.trendyart.model.Thumbnail;
import kdrosado.trendyart.model.artworks.Artwork;
import kdrosado.trendyart.utils.StringUtils;

public class SimilarArtworksAdapter extends RecyclerView.Adapter<SimilarArtworksAdapter.SimilarViewHolder> {

    private static final String TAG = SimilarArtworksAdapter.class.getSimpleName();
    private List<Artwork> mArtworkList;
    private Context mContext;

    private int mMutedColor = 0xFF333333;

    public SimilarArtworksAdapter(Context context, List<Artwork> artworkList) {
        mContext = context;
        mArtworkList = artworkList;
    }

    @NonNull
    @Override
    public SimilarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.similar_artwork_item, parent, false);

        return new SimilarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarViewHolder holder, int position) {
        Artwork artwork = mArtworkList.get(position);
        holder.bindTo(artwork);
    }

    @Override
    public int getItemCount() {
        if (mArtworkList == null) {
            return 0;
        }
        Log.d(TAG, "Size of the Similar Artworks list: " + mArtworkList);
        return mArtworkList.size();
    }

    public class SimilarViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.similar_artwork_thumbnail)
        ImageView similarArtworkThumbnail;
        @BindView(R.id.similar_artwork_bg)
        ImageView similarBackground;
        @BindView(R.id.similar_artwork_title)
        TextView similarTitle;
        @BindView(R.id.similar_artwork_artist)
        TextView similarArtist;

        public SimilarViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindTo(Artwork artwork) {
            if (artwork != null) {
                ImageLinks imageLinks = artwork.getLinks();
                String artworkThumbnailString = null;
                try {
                    Thumbnail thumbnail = imageLinks.getThumbnail();
                    artworkThumbnailString = thumbnail.getHref();
                } catch (Exception e) {
                    Log.e(TAG, "Error obtaining thumbnail from JSON: " + e.getMessage());
                }

                String similarTitleString = artwork.getTitle();
                similarTitle.setText(similarTitleString);

                String artistNameString = StringUtils.getArtistNameFromSlug(artwork);
                Log.d(TAG, "Name of the artist: " + artistNameString);
                similarArtist.setText(artistNameString);

                if (artworkThumbnailString == null || TextUtils.isEmpty(artworkThumbnailString)) {
                    // If it's empty or null -> set the placeholder
                    Picasso.get()
                            .load(R.drawable.placeholder)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(similarArtworkThumbnail);
                } else {
                    // If it's not empty -> load the image
                    Picasso.get()
                            .load(Uri.parse(artworkThumbnailString))
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(similarArtworkThumbnail, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap bitmap = ((BitmapDrawable) similarArtworkThumbnail
                                            .getDrawable()).getBitmap();
                                    similarArtworkThumbnail.setImageBitmap(bitmap);

                                    Palette palette = Palette.from(bitmap).generate();
                                    int generatedMutedColor = palette.getMutedColor(mMutedColor);

                                    similarBackground.setBackgroundColor(generatedMutedColor);
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                }

            }
        }
    }
}
