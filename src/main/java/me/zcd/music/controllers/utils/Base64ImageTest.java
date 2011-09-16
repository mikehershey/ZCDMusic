package me.zcd.music.controllers.utils;

import biz.source_code.base64coder.Base64Coder;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zcd.leetml.image.Image;
import me.zcd.leetml.image.ImageService;
import me.zcd.leetml.image.ImageServiceFactory;
import me.zcd.leetml.logging.Log;
import me.zcd.leetml.logging.LogFactory;
import me.zcd.music.utils.URLFetch;

/**
 *
 * @author mikehershey
 */
public class Base64ImageTest extends HttpServlet {
	
	private static Log log = LogFactory.getLogger(Base64ImageTest.class);
	
	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp) {
		//going to show: http://i.imgur.com/0wpRK.jpg
		byte[] data = URLFetch.getUrlData("http://i.imgur.com/0wpRK.jpg", new HashMap());
		ImageService imageService = ImageServiceFactory.get();
		Image image = imageService.saveImage(data, "image/jpg");
		log.info("Loading image w/ key: " + image.getKey());
		image = imageService.getImage(Long.parseLong(image.getKey()));
		
		try {
			resp.getWriter().println("<html><head></head><body>");
			resp.getWriter().print("<img src=\"");
			resp.getWriter().print(image.getData());
			resp.getWriter().println("\"/>");
		} catch (IOException ex) {
			Logger.getLogger(Base64ImageTest.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}
	
	
}
