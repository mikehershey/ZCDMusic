package me.zcd.music.model.db;

import java.util.Date;
import java.util.List;

/**
 *
 * @author mikehershey
 */
public interface Album {
	
	public String getKey();
	
	public void setKey(String key);

	public String getName();

	public String getArtistName();
	
	public void setArtistName(String name);

	public String getArtistKey();

	public void setReleaseDate(Date releaseDate);

	public Date getReleaseDate();

	public void setTrackKeys(List<String> tracks);

	public List<String> getTrackKeys();

	public void setAlbumArtKey(String albumArtKey);

	public String getAlbumArtKey();

	public void setName(String name);
	
	public void addTrackKey(String key);
	
}
