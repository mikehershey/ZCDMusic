package me.zcd.music.model.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Users can make monthly donations, the money is split evenly to each artist that
 * the user listened to based on number of listens. To facilitate this, this class
 * will store monthly listens, monthly donation amount, payment info. Once a month
 * I'll run a (hopefully) huge map reduce job across all user payments and match
 * processed payments to who gets the money. 
 * 
 * As of 12/15/11 I'm only coding in monthly top listens to make statistics mostly
 * complete, payment info will not be added till I'm ready for it. Monthly listens 
 * will be used in widgets for advertising and such. 
 * @author mikehershey
 */
public abstract class UserPayments {
	
	public abstract String getUserEmail();
	public abstract void setUserEmail(String email);
	
	//trackKey, Count
	public abstract Map<String, Long> getMonthlyListenRecords();
	
	/**
	 * Iterates over all songs a user has listened to
	 * @return A list of trackKeys of the most frequently listened to tracks 
	 * by this user. If they have not listened to N tracks return what they have; 
	 * null if they have 0 listens
	 */
	public List<String> getTopListens(int n) {
		Map<String,Long> listenRecords = this.getMonthlyListenRecords();
		if(listenRecords == null || listenRecords.isEmpty()) {
			return null;
		}
		List<String> ret = new ArrayList<String>(n);
		//build a sorted list of all listens in m log m
		SortedMap<Long, String> topPlays = new TreeMap<Long, String>(Collections.reverseOrder());
		for(Entry<String, Long> listenRecord : listenRecords.entrySet()) {
			System.out.println(listenRecord.getKey() + "," + listenRecord.getValue());
			topPlays.put(listenRecord.getValue(), listenRecord.getKey());
		}
		//find the first N
		int i = 0;
		for(Entry<Long, String> topPlay : topPlays.entrySet()) {
			if(i >= n) {
				break;
			}
			ret.add(topPlay.getValue());
			i++;
		}
		return ret;
	}
	
}
