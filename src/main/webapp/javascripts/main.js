/**
 * Singleton helper that maintains a chain of things that need to be init-ed and
 * inits them on document load. If you add something it must have an init
 * function.
 */
var initChain = new function InitChain() {

	var needsInit = [];

	this.addToChain = function(thingToAdd) {
		needsInit.push(thingToAdd);
	}

	this.initAll = function() {
		needsInit.forEach(function(item) {
			item.init();
		});
	}

}();

var resizer = new function Resizer() {
	var self = this;

	this.resize = function() {
		$("#now_playing").css('left', $(document).width() / 2 - 200);
		var h = $(window).height() - 61;
		$('#left_dock').height(h);
		$('#right_dock').height(h);
		$('#main_content').height(h);
		
		$('#ytPlayer').css('left',$(document).width() / 2 + 210)

		$("#slider-range-min").slider({
			range : "min",
			value : 0,
			min : 0,
			max : 100
		});
	}

	this.init = function() {
		self.resize();
		$(window).resize(function() {
			self.resize();
		});
	}

	initChain.addToChain(self);

}();

/**
 * Tabs can register themselves with the tab manager if they register before init
 * is called they will be automatically bound to left side navigation #whatever
 */
var tabManager = new function TabManager() {

	var tabNames = [];
	var tabNameToTab = new Object();
	var tabNameToButtonIdMap = new Object();
	var urlToTabMap = new Object();

	/**
	 * Tabs added must have a showTab() function which takes an option param
	 * of a query string.
	 */
	this.registerTab = function(tabName, tab, url, button) {
		tabNames.push(tabName);
		tabNameToTab[tabName] = tab;
		if(url) {
			urlToTabMap[url] = tab;
		}
		if(button) {
			tabNameToButtonIdMap[tabName] = button;
		}
	};
	
	this.showPoundedPage = function() {
		var anchor = window.location.hash.replace("#", "");
		if(!anchor) {
			return;
		}
		var page = anchor;
		var queryString = '';
		if(page.indexOf("?") != -1) {
			page = page.substring(0, page.indexOf('?'));
			queryString = anchor.substring(anchor.indexOf("?") + 1);
		}
		var tab = urlToTabMap[page];
		tab.showTab(queryString);
	};

	this.init = function() {
		for (var i = 0; i < tabNames.length; i++) {
			var tabName = tabNames[i];
			var tabButton = tabNameToButtonIdMap[tabName];
			if(tabButton) {
				$(tabButton).click(function(tabName) {
					return function() {
						tabNameToTab[tabName].showTab();
					};
				}(tabName));
			}
		}
	};

	initChain.addToChain(this);

}();

var browseTab = new function BrowseTab() {
	
	this.showTab = function() {
		$('#other_pages').empty();
		$('#my_music_library_holder').hide();
		$('#recently_viewed_playlist_holder').hide();
		$.ajax({
			url : "/tabs/browsewarehouse.h7m1",
			success : function(data) {
				$('#other_pages').css("display", "none");
				$('#other_pages').append(data);
				$('#other_pages').fadeIn();
			}
		});
	};
	
	tabManager.registerTab("browse", this, "browse", '#browse_button');
	
}

var searchTab = new function SearchTab() {

	var onAjaxTabLoad = function(data) {
		$('#other_pages').css("display", "none");
		$('#other_pages').append(data);
		$('#other_pages').fadeIn();
		$('#search_box').click(function() {
			$(this).val('');
		});

		$.widget("custom.catcomplete", $.ui.autocomplete, {
			_renderMenu : function(ul, items) {
				var self = this;
				var currentCategory = "";
				$.each(items, function(index, item) {
					if (item.type != currentCategory) {
						ul.append("<li class='ui-autocomplete-category'>" + item.type + "</li>");
						currentCategory = item.type;
					}
					self._renderItem(ul, item);
				});
			}
		});

		$("#search_box").catcomplete({
			source : "/searchWarehouse.h7m1",
			minLength : 1,
			select : function(event, ui) {
				if (ui.item.type == 'TRACK') {
					var playable = new Playable(ui.item.key, ui.item.track, ui.item.artist, ui.item.youtubeId);
					var playables = [];
					playables[0] = playable;
					recentlyPlayedPlaylist.addToPlaylist(playables);
					//play it
					playlistManager.changePlaylist(recentlyPlayedPlaylist);
					playlistManager.playFirst();
				} else {
					showArtistTab.showTab(ui.item.queryString);
				}
				return false;
			}
		}).data("catcomplete")._renderItem = function(ul, item) {
			if (item.type == 'ARTIST') {
				item.queryString = "id=" + item.key.replace("&", "%26");
				return $("<li></li>")
						.data("item.autocomplete", item)
						.append(
								"<a href='#'>"
										+ "<img src='/utils/showImage.h7m1?id="
										+ item.imageUrl
										+ "' style='float: left; margin-right: 5px;' /><h1>"
										+ item.artist + "</h1></a>").appendTo(
								ul);
			} else if (item.type == 'ALBUM') {
				item.queryString = "id=" + item.key.replace("&", "%26");
				return $("<li></li>")
						.data("item.autocomplete", item)
						.data("loadToUrl",
								"/tabs/showAlbum.h7m1?id=" + item.key)
						.append(
								"<a href='#'>"
										+ "<img src='/utils/showImage.h7m1?id="
										+ item.imageUrl
										+ "' style='float: left; margin-right: 5px;' /><h2>"
										+ item.artist + "</h2><h2>"
										+ item.album + "</h2></a>")
						.appendTo(ul);
			} else {
				item.queryString = item.key.replace("&", "%26");
				return $("<li></li>").data("item.autocomplete", item).data(
						"trackKey", item.key).append(
						"<a href='#'><h2>" + item.track + "</h2><h3>"
								+ item.artist + "</h3></a>").appendTo(ul);
			}
		};
	}

	this.showTab = function() {
		$('#other_pages').empty();
		$('#my_music_library_holder').hide();
		$('#recently_viewed_playlist_holder').hide();
		$.ajax({
			url : "/tabs/showSearchWarehouse.h7m1",
			success : function(data) {
				onAjaxTabLoad(data);
			}
		});
	}
	
	tabManager.registerTab("showSearch", this, "showSearch", '#search_button');

}();

var requestTab = new function RequestTab() {

	var submitForm = function() {
		$.ajax({
					url : "/tabs/showRequestArtist.h7m1",
					type : "POST",
					data : "artistName=" + $('#request_box').val(),
					success : function(data) {
					},
					error : function(data) {
						alert("Uh oh. As it turns out your request to add a new artist failed!")
					}
				})
		$('#request_thanks').dialog({
			resizable : false,
			height : 200,
			width : 600,
			modal : true,
			buttons : {
				"Okay" : function() {
					$(this).dialog("close");
					$('#other_pages').empty();
					$('#other_pages').hide();
					$('#my_music_library_holder').fadeIn();
				}
			}
		});
	}

	var onAjaxTabLoad = function(data) {
		$('#other_pages').css("display", "none");
		$('#other_pages').append(data);
		$('#other_pages').fadeIn();
		$('#request_box').click(function() {
			$(this).val('');
		});
		$('#request_form').submit(function(e) {
			e.preventDefault();
			submitForm();
		});
	}

	this.showTab = function() {
		$('#other_pages').empty();
		$('#my_music_library_holder').hide();
		$('#recently_viewed_playlist_holder').hide();
		$.ajax({
			url : "/tabs/showRequestArtist.h7m1",
			success : function(data) {
				onAjaxTabLoad(data);
			}
		});
	}

	tabManager.registerTab("showRequest", this, "showRequest", '#request_artist_button');

}

var recentlyViewedTab = new function RecentlyViewedTab() {

	this.showTab = function() {
		$('#other_pages').empty();
		$('#my_music_library_holder').hide();
		$('#recently_viewed_playlist_holder').fadeIn();
		if ($('#recently_played_playlist').find('.playable_track').size() < 1) {
			$('#recently_played_overlay').show();
			$('#recently_played_playlist').hide();
		} else {
			$('#recently_played_overlay').hide();
			$('#recently_played_playlist').show();
		}
	}
	
	tabManager.registerTab("recentlyViewed", this, "recentlyViewed", '#recently_viewed_button');

}();

var myMusicTab = new function MyMusicTab() {

	this.showTab = function() {
		$('#other_pages').empty();
		$('#other_pages').hide();
		$('#recently_viewed_playlist_holder').hide();
		$('#my_music_library_holder').fadeIn();
	}

	this.init = function() {
		$('#my_music_library').tablesorter();
	}

	initChain.addToChain(this);
	
	tabManager.registerTab("myMusic", this, "myMusic", "#my_music_button");

}();

var showArtistTab = new function ShowArtistTab() {

	var onAjaxTabLoad = function() {
		// init javascripts for that page
		$('.play_album_now').click(function() {
			// Ajax call for list of playable.
			var albumId = $(this).attr('albumId');
			$.ajax({
				url : "/ajax/album/getAlbumAsPlayables.h7m1",
				data: "id=" + albumId.replace("&", "%26"),
				dataType: 'json',
				success : function(data) {
					//give it to recently played playlist.
					recentlyPlayedPlaylist.addToPlaylist(data);
					//play it
					playlistManager.changePlaylist(recentlyPlayedPlaylist);
					playlistManager.playFirst();
				}
			});
		});
		
	}

	this.showTab = function(queryString) {
		document.location.href='#showArtist?' + queryString;
		$('#other_pages').empty();
		$('#my_music_library_holder').hide();
		$('#recently_viewed_playlist_holder').hide();
		$.ajax({
			url : '/tabs/showArtist.h7m1',
			data: queryString,
			success : function(data) {
				$('#other_pages').css("display", "none");
				$('#other_pages').append(data);
				$('#other_pages').fadeIn();
				onAjaxTabLoad();
			}
		});
	}

	tabManager.registerTab("showArtist", this, "showArtist");

}();

var loginTab = new function LoginTab() {
	
	this.showTab = function() {
		$('#other_pages').empty();
		$('#my_music_library_holder').hide();
		$('#recently_viewed_playlist_holder').hide();
		$.ajax({
			url : "/tabs/login.h7m1",
			success : function(data) {
				$('#other_pages').css("display", "none");
				$('#other_pages').append(data);
				$('#other_pages').fadeIn();
			}
		});
	}
	
	tabManager.registerTab("login", this, "login", '#login_button');
	
}();

/**
 * A playable thing.
 * 
 * @param trackName
 * @param artistName
 * @param youtubeId
 * @returns {Playable}
 */
function Playable(trackKey, trackName, artistName, youtubeId) {
	var self = this;
	this.trackKey = trackKey;
	this.trackName = trackName;
	this.artistName = artistName;
	this.youtubeId = youtubeId;

	this.getYoutubeId = function() {
		var ret = self.youtubeId;
		if (!youtubeId || youtubeId == 'undefined') {
			// make a server call to find the id based on song info
			$.ajax({
				type : "GET",
				url : "/FindYoutubeIdForTrack.h7m1",
				data : "trackKey=" + trackKey,
				async: false,
				success : function(data) {
					ret = data;
				}
			});
		}
		return ret;
	}

}

/**
 * Playlists are an interface, since they might be only on dom, or only in
 * memory. This is the interface.
 * 
 * @param listOfPlayables
 * @returns {Playlist}
 */
function Playlist() {

	/**
	 * @returns {Playable}
	 */
	this.next = new Function();

	/**
	 * @returns {Playable}
	 */
	this.prev = new Function();

	this.onPlay = new Function();

}

var myMusicPlaylist = new function MyMusicPlaylist() {
	var self = this;

	var getCuedPlayable = function() {
		var cued = $('#my_music_library').find('.cued');
		var trackId = cued.find('.track_id').text();
		var youtubeId = cued.find('.youtube_id').text();
		var artist = cued.find('.library_artist').text();
		var track = cued.find('.library_track').text();
		return new Playable(trackId, track, artist, youtubeId);
	}

	this.next = function() {
		var cued = $('#my_music_library').find('.cued');
		var next = cued.next();
		if (next.size() <= 0) {
			next = $('#my_music_library').find('.playable_track').first();
		}
		cued.removeClass("cued");
		next.addClass("cued");
		return getCuedPlayable();
	}

	this.prev = function() {
		var cued = $('#my_music_library').find('.cued');
		var next = cued.prev();
		if (next.size() <= 0) {
			next = $('#my_music_library').find('.playable_track').last();
		}
		cued.removeClass("cued");
		next.addClass("cued");
		return getCuedPlayable();
	}

	this.onPlay = function(playable) {
		$('#my_music_library').find(".playing").removeClass("playing");
		$('#my_music_library').find('.cued').addClass("playing");
	}

	this.syncCued = function() {
		youtubePlayer.cuePlayable(getCuedPlayable());
	}

	this.init = function() {
		playlistManager.changePlaylist(self);
		// bindings for the playlist
		$('.playable_track').click(function(e) {
			e.preventDefault();
			$('#my_music_library').find('.selected')
					.removeClass('selected');
			$(this).addClass('selected');
			// first queue the song
			if (!(youtubePlayer.isPlaying()
					|| youtubePlayer.isPaused() || youtubePlayer
					.isBuffering())) {
				// give the row cue'd class
				$('#my_music_library').find('.cued')
						.removeClass('cued');
				$(this).addClass("cued")
				self.syncCued();
			}
		})

		$('.playable_track').dblclick(function(e) {
			playlistManager.changePlaylist(self);
			e.preventDefault();
			$('#my_music_library').find('.selected').removeClass('selected');
			$(this).addClass('selected');
			$('#my_music_library').find('.cued').removeClass('cued');
			$(this).addClass("cued");
			youtubePlayer.playPlayable(getCuedPlayable());
		});
	}

	initChain.addToChain(this);

}();

var recentlyPlayedPlaylist = new function RecentlyPlayedPlaylist() {
	var self = this;

	var getCuedPlayable = function() {
		var cued = $('#recently_played_playlist').find('.cued');
		var trackId = cued.find('.track_id').text();
		var youtubeId = cued.find('.youtube_id').text();
		var artist = cued.find('.library_artist').text();
		var track = cued.find('.library_track').text();
		return new Playable(trackId, track, artist, youtubeId);
	}
	
	this.first = function() {
		var cued = $('#recently_played_playlist').find('.cued');
		next = $('#recently_played_playlist').find('.playable_track').first();
		cued.removeClass("cued");
		next.addClass("cued");
		return getCuedPlayable();
	}

	this.next = function() {
		var cued = $('#recently_played_playlist').find('.cued');
		var next = cued.next();
		if (next.size() <= 0) {
			next = $('#recently_played_playlist').find('.playable_track').first();
		}
		cued.removeClass("cued");
		next.addClass("cued");
		return getCuedPlayable();
	}

	this.prev = function() {
		var cued = $('#recently_played_playlist').find('.cued');
		var next = cued.prev();
		if (next.size() <= 0) {
			next = $('#recently_played_playlist').find('.playable_track').last();
		}
		cued.removeClass("cued");
		next.addClass("cued");
		return getCuedPlayable();
	}

	this.onPlay = function(playable) {
		$('#recently_played_playlist').find(".playing").removeClass("playing");
		$('#recently_played_playlist').find('.cued').addClass("playing");
	}

	this.syncCued = function() {
		youtubePlayer.cuePlayable(getCuedPlayable());
	}
	
	var addPlayable = function(playable) {
		$("#recently_played_playlist").find('tbody').prepend($('<tr class="playable_track"><td class="icons"><div class="row_icons "></div></td><td class="library_track">' + playable.trackName + '</td><td class="library_artist">' + playable.artistName + '</td><td class="library_album">' + playable.albumName + '</td><td><a href="#" class="add_to_library">Add to library</a></td><td style="display: none;" class="youtube_id">' + playable.youtubeId + '</td><td style="display: none;" class="track_id">' + playable.trackKey +'</td></tr>'));
	}
	
	this.addToPlaylist = function(playables) {
		//loop over the playables backwards since tracks are always pushed to the front of playlist.
		for(i = playables.length -1; i >= 0; i--) {
			addPlayable(playables[i]);
		}
		//unbind all the old click things
		$('#recently_played_playlist .playable_track').unbind('click');
		$('#recently_played_playlist .playable_track').unbind('dblclick');
		self.init();
	}

	this.init = function() {
		// bindings for the playlist
		$('#recently_played_playlist .playable_track').click(function(e) {
			e.preventDefault();
			$('#recently_played_playlist').find('.selected').removeClass('selected');
			$(this).addClass('selected');
			// first queue the song
			if (!(youtubePlayer.isPlaying() || youtubePlayer.isPaused() || youtubePlayer.isBuffering())) {
				// give the row cue'd class
				playlistManager.changePlaylist(self);
				$('#recently_played_playlist').find('.cued').removeClass('cued');
				$(this).addClass("cued")
				self.syncCued();
			}
		});

		$('#recently_played_playlist .playable_track').dblclick(function(e) {
			playlistManager.changePlaylist(self);
			e.preventDefault();
			$('#recently_played_playlist').find('.selected').removeClass('selected');
			$(this).addClass('selected');
			$('#recently_played_playlist').find('.cued').removeClass('cued');
			$(this).addClass("cued");
			youtubePlayer.playPlayable(getCuedPlayable());
		});
	}

	initChain.addToChain(this);

}();

var playlistManager = new function PlaylistManager(playlist) {

	var currentPlaylist = playlist;

	this.changePlaylist = function(playlist) {
		currentPlaylist = playlist;
	}

	// public interface
	this.playNext = function() {
		youtubePlayer.playPlayable(currentPlaylist.next());
	}
	
	this.playFirst = function() {
		youtubePlayer.playPlayable(currentPlaylist.first());
	}

	this.onPlay = function(playable) {
		currentPlaylist.onPlay(playable);
	}

	this.init = function() {
		// bind play buttons.
		$('#play_pause_button').click(function() {
			if (youtubePlayer.isPlaying()) {
				youtubePlayer.pause();
			} else {
				youtubePlayer.play();
			}
		})

		// bind next
		$('#forward_button').click(function() {
			if (youtubePlayer.isPlaying()) {
				youtubePlayer.playPlayable(currentPlaylist.next());
			} else {
				youtubePlayer.cuePlayable(currentPlaylist.next());
			}
		});

		// bind back
		$('#back_button').click(function() {
			if (youtubePlayer.isPlaying()) {
				youtubePlayer.playPlayable(currentPlaylist.prev());
			} else {
				youtubePlayer.cuePlayable(currentPlaylist.prev());
			}
		});
	}

	initChain.addToChain(this);

}(myMusicPlaylist);

function YoutubePlayer(playerId, playlistManager) {
	var self = this;

	var player = $("#" + playerId);
	var playerElement = player.get(0);
	var isPlaying = false;
	var slider = $("#slider-range-min");
	var state = -1;

	var currentPlaylistManager = playlistManager;

	this.isPlaying = function() {
		return state == 1;
	}

	this.isPaused = function() {
		return state == 2;
	}

	this.isBuffering = function() {
		return state == 3;
	}

	this.onPlayerStateChange = function(newState) {
		// unstarted (-1), ended (0), playing (1), paused (2), buffering (3),
		// video cued (5).
		state = newState;
		switch (newState) {
		case -1:
			// nothing to do
			break;
		case 0:
			currentPlaylistManager.playNext();
			break;
		case 1:
			setTimeout(function() {
				youtubePlayer.updateInfo();
			}, 250);
			var button = $('#play_pause_button');
			button.removeClass("play_button");
			button.addClass("pause_button");
			break;
		case 2:
			var button = $('#play_pause_button');
			button.removeClass("pause_button");
			button.addClass("play_button");
			break;
		case 3:
			// give playing class to the current song
			break;
		case 5:
			// nothing to do
			break;
		default:
		}
	}

	this.updateInfo = function() {
		var val = ytplayer.getCurrentTime() / playerElement.getDuration() * 100;
		slider.slider("value", val);
		if (youtubePlayer.isPlaying()) {
			setTimeout(function() {
				youtubePlayer.updateInfo(); // call again
			}, 250);
		}
	}

	this.cueVideoById = function(id, track, artist) {
		// update the things up top
		slider.slider("value", 0);
		$('#now_playing_artist').text(artist);
		$('#now_playing_track').text(track);
		playerElement.cueVideoById(id);
	}

	// Allow the user to set the volume from 0-100
	function setVideoVolume() {
		var volume = parseInt(document.getElementById("volumeSetting").value);
		if (isNaN(volume) || volume < 0 || volume > 100) {
			alert("Please enter a valid volume between 0 and 100.");
		} else if (ytplayer) {
			ytplayer.setVolume(volume);
		}
	}

	this.playPlayable = function(playable) {
		self.cueVideoById(playable.getYoutubeId(), playable.trackName, playable.artistName);
		self.play();
		currentPlaylistManager.onPlay(playable);
		// TODO tell the server that we're playing for stats.
	}

	this.cuePlayable = function(playable) {
		self.cueVideoById(playable.getYoutubeId(), playable.trackName,
				playable.artistName);
	}

	this.play = function() {
		player.get(0).playVideo();
	}

	this.pause = function() {
		player.get(0).pauseVideo();
	}

	this.mute = function() {
		ytplayer.mute();
	}

	this.unmute = function() {
		ytplayer.unMute();
	}

	this.init = function() {
	}

	initChain.addToChain(self);

}

// This function is called when an error is thrown by the player
function onPlayerError(errorCode) {
	alert("An error occured of type:" + errorCode);
}

// This function is automatically called by the player once it loads
function onYouTubePlayerReady() {
	ytplayer = document.getElementById("ytPlayer");
	ytplayer.addEventListener("onStateChange",
			"youtubePlayer.onPlayerStateChange");
	// ytplayer.addEventListener("onError", "onPlayerError");
	// make a new global youtube object
	window.youtubePlayer = new YoutubePlayer("ytPlayer", playlistManager);
	// set a default video
	ytplayer.cueVideoById("GL1-33ZDgPY");
	resizer.resize();
}

$(document).ready(function() {
	initChain.initAll();

	// load the youtube player
	// Lets Flash from another domain call JavaScript
	var params = {
		allowScriptAccess : "always"
	};
	// The element id of the Flash embed
	var atts = {
		id : "ytPlayer"
	};
	// All of the magic handled by SWFObject
	// (http://code.google.com/p/swfobject/)
	swfobject.embedSWF(
		"http://www.youtube.com/e/GL1-33ZDgPY?enablejsapi=1&version=3",
		"videoDiv", "480", "295", "8", null, null,
		params, atts
	);

	tabManager.showPoundedPage();

});