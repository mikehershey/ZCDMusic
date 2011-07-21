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

import me.zcd.music.napster.NapsterSettings;
import me.zcd.music.napster.api.resources.AlbumResponseHackContainer.AlbumResponse;
import me.zcd.music.napster.api.resources.AlbumResponseHackContainer.AlbumResponseMultipleResponses;
import me.zcd.music.napster.api.resources.AlbumResponseHackContainer.AlbumResponseSingleResponse;
import me.zcd.music.napster.api.resources.ArtistResultsResourceHackContainer.Artist;
import me.zcd.music.utils.JsonLoader;
import me.zcd.music.utils.StringUtils;

public class ArtistAlbums {

	public AlbumResponse findAllAlbumsByArtistName(String name) {
		ArtistSearch search = new ArtistSearch();
		Artist artist = null;
		try {
			artist = search.findArtists(name).getSearchResults().getArtist().get(0);
			if(artist == null) {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
		return this.findAllAlbumsByArtistId(artist.getId());
	}
	
	public AlbumResponse findAllAlbumsByArtistId(Long artistId) {
		//https://api.napster.com:8443/rest/1.1/search/artists?searchTerm=
		StringBuilder requestUrl = new StringBuilder();
		requestUrl.append(NapsterSettings.API_URL_BASE);
		requestUrl.append("albums/artist/");
		requestUrl.append(artistId);
		requestUrl.append("?format=json&countryCode=US&maxResults=100");
		String content = Session.getSession().getAuthenticatedUrl(requestUrl.toString());
		JsonLoader<AlbumResponse> loader = new JsonLoader<AlbumResponse>();
		AlbumResponse response = null;
		if(StringUtils.stripQuoted(content).contains("[")) {
			response = loader.parseJson(content, AlbumResponseMultipleResponses.class);
		} else {
			response = loader.parseJson(content, AlbumResponseSingleResponse.class);
		}
		return response;
	}
	
	public static void main(String[] argv) {
		new ArtistAlbums().findAllAlbumsByArtistId(12174372l);
		//AlbumResponse response = new ArtistAlbums().findAllAlbumsByArtistName("pink floyd");
	}
	
}
