package program;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;

/**
 * Class deals with authorization with the Spotify API
 */

public class SpotifyAuth {

    /** app-specific API configurations **/
    private static final String clientID = Confidential.SpotifyAuth_clientID;
    private static final String clientSecret = Confidential.SpotifyAuth_clientSecret;
    private static final String scope = "user-read-currently-playing, user-modify-playback-state";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/player/go");

    /** the API object to be created and passed through **/
    private static SpotifyApi api = new SpotifyApi.Builder().setClientId(clientID).setClientSecret(clientSecret).setRedirectUri(redirectUri).build();

    /** gets the first oAuth URL **/
    public static String getURL() {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = api.authorizationCodeUri()
                .scope(scope)
                .show_dialog(true)
                .build();
        final URI uri = authorizationCodeUriRequest.execute();
        return uri.toString();
    }

    /** sets an access token and returns a SpotifyAPI object of predetermined scope **/
    public static SpotifyApi getAPI(String code) {
        setAccessToken(code);
        return api;
    }



    /** sets the API's access token after oAuth approval **/
    private static void setAccessToken(String codeFromURI) {
        AuthorizationCodeRequest authorizationCodeRequest = api.authorizationCode(codeFromURI).build();
        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            api.setAccessToken(authorizationCodeCredentials.getAccessToken());

            return;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Was unable set credentials.");
    }
}