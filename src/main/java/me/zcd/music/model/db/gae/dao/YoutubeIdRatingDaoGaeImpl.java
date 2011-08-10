/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zcd.music.model.db.gae.dao;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import me.zcd.music.model.db.YoutubeIdRatings;
import me.zcd.music.model.db.dao.YoutubeIdRatingDao;
import me.zcd.music.model.db.gae.GAEModel;
import me.zcd.music.model.db.gae.jdo.YoutubeIdRatingGaeImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author mikehershey
 */
public class YoutubeIdRatingDaoGaeImpl implements YoutubeIdRatingDao {
	
	Log log = LogFactory.getLog(YoutubeIdRatingDaoGaeImpl.class);

	@Override
	public YoutubeIdRatings getYoutubeIdRatings(String youtubeId) {
		YoutubeIdRatingGaeImpl youtubeIdRating = null;
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			youtubeIdRating = pm.getObjectById(YoutubeIdRatingGaeImpl.class, youtubeId);
		} catch(JDOObjectNotFoundException je) {
		} catch(Exception e) {
			log.warn("Exception loading YoutubeIdRating", e);
		} finally {
			pm.close();
		}
		return youtubeIdRating;
	}

	@Override
	public YoutubeIdRatings addRating(String youtubeId, long toAdd) {
		YoutubeIdRatingGaeImpl youtubeIdRating = null;
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			try {
				youtubeIdRating = pm.getObjectById(YoutubeIdRatingGaeImpl.class, youtubeId);
			} catch(JDOObjectNotFoundException je) {
			}
			if(youtubeIdRating == null) {
				youtubeIdRating = new YoutubeIdRatingGaeImpl(youtubeId);
				pm.makePersistent(youtubeIdRating);
			}
			youtubeIdRating.addRating(toAdd);
		} catch(Exception e) {
			log.warn("Exception loading YoutubeIdRating", e);
		} finally {
			pm.close();
		}
		return youtubeIdRating;
	}
	
}
