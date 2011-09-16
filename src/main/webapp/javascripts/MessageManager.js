var messageManager = new function MessageManager() {
	
	$(document).ready(function() {
		setInterval(function()
		{
			var anchor = window.location.hash.replace("#", "");
			if(anchor == "log") {
				$.ajax({
					url : "/findMessages.h7m1",
					type : "POST",
					success : function(data) {
						$('#server_message_log_holder').append(data)
					}
				});
			}
		}, 5000);
	});
	
}();