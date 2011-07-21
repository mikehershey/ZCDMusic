/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zcd.music.model.db;

import java.util.List;

/**
 *
 * @author mikehershey
 */
public interface Artist {
	
	public String getKey();
	
	public void setKey(String key);
	
	public void setTrackKeys(List<String> trackKeys);

	public List<String> getTrackKeys();

	public void setAlbumKeys(List<String> albumKeys);

	public List<String> getAlbumKeys();
	
	public void addAlbumKey(String albumKey);
	
	public void addTrackKey(String trackKey);

	public void setArtistArtKey(String artistArt);

	public String getArtistArtKey();

	public void setGenre(String genre);

	public String getGenre();

	public void setName(String name);
	
	public String getName();
	
}
