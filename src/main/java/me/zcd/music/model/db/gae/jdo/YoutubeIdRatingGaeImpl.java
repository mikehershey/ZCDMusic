package me.zcd.music.model.db.gae.jdo;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.HashMap;
import java.util.Map;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import me.zcd.music.model.db.YoutubeIdRatings;

/**
 *
 * @author mikehershey
 */
@PersistenceCapable(detachable="true")
public class YoutubeIdRatingGaeImpl implements YoutubeIdRatings {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String trackKey;
	
	@Persistent(serialized = "true", defaultFetchGroup="true")
	private Map<String,Long> ratings;
	
	public YoutubeIdRatingGaeImpl(String trackKey) {
		this.key = KeyFactory.createKey(YoutubeIdRatingGaeImpl.class.getSimpleName(), trackKey);
		this.trackKey = trackKey;
		ratings = new HashMap<String,Long>();
	}

	@Override
	public String getTrackKey() {
		return this.trackKey;
	}

	@Override
	public Map<String, Long> getRatings() {
		return ratings;
	}

	@Override
	public void setRatings(Map<String, Long> ratings) {
		this.ratings = ratings;
	}
	
}
