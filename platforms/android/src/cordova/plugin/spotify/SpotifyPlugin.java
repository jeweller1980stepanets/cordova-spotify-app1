package cordova.plugin.spotify;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Debug;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;

import com.spotify.sdk.android.player.Connectivity;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.SpotifyPlayer;


import java.net.URL;

/**
 * Created by Paul on 4/4/2016.
 */
public class SpotifyPlugin extends CordovaPlugin implements
        Player.NotificationCallback, ConnectionStateCallback{
    private static final String TAG = "CDVSpotify";

    private static final String ACTION_LOGIN = "login";
    private static final String ACTION_PLAY = "play";
    private static final String ACTION_PAUSE = "pause";
    private static final String ACTION_RESUME = "resume";

    private static final int REQUEST_CODE = 1337;

    private String clientId ="4eb7b5c08bee4d759d34dbc1823fd7c5";
    private String redirectUri = "testshema://callback";


    private CallbackContext loginCallback;
    private String currentAccessToken;
    private Boolean isLoggedIn = false;

    private SpotifyPlayer currentPlayer;
    private PlaybackState mCurrentPlaybackState;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        Log.i(TAG, "Initializing...");

      //  int clientResId = cordova.getActivity().getResources().getIdentifier("client_id", "string", cordova.getActivity().getPackageName());
     //   int cbResId = cordova.getActivity().getResources().getIdentifier("redirect_uri", "string", cordova.getActivity().getPackageName());

   //     Log.i(TAG, "Client ID ID" + clientResId);
      //  Log.i(TAG, "cb ID ID" + cbResId);

      //  clientId = cordova.getActivity().getString(clientResId);
     //   redirectUri = cordova.getActivity().getString(cbResId) + "://callback";

        Log.i(TAG, "Set up local vars" + clientId + redirectUri);

        cordova.setActivityResultCallback(this);

    }

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) {
        Boolean success = false;

        if(ACTION_LOGIN.equalsIgnoreCase(action)) {
            Log.i(TAG, "LOGIN");
            JSONArray scopes = new JSONArray();
            Boolean fetchTokenManually = false;

            try {
                scopes = data.getJSONArray(0);
                fetchTokenManually = data.getBoolean(1);
            } catch(JSONException e) {
                Log.e(TAG, e.toString());
            }

            cordova.setActivityResultCallback(this);
            loginCallback = callbackContext;
            this.login(scopes, fetchTokenManually);
            success = true;
        } else if(ACTION_PLAY.equalsIgnoreCase(action)) {
            String uri = "";

            try {
                uri = data.getString(0);
            } catch(JSONException e) {
                Log.e(TAG, e.toString());
            }

            this.play(uri);
            success = true;
        } else if(ACTION_PAUSE.equalsIgnoreCase(action)) {
            this.pause();
            success = true;
        } else if(ACTION_RESUME.equalsIgnoreCase(action)) {
            this.resume();
            success = true;
        }


        return success;
    }

    private final Player.NotificationCallback mNotificationCallback = new Player.NotificationCallback(){

        @Override
        public void onPlaybackEvent(PlayerEvent playerEvent) {
            Log.d(TAG,"NotificationCallback OK! ");
        }

        @Override
        public void onPlaybackError(Error error) {
            Log.d(TAG,"NotificationCallback ERROR:" + error);
        }
    };
    private final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {
        @Override
        public void onSuccess() {
            Log.d(TAG,"OK!");
        }

        @Override
        public void onError(Error error) {
            Log.d(TAG,"ERROR:" + error);
        }
    };

    //scopes -- self exaplanatory
    //manualMode -- If you set manualMode = true, the login process will generate a CODE instead of a TOKEN, so you can manually refresh and obtain a refresh
    //token. AGAIN : YOU MUST OBTAIN A ACCESS TOKEN MANUALLY IF you SET THiS TO TRUE
    private void login(JSONArray scopes, Boolean fetchTokenManually) {
       /* AuthenticationResponse.Type authType = fetchTokenManually ? AuthenticationResponse.Type.CODE :AuthenticationResponse.Type.TOKEN;


        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(clientId,
                authType,
                redirectUri);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        builder.setShowDialog(true);
        AuthenticationRequest request = builder.build();

        Log.d(TAG, "Client ID " + clientId + "AUTH RESPONSE TYPE " + AuthenticationResponse.Type.TOKfinal AuthenticationRequest request = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(new String[]{"user-read-private", "playlist-read", "playlist-read-private", "streaming"})
                .build();EN + "REDIRECT URI " + redirectUri + " Scopes " + scopes + " manual " + fetchTokenManually);
*/
        final AuthenticationRequest request = new AuthenticationRequest.Builder(clientId, fetchTokenManually ? AuthenticationResponse.Type.CODE :AuthenticationResponse.Type.TOKEN, redirectUri)
                .setScopes(new String[]{"user-read-private", "playlist-read", "playlist-read-private", "streaming"})
                .build();
        AuthenticationClient.openLoginActivity(cordova.getActivity(), REQUEST_CODE, request);
    }

    private void logout() {
        AuthenticationClient.clearCookies(cordova.getActivity());

        this.clearPlayerState();
        isLoggedIn = false;
        currentAccessToken = null;
    }

    private void clearPlayerState() {
        if(currentPlayer != null) {
            currentPlayer.pause(mOperationCallback);
            currentPlayer.logout();
        }

        currentPlayer = null;
    }




    private void play(String uri) {
        Log.i(TAG, "Play: Is logged in -" + isLoggedIn + "Current Access" + currentAccessToken + "Current player" + currentPlayer);
        if(clientId == null || isLoggedIn == false || currentAccessToken == null || currentPlayer == null) return;

        if(!currentPlayer.isLoggedIn()) {
            Log.e(TAG, "Current Player is initialized but player is not logged in, set access token manually or call login with fetchTokenManually : false");
            return;
        }

        Log.i(TAG, "Playing URI: " + uri);

        currentPlayer.playUri(mOperationCallback, uri,0,0);
    }

    private void getPlayerState() {
        mCurrentPlaybackState =  currentPlayer.getPlaybackState();

                Log.i(TAG, "Player State" + mCurrentPlaybackState.toString());


    }

    private void pause() {
        if(clientId == null || isLoggedIn == false || currentAccessToken == null || currentPlayer == null) return;

        currentPlayer.pause(mOperationCallback);
    }

    private void resume() {
        currentPlayer.resume(mOperationCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        Log.i(TAG, "ACTIVITY RESULT: ");
        Log.i(TAG, "Request Code " + requestCode);
        Log.i(TAG, "Result Code " + resultCode);

        JSONObject ret = new JSONObject();

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch(response.getType()) {
                case TOKEN :
                    isLoggedIn = true;
                    Log.i(TAG, "TOKEN " + response.getAccessToken() );
                    currentAccessToken = response.getAccessToken();
                    onAuthenticationComplete(response);

                    break;
                case CODE :
                    isLoggedIn = false;
                    Log.i(TAG, "RECEIVED CODE" + response.getCode());

                    try {
                        ret.put("authCode", response.getCode());
                    } catch(JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }

                    loginCallback.success(ret);
                    loginCallback = null;
                    break;
                case ERROR :
                    Log.e(TAG, response.getError());
                    loginCallback.error(response.getError());
                    break;
            }
        }


    }
    private Connectivity getNetworkConnectivity(Context context) {
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            Log.d(TAG,"active network" + activeNetwork.isConnected());
            return Connectivity.fromNetworkType(activeNetwork.getType());
        } else {
            return Connectivity.OFFLINE;
        }
    }

    private void onAuthenticationComplete(AuthenticationResponse response) {
        // Once we have obtained an authorization token, we can proceed with creating a Player.

        if (currentPlayer == null) {
            Config playerConfig = new Config(cordova.getActivity(), response.getAccessToken(), clientId);
            // Since the Player is a static singleton owned by the Spotify class, we pass "this" as
            // the second argument in order to refcount it properly. Note that the method
            // Spotify.destroyPlayer() also takes an Object argument, which must be the same as the
            // one passed in here. If you pass different instances to Spotify.getPlayer() and
            // Spotify.destroyPlayer(), that will definitely result in resource leaks.
            currentPlayer = Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {

                @Override
                public void onInitialized(SpotifyPlayer player) {
                    Log.d(TAG,"-- Player initialized --");
                    currentPlayer.setConnectivityStatus(mOperationCallback, getNetworkConnectivity(cordova.getActivity().getApplicationContext()));
                    currentPlayer.addNotificationCallback(SpotifyPlugin.this);
                    currentPlayer.addConnectionStateCallback(SpotifyPlugin.this);
                    // Trigger UI refresh
                }

                @Override
                public void onError(Throwable error) {
                    Log.d(TAG,"Error in initialization: " + error.getMessage());
                }
            });


        } else {
            currentPlayer.login(response.getAccessToken());
        }

    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(int i) {
        Log.d("MainActivity", "Login failed" + i);
    }


    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
    }
}
