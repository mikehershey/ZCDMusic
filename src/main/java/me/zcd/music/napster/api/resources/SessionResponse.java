package me.zcd.music.napster.api.resources;

import java.util.Calendar;
import java.util.Date;

/**
 * When requesting a session this is probably all we care about in the response.
 * @author mikehershey
 *
 */
public class SessionResponse {

	private Session session;
	
	private Date createStamp = new Date();
	
	public static class Session {
		
		private String sessionKey;
		
		private int minutesUntilExpiry;
		

		public void setSessionKey(String sessionKey) {
			this.sessionKey = sessionKey;
		}

		public String getSessionKey() {
			return sessionKey;
		}

		public void setMinutesUntilExpiry(int minutesUntilExpiry) {
			this.minutesUntilExpiry = minutesUntilExpiry;
		}

		public int getMinutesUntilExpiry() {
			return minutesUntilExpiry;
		}
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Session getSession() {
		return session;
	}
	
	public boolean isValid() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.getCreateStamp());
		cal.add(Calendar.MINUTE, this.getSession().getMinutesUntilExpiry());
		return new Date().compareTo(cal.getTime()) <= 0;
	}

	public void setCreateStamp(Date createStamp) {
		this.createStamp = createStamp;
	}

	public Date getCreateStamp() {
		return createStamp;
	}
	
}
