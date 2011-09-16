package me.zcd.music.musicdiscovery.api.resources;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mikehershey
 */
public class TrackSearchResults {
	
	private List<TrackSearchResult> trackSearchResults = new ArrayList<TrackSearchResult>();

	public List<TrackSearchResult> getTrackSearchResults() {
		return trackSearchResults;
	}

	private void setTrackSearchResults(List<TrackSearchResult> trackSearchResults) {
		this.trackSearchResults = trackSearchResults;
	}
	
	public void addTrackSearchResult(TrackSearchResult trackSearchResult) {
		this.trackSearchResults.add(trackSearchResult);
	}
	
}
