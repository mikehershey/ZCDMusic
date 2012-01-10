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

	<!--<button type="button" style="font-size: 12px;">Still can't find a good video? Suggest your own YouTube link</button>-->
</div>
<div id="need_to_login" style="display:none" title="Please sign in with your Google account">
	<p>You need to be logged in to do that. Click the image below and login with your Google account.<p>
	<a href="$loginLink$" style="cursor: pointer">
		<img src="/images/GoogleSignin.png" style="display: block; margin-left: auto; margin-right: auto"/>
	</a>
</div>
<div id="select_sources_dialog" style="display:none" title="Avaliable sources">
	<div id="select_sources_dialog_table_holder">
	</div>
</div>