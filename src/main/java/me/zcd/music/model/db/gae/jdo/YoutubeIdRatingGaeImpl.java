package me.zcd.music.model.db.gae.jdo;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
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
	private long rating;
	
	public YoutubeIdRatingGaeImpl(String youtubeId) {
		this.key = KeyFactory.createKey(YoutubeIdRatingGaeImpl.class.getSimpleName(), youtubeId);
		this.rating = 0l;
	}
	
	@Override
	public String getYoutubeId() {
		return this.key.getName();
	}

	@Override
	public long getRating() {
		return this.rating;
	}

	@Override
	public void addRating(long toAdd) {
		this.rating += toAdd;
	}
	
}
