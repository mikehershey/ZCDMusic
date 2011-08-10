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

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zcd.leetml.LeetmlController;
import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.leetml.bean.validation.rules.RequiredRule.Required;
import me.zcd.music.model.db.Album;
import me.zcd.music.model.db.Track;
import me.zcd.music.model.db.dao.AlbumDao;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;

/**
 *
 * @author mikehershey
 */
public class ShowAlbum extends LeetmlController implements Bean{
	
	private String albumKey;
	
	private AlbumDao albumDao = DaoProviderFactory.getProvider().getAlbumDao();
	
	@ManagedField
	@Required
	public void setAlbumKey(String albumKey) {
		this.albumKey = albumKey;
	}
	
	@Override
	public String service() {
		Album album = albumDao.getAlbum(albumKey);
		this.getTemplateContext().put("album", album);
		List<Track> tracks = albumDao.getAllTracks(album);
		if(tracks.size() >= 1) {
			this.getTemplateContext().put("genre", tracks.get(0).getGenre());
		}
		this.getTemplateContext().put("tracks", tracks);
		return "showAlbum";
	}

	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Map<String, ValidationRule> invalidFields) {
		resp.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
	}
	
}
