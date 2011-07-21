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

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.logging.Logger;
import me.zcd.music.Settings;
import me.zcd.music.model.db.Album;
import me.zcd.music.utils.StringUtils;

@PersistenceCapable(detachable = "true")
public class GaeAlbumImpl implements Album {
	
	private static Logger log = Logger.getLogger(GaeAlbumImpl.class.getName());

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String name;
	
	@Persistent
	private String artistName;
	
	@Persistent
	private Date releaseDate;
	
	@Persistent
	private List<String> trackKeys;
	
	@Persistent
	private String albumArtKey;

	@Override
	public String getKey() {
		return this.key.getName();
	}
	
	@Override
	public void setKey(String key) {
		this.key = KeyFactory.createKey(GaeAlbumImpl.class.getSimpleName(), key.toLowerCase());
	}

	@Override
	public String getName() {
		if(this.name != null) {
			return StringUtils.formatName(this.name);
		}
		return null;
	}

	@Override
	public void setArtistName(String name) {
		if(name != null) {
			this.artistName = name.toLowerCase();
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
	public String getArtistKey() {
		return this.getKey().split(Settings.KEY_SEPERATOR)[0];
	}

	@Override
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	@Override
	public Date getReleaseDate() {
		return this.releaseDate;
	}

	@Override
	public void setTrackKeys(List<String> tracks) {
		this.trackKeys = tracks;
	}

	@Override
	public List<String> getTrackKeys() {
		return this.trackKeys;
	}

	@Override
	public void setAlbumArtKey(String albumArtKey) {
		this.albumArtKey = albumArtKey;
	}

	@Override
	public String getAlbumArtKey() {
		return this.albumArtKey;
	}

	@Override
	public void setName(String name) {
		if(name != null) {
			this.name = name.toLowerCase();
		}
	}

	@Override
	public void addTrackKey(String key) {
		this.trackKeys.add(key);
	}
	
}
