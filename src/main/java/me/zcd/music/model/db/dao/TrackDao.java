package me.zcd.music.model.db.dao;

import com.google.appengine.api.datastore.Key;
import java.util.List;
import me.zcd.music.model.db.Track;
import me.zcd.music.model.db.gae.jdo.GaeTrackImpl;

public interface TrackDao {

	public Track getTrack(Key key);
	public Track getTrack(String id);
	public Track createNonpersistentTrack(String artistName, String albumName, String trackName);
	public void persistAllTracks(List<Track> tracks);
	public void setYoutubeId(String trackId, String youtubeId);
	
}
