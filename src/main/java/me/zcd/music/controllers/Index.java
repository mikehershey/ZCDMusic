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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zcd.leetml.Template;

/**
 *
 * @author mikehershey
 */
public class Index extends HttpServlet {
	
	public void service(HttpServletRequest req, HttpServletResponse resp) {
		Template.getInstance().render("homepage", null, resp);
	}
	
}
