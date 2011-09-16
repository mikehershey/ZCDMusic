package me.zcd.music.musicdiscovery.api;

import java.util.List;
import me.zcd.music.musicdiscovery.api.resources.AlbumSearchResult;

/**
 *
 * @author mikehershey
 */
public interface AlbumSearch {
	
	public List<AlbumSearchResult> findAllAlbumsByArtist(String artistApiId);
	
}
