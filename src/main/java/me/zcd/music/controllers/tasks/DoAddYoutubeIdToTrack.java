/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.controllers.tasks;

import java.util.Hashtable;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.leetml.bean.validation.rules.RequiredRule.Required;
import me.zcd.music.model.db.gae.jdo.GaeTrackImpl;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import me.zcd.music.model.db.dao.TrackDao;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;

public class DoAddYoutubeIdToTrack extends HttpServlet implements Bean {
	
	private static final long serialVersionUID = 1l;

	private TrackDao trackDao = DaoProviderFactory.getProvider().getTrackDao();
	
	private String trackId;
	private String youtubeId;
	private String basicAuth;
	
	@ManagedField
	@Required
	public void setTrackKey(String trackKey) {
		this.trackId = trackKey;
	}
	
	@ManagedField
	@Required
	public void setYoutubeId(String youtubeId) {
		this.youtubeId = youtubeId;
	}
	
	@ManagedField
	@Required
	public void setBasicAuth(String basicAuth) {
		this.basicAuth = basicAuth;
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		if(this.basicAuth.equals("ewhjkasdmbg3489ufhast7")) {
			trackDao.setYoutubeId(this.trackId, this.youtubeId);
		}
	}
	
	@Override
	public void onError(HttpServletRequest arg0, HttpServletResponse arg1, Hashtable<String, ValidationRule> arg2) {
		
	}

}
