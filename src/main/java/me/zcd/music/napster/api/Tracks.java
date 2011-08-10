/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.napster.api;

import java.util.ArrayList;
import java.util.List;

import me.zcd.music.napster.NapsterSettings;
import me.zcd.music.napster.api.resources.AlbumResponseHackContainer.Album;
import me.zcd.music.napster.api.resources.AlbumResponseHackContainer.AlbumResponse;
import me.zcd.music.napster.api.resources.TrackResponseHolder.Track;
import me.zcd.music.napster.api.resources.TrackResponseHolder.TrackResponse;
import me.zcd.music.napster.api.resources.TrackResponseHolder.TrackResponseMultiple;
import me.zcd.music.napster.api.resources.TrackResponseHolder.TrackResponseSingle;
import me.zcd.music.utils.JsonLoader;
import me.zcd.music.utils.StringUtils;

/**
 * Gets all the tracks that an artist wrote.
 * Note that looking up artist by name makes 2 web requests use ID if you can.
 * @author mikehershey
 *
 */
public class Tracks {
	
	public List<TrackResponse> getTracksByArtistName(String name) {
		ArtistAlbums albumSearch = new ArtistAlbums();
		AlbumResponse albums = albumSearch.findAllAlbumsByArtistName(name);
		List<TrackResponse> ret = new ArrayList<TrackResponse>();
		for(Album a : albums.getAlbums().getAlbum()) {
			//make a request to get all the albums songs.
			StringBuilder requestUrl = new StringBuilder();
			requestUrl.append(NapsterSettings.API_URL_BASE);
			requestUrl.append("albums/");
			requestUrl.append(a.getId());
			requestUrl.append("?format=json&countryCode=US");
			String content = Session.getSession().getAuthenticatedUrl(requestUrl.toString());
			JsonLoader<TrackResponse> loader = new JsonLoader<TrackResponse>();
			TrackResponse resp;
			if(content.contains("[")) {
				resp = loader.parseJson(content, TrackResponseMultiple.class);
			} else {
				resp = loader.parseJson(content, TrackResponseSingle.class);
			}
			ret.add(resp);
		}
		return ret;
	}
	
	public TrackResponse getTracksByAlbumId(long albumId) {
		StringBuilder requestUrl = new StringBuilder();
		requestUrl.append(NapsterSettings.API_URL_BASE);
		requestUrl.append("albums/");
		requestUrl.append(albumId);
		requestUrl.append("?format=json&countryCode=US");
		String content = Session.getSession().getAuthenticatedUrl(requestUrl.toString());
		JsonLoader<TrackResponse> loader = new JsonLoader<TrackResponse>();
		TrackResponse resp;
		if(StringUtils.stripQuoted(content).contains("[")) {
			resp = loader.parseJson(content, TrackResponseMultiple.class);
		} else {
			resp = loader.parseJson(content, TrackResponseSingle.class);
		}
		return resp;
	}
	
	public static void main(String[] argv) {
		TrackResponse resp = new Tracks().getTracksByAlbumId(12744328l);
		for(Track t : resp.getAlbumInfo().getTrack()) {
			System.out.println(t.getTrackName());
		}
	}
	
	
}
