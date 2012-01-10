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

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zcd.leetml.LeetmlController;
import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.music.Settings;
import me.zcd.music.model.UserLibraryTrack;
import me.zcd.music.model.db.Track;
import me.zcd.music.model.db.UserLibrary;
import me.zcd.music.model.db.dao.UserLibraryDao;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;
import me.zcd.music.user.UserService;
import me.zcd.music.user.UserServiceFactory;

/**
 *
 * @author mikehershey
 */
public class Index extends LeetmlController implements Bean {
	
	UserService userService = UserServiceFactory.getUserService();
	UserLibraryDao userLibraryDao = DaoProviderFactory.getProvider().getUserLibraryDao();
	
	private String showLibrary;

	@ManagedField
	public void setShowLibrary(String showLibrary) {
		this.showLibrary = showLibrary;
	}
	
	@Override
	public String service() {
		if(this.req.getHeader("User-Agent").toLowerCase().contains("msie")) {
			return "noIE";
		}
		/*
		if(this.req.getHeader("User-Agent").toLowerCase().contains("iphone")) {
			try {
				this.resp.sendRedirect("http://iphone.music.zcd.me");
			} catch (IOException ex) {
				Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		 */
		//check the current user
		String userEmail = userService.getCurrentUsersEmailAddress();
		String email = showLibrary;
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory.getUserService();
		if(userEmail == null) {
			this.getTemplateContext().put("loginLink", userService.createLoginURL("/"));
		} else {
			this.getTemplateContext().put("logoutLink", userService.createLogoutURL("/"));
		}
		if(email == null) {
			email = userEmail;
		}
		Set<UserLibraryTrack> tracks;
		if(email != null && !email.isEmpty()) {
			this.getTemplateContext().put("userEmail", userEmail);
			UserLibrary userLibrary = userLibraryDao.getOrCreateUserLibrary(email);
			if(userLibrary != null) {
				tracks = userLibrary.getTracks();
				this.getTemplateContext().put("userTracks", tracks);
			}
		}
		if(Settings.shouldMinify()) {
			this.getTemplateContext().put("minify", true);
		}
		return "homepage";
	}

	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Map<String, ValidationRule> invalidFields) {
		resp.setStatus(400);
	}
	
}
