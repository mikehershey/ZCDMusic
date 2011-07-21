<div class="tab_header">
	<h2 class="tab_title">Album: $album.name$</h2>
</div>
<div class="tab_other_content_block">
	$if(album.albumArtKey)$
		<img src="/utils/showImage.h7m1?id=$album.albumArtKey$" class="show_album_picture" />
	$else$
		<img src="/images/default_album.jpg" class="show_album_picture" />
	$endif$
	<div class="album_header_block">
		<h2>$album.name$</h2>
		<h3>$album.artistName$</h3>
	</div>
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
		$tracks:{ track |
			<tr class="playable_track">
				<td class="icons"><div class="row_icons "></div></td>
				<td class="library_track">$track.title$</td>
				<td class="library_artist">$track.artistName$</td>
				<td>$track.albumName$</td>
				<td style="display: none;" class="youtube_id">$track.youtubeLocation$</td>
				<td style="display: none;" class="track_id">$track.key$</td>
			</tr>
		}$
		</tbody> 
	</table>
</div>