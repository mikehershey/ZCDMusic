<div id="downvote_dialog" style="display:none" title="Sorry about that!">
	<h4>What made this youtube video bad:</h4>
	<input type="radio" name="downVoteReason" value="WRONG_SONG" />Wrong Song 
	<input type="radio" name="downVoteReason" value="BAD_QUALITY" />Bad Quality 
	<input type="radio" name="downVoteReason" value="LIVE" />Live 
	<input type="text" id="playing_track_id_when_this_opened" style="display: none"/>
	<p>Select a reason and hit submit. We'll (hopefully) find a better 
	version of this song for you.</p>
	<p>Unfortunately youtube videos are not always accurately labeled, and there 
	is very little we can do without user feedback. By providing feedback to us 
	we can adjust our youtube video ranking algorithm to pick a better song in 
	the future!</p>
</div>
<div id="appheader" class="appheader">
	<h3 class="title">ZCD</h3>
	<h3 class="title title_bottom">Music</h3>
	<div class="music_navigation">
		<a href="#" id="back_button" class="backward_button_position backward_button"></a>
		<a href="#" id="play_pause_button" class="play_pause_button_position play_button"></a>
		<a href="#" id="forward_button" class="forward_button_position forward_button"></a>
	</div>
	<div id="now_playing" class="now_playing">
		<p id="now_playing_artist" class="now_playing_artist">Artist</p>
		<p id="now_playing_track" class="now_playing_track">Track</p>
		<div id="slider-range-min"></div>
	</div>
	<!-- Tab slider is 100% javascript -->
</div>