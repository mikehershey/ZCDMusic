
/**
 * The slideout tab that handles the youtube controls (downvote see title etc)
 */
var youtubeTab = new function YoutubeTab() {

	var youtube_white = $('<img/>').attr('src', '/images/sideways_tab/youtube_white.png').addClass('tab_slider_image').addClass('youtube_white');

	this.getTabImage = function() {
		return youtube_white;
	}

	this.getTabJquery = function() {
		return $('.youtube_white');
	}

	this.onRender = function() {
	}

	this.clickBottomRight = function() {
		var trackKey = playlistManager.getCurrentlyPlaying().trackKey;
		$('#playing_track_id_when_this_opened').val(trackKey);
		$('#downvote_dialog').dialog({
			width: 500,
			modal : true,
			buttons : {
			"Submit" : function() {
				var downVoteReason = $(this).find("input[type='radio']:checked").val();
				var trackKey = $('#playing_track_id_when_this_opened').val();
				var ajaxData = {'trackKey' : trackKey, 'downVoteReason' : downVoteReason};
				$.ajax({
					url : "/ajax/track/rateYoutubeQuality.h7m1",
					data : ajaxData,
					success : function(data) {
						isVoted = true;

						$('.playing .youtube_id').text('');
						playlistManager.restartCurrent();
					}
				});
				$(this).dialog("close");
			}
		}
		});
	}

	this.clickTopRight = function() {
		alert('clicked top left');
	}

}

/**
 * The slideout tab that handles the mail (for system messages)
 */
var mailTab = new function YoutubeTab() {

	var mail_white = $('<img/>').attr('src', '/images/sideways_tab/mail_white.png').addClass('tab_slider_image').addClass('mail_white');

	var isVoted = false;

	this.getTabImage = function() {
		return mail_white;
	}

	this.getTabJquery = function() {
		return $('.mail_white');
	}

	this.onRender = function() {
	}

	this.clickBottomRight = function() {
		alert('clicked bottom right');
	}

	this.clickTopRight = function() {
		alert('clicked top right');
	}

}

var tabSlider = new function TabSlider() {
	var self = this;
	
	var slider = $('<div/>').attr('id', "tab_slider");
	var higherTab = mailTab;
	var lowerTab = youtubeTab;
	var activeTab = youtubeTab;
	
	self.renderSlider = function() {
		$('#tab_slider').remove();
		
		var position = $(document).width() - 50;
		slider.css('left', position);
		
		slider.empty();
		
		var higherTabImage = higherTab.getTabImage();
		var lowerTabImage = lowerTab.getTabImage();
		
		slider.append(higherTabImage);
		higherTab.onRender();
		slider.append(lowerTabImage);
		lowerTab.onRender();
		
		if(higherTab == activeTab) {
			higherTabImage.css('z-index', '5');
			lowerTabImage.css('z-index', '2');
		}
		if(lowerTab == activeTab) {
			higherTabImage.css('z-index', '2');
			lowerTabImage.css('z-index', '5');
		}
		
		slider.click(function(event) {
			var offset = $('#tab_slider').offset();
			var x = event.pageX - offset.left;
			var y = event.pageY - offset.top;
			handleClick(x, y);
		})
		
		$('body').prepend(slider);
	}
	
	var handleClick = function(x, y) {
		if(x < 25 && y < 25) {
			//top left
			activeTab = higherTab;
		}
		if(x < 25 && y >= 25) {
			//bottom left
			activeTab = lowerTab;
		}
		if(x >= 25 && y >= 25) {
			//bottom right
			activeTab.clickBottomRight();
		}
		if(x >= 25 && y < 25) {
			//bottom right
			activeTab.clickTopRight();
		}
		self.renderSlider();
	}
	
	this.init = function() {
		self.renderSlider();
		$(window).resize(function() {
			self.renderSlider();
		});
	}
	
	initChain.addToChain(self);

}
