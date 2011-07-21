package me.zcd.music.model.db.dao;

import java.util.List;
import me.zcd.music.model.db.Album;
import me.zcd.music.model.db.Track;

public interface AlbumDao {

	public Album getAlbum(String key);
	public List<Track> getAllTracks(Album album);
	public Album createNonpersistentAlbum(String artistName, String albumName);
	public void persistAllAlbums(List<Album> albums);
	
}
