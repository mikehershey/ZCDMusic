package me.zcd.music.model.db.gae.jdo;

import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import me.zcd.music.model.db.Artist;
import me.zcd.music.utils.StringUtils;

@PersistenceCapable(detachable = "true")
public class GaeArtistImpl implements Artist {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String name;
	
	@Persistent
	private List<String> trackKeys;
	
	@Persistent
	private List<String> albumKeys;
	
	@Persistent 
	private String artistArtUrl;
	
	@Persistent
	private String genre;

	@Override
	public String getKey() {
		return this.key.getName();
	}

	@Override
	public void setKey(String key) {
		this.key = KeyFactory.createKey(GaeArtistImpl.class.getSimpleName(), key.toLowerCase());
	}

	
	@Override
	public void setTrackKeys(List<String> trackKeys) {
		this.trackKeys = trackKeys;
	}

	@Override
	public List<String> getTrackKeys() {
		return this.trackKeys;
	}

	@Override
	public void setAlbumKeys(List<String> albumKeys) {
		this.albumKeys = albumKeys;
	}

	@Override
	public List<String> getAlbumKeys() {
		return this.albumKeys;
	}

	@Override
	public void addAlbumKey(String albumKey) {
		this.albumKeys.add(albumKey);
	}

	@Override
	public void addTrackKey(String trackKey) {
		this.trackKeys.add(trackKey);
	}

	@Override
	public void setArtistArtKey(String artistArt) {
		this.artistArtUrl = artistArt;				
	}

	@Override
	public String getArtistArtKey() {
		return this.artistArtUrl;
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
	public void setName(String name) {
		if(name != null) {
			this.name = name.toLowerCase();
		}
	}

	@Override
	public String getName() {
		if(this.name != null) {
			return StringUtils.formatName(this.name);
		}
		return null;
	}
 
}
