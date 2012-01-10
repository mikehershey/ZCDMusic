/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.model.db;

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
	
	public int getTrackNumber();
	
	public void setTrackNumber(int i);
	
	public void incrementTotalPlays();
	
	public Long getTotalPlays();
	
}
