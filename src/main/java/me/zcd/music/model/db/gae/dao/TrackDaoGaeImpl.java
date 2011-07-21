/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zcd.music.model.db.gae.dao;

import com.google.appengine.api.datastore.Key;
import java.util.List;
import javax.jdo.PersistenceManager;
import me.zcd.music.model.db.Track;
import me.zcd.music.model.db.dao.TrackDao;
import me.zcd.music.model.db.gae.GAEModel;
import me.zcd.music.model.db.gae.jdo.GaeTrackImpl;
import me.zcd.music.model.db.utils.KeygenService;

/**
 *
 * @author mikehershey
 */
public class TrackDaoGaeImpl implements TrackDao {
	
	@Override
	public Track getTrack(Key key) {
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		Track t = null;
		try {
			t = pm.getObjectById(GaeTrackImpl.class, key);
		} finally {
			pm.close();
		}
		return t;
	}

	@Override
	public Track getTrack(String key) {
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		Track t = null;
		try {
			t = pm.getObjectById(GaeTrackImpl.class, key);
		} finally {
			pm.close();
		}
		return t;
	}

	@Override
	public Track createNonpersistentTrack(String artistName, String albumName, String trackName) {
		Track track = new GaeTrackImpl();
		track.setKey(KeygenService.createTrackKey(artistName, albumName, trackName));
		track.setTitle(trackName);
		track.setArtistName(artistName);
		track.setAlbumName(albumName);
		return track;
	}

	@Override
	public void persistAllTracks(List<Track> tracks) {
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			pm.makePersistentAll(tracks);
		} finally {
			pm.close();
		}
	}

	@Override
	public void setYoutubeId(String trackId, String youtubeId) {
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		Track t = null;
		try {
			t = pm.getObjectById(GaeTrackImpl.class, trackId);
			t.setYoutubeLocation(youtubeId);
		} finally {
			pm.close();
		}
	}
	
}
