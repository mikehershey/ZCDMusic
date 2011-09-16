package me.zcd.music.musicdiscovery.api.resources;

import me.zcd.leetml.logging.Log;
import me.zcd.leetml.logging.LogFactory;

/**
 *
 * @author mikehershey
 */
public class TrackSearchResult {
	
	private static final Log log = LogFactory.getLogger(TrackSearchResult.class);
	
	private String apiId;
	private String name;
	private int trackNumber;
	
	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(String trackNumber) {
		int i = 0;
		try {
			i = Integer.parseInt(trackNumber);
		} catch (Exception e) {
			log.warn("", e);
			return;
		}
		this.trackNumber = i;
	}
	
}
