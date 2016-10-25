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
    //final  private string TAG = "SpotifyPlugin";
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView){
        super.initialize(cordova,webView);
        Log.i("SpotifyPlugin:"," initialize");
        //// TODO: 25.10.2016
    }
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            
            
            return true;
        }
        return false;
    }

    
}
