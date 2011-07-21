package me.zcd.music.model.db;

import me.zcd.music.Settings;

/**
 *
 * @author mikehershey
 */
public interface Track {
	
	public void setKey(String key);

	public String getKey();
	
	public String getArtistKey();

	public String getAlbumKey();

	public void setTitle(String title);

	public String getTitle();

	public void setYoutubeLocation(String youtubeLocation);

	public String getYoutubeLocation();

	public void setArtistName(String artistName);
	
	public  String getArtistName();
	
	public void setAlbumName(String albumName);

	public String getAlbumName();

	public void setGenre(String genre);
	
	public String getGenre();
	
}
