package me.zcd.music.musicdiscovery.api;

import me.zcd.music.musicdiscovery.api.resources.TrackSearchResults;

/**
 *
 * @author mikehershey
 */
public interface TrackSearch {
	
	public TrackSearchResults findTracksByAlbum(String albumApiId);
	
}
