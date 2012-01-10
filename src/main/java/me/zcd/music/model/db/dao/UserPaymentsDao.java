package me.zcd.music.model.db.dao;

import me.zcd.music.model.db.UserPayments;

/**
 * Convientent functions to manipulate userPayment objects
 * @author mikehershey
 */
public interface UserPaymentsDao {
	
	public UserPayments getUserPayments(String email);
	public UserPayments incrementTrackCount(String trackKey, String emailAddress);
	
}
