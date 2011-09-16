package me.zcd.music.musicdiscovery;

import me.zcd.music.musicdiscovery.api.ArtistSearch;
import me.zcd.music.musicdiscovery.musicbrainz.api.ArtistSearchMBImpl;

/**
 *
 * @author mikehershey
 */
public class MusicDiscoveryApiProviderMBImpl implements MusicDiscoveryApiProvider {

	@Override
	public ArtistSearch getArtistSearch() {
		return new ArtistSearchMBImpl();
	}
	
}
