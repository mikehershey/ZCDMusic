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
import me.zcd.music.model.db.gae.GAEModel;
import me.zcd.music.model.db.gae.jdo.UserLibraryGaeImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mikehershey
 */
public class UserLibraryDaoGaeImpl implements UserLibraryDao {
	
	TrackDao trackDao = DaoProviderFactory.getProvider().getTrackDao();
	Log log = LogFactory.getLog(UserLibraryDaoGaeImpl.class);
	
	@Override
	public UserLibrary getUserLibrary(String emailAddress) {
		UserLibraryGaeImpl userLibrary = null;
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			userLibrary = pm.getObjectById(UserLibraryGaeImpl.class, emailAddress);
		} catch(Exception e) {
			log.warn("Exception loading user library", e);
		} finally {
			pm.close();
		}
		return userLibrary;
	}

	@Override
	public List<Track> getAllTracksFromLibrary(UserLibrary library) {
		return trackDao.getTracks(new ArrayList<String>(library.getTrackKeys()));
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
			for(String trackKey : trackKeys) {
				userLibrary.addTrackKey(trackKey);
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
		UserLibraryGaeImpl userLibrary = null;
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			try {
				userLibrary = pm.getObjectById(UserLibraryGaeImpl.class, emailAddress);
			} catch(JDOObjectNotFoundException e) {
				log.info("No userLibrary found creating a new one.", e);
				//make a new one.
				userLibrary = (UserLibraryGaeImpl) this.createNonpersistedUserLibrary(emailAddress);
			}
			for(String trackKey : trackKeys) {
				userLibrary.addTrackKey(trackKey);
			}
			pm.makePersistent(userLibrary);
		} catch(Exception e) {
			log.warn("Exception loading user library", e);
		} finally {
			pm.close();
		}
		return userLibrary;
	}

	public UserLibrary createNonpersistedUserLibrary(String emailAddress) {
		UserLibrary userLibrary = new UserLibraryGaeImpl(emailAddress);
		return userLibrary;
	}
	
}