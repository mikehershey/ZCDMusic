/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.controllers.utils;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.leetml.bean.validation.rules.RequiredRule.Required;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;

public class ShowImage extends HttpServlet implements Bean {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	@ManagedField
	@Required
	public void setId(String id) {
		this.id = id;
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		DaoProviderFactory.getProvider().getImageDao().serveImage(id, resp);
	}

	@Override
	public void onError(HttpServletRequest arg0, HttpServletResponse arg1, Hashtable<String, ValidationRule> arg2) {
		
	}
	
}
