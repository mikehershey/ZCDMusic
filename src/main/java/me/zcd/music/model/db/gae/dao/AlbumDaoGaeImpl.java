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

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.logging.Logger;
import me.zcd.music.model.db.Album;
import me.zcd.music.model.db.Track;

import me.zcd.music.model.db.dao.AlbumDao;
import me.zcd.music.model.db.gae.GAEModel;
import me.zcd.music.model.db.gae.jdo.GaeAlbumImpl;
import me.zcd.music.model.db.gae.jdo.GaeTrackImpl;
import me.zcd.music.model.db.utils.KeygenService;

public class AlbumDaoGaeImpl implements AlbumDao {

	private Logger log = Logger.getLogger(AlbumDaoGaeImpl.class.getName());
	
	@Override
	public Album getAlbum(String key) {
		GaeAlbumImpl album = null;
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			album = pm.getObjectById(GaeAlbumImpl.class, key);
		} finally {
			pm.close();
		}
		return album;
	}

	@Override
	public List<Track> getAllTracks(Album album) {
		List<Track> ret = new ArrayList<Track>();
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			for(String key : album.getTrackKeys()) {
				GaeTrackImpl t = pm.getObjectById(GaeTrackImpl.class, key);
				ret.add(t);
			}
		} finally {
			pm.close();
		}
		return ret;
	}

	@Override
	public Album createNonpersistentAlbum(String artistName, String albumName) {
		Album album = new GaeAlbumImpl();
		log.info("About to set key: " + KeygenService.createAlbumKey(artistName, albumName));
		album.setKey(KeygenService.createAlbumKey(artistName, albumName));
		album.setArtistName(artistName);
		album.setName(albumName);
		album.setTrackKeys(new ArrayList<String>());
		return album;
	}

	@Override
	public void persistAllAlbums(List<Album> albums) {
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			pm.makePersistentAll(albums);
		} finally {
			pm.close();
		}
	}

}
