var requestTab = new function RequestTab() {

	var showProgress = function(data) {
		//open the log tab (loads are all logged as messages).
		logTab.gotoTab();
	}

	var showArtistSuggestionData = function(data) {
		$('#other_pages').css("display", "none");
		try {
			$('#other_pages').append(data);
		} catch(error) {
			error = error;
		}
		$('#other_pages').fadeIn();
		$('#artist_suggestion_form').submit(function(e) {
			e.preventDefault();
			$.ajax({
			url : "/request/queueLoadArtist.h7m1",
				type : "POST",
				data : $('#artist_suggestion_form').serialize(),
				success : function(data) {
					showProgress(data);
				},
				error : function(data) {
					alert("Your request to add a new artist failed!")
				}
			});
			return false;
		});
	}

	var submitForm = function() {
		$.ajax({
			url : "/request/getArtistSuggestions.h7m1",
			type : "POST",
			data : "artist=" + $('#request_box').val(),
			success : function(data) {
				showArtistSuggestionData(data);
			},
			error : function(data) {
				alert("Your request to add a new artist failed!")
			}
		});
		$('#other_pages').empty();
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
			return false;
		});
	}

	this.showTab = function() {
		tabManager.clearAllTabs();
		$.ajax({
			url : "/tabs/showRequestArtist.h7m1",
			success : function(data) {
				onAjaxTabLoad(data);
			}
		});
	}

	this.gotoTab = function() {
		document.location.href='#showRequest';
	}

	tabManager.registerTab("showRequest", this, "showRequest", '#request_artist_button');

}