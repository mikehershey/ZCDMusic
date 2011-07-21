package me.zcd.music.controllers.tabs;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.zcd.leetml.Template;

public class BrowseWarehouse extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		Template.getInstance().render("browsewarehouse", null, resp);
	}
	
}
