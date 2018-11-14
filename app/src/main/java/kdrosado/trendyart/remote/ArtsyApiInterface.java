package kdrosado.trendyart.remote;

import kdrosado.trendyart.model.artists.ArtistWrapperResponse;
import kdrosado.trendyart.model.artworks.ArtworkWrapperResponse;
import kdrosado.trendyart.model.token.TypeToken;
import kdrosado.trendyart.model.search.SearchWrapperResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ArtsyApiInterface {


    @POST("/api/tokens/xapp_token")
    Call<TypeToken> refreshToken(@Query("client_id") String clientId,
                                 @Query("client_secret") String clientSecret);

    /**
     * Endpoint for fetching Artworks
     * link: https://api.artsy.net/api/artworks?size=10 + header with token
     *
     * @param itemSize displayed items to the user
     * @return a call to the Artsy Response
     */
    @GET("/api/artworks")
    Call<ArtworkWrapperResponse> getArtsyResponse(@Query("size") int itemSize);

    /**
     * Make call according to the url that is received from the json response
     * @param nextUrl is dynamic link for next page with items
     * @param itemSize is the size of requested items at once
     * @return the response for the next page
     */
    @GET
    Call<ArtworkWrapperResponse> getNextLink(@Url String nextUrl, @Query("size") int itemSize);

    /**
     * Make call according to the url that is received from the json response
     *
     * @param artistLink is dynamic link for artist page
     * @return the response for the artist page
     */
    @GET
    Call<ArtistWrapperResponse> getArtistLink(@Url String artistLink);

    /**
     * Make call according to the url that is received from the JSON
     *
     * @param similarArtLink is dynamic link for similar artworks page
     * @return the response for the similar artworks page
     */
    @GET
    Call<ArtworkWrapperResponse> getSimilarArtLink(@Url String similarArtLink);

    /**
     * Endpoint for fetching Artist of the current Artwork
     */
    @GET("/api/artists")
    Call<ArtistWrapperResponse> getArtist(@Query("artwork_id") String artworkId);

    /**
     * Endpoint for Search results
     */
    @GET("/api/search")
    Call<SearchWrapperResponse> getSearchResults(@Query("q") String queryWord, @Query("size") int itemSize, @Query("type") String type);

    @GET
    Call<SearchWrapperResponse> getNextLinkForSearch(@Url String nextUrl, @Query("size") int itemSize, @Query("type") String type);

}
