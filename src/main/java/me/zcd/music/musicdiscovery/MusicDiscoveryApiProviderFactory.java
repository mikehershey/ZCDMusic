package me.zcd.music.musicdiscovery;

import me.zcd.leetml.logging.Log;
import me.zcd.leetml.logging.LogFactory;
import me.zcd.music.Settings;

/**
 * Provides Impls of the music discovery service based on soft coded settings.
 * @author mikehershey
 */
public class MusicDiscoveryApiProviderFactory {
	
	private static final Log log = LogFactory.getLogger(MusicDiscoveryApiProvider.class);
	
	public static MusicDiscoveryApiProvider getProvider() {
		switch(Settings.getService()) {
			case MusicBrainz:
				return new MusicDiscoveryApiProviderMBImpl();
		}
		log.error("Returned a null music discovery service! Perhaps something is not configured correctly.");
		return null;
	}
	
}
