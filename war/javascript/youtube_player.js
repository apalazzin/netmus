

function onYouTubePlayerReady(playerId) {
  ytplayer = document.getElementById("youtube_player");
  ytplayer.addEventListener("onStateChange", "onytplayerStateChange");
  ytplayer.addEventListener("onError", "onytplayerError");
}

function onytplayerStateChange(newState) {
	
	youTubeChange(newState);
}

function onytplayerError(newState) {
	
	youTubeError(newState);
}
