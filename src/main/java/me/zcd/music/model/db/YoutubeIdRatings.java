package me.zcd.music.model.db;

/**
 *
 * @author mikehershey
 */
public interface YoutubeIdRatings {

	public String getYoutubeId();
	public long getRating();
	public void addRating(long toAdd);
	
}
