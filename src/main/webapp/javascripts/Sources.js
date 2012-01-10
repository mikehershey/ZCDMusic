/**
 * Opens and shows source dialog for the given track id
 */
function SourcesDialog(trackId) {
	
	$.ajax({
		url : "/ajax/track/getSourcesForTrack.h7m1",
		data : {'trackKey' : trackId},
		'type' : 'POST',
		'async' : false,
		success : function(data) {
			var tableHolder = $('#select_sources_dialog_table_holder');
			tableHolder.empty();
			tableHolder.append(data);
		}
	});
	
	$('#youtube_avaliable_sources').tablesorter();
	
	//wire up the use this source button
	$('.use_this_source_button').click(function() {
		//close the dialog
		$("#select_sources_dialog").dialog("close");
		//tell the server that this user likes this source
		
		alert($(this).parent().parent().find('.youtubeId').text());
	})
	
	$('#select_sources_dialog').dialog({
		width: 1000,
		height: 600
	});
	
}

$(document).ready(function() {
	
	//init context menu
	$.contextMenu({
        selector: '.playable_track', 
		items: {
            "Select Source": {
				name: "Select Source", 
				callback: function(key, opt) {
					new SourcesDialog($(this).find(".track_id").text());
				}
			}
        }
    });
	
});