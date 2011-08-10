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

import java.util.List;
import me.zcd.leetml.LeetmlController;
import me.zcd.music.model.db.Track;
import me.zcd.music.model.db.UserLibrary;
import me.zcd.music.model.db.dao.UserLibraryDao;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;
import me.zcd.music.user.UserService;
import me.zcd.music.user.UserServiceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author mikehershey
 */
public class Index extends LeetmlController {
	
	UserService userService = UserServiceFactory.getUserService();
	UserLibraryDao userLibraryDao = DaoProviderFactory.getProvider().getUserLibraryDao();
	Log log = LogFactory.getLog(Index.class);
	
	@Override
	public String service() {
		//check the current user
		String email = userService.getCurrentUsersEmailAddress();
		if(email != null && !email.isEmpty()) {
			this.getTemplateContext().put("userEmail", email);
			UserLibrary userLibrary = userLibraryDao.getUserLibrary(email);
			if(userLibrary != null) {
				List<Track> tracks = userLibraryDao.getAllTracksFromLibrary(userLibrary);
				this.getTemplateContext().put("userTracks", tracks);
			}
		}
		return "homepage";
	}
	
}
