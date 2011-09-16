var logTab = new function LogTab() {

	this.showTab = function() {
		tabManager.clearAllTabs();
		$('#log_page_holder').fadeIn();
	};

	this.gotoTab = function() {
		document.location.href='#log';
	}

	tabManager.registerTab("log", this, "log", '#log_button');

}();