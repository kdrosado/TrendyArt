package kdrosado.trendyart.utils;

import android.util.Log;

import java.text.Normalizer;

import kdrosado.trendyart.model.artworks.Artwork;

// Helper method for extracting the name of the Artist from the slug received for each artwork response
// in the form of (e.g. "gustav-klimt-der-kuss-the-kiss")
public class StringUtils {

    private static final String TAG = StringUtils.class.getSimpleName();

    public static String getArtistNameFromSlug(Artwork artwork) {
        String artistNameFromSlug;

        // Get the title of the current artwork
        String artworkTitleString = artwork.getTitle();

        // The Slug contains the name of the artist as well as the name of the artwork
        // e.g. "gustav-klimt-der-kuss-the-kiss"
        String artworkSlug = artwork.getSlug();
        Log.d(TAG, "StringUtils: Retrieved Slug string: " + artworkSlug);

        // Remove all "-" from the slug
        String newSlugString = artworkSlug.replaceAll("-", " ").toLowerCase();
        Log.d(TAG, "StringUtils: New Slug string: " + newSlugString);

        // Clear the title of the artwork from any punctuations or characters that are not English letters
        String newTitleString = artworkTitleString
                .toLowerCase()
                .replaceAll("'", "")
                .replaceAll("\\.", "")
                .replaceAll(",", "")
                .replaceAll(":", "")
                .replaceAll("-", " ")
                .replaceAll("[()]", "")
                ;

        Log.d(TAG, "StringUtils: New title string: " + newTitleString);

        // Normalize the letters
        // Tutorial here: https://www.drillio.com/en/2011/java-remove-accent-diacritic/
        String normalizedTitleString = Normalizer.normalize(newTitleString, Normalizer.Form.NFD);
        String removedDiacriticsFromTitle = normalizedTitleString
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").trim();

        if (newSlugString.contains(removedDiacriticsFromTitle)) {

            artistNameFromSlug = newSlugString
                    .replace(removedDiacriticsFromTitle, "").trim();
            Log.d(TAG, "StringUtils: Artist Name From Slug: " + artistNameFromSlug);

            // Check if the name is empty or contains any numbers
            if (artistNameFromSlug.equals(("")) || artistNameFromSlug.equals("[0-9]")) {
                artistNameFromSlug = "N/A";
            }

            // Return the name of the Artist
            return artistNameFromSlug;
        } else {
            // Return just "Artist"
            return "Artist";
        }
    }

}

