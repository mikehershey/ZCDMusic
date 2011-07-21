package me.zcd.music.controllers.tabs;

import java.util.Hashtable;
import java.util.Map;
import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.zcd.leetml.Template;
import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.leetml.bean.validation.rules.RequiredRule.Required;
import me.zcd.music.model.db.Artist;
import me.zcd.music.model.db.dao.ArtistDao;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;
import me.zcd.music.model.db.gae.jdo.GaeArtistImpl;

public class ShowArtist extends HttpServlet implements Bean {

	private static final long serialVersionUID = 1L;

	private ArtistDao artistDao = DaoProviderFactory.getProvider().getArtistDao();
	
	private String id;
	
	@ManagedField
	@Required
	public void setId(String id) {
		this.id = id;
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> replyContext = Template.createReplyContext();
		//load the artist
		Artist artist = this.artistDao.getArtist(id);
		replyContext.put("artist", artist);
		replyContext.put("albums", this.artistDao.getAlbumsFromArtist(artist));
		Template.getInstance().render("showArtist", replyContext, resp);
	}
	
	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Hashtable<String, ValidationRule> arg2) {
		try {
			resp.getWriter().println("Artist key is required.");
		} catch (Exception e) {}
		resp.setStatus(400);
	}

	
	
}
