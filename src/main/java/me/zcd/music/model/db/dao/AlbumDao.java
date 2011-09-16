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
import me.zcd.music.model.db.Album;
import me.zcd.music.model.db.Track;

public interface AlbumDao {

	public Album getAlbum(String key);
	public List<Track> getAllTracks(Album album);
	public Album createNonpersistentAlbum(String artistName, String albumName);
	public void persistAllAlbums(List<Album> albums);
	public void persistAlbum(Album album);
	
}
