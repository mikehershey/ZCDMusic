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
import me.zcd.music.model.db.UserLibrary;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.HashSet;
import java.util.Set;

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
	
	@Persistent
	private Set<String> trackKeys;

	public UserLibraryGaeImpl(String emailAddress) {
		this.key = KeyFactory.createKey(UserLibraryGaeImpl.class.getSimpleName(), emailAddress);
		this.emailAddress = emailAddress;
		this.trackKeys = new HashSet<String>();
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	@Override
	public Set<String> getTrackKeys() {
		return trackKeys;
	}

	@Override
	public void setTrackKeys(Set<String> trackKeys) {
		this.trackKeys = trackKeys;
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
	public void addTrackKey(String trackKey) {
		this.trackKeys.add(trackKey);
	}
	
}
