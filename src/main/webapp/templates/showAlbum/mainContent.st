<div class="tab_header">
	<h2 class="tab_title">Album: $album.name$</h2>
</div>
<div class="tab_other_content_block">
	<div class="tab_other_content_left_column">
		$if(album.albumArtKey)$
			<img src="$album.albumImage$" class="show_album_picture" />
		$else$
			<img src="/images/default_album.jpg" class="show_album_picture" />
		$endif$
		<input type="submit" class="play_album_now mac_style_button" albumId="$album.key$" value="Play now"/>
		<input type="submit" class="add_album_to_library mac_style_button" albumId="$album.key$" value="Add to library"/>
		<ul class="album_info_list" style="padding-bottom: 10px;">
			<li>Released: $album.formattedReleaseDate$</li>
			<li>Genre: $genre$</li>
		</ul>
	</div>
	<div class="tab_other_content_right_column">
		<div class="album_header_block">
			<h2>$album.name$ - $album.artistName$</h2>
		</div>
		<table id="album_list" class="tablesorter">
			<thead>
				<tr>
					<th style="width: 15px;">#</th>
					<th>Name</th>
					<th>Artist</th>
					<th>Album</th>
					<th style="display: none;">Youtube ID</th>
					<th style="display: none;">Track ID</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
			$tracks:{ track |
				<tr class="addable_track">
					<td class="track_number">$track.trackNumber$</td>
					<td class="library_track">$track.title$</td>
					<td class="library_artist">$track.artistName$</td>
					<td class="library_album">$track.albumName$</td>
					<td style="display: none;" class="youtube_id">$track.youtubeLocation$</td>
					<td style="display: none;" class="track_id">$track.key$</td>
					<td style="padding:2px;width: 90px;"><input type="submit" class="add_track_from_album_page_to_library" key="$track.key$" name="Add to library" value="Add to library" />
				</tr>
			}$
			</tbody> 
		</table>
	</div>
</div>