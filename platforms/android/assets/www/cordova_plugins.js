cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "id": "cordova.plugin.spotify.SpotifyPlugin",
        "file": "plugins/cordova.plugin.spotify/www/SpotifyPlugin.js",
        "pluginId": "cordova.plugin.spotify",
        "clobbers": [
            "cordova.plugins.SpotifyPlugin"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "cordova-plugin-whitelist": "1.3.0",
    "cordova.plugin.spotify": "0.0.1"
};
// BOTTOM OF METADATA
});