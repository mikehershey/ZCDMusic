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
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.Query;
import me.zcd.music.model.db.Album;
import me.zcd.music.model.db.Artist;

import me.zcd.music.model.db.dao.ArtistDao;
import me.zcd.music.model.db.gae.GAEModel;
import me.zcd.music.model.db.gae.jdo.GaeAlbumImpl;
import me.zcd.music.model.db.gae.jdo.GaeArtistImpl;
import me.zcd.music.model.db.utils.KeygenService;

//Artist Data access object google app engine implementation incase your wondering...
public class ArtistDaoGaeImpl implements ArtistDao {

	@Override
	public Artist getArtist(Key key) {
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		GaeArtistImpl a = null;
		try {
			a = pm.getObjectById(GaeArtistImpl.class, key);
		} catch (JDOObjectNotFoundException e) {
			
		}finally {
			pm.close();
		}
		return a;
	}

	@Override
	public Artist getArtist(String key) {
		Artist artist = null;
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			artist = pm.getObjectById(GaeArtistImpl.class, key);
		} catch (JDOObjectNotFoundException e) {
			
		} finally {
			pm.close();
		}
		return artist;
	}

	@Override
	public List<Album> getAlbumsFromArtist(Artist artist) {
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		List<Album> albums = new ArrayList<Album>();
		try {
			for(String key : artist.getAlbumKeys()) {
				albums.add(pm.getObjectById(GaeAlbumImpl.class, key));
			}
		} finally {
			pm.close();
		}
		return albums;
	}

	@Override
	public Artist createNonpersistentArist(String name) {
		Artist artist = new GaeArtistImpl();
		artist.setKey(KeygenService.createArtistKey(name));
		artist.setName(name);
		artist.setAlbumKeys(new ArrayList<String>());
		artist.setTrackKeys(new ArrayList<String>());
		return artist;
	}

	@Override
	public void persistArtist(Artist artist) {
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			pm.makePersistent(artist);
		} finally {
			pm.close();
		}
	}

	@Override
	public List<Artist> getArtistsThatStartWith(String prefix) {
		List<Artist> artistResults = null;
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			Query q = pm.newQuery(GaeArtistImpl.class);
			try {
				q.setRange(0, 5);
				q.setFilter("name >= beginsWithParam && name < beginsWithMaxParam");
				q.declareParameters("String beginsWithParam, String beginsWithMaxParam");
				artistResults = (List<Artist>) q.execute(prefix, prefix + "\ufffd");
				pm.detachCopyAll(artistResults);
			} finally {
				q.closeAll();
			}
		} finally {
			pm.close();
		}
		return artistResults;
	}
	
}
