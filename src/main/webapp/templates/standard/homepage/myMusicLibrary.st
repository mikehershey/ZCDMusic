<table id="my_music_library" class="tablesorter">
	<thead>
		<tr>
			<th style="width: 15px;"></th>
			<th>Track #</th>
			<th>Name</th>
			<th>Artist</th>
			<th>Album</th>
			<th>Plays</th>
			<th style="display: none;">Track ID</th>
		</tr>
	</thead>
	<tbody>
		<!-- hack to make table sorter work (it needs at least 1 row, because even the example it has online of tables with no elements doesn't work)-->
		<tr style="display: none">
			<td><div class="row_icons"></div></td>
			<td>1</td>
			<td>A</td>
			<td>A</td>
			<td>A</td>
			<td>100</td>
			<td>A</td>
		</tr>
		$userTracks:{ track |
			<tr class="playable_track">
				<td class="icons"><div class="row_icons"></div></td>
				<td class="track_number">$track.trackNumber$</td>
				<td class="library_track">$track.title$</td>
				<td class="library_artist">$track.artistName$</td>
				<td class="library_album">$track.albumName$</td>
				<td class="library_plays">$track.playCount$</td>
				<td style="display: none;" class="track_id">$track.trackKey$</td>
			</tr>
		}$
	</tbody> 
</table>