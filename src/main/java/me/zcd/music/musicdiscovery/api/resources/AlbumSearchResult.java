package me.zcd.music.musicdiscovery.api.resources;

import java.util.Date;
import me.zcd.leetml.logging.Log;
import me.zcd.leetml.logging.LogFactory;

/**
 *
 * @author mikehershey
 */
public class AlbumSearchResult {

	private static final Log log = LogFactory.getLogger(AlbumSearchResult.class);
	
	private String apiId;
	private String name;
	private Date releaseDate;
	private String type;

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

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
