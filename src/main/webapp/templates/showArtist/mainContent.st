<div class="tab_header">
	<h2 class="tab_title">Artist: $artist.name$</h2>
</div>
<div class="tab_other_content_block">
	<h3>Albums</h3>
	$albums:{ album |
		<a href="#showAlbum?albumKey=$album.key; format="URI_COMPONENT"$" key="$album.key; format="URI_COMPONENT"$" class="show_album_button">
			<div class="album_block">
				<div class="album_image_holder">
					$if(album.albumArtKey)$
						<img src="$album.albumImage$" />
					$else$
						<img src="/images/default_album.jpg" />
					$endif$
				</div>
				<div class="album_info_holder">
					<h1>$album.name$</h1>
					<h2>$album.artistName$</h2>
					<input type="submit" class="play_album_now mac_style_button" albumId="$album.key; format="URI_COMPONENT"$" value="Play now"/>
					<input type="submit" class="add_album_to_library mac_style_button" albumId="$album.key; format="URI_COMPONENT"$" value="Add to library"/>
				</div>
			</div>
		</a>
	}$
</div>