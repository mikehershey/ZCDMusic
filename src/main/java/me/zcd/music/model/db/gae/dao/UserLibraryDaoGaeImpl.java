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

import me.zcd.music.model.db.Track;
import me.zcd.music.model.db.UserLibrary;
import me.zcd.music.model.db.dao.TrackDao;
import me.zcd.music.model.db.dao.UserLibraryDao;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;
import me.zcd.music.model.db.gae.jdo.UserLibraryGaeImpl;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.jdo.JDOHelper;
import me.zcd.leetml.gae.GAEModel;
import me.zcd.leetml.logging.Log;
import me.zcd.leetml.logging.LogFactory;
import me.zcd.music.model.UserLibraryTrack;

/**
 *
 * @author mikehershey
 */
public class UserLibraryDaoGaeImpl implements UserLibraryDao {
	
	TrackDao trackDao = DaoProviderFactory.getProvider().getTrackDao();
	Log log = LogFactory.getLogger(UserLibraryDaoGaeImpl.class);
	
	@Override
	public UserLibrary getUserLibrary(String emailAddress) {
		UserLibraryGaeImpl userLibrary = null;
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			try {
				userLibrary = pm.getObjectById(UserLibraryGaeImpl.class, emailAddress);
			} catch(JDOObjectNotFoundException e) {
				//make a new one.
				userLibrary = (UserLibraryGaeImpl) this.createNonpersistedUserLibrary(emailAddress);
			}
		} catch(Exception e) {
			log.warn("Exception loading user library", e);
		} finally {
			pm.close();
		}
		return userLibrary;
	}

	/**
	 * Adds an array of tracks to the specified user's library, if the library doesn't exist it will be created.
	 * @param trackKeys
	 * @param emailAddress
	 * @return
	 */
	@Override
	public UserLibrary addTracksToLibrary(List<String> trackKeys, String emailAddress) {
		UserLibraryGaeImpl userLibrary = null;
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			try {
				userLibrary = pm.getObjectById(UserLibraryGaeImpl.class, emailAddress);
			} catch(JDOObjectNotFoundException e) {
				//make a new one.
				userLibrary = (UserLibraryGaeImpl) this.createNonpersistedUserLibrary(emailAddress);
			}
			List<Track> tracks = trackDao.getTracks(trackKeys);
			for(Track t : tracks) {
				UserLibraryTrack newT = new UserLibraryTrack();
				newT.setAlbumName(t.getAlbumName());
				newT.setArtistName(t.getArtistName());
				newT.setGenre(t.getGenre());
				newT.setPlayCount(0);
				newT.setTitle(t.getTitle());
				newT.setTrackNumber(t.getTrackNumber());
				newT.setYoutubeLocation(null);
				newT.setTrackKey(t.getKey());
				userLibrary.addTrack(newT);
			}
			pm.makePersistent(userLibrary);
		} catch(Exception e) {
			log.warn("Exception loading user library", e);
		} finally {
			pm.close();
		}
		return userLibrary;
	}

	/**
	 * Adds an array of tracks to the specified user's library, if the library doesn't exist it will be created.
	 * @param trackKeys
	 * @param emailAddress
	 * @return
	 */
	@Override
	public UserLibrary addTracksToLibrary(String[] trackKeys, String emailAddress) {
		return this.addTracksToLibrary(Arrays.asList(trackKeys), emailAddress);
	}

	public UserLibrary createNonpersistedUserLibrary(String emailAddress) {
		UserLibrary userLibrary = new UserLibraryGaeImpl(emailAddress);
		return userLibrary;
	}

	@Override
	public UserLibrary persistUserLibrary(UserLibrary library) {
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			pm.makePersistent(library);
		} catch(Exception e) {
			log.error("error saving new userLibrary", e);
		} finally {
			pm.close();
		}
		return library;
	}

	@Override
	public UserLibrary incrementTrackPlayCount(String trackKey, String emailAddress) {
		UserLibraryGaeImpl userLibrary = null;
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			try {
				userLibrary = pm.getObjectById(UserLibraryGaeImpl.class, emailAddress);
			} catch(JDOObjectNotFoundException e) {
				//make a new one.
				userLibrary = (UserLibraryGaeImpl) this.createNonpersistedUserLibrary(emailAddress);
			}
			if(userLibrary.getIndexedTracks().containsKey(trackKey)) {
				UserLibraryTrack t = userLibrary.getIndexedTracks().get(trackKey);
				t.setPlayCount(t.getPlayCount() + 1);
				log.info(emailAddress + " " + trackKey + " listen count: " + t.getPlayCount());
				userLibrary.getIndexedTracks().put(trackKey, t);
				JDOHelper.makeDirty(userLibrary, "tracks");
			}
		} catch(Exception e) {
			log.warn("Exception loading user library", e);
		} finally {
			pm.close();
		}
		return userLibrary;
	}
	
}