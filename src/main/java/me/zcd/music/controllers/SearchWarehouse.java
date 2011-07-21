package me.zcd.music.controllers;

import com.google.appengine.api.blobstore.BlobKey;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.leetml.bean.validation.rules.RequiredRule.Required;
import me.zcd.music.model.db.gae.GAEModel;
import me.zcd.music.model.db.gae.jdo.GaeAlbumImpl;
import me.zcd.music.model.db.gae.jdo.GaeArtistImpl;
import me.zcd.music.model.db.gae.jdo.GaeTrackImpl;

import com.google.gson.Gson;
import me.zcd.music.model.db.Artist;
import me.zcd.music.model.db.dao.ArtistDao;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;

public class SearchWarehouse extends HttpServlet implements Bean {
	
	Logger log = Logger.getLogger(SearchWarehouse.class.getName());

	private static final long serialVersionUID = 1L;
	
	private ArtistDao artistDao = DaoProviderFactory.getProvider().getArtistDao();

	private String term;
	
	private String type = "ANY";
	
	@ManagedField
	@Required
	public void setTerm(String searchTerm) {
		this.term = searchTerm;
	}
	
	@ManagedField
	public void setType(String type) {
		this.type = type;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp) {
		List<JsonResult> matches = new ArrayList<JsonResult>();
		if(type.equals("ANY") || type.equals("ARTIST")) {
			this.term = this.term.toLowerCase().trim();
			List<Artist> artistResults = this.artistDao.getArtistsThatStartWith(term);
			for(Artist a : artistResults) {
				matches.add(new JsonResult(a.getKey(), "ARTIST", a.getName(), null, null, a.getArtistArtKey(), null));
			}
				
		}
		if(type.equals("ANY") || type.equals("ALBUM")) {
			this.term = this.term.toLowerCase().trim();
			List<GaeAlbumImpl> albumResults = null;
			PersistenceManager pm = GAEModel.get().getPersistenceManager();
			try {
				Query q = pm.newQuery(GaeAlbumImpl.class);
				try {
					q.setFilter("name >= beginsWithParam && name < beginsWithMaxParam");
					q.setRange(0, 5);
					q.declareParameters("String beginsWithParam, String beginsWithMaxParam");
					albumResults = (List<GaeAlbumImpl>) q.execute(term, term + "\ufffd");
					pm.detachCopyAll(albumResults);
				} finally {
					q.closeAll();
				}
			} finally {
				pm.close();
			}
			for(GaeAlbumImpl a : albumResults) {
				matches.add(new JsonResult(a.getKey(),"ALBUM", a.getArtistName(), a.getName(), null, a.getAlbumArtKey(), null));
			}
		}
		if(type.equals("ANY") || type.equals("TRACK")) {
			this.term = this.term.toLowerCase().trim();
			List<GaeTrackImpl> trackResults = null;
			PersistenceManager pm = GAEModel.get().getPersistenceManager();
			try {
				Query q = pm.newQuery(GaeTrackImpl.class);
				try {
					q.setRange(0, 10);
					q.setFilter("title >= beginsWithParam && title < beginsWithMaxParam");
					q.declareParameters("String beginsWithParam, String beginsWithMaxParam");
					trackResults = (List<GaeTrackImpl>) q.execute(term, term + "\ufffd");
					pm.detachCopyAll(trackResults);
				} finally {
					q.closeAll();
				}
			} finally {
				pm.close();
			}
			for(GaeTrackImpl t : trackResults) {
				matches.add(new JsonResult(t.getKey(), "TRACK", t.getArtistName(), t.getAlbumName(), t.getTitle(), null, t.getYoutubeLocation()));
			}
		}
		Gson gson = new Gson();
		String json = gson.toJson(matches);
		try {
			resp.getWriter().write(json);
		} catch (IOException e) {
		}
		
	}
	
	public static class JsonResult {
		public JsonResult(String key, String type, String artist, String album, String track, String imageUrl, String youtubeId) {
			this.key = key;
			this.type = type;
			this.artist = artist;
			this.album = album;
			this.track = track;
			this.imageUrl = imageUrl;
			this.youtubeId = youtubeId;
		}
		public String type;
		public String artist;
		public String album;
		public String track;
		public String imageUrl;
		public String key;
		public String youtubeId;
	}
	
	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Hashtable<String, ValidationRule> arg2) {
		try {
			resp.getWriter().println("Validation error: search term is required.");
		} catch (IOException e) {
			
		}
	}

}
