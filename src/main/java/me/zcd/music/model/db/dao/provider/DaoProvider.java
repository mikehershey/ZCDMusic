/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.model.db.dao.provider;

import me.zcd.music.model.db.dao.AlbumDao;
import me.zcd.music.model.db.dao.ArtistDao;
import me.zcd.music.model.db.dao.ImageDao;
import me.zcd.music.model.db.dao.TrackDao;

/**
 *
 * @author mikehershey
 */
public interface DaoProvider {
	
	public ArtistDao getArtistDao();
	public AlbumDao getAlbumDao();
	public TrackDao getTrackDao();
	public ImageDao getImageDao();
	
}
