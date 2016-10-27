package cordova.plugin.spotify;

import android.util.Log;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class SpotifyPlugin extends CordovaPlugin {
    private final static String LOG_TAG = "SpotifyPlugin";

    private CordovaInterface mInterface;
    private CordovaWebView mWebView;

    private SpotifyPluginController mController;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView){
        super.initialize(cordova,webView);
        Log.i("SpotifyPlugin:"," initialize");

        mInterface = cordova;
        mWebView = webView;

        mController = new SpotifyPluginController(mInterface.getActivity(),this);
    }
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("login")) {
            
            mController.login();
            return true;
        }
        return false;
    }

    
}
