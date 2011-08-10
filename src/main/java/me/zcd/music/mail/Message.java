/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.mail;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author mikehershey
 */
public class Message {
	
	private Set<String> to = new HashSet<String>();
	private String subject;
	private String body;
	private String sender;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Set<String> getTo() {
		return to;
	}

	public void setTo(Set<String> to) {
		this.to = to;
	}
	
	public void addTo(String to) {
		this.to.add(to);
	}
	
}
