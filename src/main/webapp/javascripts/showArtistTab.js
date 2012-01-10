var showArtistTab = new function ShowArtistTab() {

	var onAjaxTabLoad = function() {
		// init javascripts for that page
		$('.play_album_now').click(function(event) {
			event.preventDefault();
			// Ajax call for list of playable.
			var albumId = $(this).attr('albumId');
			$.ajax({
				url : "/ajax/album/getAlbumAsPlayables.h7m1",
				data: "id=" + albumId.replace(/&/g, '%26'),
				dataType: 'json',
				'type' : 'POST',
				success : function(data) {
					//give it to recently played playlist.
					playQueuePlaylist.addToPlaylist(data);
					//play it
					playlistManager.changePlaylist(playQueuePlaylist);
					playlistManager.playFirst();
				}
			});
			return false;
		});
		$('.add_album_to_library').click(function(event) {
			event.preventDefault();
			var albumId = $(this).attr('albumId');
			$.ajax({
				url : "/ajax/album/getAlbumAsPlayables.h7m1",
				data: "id=" + albumId.replace(/&/g, '%26'),
				dataType: 'json',
				'type' : 'POST',
				success : function(data) {
					myMusicPlaylist.addToPlaylist(data);
				}
			});
			return false;
		});
		$('.show_album_button').click(function(event) {
			event.preventDefault();
			var albumId = $(this).attr('key');
			showAlbumTab.gotoTab('albumKey=' + albumId);
			return false;
		});
		//init the chosen element
		$('#album_type_selection_field').chosen().change(function() {
			var value = $(this).val().toLowerCase();
			if(value == "all") {
				$('.show_album_button').show();
				$('#no_albums_message').hide()
			} else {
				$('.show_album_button').hide();
				$('.show_album_button[data-album-type=' + value + ']').show();
				if($('.show_album_button[data-album-type=' + value + ']').length == 0) {
					$('#no_albums_message').show()
				} else {
					$('#no_albums_message').hide()
				}
			}
		});
	}

	this.showTab = function(queryString) {
		tabManager.clearAllTabs();
		$.ajax({
			url : '/tabs/showArtist.h7m1',
			data: queryString,
			'type' : 'POST',
			success : function(data) {
				$('#other_pages').css("display", "none");
				$('#other_pages').append(data);
				$('#other_pages').fadeIn();
				onAjaxTabLoad();
			}
		});
	}

	this.gotoTab = function(queryString) {
		document.location.href='#showArtist?' + queryString;
	}

	tabManager.registerTab("showArtist", this, "showArtist");

}();