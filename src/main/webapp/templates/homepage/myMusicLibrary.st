<table id="my_music_library" class="tablesorter">
	<thead>
		<tr>
			<th style="width: 15px;"></th>
			<th>Name</th>
			<th>Artist</th>
			<th>Album</th>
			<th style="display: none;">Youtube ID</th>
			<th style="display: none;">Track ID</th>
		</tr>
	</thead>
	<tbody>
		$userTracks:{ track |
			<tr class="playable_track">
				<td class="icons"><div class="row_icons "></div></td>
				<td class="library_track">$track.title$</td>
				<td class="library_artist">$track.artistName$</td>
				<td class="library_album">$track.albumName$</td>
				<td style="display: none;" class="youtube_id">$track.youtubeLocation$</td>
				<td style="display: none;" class="track_id">$track.key$</td>
			</tr>
		}$
	</tbody> 
</table>