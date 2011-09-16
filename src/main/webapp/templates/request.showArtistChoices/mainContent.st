<div>
	<div class="tab_header">
		<h2 class="tab_title">Request > Add an Artist:</h2>
	</div>
	<div class="tab_other_content_block" style="margin: 30px;">
		<h3>Which artist did you mean?</h3>
		<form id="artist_suggestion_form" action="#">
			$ artistSearchResults :{ asr |
				<div style="margin-top: 10px">
					<input type="radio" name="artistApiId" value="$asr.apiID$">
					<label for="artistApiId">$asr.artistName$ 
					$if(asr.disambiguation)$
						($asr.disambiguation$)
					$endif$
					</label><br/>
				</div>
			}$
			<input type="submit" id="doLoadArtistSubmit" value="Load artist" style="margin-top: 10px;" />
		</form>
	</div>
</div>