/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zcd.music.model.js;

/**
 *
 * @author mikehershey
 */
public class Playable {
	
	public Playable(String trackKey, String trackName, String artistName, String albumName, String youtubeId, String trackNumber) {
		this.trackKey = trackKey;
		this.trackName = trackName;
		this.artistName = artistName;
		this.albumName = albumName;
		this.youtubeId = youtubeId;
		this.trackNumber = trackNumber;
	}
	
	private String trackKey;
	private String trackName;
	private String artistName;
	private String albumName;
	private String youtubeId;
	private String trackNumber;
}
