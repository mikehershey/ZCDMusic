/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.controllers.tabs;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zcd.leetml.LeetmlController;
import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;

/**
 *
 * @author mikehershey
 */
public class Login extends LeetmlController implements Bean {

	@Override
	public String service() {
		UserService userService = UserServiceFactory.getUserService();
		this.getTemplateContext().put("loginLink", userService.createLoginURL("/"));
		return "login";
	}

	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Map<String, ValidationRule> invalidFields) {
		try {
			resp.getWriter().println("Validation failed.");
		} catch (IOException ex) {
			log.error("FAILED WRITING ON ERROR", ex);
		}
	}
	
	
	
}
