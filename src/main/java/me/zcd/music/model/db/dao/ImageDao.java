package me.zcd.music.model.db.dao;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mikehershey
 */
public interface ImageDao {
	
	public String saveImage(byte[] imageData, String mimeType);
	public void serveImage(String key, HttpServletResponse resp);
	
}
