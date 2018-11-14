package kdrosado.trendyart.ui.favorites.adapter;

import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import kdrosado.trendyart.R;
import kdrosado.trendyart.callbacks.OnFavItemClickListener;
import kdrosado.trendyart.database.entity.FavoriteArtworks;

public class FavArtworkListAdapter extends PagedListAdapter<FavoriteArtworks, RecyclerView.ViewHolder> {

    private Context mContext;
    private List<FavoriteArtworks> mFavArtworkList;

    private OnFavItemClickListener mCallback;

    public FavArtworkListAdapter(Context context, OnFavItemClickListener onFavItemClickListener) {
        super(FavoriteArtworks.DIFF_CALLBACK);
        mContext = context;
        mCallback = onFavItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fav_artwork_item, parent, false);

        return new FavArtworksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItem(position) != null) {
            ((FavArtworksViewHolder) holder).bindTo(mFavArtworkList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (mFavArtworkList != null) {
            return mFavArtworkList.size();
        }

        return super.getItemCount();
    }

    public FavoriteArtworks getFavoriteAtPosition(int position) {
        return mFavArtworkList.get(position);
    }

    @Override
    public void onCurrentListChanged(@Nullable PagedList<FavoriteArtworks> currentList) {
        mFavArtworkList = currentList;

        notifyDataSetChanged();

        super.onCurrentListChanged(currentList);
    }

    public void swapData(List<FavoriteArtworks> favArtworkList) {
        mFavArtworkList = favArtworkList;
        notifyDataSetChanged();
    }

    public class FavArtworksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.fav_artwork_thumbnail)
        ImageView favThumbnail;
        @BindView(R.id.fav_artwork_title)
        TextView favTitle;
        @BindView(R.id.fav_artwork_category)
        TextView favCategory;
        @BindView(R.id.fav_artwork_medium)
        TextView favMedium;
        @BindView(R.id.fav_artwork_year)
        TextView favYear;

        public FavArtworksViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void bindTo(FavoriteArtworks favArtwork) {
            if (favArtwork != null) {
                String favTitleString = favArtwork.getArtworkTitle();
                favTitle.setText(favTitleString);

                String favCategoryString = favArtwork.getArtworkCategory();
                favCategory.setText(favCategoryString);

                String favMediumString = favArtwork.getArtworkMedium();
                favMedium.setText(favMediumString);

                String favYearString = favArtwork.getArtworkDate();
                favYear.setText(favYearString);

                if (favArtwork.getArtworkThumbnailPath() != null) {
                    String favThumbnailPathString = favArtwork.getArtworkThumbnailPath();
                    Picasso.get()
                            .load(Uri.parse(favThumbnailPathString))
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(favThumbnail);
                } else {
                    Picasso.get()
                            .load(R.drawable.placeholder)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(favThumbnail);
                }
            }
        }

        @Override
        public void onClick(View v) {
            FavoriteArtworks favArtwork = getItem(getAdapterPosition());
            mCallback.onFavItemClick(favArtwork);
        }
    }
}
