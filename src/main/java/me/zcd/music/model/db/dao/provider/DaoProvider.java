/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
