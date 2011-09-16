/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.controllers.ajax.album;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import java.util.Map;
import javax.servlet.http.HttpServlet;

import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.leetml.bean.validation.rules.RequiredRule.Required;
import me.zcd.music.model.db.Album;
import me.zcd.music.model.db.Track;
import me.zcd.music.model.db.dao.AlbumDao;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;
import me.zcd.music.utils.StringUtils;

public class GetAlbumAsPlayables extends HttpServlet implements Bean {

	private static final long serialVersionUID = 1L;
	
	private AlbumDao albumDao = DaoProviderFactory.getProvider().getAlbumDao();
	
	private String id;
	
	@ManagedField
	@Required
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		List<Playable> ret = new ArrayList<Playable>();
		Album album = albumDao.getAlbum(id);
		List<Track> tracks = albumDao.getAllTracks(album);
		for(Track track : tracks) {
			Playable p = new Playable();
			p.trackKey = track.getKey();
			p.trackName = StringUtils.formatName(track.getTitle());
			p.artistName = StringUtils.formatName(track.getArtistName());
			p.albumName = StringUtils.formatName(track.getAlbumName());
			p.youtubeId = track.getYoutubeLocation();
			p.trackNumber = Integer.toString(track.getTrackNumber());
			ret.add(p);
		}
		Gson gson = new Gson();
		String json = gson.toJson(ret);
		try {
			resp.getWriter().write(json);
		} catch (IOException e) {
		}
	}

	public static class Playable {
		public String trackKey;
		public String trackName;
		public String artistName;
		public String albumName;
		public String youtubeId;
		public String trackNumber;
	}

	@Override
	public void onError(HttpServletRequest arg0, HttpServletResponse arg1, Map<String, ValidationRule> arg2) {
		// TODO Auto-generated method stub
		
	}
	
}
