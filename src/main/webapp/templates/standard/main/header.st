<div id="appheader" class="appheader">
	<div id="header_div_user_login" style="float: right; padding-right: 5px; width: 275px">
		<div style="float: right; margin-top: 5px; height: 20px;">
			$if(userEmail)$
				<p style="padding: 0; margin: 0; display: inline-block; margin-bottom: 13px; padding-right: 2px; border-right: 1px solid black">$userEmail$</p>
			$else$
				<a href="$loginLink$" style="cursor: pointer">
					<img src="/images/header/signin.png"/>
				</a>
			$endif$
			$if(userEmail)$
				<!--Only logout link for now gear is too hard to make look good<img id="user_preferences_gear" src="/images/header/gear.png"/>-->
				<a href="$logoutLink$" id="signout_link">Sign out</a>
			$endif$
		</div>
		<input type="text" name="searchTerm" class="small_search_box" id="header_search_box" value="Search for artist/album/track..."/>
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