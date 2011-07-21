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

import java.util.ArrayList;
import java.util.List;

/**
 * Making the (false) assumption that albums all have multiple tracks.
 * 
 * EDIT: assumption failed on my first test case, now handles single or multiple tracks.
 * @author mikehershey
 *
 */
public class TrackResponseHolder {

	public interface TrackResponse {
		public Album getAlbumInfo();
	}
	
	public interface Album {
		public long getId();
		public String getName();
		public long getArtistId();
		public String getArtistName();
		public String getLabel();
		public int getTotalTracks();
		public List<Track> getTrack();
	}
	
	public static class TrackResponseMultiple implements TrackResponse {
		
		private AlbumMultipleTracks album;
		
		public Album getAlbumInfo() {
			return this.album;
		}
		
		public static class AlbumMultipleTracks implements Album {
			
			private long id;
			private String name;
			private long artistId;
			private String artistName;
			private String label;
			private int totalTracks;
			private List<Track> track;
	
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
	
			public void setArtistId(long artistId) {
				this.artistId = artistId;
			}
	
			public long getArtistId() {
				return artistId;
			}
	
			public void setArtistName(String artistName) {
				this.artistName = artistName;
			}
	
			public String getArtistName() {
				return artistName;
			}
	
			public void setLabel(String label) {
				this.label = label;
			}
	
			public String getLabel() {
				return label;
			}
	
			public void setTotalTracks(int totalTracks) {
				this.totalTracks = totalTracks;
			}
	
			public int getTotalTracks() {
				return totalTracks;
			}
	
			public void setTrack(List<Track> track) {
				this.track = track;
			}
	
			public List<Track> getTrack() {
				return track;
			}
			
		}
	}
	
	public static class TrackResponseSingle implements TrackResponse {
		
		private AlbumSingleTracks album;
		
		public Album getAlbumInfo() {
			return this.album;
		}
		
		public static class AlbumSingleTracks implements Album {
			
			private long id;
			private String name;
			private long artistId;
			private String artistName;
			private String label;
			private int totalTracks;
			private Track track;
	
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
	
			public void setArtistId(long artistId) {
				this.artistId = artistId;
			}
	
			public long getArtistId() {
				return artistId;
			}
	
			public void setArtistName(String artistName) {
				this.artistName = artistName;
			}
	
			public String getArtistName() {
				return artistName;
			}
	
			public void setLabel(String label) {
				this.label = label;
			}
	
			public String getLabel() {
				return label;
			}
	
			public void setTotalTracks(int totalTracks) {
				this.totalTracks = totalTracks;
			}
	
			public int getTotalTracks() {
				return totalTracks;
			}
	
			public List<Track> getTrack() {
				List<Track> ret = new ArrayList<Track>();
				ret.add(track);
				return ret;
			}
			
		}
	}
	
	public static class Track {
		
		private long id;
		private String trackName;
		private String duration;
		private String genre;
		private long artistId;
		private long albumId;
		
		public void setId(long id) {
			this.id = id;
		}
		
		public long getId() {
			return id;
		}
		
		public void setTrackName(String trackName) {
			this.trackName = trackName;
		}
		
		public String getTrackName() {
			return trackName;
		}
		
		public void setDuration(String duration) {
			this.duration = duration;
		}
		
		public String getDuration() {
			return duration;
		}
		
		public void setGenre(String genre) {
			this.genre = genre;
		}
		
		public String getGenre() {
			return genre;
		}

		public void setArtistId(long artistId) {
			this.artistId = artistId;
		}

		public long getArtistId() {
			return artistId;
		}

		public void setAlbumId(long albumId) {
			this.albumId = albumId;
		}

		public long getAlbumId() {
			return albumId;
		}
		
	}
	
}
