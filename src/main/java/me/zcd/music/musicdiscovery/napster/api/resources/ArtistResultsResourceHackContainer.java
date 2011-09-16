/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.musicdiscovery.napster.api.resources;

import java.util.ArrayList;
import java.util.List;

/**
 * List of artists returned by artist search.
 * 
 * Napster and google both screwed up here, gson should
 * use the bean convension so i could easily handle napsters inconsistent types, but
 * why is napster given inconsitent types.... anyway hack fix all in this one file.
 * 
 * @author mikehershey
 *
 */
public class ArtistResultsResourceHackContainer {

	public interface ArtistResultsResource {
		public SearchResults getSearchResults();
	}
	
	public interface SearchResults {
		public List<Artist> getArtist();
		public int getNumberOfResults();
	}
	
	public static class ArtistResultsResourceMultipleResults implements ArtistResultsResource{
		
		private SearchResultsMultipleResults searchResults;

		public SearchResults getSearchResults() {
			return searchResults;
		}
		
		public static class SearchResultsMultipleResults implements SearchResults {
			
			private List<Artist> artist;
			private int numberOfResults;
		
			public void setArtist(List<Artist> artist) {
				this.artist = artist;
			}

			public List<Artist> getArtist() {
				return artist;
			}

			public void setNumberOfResults(int numberOfResults) {
				this.numberOfResults = numberOfResults;
			}

			public int getNumberOfResults() {
				return numberOfResults;
			}
			
		}
		
	}
	
public static class ArtistResultsResourceSingleResult implements ArtistResultsResource{
		
		private SearchResultsSingleResult searchResults;

		public SearchResults getSearchResults() {
			return searchResults;
		}
		
		public static class SearchResultsSingleResult implements SearchResults {
			
			private Artist artist;
			private int numberOfResults;

			public List<Artist> getArtist() {
				ArrayList<Artist> ret = new ArrayList<Artist>();
				ret.add(artist);
				return ret;
			}

			public void setNumberOfResults(int numberOfResults) {
				this.numberOfResults = numberOfResults;
			}

			public int getNumberOfResults() {
				return numberOfResults;
			}
			
		}
		
	}
		
	public static class Artist {
		private long id;
		private String name;
		private String restArtistURL;
		private String artistPhotoURL;
		
		public void setId(long id) {
			this.id = id;
		}
		
		public long getId() {
			return id;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public void setRestArtistURL(String restArtistURL) {
			this.restArtistURL = restArtistURL;
		}
		
		public String getRestArtistURL() {
			return restArtistURL;
		}

		public void setArtistPhotoURL(String artistPhotoURL) {
			this.artistPhotoURL = artistPhotoURL;
		}

		public String getArtistPhotoURL() {
			return artistPhotoURL;
		}
		
	}
	
}
