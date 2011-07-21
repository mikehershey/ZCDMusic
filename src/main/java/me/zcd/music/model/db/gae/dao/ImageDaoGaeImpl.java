/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zcd.music.model.db.gae.dao;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.FinalizationException;
import com.google.appengine.api.files.LockException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import me.zcd.music.model.db.dao.ImageDao;

/**
 *
 * @author mikehershey
 */
public class ImageDaoGaeImpl implements ImageDao{

	private static final Logger log = Logger.getLogger(ImageDaoGaeImpl.class.getName());
	
	@Override
	public String saveImage(byte[] imageData, String mimeType) {
		if(imageData == null) {
			return null;
		}
		try {
			FileService fileService = FileServiceFactory.getFileService();

			AppEngineFile file = fileService.createNewBlobFile(mimeType);

			// Open a channel to write to it
			boolean lock = true;
			FileWriteChannel writeChannel = fileService.openWriteChannel(file, lock);

			OutputStream out = Channels.newOutputStream(writeChannel);
			if(out != null) {
				out.write(imageData);
				out.close();
			}
			
			writeChannel.closeFinally();
			
			BlobKey blobKey = fileService.getBlobKey(file);
			log.info("Stored image at: " + blobKey);
			return blobKey.getKeyString();
		} catch (Exception ex) {
			Logger.getLogger(ImageDaoGaeImpl.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	@Override
	public void serveImage(String key, HttpServletResponse resp) {
		if(key == null) {
			return;
		}
		try {
			BlobstoreServiceFactory.getBlobstoreService().serve(new BlobKey(key), resp);
		} catch (IOException ex) {
			Logger.getLogger(ImageDaoGaeImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
}
