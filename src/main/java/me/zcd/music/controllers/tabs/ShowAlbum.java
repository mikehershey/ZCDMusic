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
import me.zcd.music.model.db.Album;
import me.zcd.music.model.db.dao.AlbumDao;
import me.zcd.music.model.db.dao.TrackDao;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;

/**
 *
 * @author mikehershey
 */
public class ShowAlbum extends HttpServlet implements Bean{
	
	private String albumKey;
	
	private AlbumDao albumDao = DaoProviderFactory.getProvider().getAlbumDao();
	
	@ManagedField
	@Required
	public void setAlbumKey(String albumKey) {
		this.albumKey = albumKey;
	}
	
	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp) {
		Album album = albumDao.getAlbum(albumKey);
		Map<String, Object> replyContext = Template.createReplyContext();
		replyContext.put("album", album);
		replyContext.put("tracks", albumDao.getAllTracks(album));
		Template.getInstance().render("showAlbum", replyContext, resp);
	}

	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Hashtable<String, ValidationRule> invalidFields) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
}
