package me.zcd.music.model.db;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mikehershey
 */
public interface YoutubeIdRatings {

	public String getTrackKey();
	
	public Map<String, Long> getRatings();

	public void setRatings(Map<String, Long> ratings);
	
}
