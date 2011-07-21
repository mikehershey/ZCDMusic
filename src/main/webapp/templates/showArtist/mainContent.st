<div class="tab_header">
	<h2 class="tab_title">Artist: $artist.name$</h2>
</div>
<div class="tab_other_content_block">
	<h3>Albums</h3>
	$albums:{ album |
		<a href="#showAlbum?albumKey=$album.key$" key="$album.key$" class="show_album_button">
			<div class="album_block">
				<div class="album_image_holder">
					$if(album.albumArtKey)$
						<img src="/utils/showImage.h7m1?id=$album.albumArtKey$" />
					$else$
						<img src="/images/default_album.jpg" />
					$endif$
				</div>
				<div class="album_info_holder">
					<h1>$album.name$</h1>
					<h2>$album.artistName$</h2>
					<input type="submit" class="play_album_now mac_style_button" albumId="$album.key$" value="Play now"/>
					<input type="submit" class="add_album_to_library mac_style_button" albumId="$album.key$" value="Add to my music"/>
				</div>
			</div>
		</a>
	}$
</div>