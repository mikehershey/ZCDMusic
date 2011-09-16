package me.zcd.music.musicdiscovery.musicbrainz;

import java.util.HashSet;
import java.util.Set;

/**
 * Java client to musicbrainz xml api, if your using this host your own music
 * brainz server they don't want more then 1 request a second.
 * @author mikehershey
 */
public class MusicBrainzSettings {
	
	public static final String SERVER_URL = "http://musicbrainz.org";
	public static final String API_URL = "/ws/2/";
	
	/**
	 * Ignore release groups that are not explicity in this set.
	 */
	public static final Set<String> ACCEPTABLE_RELEASE_GROUP_TYPES = new HashSet<String>();
	static {
		ACCEPTABLE_RELEASE_GROUP_TYPES.add("ep");
		ACCEPTABLE_RELEASE_GROUP_TYPES.add("album");
	}
	
}
