/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.youtube.api;

/**
 * Holds a youtube result. For some reason on app engine, a yt:videoid is not supplied
 * so you have to parse it out yourself. 
 * @author mikehershey
 */
public class SearchResult {
	
	private String videoId;
	private String title;
	private String description;
	
	private boolean allowEmbed;
	private boolean allowAutoPlay;
	private boolean allowSyndicate;

	private double averageRating;
	private int views;
	
	private String uploader;
	
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	
	public String getVideoId() {
		return videoId;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setAllowEmbed(boolean allowEmbed) {
		this.allowEmbed = allowEmbed;
	}
	
	public boolean isAllowEmbed() {
		return allowEmbed;
	}
	
	public void setAllowAutoPlay(boolean allowAutoPlay) {
		this.allowAutoPlay = allowAutoPlay;
	}
	
	public boolean isAllowAutoPlay() {
		return allowAutoPlay;
	}
	
	public void setAllowSyndicate(boolean allowSyndicate) {
		this.allowSyndicate = allowSyndicate;
	}
	
	public boolean isAllowSyndicate() {
		return allowSyndicate;
	}
	
	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}
	
	public double getAverageRating() {
		return averageRating;
	}
	
	public void setViews(int views) {
		this.views = views;
	}
	
	public int getViews() {
		return views;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public String getUploader() {
		return uploader;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}
