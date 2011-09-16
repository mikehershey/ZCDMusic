/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zcd.music.model.db.gae.dao;

import java.util.HashMap;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import me.zcd.music.model.db.YoutubeIdRatings;
import me.zcd.music.model.db.dao.YoutubeIdRatingDao;
import me.zcd.music.model.db.gae.jdo.YoutubeIdRatingGaeImpl;

import java.util.List;
import java.util.Map;
import javax.jdo.JDOHelper;
import me.zcd.leetml.gae.GAEModel;
import me.zcd.leetml.logging.Log;
import me.zcd.leetml.logging.LogFactory;

/**
 *
 * @author mikehershey
 */
public class YoutubeIdRatingDaoGaeImpl implements YoutubeIdRatingDao {
	
	Log log = LogFactory.getLogger(YoutubeIdRatingDaoGaeImpl.class);

	@Override
	public YoutubeIdRatings getYoutubeIdRatings(String trackKey) {
		log.info("finding rating for track: " + trackKey);
		YoutubeIdRatingGaeImpl youtubeIdRating = null;
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			youtubeIdRating = pm.getObjectById(YoutubeIdRatingGaeImpl.class, trackKey);
		} catch(JDOObjectNotFoundException je) {
		} catch(Exception e) {
			log.error("Exception loading YoutubeIdRating", e);
		} finally {
			pm.close();
		}
		return youtubeIdRating;
	}

	@Override
	public YoutubeIdRatings addRating(String youtubeId, String trackKey, long toAdd) {
		log.info("finding rating for track: " + trackKey);
		YoutubeIdRatingGaeImpl youtubeIdRating = null;
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			try {
				youtubeIdRating = pm.getObjectById(YoutubeIdRatingGaeImpl.class, trackKey);
			} catch(JDOObjectNotFoundException je) {
				youtubeIdRating = new YoutubeIdRatingGaeImpl(trackKey);
			}
			Map<String, Long> ratings = youtubeIdRating.getRatings();
			log.info("The map is: " + ratings);
			if(ratings.containsKey(youtubeId)) {
				long rating = ratings.get(youtubeId);
				rating += toAdd;
				ratings.put(youtubeId, rating);
			} else {
				ratings.put(youtubeId, toAdd);
			}
			youtubeIdRating.setRatings(ratings);
			JDOHelper.makeDirty(youtubeIdRating, "ratings");
			pm.makePersistent(youtubeIdRating);
		} catch(Exception e) {
			log.error("Exception loading YoutubeIdRating", e);
		} finally {
			pm.close();
		}
		return youtubeIdRating;
	}
	
}
