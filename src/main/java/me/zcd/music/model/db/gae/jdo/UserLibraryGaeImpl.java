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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.HashMap;
import me.zcd.music.model.db.UserLibrary;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import me.zcd.music.model.UserLibraryTrack;
import me.zcd.music.model.db.Track;

/**
 *
 * @author mikehershey
 */
@PersistenceCapable(detachable="true")
public class UserLibraryGaeImpl implements UserLibrary {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String emailAddress;
	
	@Persistent(serialized = "true", defaultFetchGroup="true")
	private Map<String, UserLibraryTrack> tracks;

	public UserLibraryGaeImpl(String emailAddress) {
		this.key = KeyFactory.createKey(UserLibraryGaeImpl.class.getSimpleName(), emailAddress);
		this.emailAddress = emailAddress;
		this.tracks = new HashMap<String, UserLibraryTrack>();
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	@Override
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Override
	public String getEmailAddress() {
		return this.emailAddress;
	}

	@Override
	public Map<String, UserLibraryTrack> getIndexedTracks() {
		return this.tracks;
	}

	@Override
	public void addTrack(UserLibraryTrack trackKey) {
		this.tracks.put(trackKey.getTrackKey(), trackKey);
	}

	@Override
	public void setIndexedTracks(Map<String, UserLibraryTrack> trackKeys) {
		this.tracks = trackKeys;
	}

	@Override
	public Set<UserLibraryTrack> getTracks() {
		return new HashSet(this.tracks.values());
	}

	@Override
	public void setTracks(Set<UserLibraryTrack> trackKeys) {
		this.tracks = new HashMap<String, UserLibraryTrack>();
		for(UserLibraryTrack t : trackKeys) {
			this.tracks.put(t.getTrackKey(), t);
		}
	}
	
}
