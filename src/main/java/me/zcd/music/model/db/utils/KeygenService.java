package me.zcd.music.model.db.utils;

import me.zcd.music.Settings;

/**
 *
 * @author mikehershey
 */
public class KeygenService {
	
	public static String createArtistKey(String artistName) {
		artistName = artistName.toLowerCase();
		if(artistName.length() > 150) {
			artistName = artistName.substring(0,150);
		}
		return artistName;
	}
	
	public static String createAlbumKey(String artistName, String albumName) {
		artistName = createArtistKey(artistName);
		albumName = albumName.toLowerCase();
		if(albumName.length() > 150) {
			albumName = albumName.substring(0,150);
		}
		return artistName + Settings.KEY_SEPERATOR + albumName;
	}
	
	public static String createTrackKey(String artistName, String albumName, String trackName) {
		trackName = trackName.toLowerCase();
		if(trackName.length() > 150) {
			trackName = trackName.substring(0,150);
		}
		return createAlbumKey(artistName, albumName) + Settings.KEY_SEPERATOR + trackName;
	}
	
}
