/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.user;

import com.google.appengine.api.users.User;

/**
 *
 * @author mikehershey
 */
public class UserServiceGaeImpl implements UserService {

	@Override
	public String getCurrentUsersEmailAddress() {
		com.google.appengine.api.users.UserService userService = com.google.appengine.api.users.UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if(user == null) {
			return null;
		}
		return user.getEmail();
	}
	
	
	
}
