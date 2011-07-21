<html>
	<head>
		<title>ZCD Soundsystem</title>
		<link rel="stylesheet" type="text/css" href="/jquery/jquery-ui-1.8.13.custom.css" />
		<link rel="stylesheet" type="text/css" href="/styles/main.css" />
		<script type='text/javascript' src='/jquery/jquery-1.5.1.min.js'></script>
		<script type='text/javascript' src='/jquery/jquery-ui-1.8.13.custom.min.js'></script>
		<script type='text/javascript' src='/javascripts/main.js'></script>
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/swfobject/2.1/swfobject.js"></script>
		<script type='text/javascript' src='/tablesorter/jquery.tablesorter.min.js'></script>
		<link rel="stylesheet" type="text/css" href="/tablesorter/style.css" />
	</head>
	<body>
		$header()$
		<div id="left_dock" class="left_dock">
			$leftSide()$
		</div>
		<div id="right_dock" class="right_dock">
			$ads()$
		</div>
		<div id="main_content" class="main_content">
			$mainContent()$
			<div id="other_pages"></div>
		</div>
	</body>
</html>