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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zcd.leetml.LeetmlController;

import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.leetml.bean.validation.rules.RequiredRule.Required;
import me.zcd.music.model.db.Artist;
import me.zcd.music.model.db.dao.ArtistDao;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;

public class ShowArtist extends LeetmlController implements Bean {

	private static final long serialVersionUID = 1L;

	private ArtistDao artistDao = DaoProviderFactory.getProvider().getArtistDao();
	
	private String id;
	
	@ManagedField
	@Required
	public void setId(String id) {
		this.id = id;
	}
	
	
	@Override
	public String service() {
		//load the artist
		Artist artist = this.artistDao.getArtist(id);
		this.getTemplateContext().put("artist", artist);
		this.getTemplateContext().put("albums", this.artistDao.getAlbumsFromArtist(artist));
		return "showArtist";
	}
	
	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Map<String, ValidationRule> arg2) {
		try {
			resp.getWriter().println("Artist key is required.");
		} catch (Exception e) {}
		resp.setStatus(400);
	}

	
	
}
