/**
 * Copyright � 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
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
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zcd.leetml.Template;
import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;

/**
 *
 * @author mikehershey
 */
public class Login extends HttpServlet implements Bean {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> replyContext = Template.createReplyContext();
		UserService userService = UserServiceFactory.getUserService();
		replyContext.put("loginLink", userService.createLoginURL("/"));
		Template.getInstance().render("login", replyContext, resp);
	}
	
	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Hashtable<String, ValidationRule> invalidFields) {
		try {
			resp.getWriter().println("Validation failed.");
		} catch (IOException ex) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	
	
}
