/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.napster.api.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlbumResponseHackContainer {

	//{"albums":{"@version":"1.1","numberOfResults":5,"album":[,{"id":13023795,"name":"Phantom on the Horizon","albumResourceURL":"http:\/\/api.napster.com:8443\/rest\/1.1\/albums\/13023795?format=json&countryCode=US","albumArtURL":"http:\/\/api.napster.com:8443\/rest\/1.1\/images\/album\/13023795\/coverArt?format=json&countryCode=US","artistId":12005675,"artistName":"The Fall Of Troy","artistResourceURL":"http:\/\/api.napster.com:8443\/rest\/1.1\/artists\/12005675?format=json&countryCode=US","popularity":1,"releaseDate":"2008-12-16T00:00:00-08:00","creditPrice":5,"albumPrice":"$ 4.95","explicit":false,"genre":"Rock"},{"id":13066170,"name":"Manipulator","albumResourceURL":"http:\/\/api.napster.com:8443\/rest\/1.1\/albums\/13066170?format=json&countryCode=US","albumArtURL":"http:\/\/api.napster.com:8443\/rest\/1.1\/images\/album\/13066170\/coverArt?format=json&countryCode=US","artistId":12005675,"artistName":"The Fall Of Troy","artistResourceURL":"http:\/\/api.napster.com:8443\/rest\/1.1\/artists\/12005675?format=json&countryCode=US","popularity":2,"releaseDate":"2007-05-01T00:00:00-07:00","creditPrice":10,"albumPrice":"$ 9.95","explicit":false,"genre":"Rock"},{"id":13033691,"name":"Doppelganger","albumResourceURL":"http:\/\/api.napster.com:8443\/rest\/1.1\/albums\/13033691?format=json&countryCode=US","albumArtURL":"http:\/\/api.napster.com:8443\/rest\/1.1\/images\/album\/13033691\/coverArt?format=json&countryCode=US","artistId":12005675,"artistName":"The Fall Of Troy","artistResourceURL":"http:\/\/api.napster.com:8443\/rest\/1.1\/artists\/12005675?format=json&countryCode=US","popularity":36,"releaseDate":"2005-08-16T00:00:00-07:00","creditPrice":10,"albumPrice":"$ 9.95","explicit":false,"genre":"Rock"},{"id":13033703,"name":"The Fall of Troy","albumResourceURL":"http:\/\/api.napster.com:8443\/rest\/1.1\/albums\/13033703?format=json&countryCode=US","albumArtURL":"http:\/\/api.napster.com:8443\/rest\/1.1\/images\/album\/13033703\/coverArt?format=json&countryCode=US","artistId":12005675,"artistName":"The Fall Of Troy","artistResourceURL":"http:\/\/api.napster.com:8443\/rest\/1.1\/artists\/12005675?format=json&countryCode=US","popularity":7,"releaseDate":"2003-11-04T00:00:00-08:00","creditPrice":10,"albumPrice":"$ 9.95","explicit":false,"genre":"Rock"}]}}
	
	
	public interface AlbumResponse {
		public AlbumSearchResults getAlbums();
	}
	
	public interface AlbumSearchResults {
		
		public int getNumberOfResults();
		
		public List<Album> getAlbum();
		
	}
	
	public static class AlbumResponseMultipleResponses implements AlbumResponse {
	
		private AlbumSearchResultsMultipleResponses albums;

		public AlbumSearchResults getAlbums() {
			return albums;
		}
		
		public static class AlbumSearchResultsMultipleResponses implements AlbumSearchResults {
			
			private int numberOfResults;
			
			private List<Album> album;
	
			public int getNumberOfResults() {
				return numberOfResults;
			}
			
			public List<Album> getAlbum() {
				return album;
			}
			
		}

	}
	
	public static class AlbumResponseSingleResponse implements AlbumResponse {
		
		private AlbumSearchResultsSingleResponse albums;

		public AlbumSearchResults getAlbums() {
			return albums;
		}
		
		public static class AlbumSearchResultsSingleResponse implements AlbumSearchResults {
		
			private int numberOfResults;
			
			private Album album;
			
			public int getNumberOfResults() {
				return numberOfResults;
			}
	
			public List<Album> getAlbum() {
				List<Album> ret = new ArrayList<Album>();
				ret.add(album);
				return ret;
			}
		}
	}
	
	public static class Album { 
		
		private long id;
		private String name;
		private long artistId;
		private String artistName;
		private int popularity;
		private String genre;
		private String albumArtURL;
		private String releaseDate;
		
		public long getId() {
			return id;
		}
		
		public String getName() {
			return name;
		}
		
		public long getArtistId() {
			return artistId;
		}
		
		public String getArtistName() {
			return artistName;
		}
		
		public int getPopularity() {
			return popularity;
		}
		
		public String getGenre() {
			return genre;
		}

		public String getAlbumArtURL() {
			return albumArtURL.replace("http://", "https://");
		}
		
		public Date getReleaseDate() {
			if(this.releaseDate != null) {
				String date = this.releaseDate.split("T")[0];
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				try {
					return simpleDateFormat.parse(date);
				} catch (ParseException ex) {
					Logger.getLogger(AlbumResponseHackContainer.class.getName()).log(Level.SEVERE, null, ex);
					return null;
				}
			}
			return null;
		}
		
	}
	
}
