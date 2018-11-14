package kdrosado.trendyart.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import kdrosado.trendyart.model.token.TypeToken;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import static kdrosado.trendyart.utils.Utils.CLIENT_ID;
import static kdrosado.trendyart.utils.Utils.CLIENT_SECRET;

public class TokenAuthenticator implements Authenticator {

    private static final String TAG = TokenAuthenticator.class.getSimpleName();

    private final TokenServiceHolder mTokenServiceHolder;
    private retrofit2.Response<TypeToken> mAccessToken;
    private String token;

    public TokenAuthenticator(TokenServiceHolder tokenServiceHolder) {
        mTokenServiceHolder = tokenServiceHolder;
    }


    @Override
    public Request authenticate(@NonNull Route route, @NonNull Response response) throws IOException {

        ArtsyApiInterface service = mTokenServiceHolder.get();
        if (service == null) {
            return null;
        }

        mAccessToken = service.refreshToken(CLIENT_ID, CLIENT_SECRET).execute();
        TypeToken typeToken = mAccessToken.body();
        if (typeToken != null) {
            token = typeToken.getToken();
        }
        Log.d(TAG, "Token: " + token);

        return response.request().newBuilder()
                .build();
    }

}

