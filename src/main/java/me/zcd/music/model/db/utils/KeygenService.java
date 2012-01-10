/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
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
	
	public static String getArtistFromTrackKey(String trackKey) {
		String[] parts = trackKey.split(Settings.KEY_SEPERATOR);
		return parts[0];
	}
	
	public static String getTrackFromTrackKey(String trackKey) {
		String[] parts = trackKey.split(Settings.KEY_SEPERATOR);
		return parts[2];
	}
	
}
