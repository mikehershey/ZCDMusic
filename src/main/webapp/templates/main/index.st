<!DOCTYPE HTML>
<html>
	<head>
		<title>Music by ZCD Media</title>

		<!--
			<link rel="stylesheet" type="text/css" href="/wro/all.css" />
			<script type='text/javascript' src='/wro/all.js'></script>
		-->
		
			$regularJavascripts()$
		

		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/swfobject/2.1/swfobject.js"></script>

		<!--analytics-->
		<script type="text/javascript">
			var _gaq = _gaq || [];
			_gaq.push(['_setAccount', 'UA-9659788-8']);
			_gaq.push(['_setDomainName', '.zcd.me']);
			_gaq.push(['_trackPageview']);

			(function() {
				var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
				ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
				var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
			})();
		</script>

	</head>
	<body>
		$legalDialogs()$
		$header()$
		<div id="left_dock" class="left_dock">
			$leftSide()$
		</div>
		<!--
		<div id="right_dock" class="right_dock">
			$ads()$
		</div>
		-->
		<div id="main_content" class="main_content">
			$mainContent()$
			<div id="log_page_holder" style="display: none;">
				$logPage()$
			</div>
			<div id="other_pages"></div>
		</div>
	</body>
</html>