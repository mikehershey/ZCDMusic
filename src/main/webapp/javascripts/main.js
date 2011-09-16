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
		for (i=0; i <= needsInit.length; i++) {
			try {
				needsInit[i].init();
			} catch(e) {}
		}
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
		$('#my_music_library_playlist_holder').height(h);
		$('#recently_viewed_playlist_holder').height(h);
		
		$('#left_side_bottom_buttons').css("top", h + 38);

		$("#slider-range-min").slider({
			range : "min",
			value : 0,
			min : 0,
			max : 100
		});
		
		youtubeSlideoutManager.resize();
		
	}

	this.init = function() {
		self.resize();
		$(window).resize(function() {
			self.resize();
		});
	}

	initChain.addToChain(self);

}();


var youtubeSlideoutManager = new function YoutubeSlideoutManager() {
	
	var collapsed = false;
	
	var inited = false;
	
	var addition = -29;
	
	var slideEffect = function() {
		var h = $(window).height();
		var gotoTop = 0;
		if(collapsed) {
			gotoTop = h;
		} else {
			gotoTop = h - 225 + addition;
		}
		$('#youtube_object_wrapper').animate({
			'top': gotoTop
		} , "fast");
	}
	
	this.resize = function() {
		var h = $(window).height();
		if(collapsed) {
			$('#youtube_object_wrapper').css('top', h);
		} else {
			$('#youtube_object_wrapper').css('top', h - 225 + addition);
		}
	}
	
	this.resetVoteRecord = function() {
		$('#vote_good_youtube_quality').attr('src', '/images/voting/thumbs-up.png');
	}
	
	//note not init'ed by init manager, its done after youtube is ready
	this.init = function() {
		if(inited) {
			return;
		}
		inited = true;
		var div = $("<div/>").attr("id", "youtube_object_wrapper").css("position", "absolute");
		$('#ytPlayer').wrap(div);
		
		var titleDive = $("<div/>").addClass('tab_header').css("padding", "0px").width('200px').height('30px');
		var nowPlaying = '<p id="youtube_now_playing_text" style="padding: 0px; margin: 0px; font-size: 16px;font-weight: normal; overflow: hidden;float: left;padding-top: 5px;">How\'s the quality?</p>';
		titleDive.append(nowPlaying);
		titleDive.append('<div id="voting_holder" style="float: right; padding-top: 3px;"><img src="/images/voting/thumbs-down.png" id="vote_bad_youtube_quality"/><img src="/images/voting/thumbs-up.png" id="vote_good_youtube_quality"/></div>');
		
		$('#youtube_object_wrapper').prepend(titleDive);
		
		//setup those buttons
		$('#vote_good_youtube_quality').click(function() {
			if($(this).attr('src') == '/images/voting/thumbs-up.png') {
				if(playlistManager.getCurrentlyPlaying() == undefined) {
					alert("Could not rate this song because you're not listening to a song! (or a bug)");
					return;
				}
				$('#vote_good_youtube_quality').attr('src', '/images/voting/thumbs-up-green.png');
				//inform the server
				var trackKey = playlistManager.getCurrentlyPlaying().trackKey;
				var ajaxData = {
					'trackKey' : trackKey, 
					'downVoteReason' : 'GOOD'
				};
				$.ajax({
					url : "/ajax/track/rateYoutubeQuality.h7m1",
					data : ajaxData,
					dataType: 'json',
					success : function(data) {
					}
				});
			}
		});
		$('#vote_bad_youtube_quality').click(function() {
			if(playlistManager.getCurrentlyPlaying() == undefined) {
				alert("Could not rate this song because you're not listening to a song! (or a bug)");
				return;
			}
			var trackKey = playlistManager.getCurrentlyPlaying().trackKey;
			$('#playing_track_id_when_this_opened').val(trackKey);
			$('#downvote_dialog').dialog({
				width: 500,
				modal : true,
				buttons : {
					"Submit" : function() {
						var downVoteReason = $(this).find("input[type='radio']:checked").val();
						var trackKey = $('#playing_track_id_when_this_opened').val();
						var ajaxData = {
							'trackKey' : trackKey, 
							'downVoteReason' : downVoteReason
						};
						$.ajax({
							url : "/ajax/track/rateYoutubeQuality.h7m1",
							data : ajaxData,
							dataType: 'json',
							success : function(data) {
								isVoted = true;
								youtubePlayer.playPlayable(new Playable(data.trackKey, data.trackName, data.artistName, data.albumName, data.trackNumber));
							}
						});
						$(this).dialog("close");
					}
				}
			});
		});
		
		$('#youtube_object_wrapper').css("left", "0")
		var h = $(window).height();
		if(collapsed) {
			$('#youtube_object_wrapper').css('top', h);
		} else {
			$('#youtube_object_wrapper').css('top', h - 225 + addition);
		}
		$('#show_youtube_button').click(function() {
			if($(this).hasClass('expand_down')) {
				$(this).removeClass('expand_down');
				$(this).addClass('expand_up');
				collapsed = true;
			} else {
				$(this).removeClass('expand_up');
				$(this).addClass('expand_down');
				collapsed = false;
			}
			slideEffect();
			return false;
		});
	}
	
}


/**
 * Tabs can register themselves with the tab manager. If they register before init
 * is called they will be automatically bound to left side navigation #whatever
 */
var tabManager = new function TabManager() {

	var tabNames = [];
	var tabNameToTab = new Object();
	var tabNameToButtonIdMap = new Object();
	var urlToTabMap = new Object();
	var currentTab = null;

	this.clearAllTabs = function() {
		$('#other_pages').empty();
		$('#my_music_library_holder').hide();
		$('#recently_viewed_playlist_holder').hide();
		$('#log_page_holder').hide();
	}

	/**
	 * Tabs added must have a showTab() function which takes an option param
	 * and a gotoTab() that puts the #poundedpage in url
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
		currentTab = tab;
		tab.showTab(queryString);
	};

	this.init = function() {
		for (var i = 0; i < tabNames.length; i++) {
			var tabName = tabNames[i];
			var tabButton = tabNameToButtonIdMap[tabName];
			if(tabButton) {
				$(tabButton).click(function(tabName) {
					return function() {
						tabNameToTab[tabName].gotoTab();
					};
				}(tabName));
			}
		}
	};

	initChain.addToChain(this);

}();

var legalTab = new function LegalTab() {
	
	var showCatDialog = function() {
		$('#areYouSureYouHateKittens').dialog({
			modal: true,
			width: 800,
			buttons: {
				"Yes, I want your pet to die.": function() {
					$( this ).dialog( "close" );
					$('#thankYouForBeingALawyer').dialog({
						modal: true,
						width: 800,
						buttons: {
							"Continue to Legal Contact info": function() {
								tabManager.clearAllTabs();
								$.ajax({
									url : '/tabs/legal.h7m1',
									success : function(data) {
										$('#other_pages').css("display", "none");
										$('#other_pages').append(data);
										$('#other_pages').fadeIn();
									}
								});
								$( this ).dialog( "close" );
							}
						}
					});
				},
				"No! I just misclicked!!": function() {
					$( this ).dialog( "close" );
					document.location.href='#';
				}
			}
		});
	}
	
	this.showTab = function() {
		showCatDialog();
	};

	this.gotoTab = function() {
		document.location.href='#legal';
	}

	tabManager.registerTab("legal", this, "legal", '#legal_button');
	
}

var browseTab = new function BrowseTab() {

	this.showTab = function() {
		tabManager.clearAllTabs();
		$.ajax({
			url : "/tabs/browsewarehouse.h7m1",
			success : function(data) {
				$('#other_pages').css("display", "none");
				$('#other_pages').append(data);
				$('#other_pages').fadeIn();
			}
		});
	};

	this.gotoTab = function() {
		document.location.href='#browse';
	}

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
			var playable = new Playable(ui.item.key, ui.item.track, ui.item.artist, ui.item.album, ui.item.trackNumber);
			var playables = [];
			playables[0] = playable;
			playQueuePlaylist.addToPlaylist(playables);
			//play it
			playlistManager.changePlaylist(playQueuePlaylist);
			playlistManager.playFirst();
			} else if(ui.item.type == 'ARTIST') {
			showArtistTab.gotoTab(ui.item.queryString);
			} else if(ui.item.type == 'ALBUM') {
			showAlbumTab.gotoTab(ui.item.queryString);
			}
			return false;
			}
			}).data("catcomplete")._renderItem = function(ul, item) {
			if (item.type == 'ARTIST') {
				item.queryString = "id=" + encodeURIComponent(item.key);
				var ret = $("<li/>");
				ret.data("item.autocomplete", item);
				var image = $('<img/>');
				if(item.imageUrl) {
					image.attr('src', item.imageUrl);
					image.attr('style', 'float: left; margin-right: 5px;');
				}
				else {
					image.attr('src', '/images/Question-mark-face.png');
					image.attr('style', 'float: left; margin-right: 5px;');
				}
				var link = $('<a href="#"/>')
				link.append(image);
				var h1 = $('<h1/>');
				h1.text(item.artist);
				link.append(h1)
				ret.append(link);
				return ret.appendTo(ul);
			} else if (item.type == 'ALBUM') {
				item.queryString = "albumKey=" + encodeURIComponent(item.key);
				var ret = $("<li/>");
				ret.data("item.autocomplete", item);
				var image = $('<img/>');
				if(item.imageUrl) {
					image.attr('src', item.imageUrl);
					image.attr('style', 'float: left; margin-right: 5px;');
				}
				else {
					image.attr('src', '/images/default_album.jpg');
					image.attr('style', 'float: left; margin-right: 5px;');
				}
				var link = $('<a href="#"/>');
				link.append(image);
				var h2 = $('<h2/>');
				h2.text(item.artist);
				link.append(h2);
				var h22 = $('<h2/>')
				h22.text(item.album);
				link.append(h22);
				ret.append(link);
				return ret.appendTo(ul);
			} else {
				item.queryString = encodeURIComponent(item.key);
				return $("<li></li>").data("item.autocomplete", item).data(
					"trackKey", item.key).append(
					"<a href='#'><h2>" + item.track + "</h2><h3>"
					+ item.artist + "</h3></a>").appendTo(ul);
			}
		};
	}

	this.showTab = function() {
		tabManager.clearAllTabs();
		$.ajax({
			url : "/tabs/showSearchWarehouse.h7m1",
			success : function(data) {
				onAjaxTabLoad(data);
			}
		});
	}

	this.gotoTab = function() {
		document.location.href='#showSearch';
	}

	tabManager.registerTab("showSearch", this, "showSearch", '#search_button');

}();

var playQueueTab = new function PlayQueueTab() {

	this.showTab = function() {
		tabManager.clearAllTabs();
		$('#recently_viewed_playlist_holder').fadeIn();
		if ($('#recently_played_playlist').find('.playable_track').size() < 1) {
			$('#recently_played_overlay').show();
			$('#recently_played_playlist').hide();
		} else {
			$('#recently_played_overlay').hide();
			$('#recently_played_playlist').show();
		}
	}

	this.gotoTab = function() {
		document.location.href='#playQueue';
	}
	
	$(document).ready(function() {
		$("#recently_played_playlist").trigger("update");
	});

	tabManager.registerTab("playQueue", this, "playQueue", '#recently_viewed_button');

}();

var myMusicTab = new function MyMusicTab() {

	this.showTab = function() {
		tabManager.clearAllTabs();
		$('#my_music_library_holder').show();
		if ($('#my_music_library').find('.playable_track').size() < 1) {
			$('#splash_page_holder').show();
			$('#my_music_library_playlist_holder').hide();
		} else {
			$('#splash_page_holder').hide();
			$('#my_music_library_playlist_holder').show();
		}
	}

	this.init = function() {
		$('#my_music_library').tablesorter({
			sortList: [[3,0],[4,0],[1,0]] 
		});
	}

	initChain.addToChain(this);

	this.gotoTab = function() {
		document.location.href='#myMusic';
	}

	tabManager.registerTab("myMusic", this, "myMusic", "#my_music_button");

}();

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
	}

	this.showTab = function(queryString) {
		tabManager.clearAllTabs();
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

	this.gotoTab = function(queryString) {
		document.location.href='#showArtist?' + queryString;
	}

	tabManager.registerTab("showArtist", this, "showArtist");

}();

var showAlbumTab = new function ShowAlbumTab() {

	var onAjaxTabLoad = function() {
		$('#album_list').tablesorter({
			sortList: [[0,0]] 
		});
		$('.play_album_now').click(function() {
			var playables = [];
			$('.addable_track').each(function() {
				var playable = new Playable($(this).find('.track_id').text(), $(this).find('.library_track').text(), $(this).find('.library_artist').text(), $(this).find('.library_album').text(), $(this).find('.track_number').text());
				playables.push(playable);
			});
			playQueuePlaylist.addToPlaylist(playables);
			//play it
			playlistManager.changePlaylist(playQueuePlaylist);
			playlistManager.playFirst();
		});
		$('.add_album_to_library').click(function() {
			var playables = [];
			$('.addable_track').each(function() {
				var playable = new Playable($(this).find('.track_id').text(), $(this).find('.library_track').text(), $(this).find('.library_artist').text(), $(this).find('.library_album').text(), $(this).find('.track_number').text());
				playables.push(playable);
			});
			myMusicPlaylist.addToPlaylist(playables);
		});
		$('.add_track_from_album_page_to_library').click(function() {
			var row = $(this).parent().parent();
			var playable = new Playable(row.find('.track_id').text(), row.find('.library_track').text(), row.find('.library_artist').text(), row.find('.library_album').text(), $(this).find('.track_number').text());
			var playables = [];
			playables.push(playable);
			myMusicPlaylist.addToPlaylist(playables);
		});
		$('.addable_track').click(function(e) {
			e.preventDefault();
			$('#album_list').find('.selected').removeClass('selected');
			$(this).addClass('selected');
		});
		$('.addable_track').dblclick(function(e) {
			//add this track to play_queue
			var playable = new Playable($(this).find('.track_id').text(), $(this).find('.library_track').text(), $(this).find('.library_artist').text(), $(this).find('.library_album').text() , $(this).find('.track_number').text());
			var playables = [];
			playables[0] = playable;
			playQueuePlaylist.addToPlaylist(playables);
			//play it
			playlistManager.changePlaylist(playQueuePlaylist);
			playlistManager.playFirst();
		});
	}

	this.showTab = function(queryString) {
		tabManager.clearAllTabs();
		$.ajax({
			url : '/tabs/showAlbum.h7m1',
			data: queryString.replace(/&/g, '%26'),
			success : function(data) {
				$('#other_pages').css("display", "none");
				$('#other_pages').append(data);
				$('#other_pages').fadeIn();
				onAjaxTabLoad();
			}
		});
	}

	this.gotoTab = function(queryString) {
		document.location.href='#showAlbum?' + queryString;
	}

	tabManager.registerTab("showAlbum", this, "showAlbum");

};

var loginTab = new function LoginTab() {

	this.showTab = function() {
		tabManager.clearAllTabs();
		$.ajax({
			url : "/tabs/login.h7m1",
			success : function(data) {
				$('#other_pages').css("display", "none");
				$('#other_pages').append(data);
				$('#other_pages').fadeIn();
			}
		});
	}

	this.gotoTab = function() {
		document.location.href='#login';
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
function Playable(trackKey, trackName, artistName, albumName, trackNumber) {
	var self = this;
	this.trackNumber = trackNumber
	this.trackKey = trackKey;
	this.trackName = trackName;
	this.artistName = artistName;
	this.albumName = albumName;

	this.getYoutubeId = function() {
		var ret = undefined;
		// make a server call to find the id based on song info
		$.ajax({
			type : "GET",
			url : "/FindYoutubeIdForTrack.h7m1",
			data : "trackKey=" + self.trackKey.replace(/&/g, '%26'),
			async: false,
			success : function(data) {
				ret = data;
			}
		});
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
		var artist = cued.find('.library_artist').text();
		var track = cued.find('.library_track').text();
		var album =  cued.find('.library_album').text();
		var trackNumber = cued.find('.track_number').text();
		return new Playable(trackId, track, artist, album, trackNumber);
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

	var addPlayable = function(playable) {
		var row = $('<tr class="playable_track" />');
		row.append('<td class="icons"><div class="row_icons "></div></td>');
		row.append('<td class="track_number">' + playable.trackNumber + '</td>');
		row.append('<td class="library_track">' + playable.trackName + '</td>');
		row.append('<td class="library_artist">' + playable.artistName + '</td>');
		row.append('<td class="library_album">' + playable.albumName + '</td>');
		row.append('<td style="display: none;" class="track_id">' + playable.trackKey +'</td>');
		$("#my_music_library").find('tbody').prepend(row);
	}

	this.addToPlaylist = function(playables) {
		var trackKeys = [];
		//loop over the playables backwards since tracks are always pushed to the front of playlist.
		for(i = 0; i < playables.length; i++) {
			trackKeys.push(playables[i].trackKey);
		}
		var ajaxData = {
			'trackKeys' : trackKeys
		};
		$.ajax({
			url : "/addTracksToLibrary.h7m1",
			data : ajaxData,
			success : function(data) {
				for(i = playables.length - 1; i >= 0 ; i--) {
					addPlayable(playables[i]);
				}
				//unbind all the old click things
				$('#my_music_library .playable_track').unbind('click');
				$('#my_music_library .playable_track').unbind('dblclick');
				self.init();
				myMusicTab.gotoTab();
			}
		});

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

var playQueuePlaylist = new function PlayQueuePlaylist() {
	var self = this;

	var getCuedPlayable = function() {
		var cued = $('#recently_played_playlist').find('.cued');
		var trackId = cued.find('.track_id').text();
		var artist = cued.find('.library_artist').text();
		var album = cued.find('.library_album').text();
		var track = cued.find('.library_track').text();
		var trackNumber = cued.find('.track_number').text();
		return new Playable(trackId, track, artist, album, trackNumber);
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
		var row = $('<tr class="playable_track" />');
		row.append('<td class="icons"><div class="row_icons "></div></td>');
		row.append('<td class="track_number">' + playable.trackNumber + '</td>');
		row.append('<td class="library_track">' + playable.trackName + '</td>');
		row.append('<td class="library_artist">' + playable.artistName + '</td>');
		row.append('<td class="library_album">' + playable.albumName + '</td>');
		row.append('<td><a href="#" class="add_to_library">Add to library</a></td>');
		row.append('<td style="display: none;" class="track_id">' + playable.trackKey +'</td>');
		$("#recently_played_playlist").find('tbody').prepend(row);
	}

	this.addToPlaylist = function(playables) {
		//loop over the playables backwards since tracks are always pushed to the front of playlist.
		for(i = playables.length -1; i >= 0; i--) {
			addPlayable(playables[i]);
		}
		//unbind all the old click things
		$('#recently_played_playlist .playable_track').unbind('click');
		$('#recently_played_playlist .playable_track').unbind('dblclick');
		$("#recently_played_playlist").tablesorter({
			sortList: [[3,0],[4,0],[1,0]] 
		});
		self.init();
		playQueueTab.gotoTab();
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
	var currentlyPlaying;
	
	this.getCurrentlyPlaying = function() {
		return currentlyPlaying;
	}

	this.changePlaylist = function(playlist) {
		currentPlaylist = playlist;
	}

	this.playNext = function() {
		currentlyPlaying = currentPlaylist.next();
		youtubePlayer.playPlayable(currentlyPlaying);
	}
	
	this.restartCurrent = function() {
		currentPlaylist.next();
		currentlyPlaying = currentPlaylist.prev();
		youtubePlayer.playPlayable(currentlyPlaying);
	}

	this.playFirst = function() {
		currentlyPlaying = currentPlaylist.first();
		youtubePlayer.playPlayable(currentlyPlaying);
	}

	this.onPlay = function(playable) {
		currentlyPlaying = playable;
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
			currentlyPlaying = currentPlaylist.next();
			if (youtubePlayer.isPlaying()) {
				youtubePlayer.playPlayable(currentlyPlaying);
			} else {
				youtubePlayer.cuePlayable(currentlyPlaying);
			}
		});

		// bind back
		$('#back_button').click(function() {
			currentlyPlaying = currentPlaylist.prev();
			if (youtubePlayer.isPlaying()) {
				youtubePlayer.playPlayable(currentlyPlaying);
			} else {
				youtubePlayer.cuePlayable(currentlyPlaying);
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
				playerElement.setPlaybackQuality('large');
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
				playerElement.setPlaybackQuality('large');
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
		playerElement.setPlaybackQuality('large');
		//reset the thumbs up to be green
		youtubeSlideoutManager.resetVoteRecord();
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
	// TODO show an ad here.
	//ytplayer.cueVideoById("GL1-33ZDgPY");
	youtubeSlideoutManager.init();
}

$(document).ready(function() {

	// load the youtube player
	// Lets Flash from another domain call JavaScript
	var params = {
		allowScriptAccess : "always",
		value: "transparent"
	};
	// The element id of the Flash embed
	var atts = {
		id : "ytPlayer",
		wmode : "transparent"
	};
	// All of the magic handled by SWFObject
	// (http://code.google.com/p/swfobject/)
	swfobject.embedSWF(
		"http://www.youtube.com/v/XZ5TajZYW6Y?enablejsapi=1&version=3",
		"videoDiv", "480", "295", "8", null, null,
		params, atts
		);
			
	resizer.resize();
	initChain.initAll();
	tabManager.showPoundedPage();

	var hash = location.hash;

	setInterval(function()
	{
		if (location.hash != hash)
		{
			hash = location.hash;
			tabManager.showPoundedPage();
		}
	}, 200);
	
	$('#app_logo').click(function() {
		myMusicTab.gotoTab();
	});
	
	var anchor = window.location.hash.replace("#", "");
	if(!anchor) {
		myMusicTab.gotoTab();
	}

	$.loading({
		onAjax:true,
		mask:true, 
		img:'/jquery.loading/loading.gif', 
		align:'center'
	});
	
	$('#header_search_box').click(function() {
		$(this).val('');
	})

	$.widget("custom.catcomplete", $.ui.autocomplete, {
		_renderMenu : function(ul, items) {
			var self = this;
			var currentCategory = "";
			$.each(items, function(index, item) {
				if (item.type != currentCategory) {
					ul.append("<li class='ui-autocomplete-category' style='font-size: 16px;'>" + item.type + "</li>");
					currentCategory = item.type;
				}
				self._renderItem(ul, item);
			});
		}
	});

	$("#header_search_box").catcomplete({
		source : "/searchWarehouse.h7m1",
		minLength : 1,
		select : function(event, ui) {
			if (ui.item.type == 'TRACK') {
				var playable = new Playable(ui.item.key, ui.item.track, ui.item.artist, ui.item.youtubeId, ui.item.album, ui.item.trackNumber);
				var playables = [];
				playables[0] = playable;
				playQueuePlaylist.addToPlaylist(playables);
				//play it
				playlistManager.changePlaylist(playQueuePlaylist);
				playlistManager.playFirst();
			} else if(ui.item.type == 'ARTIST') {
				showArtistTab.gotoTab(ui.item.queryString);
			} else if(ui.item.type == 'ALBUM') {
				showAlbumTab.gotoTab(ui.item.queryString);
			}
			return false;
		}
	}).data("catcomplete")._renderItem = function(ul, item) {
		if (item.type == 'ARTIST') {
			item.queryString = "id=" + encodeURIComponent(item.key);
			var ret = $("<li/>").height('30px').css('margin-top', '3px').css('margin-bottom', '3px');
			ret.data("item.autocomplete", item);
			var link = $('<a href="#"/>').height('30px');
			var h1 = $('<h1/>').css("font-size", "12px").css('line-height','14px').height('14px').css('overflow','hidden').css('white-space', 'nowrap');
			h1.text(item.artist);
			link.append(h1)
			ret.append(link);
			return ret.appendTo(ul);
		} else if (item.type == 'ALBUM') {
			item.queryString = "albumKey=" + encodeURIComponent(item.key);
			var ret = $("<li/>").height('30px').css('margin-top', '3px').css('margin-bottom', '3px');
			ret.data("item.autocomplete", item);
			var link = $('<a href="#"/>').height('30px');
			var h22 = $('<h2/>').css("font-size", "12px").css('line-height','14px').height('14px').css('overflow','hidden').css('white-space', 'nowrap');
			h22.text(item.album);
			link.append(h22);
			var h2 = $('<h2/>').css("font-size", "10px").css('line-height','11px').height('11px').css('overflow','hidden').css('white-space', 'nowrap');
			h2.text(item.artist);
			link.append(h2);
			ret.append(link);
			return ret.appendTo(ul);
		} else {
			item.queryString = encodeURIComponent(item.key);
			var li = $("<li/>").data("item.autocomplete", item).data("trackKey", item.key).height('30px').css('margin-top', '3px').css('margin-bottom', '3px');
			li.append("<a href='#' style='height: 30px;'><h2 style='font-size: 12px;overflow: hidden;white-space: nowrap;'>" + item.track + "</h2><h3 style='font-size: 10px;overflow: hidden;white-space: nowrap;'>" + item.artist + "</h3></a>")
			return li.appendTo(ul);
		}
	};

});