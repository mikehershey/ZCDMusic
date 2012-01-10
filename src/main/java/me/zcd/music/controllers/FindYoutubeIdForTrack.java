/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.controllers;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.leetml.bean.validation.rules.RequiredRule.Required;
import me.zcd.music.youtube.api.Search;

import java.util.Map;
import javax.servlet.http.HttpServlet;
import me.zcd.leetml.logging.Log;
import me.zcd.leetml.logging.LogFactory;
import me.zcd.music.model.db.Track;
import me.zcd.music.model.db.UserLibrary;
import me.zcd.music.model.db.dao.TrackDao;
import me.zcd.music.model.db.dao.UserLibraryDao;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;
import me.zcd.music.user.UserServiceFactory;

/**
 * Youtube ids are found on demand as they are requested. If a video does not
 * have an ID make an ajax call to this, it will use the youtube api to find the
 * best match and return it as plain text.
 * 
 * It will then kick off a task to save the found youtube ID in the database.
 * 
 * @author mikehershey
 * 
 */
public class FindYoutubeIdForTrack extends HttpServlet implements Bean {

	Log log = LogFactory.getLogger(FindYoutubeIdForTrack.class);
	private static final long serialVersionUID = 1L;
	private String trackKey;

	private TrackDao trackDao = DaoProviderFactory.getProvider().getTrackDao();
	private UserLibraryDao userLibraryDao = DaoProviderFactory.getProvider().getUserLibraryDao();
	
	@ManagedField
	@Required
	public void setTrackKey(String trackKey) {
		this.trackKey = trackKey;
	}

	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp) {
		// get the url
		try {
			//lookup the track based on ID
			log.info("Looking up youtube song for track: " + this.trackKey);
			Track track = this.trackDao.getTrack(this.trackKey);
			log.debug("Found track info: " + track.getArtistName() + " - " + track.getTitle());
			String youtubeId = null;
			if(track.getYoutubeLocation() != null && !track.getYoutubeLocation().isEmpty()) {
				youtubeId = track.getYoutubeLocation();
			} else {
				youtubeId = new Search().findYoutubeId(track.getArtistName(), track.getTitle(), track.getKey()).get(0);
				trackDao.setYoutubeId(this.trackKey, youtubeId);
			}
			resp.getWriter().println(youtubeId);
		} catch (Exception e) {
			log.error("Exception finding youtube ID", e);
		}
	}

	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Map<String, ValidationRule> arg2) {
		resp.setStatus(400);
	}

}