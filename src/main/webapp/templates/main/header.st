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

	<button type="button" style="font-size: 12px;">Still can't find a good video? Suggest your own YouTube link</button>
</div>
<div id="appheader" class="appheader">
	<div id="header_div_user_login" style="float: right; padding-right: 5px; width: 200px">
		<p style="text-align:right; padding: 0; margin: 0; padding-top: 10px;">$userEmail$</p>
		<input type="text" name="searchTerm" class="small_search_box" id="header_search_box" value="Search for artist/album/track..." style="margin-top: 5px;"/>
	</div>
	<img src="/images/header/music.png" id="app_logo" />
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
</div>