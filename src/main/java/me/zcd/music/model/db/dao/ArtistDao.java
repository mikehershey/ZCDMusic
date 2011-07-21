/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.model.db.dao;

import java.util.List;

import com.google.appengine.api.datastore.Key;
import me.zcd.music.model.db.Album;
import me.zcd.music.model.db.Artist;

public interface ArtistDao {

	public Artist getArtist(Key key);
	public Artist getArtist(String key);
	public List<Album> getAlbumsFromArtist(Artist artist);
	public Artist createNonpersistentArist(String name);
	public void persistArtist(Artist artist);
	public List<Artist> getArtistsThatStartWith(String prefix);
	
}
