package me.zcd.music.musicdiscovery.musicbrainz.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import me.zcd.leetml.logging.Log;
import me.zcd.leetml.logging.LogFactory;

/**
 *
 * @author mikehershey
 */
public class ReleaseGroupResult {
	
	private static final Log log = LogFactory.getLogger(ReleaseGroupResult.class);
	
	private String apiId;
	private String name;
	private String type;
	private Date releaseDate;
	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = Integer.parseInt(count);
	}

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

	public void setReleaseDate(String releaseDate) {
		if(releaseDate == null || releaseDate.isEmpty()) {
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.releaseDate = sdf.parse(releaseDate);
		} catch (ParseException ex) {
			sdf = new SimpleDateFormat("yyyy");
			try {
				this.releaseDate = sdf.parse(releaseDate);
			} catch (ParseException ex1) {
				log.error("Error parsing date from music brainz!@!!! ", ex1);
			}
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
