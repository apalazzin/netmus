

function onYouTubePlayerReady(playerId) {
  ytplayer = document.getElementById("youtube_player");
  ytplayer.addEventListener("onStateChange", "onytplayerStateChange");
}

function onytplayerStateChange(newState) {
   
	youTubeChange(newState);
}

