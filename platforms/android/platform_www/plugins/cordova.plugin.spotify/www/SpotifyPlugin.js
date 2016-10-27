cordova.define("cordova.plugin.spotify.SpotifyPlugin", function(require, exports, module) {
cordova.define("cordova.plugin.spotify.SpotifyPlugin", function(require, exports, module) {
var exec = require('cordova/exec');


});
var Spotify  = {
  play : function(songId) {
    cordova.exec(
      function() {},
      function() {},
      "SpotifyPlugin",
      "play",
      [songId]
    )
  },
  pause : function() {
    cordova.exec(
      function() {},
      function() {},
      "SpotifyPlugin",
      "pause",
      []
    )
  },
  resume : function() {
    cordova.exec(
      function() {},
      function() {},
      "SpotifyPlugin",
      "resume",
      []
    )
  },
  login : function(options, success, failure) {
      var scopes = options && options.scopes || ["user-read-private", "streaming"];
      var fetchTokenManually = options && options.fetchTokenManually || false;


      var successCB = success || function() {},
        failureCB = failure || function() {}

      cordova.exec(
        successCB,
        failureCB,
        "SpotifyPlugin",
        "login",
        [scopes, fetchTokenManually]
      )
    },
    logout : function(success) {

      cordova.exec(
        success || function() {},
        function() {},
        "SpotifyPlugin",
        "logout",
        []
      )
    }
};
exports = Spotify;
});
