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

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

import me.zcd.leetml.Template;
import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.leetml.bean.validation.rules.PostOnly;
import me.zcd.leetml.bean.validation.rules.RequiredRule.Required;

public class ShowRequestArtist extends HttpServlet implements Bean {

	private static final long serialVersionUID = 1l;
	private String artistName;
	
	@ManagedField
	@Required
	@PostOnly
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		Template.getInstance().render("showRequestArtist", null, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(withUrl("/tasks/DoLoadArtist.h7m1").param("artistName", this.artistName).param("basicAuth", "ksh982oihfa83yhuhf3").method(Method.GET));
		try {
			resp.getOutputStream().println("Added to queue.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resp.setStatus(200);
	}
	
	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Hashtable<String, ValidationRule> arg2) {
		try {
			resp.getOutputStream().println("Artist name is required");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resp.setStatus(400);
	}
	
}
