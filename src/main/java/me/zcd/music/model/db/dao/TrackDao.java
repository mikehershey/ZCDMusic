/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.model.db.dao;

import java.util.List;
import me.zcd.music.model.VoteReason;
import me.zcd.music.model.db.Track;

public interface TrackDao {
	
	public Track getTrack(String id);
	public List<Track> getTracks(List<String> trackKeys);
	public Track createNonpersistentTrack(String artistName, String albumName, String trackName);
	public void persistTrack(Track track);
	public void persistAllTracks(List<Track> tracks);
	public void setYoutubeId(String trackId, String youtubeId);
	
}
