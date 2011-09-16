/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.musicdiscovery.napster.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import me.zcd.leetml.logging.Log;
import me.zcd.leetml.logging.LogFactory;
import me.zcd.music.musicdiscovery.napster.NapsterSettings;
import me.zcd.music.musicdiscovery.napster.api.resources.ArtistResultsResourceHackContainer.Artist;
import me.zcd.music.musicdiscovery.napster.api.resources.ArtistResultsResourceHackContainer.ArtistResultsResource;
import me.zcd.music.musicdiscovery.napster.api.resources.ArtistResultsResourceHackContainer.ArtistResultsResourceMultipleResults;
import me.zcd.music.musicdiscovery.napster.api.resources.ArtistResultsResourceHackContainer.ArtistResultsResourceSingleResult;
import me.zcd.music.utils.JsonLoader;

public class ArtistSearch {

	private static final Log log = LogFactory.getLogger(ArtistSearch.class);
	
	/**
	 * Takes a URL unescaped artist name, escapes it then uses
	 * the napster API to find closest matches.
	 * @param name the band name to search for
	 * @return The closest results.
	 */
	public List<String> findArtistNames(String name) {
		try {
			name = URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("UTF-8 encoding not supported by URLencoder!!!!");
			return null;
		}
		//https://api.napster.com:8443/rest/1.1/search/artists?searchTerm=
		StringBuilder requestUrl = new StringBuilder();
		requestUrl.append(NapsterSettings.API_URL_BASE);
		requestUrl.append("search/artists?searchTerm=");
		requestUrl.append(name);
		requestUrl.append("&format=json");
		JsonLoader<ArtistResultsResource> loader = new JsonLoader<ArtistResultsResource>();
		String content = Session.getSession().getAuthenticatedUrl(requestUrl.toString());
		ArtistResultsResource result = null;
		if(content.contains("[")) {
			//handle array
			 result = loader.parseJson(content, ArtistResultsResourceMultipleResults.class);
		} else {
			//handle single result. Not sure who i hate more napster for giving inconsitent types
			//or gson for not using the bean convention.
			result = loader.parseJson(content, ArtistResultsResourceSingleResult.class);
		}
		if(result != null && result.getSearchResults() != null && result.getSearchResults().getArtist() != null) {
			List<String> ret = new ArrayList<String>();
			for(Artist a : result.getSearchResults().getArtist()) {
				ret.add(a.getName());
			}
			return ret;
		} else {
			return null;
		}
	}
	
	/**
	 * Takes a URL unescaped artist name, escapes it then uses
	 * the napster API to find closest matches.
	 * @param name the band name to search for
	 * @return The reponse object given by napster.
	 */
	public ArtistResultsResource findArtists(String name) {
		try {
			name = URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("UTF-8 encoding not supported by URLencoder!!!!");
			return null;
		}
		//https://api.napster.com:8443/rest/1.1/search/artists?searchTerm=
		StringBuilder requestUrl = new StringBuilder();
		requestUrl.append(NapsterSettings.API_URL_BASE);
		requestUrl.append("search/artists?searchTerm=");
		requestUrl.append(name);
		requestUrl.append("&format=json");
		JsonLoader<ArtistResultsResource> loader = new JsonLoader<ArtistResultsResource>();
		String content = Session.getSession().getAuthenticatedUrl(requestUrl.toString());
		ArtistResultsResource result = null;
		if(content.contains("[")) {
			//handle array
			 result = loader.parseJson(content, ArtistResultsResourceMultipleResults.class);
		} else {
			//handle single result. Not sure who i hate more napster for giving inconsitent types
			//or gson for not using the bean convention.
			result = loader.parseJson(content, ArtistResultsResourceSingleResult.class);
		}
		if(result != null) {
			return result;
		} else {
			return null;
		}
	}
	
	/**
	 * For testing only...
	 * @param argv
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public static void main(String[] argv) throws IllegalStateException, IOException {
		List<String> artists = new ArtistSearch().findArtistNames("troy");
		for(String artist : artists) {
			System.out.println(artist);
		}
		ArtistResultsResource artistResults = new ArtistSearch().findArtists("the fall of troy");
		String url = artistResults.getSearchResults().getArtist().get(0).getArtistPhotoURL();
		byte[] data = Session.getSession().getAuthenticatedUrlData(url);
	}
	
}
