package kdrosado.trendyart.widget;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import kdrosado.trendyart.AppExecutors;
import kdrosado.trendyart.R;
import kdrosado.trendyart.database.entity.FavoriteArtworks;
import kdrosado.trendyart.repository.FavArtRepository;

public class FavListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = FavListRemoteViewsFactory.class.getSimpleName();

    private List<FavoriteArtworks> mFavList;
    private Application mApplication;
    private int mAppWidgetId;

    public FavListRemoteViewsFactory(Application application, Intent intent){
        mApplication = application;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Widget: onCreate triggered now");
    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "Widget: onDataSetChanged triggered now");

        // Get the data here from the Repository on a background thread
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mFavList = FavArtRepository.getInstance(mApplication).getFavArtworksList();

                Log.d(TAG, "Widget: Get the fav list: " + mFavList.size());
            }
        });

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Widget: onDestroy triggered now");
    }

    @Override
    public int getCount() {
        if (mFavList != null) {
            Log.d(TAG, "Widget: getCount the fav list: " + mFavList.size());
            return mFavList.size();
        }
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Log.d(TAG, "Widget: getViewAt triggered now");

        if (mFavList == null || mFavList.size() == 0) {
            return null;
        }

        RemoteViews views = new RemoteViews(mApplication.getPackageName(),
                R.layout.widget_fav_item);

        String idString = mFavList.get(position).getArtworkId();
        Log.d(TAG, "Widget: Get the id: " + idString);

        Bundle extras = new Bundle();
        extras.putInt(TrendyArtWidget.EXTRA_ITEM, position);

        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.widget_thumbnail, fillIntent);

        String thumbnailString = mFavList.get(position).getArtworkThumbnailPath();
        // Set the thumbnail on the widget view
        // SO post: https://stackoverflow.com/a/27851642/8132331
        try {
            Bitmap bitmap;

            if (thumbnailString == null || thumbnailString.isEmpty()) {
                bitmap = Picasso.get()
                        .load(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .resize(300, 300)
                        .centerCrop()
                        .error(R.mipmap.ic_launcher).get();
            } else {
                bitmap = Picasso.get()
                        .load(thumbnailString)
                        .placeholder(R.drawable.placeholder)
                        .resize(300, 300)
                        .centerCrop()
                        .error(R.mipmap.ic_launcher).get();
            }

            views.setImageViewBitmap(R.id.widget_thumbnail, bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
