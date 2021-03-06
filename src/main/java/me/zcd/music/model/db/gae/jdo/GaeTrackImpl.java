/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.model.db.gae.jdo;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.NotPersistent;
import me.zcd.music.Settings;
import me.zcd.music.model.db.Track;
import me.zcd.music.model.db.dao.TrackDao;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;
import me.zcd.music.utils.StringUtils;

/**
 * @author mikehershey
 *
 */
@PersistenceCapable(detachable="true")
public class GaeTrackImpl implements Track {

	@NotPersistent
	TrackDao trackDao = DaoProviderFactory.getProvider().getTrackDao();
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String title;
	
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
	private String youtubeLocation;
	
	@Persistent
	private String genre;
	
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
	private String artistName;
	
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
	private String albumName;
	
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
	private int trackNumber;
	
	@Persistent
	//make sure to use the object wrappers so Null can be assigned to them.
	//sharded counters can't be used B/C this needs to be queriable, as such
	//this field is no accurate, and should not be used for anything other then stats
	private Long totalPlays;

	public Long getTotalPlays() {
		if(totalPlays == null) {
			totalPlays = new Long(0);
		}
		return totalPlays;
	}

	public void setTotalPlays(Long totalPlays) {
		this.totalPlays = totalPlays;
	}
	
	@Override
	public void incrementTotalPlays() {
		this.setTotalPlays(this.getTotalPlays() + 1);
	}

	@Override
	public void setKey(String key) {
		this.key = KeyFactory.createKey(GaeTrackImpl.class.getSimpleName(), key.toLowerCase());
	}

	@Override
	public String getKey() {
		return this.key.getName();
	}

	@Override
	public String getArtistKey() {
		return this.key.getName().split(Settings.KEY_SEPERATOR)[0];
	}

	@Override
	public String getAlbumKey() {
		return this.key.getName().split(Settings.KEY_SEPERATOR)[1];
	}

	@Override
	public void setTitle(String title) {
		if(title != null) {
			this.title = title.toLowerCase();
		}
	}

	@Override
	public String getTitle() {
		if(this.title != null) {			
			return StringUtils.formatName(this.title);
		}
		return null;
	}

	@Override
	public void setYoutubeLocation(String youtubeLocation) {
		this.youtubeLocation = youtubeLocation;
	}

	@Override
	public String getYoutubeLocation() {
		return this.youtubeLocation;
	}

	@Override
	public void setArtistName(String artistName) {
		if(artistName != null) {
			this.artistName = artistName.toLowerCase();
		}
	}

	@Override
	public String getArtistName() {
		if(this.artistName != null) {
			return StringUtils.formatName(this.artistName);
		} 
		return null;
	}

	@Override
	public void setAlbumName(String albumName) {
		if(albumName != null) {
			this.albumName = albumName.toLowerCase();
		}
	}

	@Override
	public String getAlbumName() {
		if(this.albumName != null) {
			return StringUtils.formatName(this.albumName);
		}
		return null;
	}

	@Override
	public void setGenre(String genre) {
		if(genre != null) {
			this.genre = genre.toLowerCase();
		}
	}

	@Override
	public String getGenre() {
		if(this.genre != null) {
			return StringUtils.formatName(this.genre);
		}
		return null;
	}

	@Override
	public int getTrackNumber() {
		return this.trackNumber;
	}

	@Override
	public void setTrackNumber(int i) {
		this.trackNumber = i;
	}
	
}