package me.zcd.music.model;

import java.io.Serializable;

/**
 *
 * @author mikehershey
 */
public class UserLibraryTrack implements Serializable {
	
	private String title;
	
	private String youtubeLocation;
	
	private String genre;
	
	private String artistName;
	
	private String albumName;

	private int trackNumber;
	
	private long playCount;
	
	private String trackKey;

	public String getTrackKey() {
		return trackKey;
	}

	public void setTrackKey(String trackKey) {
		this.trackKey = trackKey;
	}

	public long getPlayCount() {
		return playCount;
	}

	public void setPlayCount(long playCount) {
		this.playCount = playCount;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(int trackNumber) {
		this.trackNumber = trackNumber;
	}

	public String getYoutubeLocation() {
		return youtubeLocation;
	}

	public void setYoutubeLocation(String youtubeLocation) {
		this.youtubeLocation = youtubeLocation;
	}
	
}
