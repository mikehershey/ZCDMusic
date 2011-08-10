/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
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
			log.log(Level.WARNING, "Someone just tried to save a null image: ", Thread.currentThread().getStackTrace());
			return null;
		}
		boolean success = false;
		BlobKey blobKey = null;
		for(int i = 0; i < 3 && !success; i++) {
			FileWriteChannel writeChannel = null;
			try {
				FileService fileService = FileServiceFactory.getFileService();
				AppEngineFile file = fileService.createNewBlobFile(mimeType);
				boolean lock = true;
				writeChannel = fileService.openWriteChannel(file, lock);
				
				OutputStream out = Channels.newOutputStream(writeChannel);
				if(out != null) {
					out.write(imageData);
					out.close();
				}
				writeChannel.closeFinally();
				writeChannel.close();

				blobKey = fileService.getBlobKey(file);
				if(blobKey != null) {
					success = true;
				}
			} catch (Exception ex) {
				log.log(Level.WARNING, "Possibly trying again (3 tries)", ex);
			} finally {
				try {
					
				} catch (Exception ex) {
					log.log(Level.INFO, "Write Channel could not be closed, probably was never opened.", ex);
				}
			}
		}
		if(blobKey == null) {
			log.severe("ImageDao was asked to store an image, with valid image data, but failed to do so.");
			return null;
		} else {
			return blobKey.getKeyString();
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
