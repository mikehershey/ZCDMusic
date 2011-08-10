<div id="my_music_library_holder">
    <div id="my_music_library_playlist_holder" style=$if(userTracks)$"display: block;"$else$"display: none;"$endif$>
        <div class="tab_header">
            <h2 class="tab_title">Library:</h2>
        </div>
        $myMusicLibrary()$
    </div>
	<div id="splash_page_holder" style=$if(!userTracks)$"display: block;"$else$"display: none;"$endif$>
		<div class="tab_header">
			<h2 class="tab_title"></h2>
		</div>
		$splashPage()$
	</div>
</div>
$playQueue()$