/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.iphone.controllers;

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
		//check the current user
		String userEmail = userService.getCurrentUsersEmailAddress();
		String email = showLibrary;
		if(email == null) {
			email = userEmail;
			com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory.getUserService();
			this.getTemplateContext().put("loginLink", userService.createLoginURL("/"));
		} else {
			//already logged in go directly to their library
		}
		return "homepage";
	}

	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Map<String, ValidationRule> invalidFields) {
		resp.setStatus(400);
	}
	
}
