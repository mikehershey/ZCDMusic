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
