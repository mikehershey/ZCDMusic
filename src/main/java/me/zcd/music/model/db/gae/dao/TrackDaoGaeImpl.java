/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.model.db.gae.dao;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import me.zcd.leetml.gae.GAEModel;
import me.zcd.leetml.logging.Log;
import me.zcd.leetml.logging.LogFactory;
import me.zcd.music.model.db.Track;
import me.zcd.music.model.db.dao.TrackDao;
import me.zcd.music.model.db.gae.jdo.GaeTrackImpl;
import me.zcd.music.model.db.utils.KeygenService;

/**
 *
 * @author mikehershey
 */
public class TrackDaoGaeImpl implements TrackDao {
	
	private static final Log log = LogFactory.getLogger(TrackDaoGaeImpl.class);
	private DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	
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
	public List<Track> getTracks(List<String> trackKeys) {
		List<Track> ret = new ArrayList<Track>();
		try {
			//build a list of app engine keys
			List<Key> googleKeys = new ArrayList<Key>();
			for(String trackKey : trackKeys) {
				googleKeys.add(KeyFactory.createKey(GaeTrackImpl.class.getSimpleName(), trackKey));
			}
			
			//use low level api to do effecient bulk load
			Map<Key, Entity> results = this.datastoreService.get(googleKeys);
			
			for(Entry<Key, Entity> trackEntEntry : results.entrySet()) {
				ret.add(buildTrackFromEntity(trackEntEntry.getValue()));
			}
		} catch(Exception e) {
			log.error("Error loading tracks!", e);
		}
		return ret;
	}
	
	private Track buildTrackFromEntity(Entity ent) {
		Track ret = new GaeTrackImpl();
		ret.setKey(ent.getKey().getName());
		ret.setTitle((String)ent.getProperty("title"));
		ret.setYoutubeLocation((String)ent.getProperty("youtubeLocation"));
		ret.setGenre((String)ent.getProperty("genre"));
		ret.setArtistName((String)ent.getProperty("artistName"));
		ret.setAlbumName((String)ent.getProperty("albumName"));
		ret.setTrackNumber((int)(long)(Long)ent.getProperty("trackNumber"));
		return ret;
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

	@Override
	public void persistTrack(Track track) {
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			pm.makePersistent(track);
		} finally {
			pm.close();
		}
	}

	@Override
	public void incrementGlobalPlayCount(String trackId) {
		log.debug("incrementing global count for track: " + trackId);
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		Track t = null;
		try {
			t = pm.getObjectById(GaeTrackImpl.class, trackId);
			log.debug("Count before increment: " + t.getTotalPlays());
			t.incrementTotalPlays();
			JDOHelper.makeDirty(t, "totalPlays");
		} catch(Exception e) {
			log.error("error incrementing global count", e);
		}finally {
			pm.close();
		}
		log.debug("Count after increment: " + t.getTotalPlays());
	}
}
