package kdrosado.trendyart.ui.contentdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import kdrosado.trendyart.model.search.Result;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import kdrosado.trendyart.R;
import kdrosado.trendyart.model.Thumbnail;
import kdrosado.trendyart.model.search.LinksResult;

public class SearchDetailActivity extends AppCompatActivity {

    private static final String RESULT_PARCEL_KEY = "results_key";
    private static final String TAG = SearchDetailActivity.class.getSimpleName();

    @BindView(R.id.content_title)
    TextView contentTitle;
    @BindView(R.id.content_type)
    TextView contentType;
    @BindView(R.id.content_image)
    ImageView contentImage;
    @BindView(R.id.content_image2)
    ImageView secondImage;
    @BindView(R.id.content_description)
    TextView contentDescription;
    @BindView(R.id.toolbar_detail)
    Toolbar toolbar;

    private Result mResults;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra(RESULT_PARCEL_KEY)) {
                Bundle receivedBundle = getIntent().getExtras();
                mResults = receivedBundle.getParcelable(RESULT_PARCEL_KEY);

                if (mResults != null) {
                    String titleString = mResults.getTitle();
                    String typeString = mResults.getType();
                    String descriptionString = mResults.getDescription();
                    Log.d(TAG, "Title: " + titleString + "\nType: " + typeString + "\nDescription: " + descriptionString);

                    contentTitle.setText(titleString);
                    contentType.setText(typeString);
                    contentDescription.setText(descriptionString);

                    if (mResults.getLinks() != null) {
                        LinksResult linksResult = mResults.getLinks();

                        if (linksResult.getThumbnail() != null) {
                            Thumbnail thumbnail = linksResult.getThumbnail();
                            String imageThumbnailString = thumbnail.getHref();

                            if (imageThumbnailString != null || imageThumbnailString.isEmpty()) {
                                // Set the backdrop image
                                Picasso.get()
                                        .load(imageThumbnailString)
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.placeholder)
                                        .into(contentImage);

                                // Set the second image
                                Picasso.get()
                                        .load(imageThumbnailString)
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.placeholder)
                                        .into(secondImage);
                            } else {
                                // Set the backdrop image
                                Picasso.get()
                                        .load(R.drawable.placeholder)
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.placeholder)
                                        .into(contentImage);

                                // Set the second image
                                Picasso.get()
                                        .load(imageThumbnailString)
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.placeholder)
                                        .into(secondImage);
                            }

                        }

                    }
                }
            }
        }
    }
}
