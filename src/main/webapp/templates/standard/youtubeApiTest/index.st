<html>
<head>
	<script type='text/javascript' src='http://www.youtube.com/player_api'></script>
	<script type='text/javascript'>

		function onYouTubePlayerAPIReady() {
			player = new YT.Player('videoDiv', {
				height: '200',
				width: '200',
				videoId: 'DFZC5-0XwU8',
				events: {
					'onReady': onPlayerReady,
					'onError': onError
				}
			});
		}

		function onPlayerReady(evt) {
			
		}

		function onError(event) {
			alert("Error: " + event.data);
		}

	</script>

</head>
<body>
	<p>So click the video to get past the apple autoplay block and notice the video plays fine</p>
	<div id="videoDiv">
	</div>
	<p>These links below work on my macbook but cause the error 2 in safari on IPAD 2</p>
	<button type="button" onClick='player.cueVideoById("DFZC5-0XwU8");player.setPlaybackQuality('large');player.playVideo()'>loadVideoById("DFZC5-0XwU8")</button>
	<button type="button" onClick='player.loadVideoById("V6rp6EppuwI")'>loadVideoById("V6rp6EppuwI")</button>
</body>
</html>