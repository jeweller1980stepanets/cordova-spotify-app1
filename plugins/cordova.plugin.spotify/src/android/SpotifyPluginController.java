package cordova.plugin.spotify;

import android.app.Activity;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.Player.NotificationCallback;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.PlayerEvent;


public class SpotifyPluginController implements
       NotificationCallback, ConnectionStateCallback {

    private final static String LOG_TAG = "SpotifyPluginController";

    @SuppressWarnings("SpellCheckingInspection")
    private static final String CLIENT_ID = "089d841ccc194c10a77afad9e1c11d54";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String REDIRECT_URI = "testschema://callback";
    private static final int REQUEST_CODE = 1337;

    private Activity mActivity;
    private SpotifyPlugin mPlugin;

    public SpotifyPluginController(final Activity activity, final SpotifyPlugin plugin){
        Log.i(LOG_TAG,"constructor");
        mActivity = activity;
        mPlugin = plugin;
    }

    public void login() {
        final AuthenticationRequest request = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(new String[]{"user-read-private", "playlist-read", "playlist-read-private", "streaming"})
                .build();

        AuthenticationClient.openLoginActivity(mActivity,REQUEST_CODE,request);
        Log.i(LOG_TAG,"login");
    }


    @Override
    public void onLoggedIn() {

    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(int i) {

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {

    }

    @Override
    public void onPlaybackError(Error error) {

    }


}