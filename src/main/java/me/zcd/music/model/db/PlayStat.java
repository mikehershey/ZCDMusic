package me.zcd.music.model.db;

/**
 * Records information about the number of times a song has been played.
 * @author mikehershey
 */
public interface PlayStat {
	
	public String getTrackKey();
	public void setTrackKey(String trackKey);
	
	public long getPlayCount();
	public void incrementPlayCount();
	
}
