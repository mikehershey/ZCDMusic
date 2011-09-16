package me.zcd.music.model.db.dao;

import me.zcd.music.model.db.YoutubeIdRatings;

/**
 *
 * @author mikehershey
 */
public interface YoutubeIdRatingDao {
	
	public YoutubeIdRatings getYoutubeIdRatings(String trackKey);
	public YoutubeIdRatings addRating(String youtubeId,String trackKey, long toAdd);
	
}
