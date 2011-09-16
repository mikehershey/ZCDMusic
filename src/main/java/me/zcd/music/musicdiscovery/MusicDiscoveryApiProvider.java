package me.zcd.music.musicdiscovery;

import me.zcd.music.musicdiscovery.api.ArtistSearch;

/**
 *
 * @author mikehershey
 */
public interface MusicDiscoveryApiProvider {

	public ArtistSearch getArtistSearch();
	
}
