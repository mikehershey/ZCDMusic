/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zcd.music.model.db.dao.provider;

import me.zcd.music.model.db.dao.AlbumDao;
import me.zcd.music.model.db.dao.ArtistDao;
import me.zcd.music.model.db.dao.ImageDao;
import me.zcd.music.model.db.dao.TrackDao;
import me.zcd.music.model.db.gae.dao.AlbumDaoGaeImpl;
import me.zcd.music.model.db.gae.dao.ArtistDaoGaeImpl;
import me.zcd.music.model.db.gae.dao.ImageDaoGaeImpl;
import me.zcd.music.model.db.gae.dao.TrackDaoGaeImpl;

/**
 *
 * @author mikehershey
 */
public class GaeDaoProviderImpl implements DaoProvider {

	@Override
	public ArtistDao getArtistDao() {
		return new ArtistDaoGaeImpl();
	}

	@Override
	public AlbumDao getAlbumDao() {
		return new AlbumDaoGaeImpl();
	}

	@Override
	public TrackDao getTrackDao() {
		return new TrackDaoGaeImpl();
	}
	
	@Override
	public ImageDao getImageDao() {
		return new ImageDaoGaeImpl();
	}
	
}
