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
import me.zcd.music.model.db.dao.TrackDao;
import me.zcd.music.model.db.dao.UserLibraryDao;
import me.zcd.music.model.db.dao.YoutubeIdRatingDao;
import me.zcd.music.model.db.gae.dao.AlbumDaoGaeImpl;
import me.zcd.music.model.db.gae.dao.ArtistDaoGaeImpl;
import me.zcd.music.model.db.gae.dao.TrackDaoGaeImpl;
import me.zcd.music.model.db.gae.dao.UserLibraryDaoGaeImpl;
import me.zcd.music.model.db.gae.dao.YoutubeIdRatingDaoGaeImpl;

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
	public UserLibraryDao getUserLibraryDao() {
		return new UserLibraryDaoGaeImpl();
	}

	@Override
	public YoutubeIdRatingDao getYoutubeIdRatingDao() {
		return new YoutubeIdRatingDaoGaeImpl();
	}
	
}
