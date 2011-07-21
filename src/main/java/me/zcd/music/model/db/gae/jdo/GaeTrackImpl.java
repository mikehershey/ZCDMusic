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
import me.zcd.music.Settings;
import me.zcd.music.model.db.Track;
import me.zcd.music.utils.StringUtils;

/**
 * @author mikehershey
 *
 */
@PersistenceCapable(detachable="true")
public class GaeTrackImpl implements Track {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String title;
	
	@Persistent
	private String youtubeLocation;
	
	@Persistent
	private String genre;
	
	@Persistent
	private String artistName;
	
	@Persistent
	private String albumName;

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
	
}