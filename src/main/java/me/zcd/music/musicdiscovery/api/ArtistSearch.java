package me.zcd.music.musicdiscovery.api;

import java.util.List;
import me.zcd.music.musicdiscovery.api.resources.ArtistSearchResult;

/**
 * Uses a music service IMPL to find artistresults for the specified artist.
 * @author mikehershey
 */
public interface ArtistSearch {
	
	public ArtistSearchResult findArtistByApiId(String artistApiId);
	
	public ArtistSearchResult findArtist(String name);
	
	public List<ArtistSearchResult> findArtist(String name, int numResults);
	
}
