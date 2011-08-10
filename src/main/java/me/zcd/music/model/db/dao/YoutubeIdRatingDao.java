package me.zcd.music.model.db.dao;

import me.zcd.music.model.db.YoutubeIdRatings;

/**
 *
 * @author mikehershey
 */
public interface YoutubeIdRatingDao {
	
	public YoutubeIdRatings getYoutubeIdRatings(String youtubeId);
	public YoutubeIdRatings addRating(String youtubeId, long toAdd);
	
}
