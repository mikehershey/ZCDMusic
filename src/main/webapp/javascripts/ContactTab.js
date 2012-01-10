var contactTab = new function ContactTab() {

	var onAjaxTabLoad = function(data) {
		$('#other_pages').css("display", "none");
		$('#other_pages').append(data);
		$('#other_pages').fadeIn();
	}

	this.showTab = function() {
		tabManager.clearAllTabs();
		$.ajax({
			url : "/tabs/contact.h7m1",
			success : function(data) {
				onAjaxTabLoad(data);
			}
		});
	}

	this.gotoTab = function() {
		document.location.href='#contact';
	}

	tabManager.registerTab("contact", this, "contact", '#contact_button');
}