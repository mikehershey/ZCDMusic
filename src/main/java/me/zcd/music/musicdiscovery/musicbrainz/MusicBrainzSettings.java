package me.zcd.music.musicdiscovery.musicbrainz;

import java.util.ArrayList;
import java.util.List;

/**
 * Java client to musicbrainz xml api, if your using this host your own music
 * brainz server they don't want more then 1 request a second.
 * @author mikehershey
 */
public class MusicBrainzSettings {
	
	public static final String SERVER_URL = "http://pea.zcd.me:3000";
	//public static final String SERVER_URL = "http://musicbrainz.org";
	public static final String API_URL = "/ws/2/";
	
	/**
	 * Ignore release groups that are not explicity in this set.
	 */
	public static final List<String> ACCEPTABLE_RELEASE_GROUP_TYPES = new ArrayList<String>();
	static {
		ACCEPTABLE_RELEASE_GROUP_TYPES.add("album");
		ACCEPTABLE_RELEASE_GROUP_TYPES.add("compilation");
		ACCEPTABLE_RELEASE_GROUP_TYPES.add("ep");
		ACCEPTABLE_RELEASE_GROUP_TYPES.add("single");
		ACCEPTABLE_RELEASE_GROUP_TYPES.add("soundtrack");
	}
	
	public static final int WAIT_TIME = 0;
	
}
