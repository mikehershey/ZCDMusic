package me.zcd.music.musicdiscovery.musicbrainz.api;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import me.zcd.leetml.logging.Log;
import me.zcd.leetml.logging.LogFactory;
import me.zcd.music.musicdiscovery.api.ArtistSearch;
import me.zcd.music.musicdiscovery.api.resources.ArtistSearchResult;
import me.zcd.music.musicdiscovery.musicbrainz.MusicBrainzSettings;
import me.zcd.music.utils.URLFetch;
import org.antlr.stringtemplate.StringTemplate;
import org.apache.commons.digester3.Digester;

/**
 *
 * @author mikehershey
 */
public class ArtistSearchMBImpl implements ArtistSearch {
	
	private static final Log log = LogFactory.getLogger(ArtistSearchMBImpl.class); 
	
	/**
	 * String template of the url to request.
	 */
	private static final String urlTemplate = MusicBrainzSettings.SERVER_URL + MusicBrainzSettings.API_URL + "artist/?query=artist:$artist$&limit=$limit$&offset=0";

	@Override
	public ArtistSearchResult findArtist(String name) {
		List<ArtistSearchResult> ret = findArtist(name, 1);
		if(ret != null) {
			return ret.get(0);
		}
		return null;
	}
	
	private List<ArtistSearchResult> buildResultFromXml(String xmlResult) {
		InputStream is = new ByteArrayInputStream(xmlResult.getBytes());
		Digester digester = new Digester();
		digester.addObjectCreate("*/artist-list", ArrayList.class);
		digester.addObjectCreate("*/artist-list/artist", ArtistSearchResult.class);
		digester.addSetProperties("*/artist-list/artist", "id", "apiID");
		digester.addCallMethod("*/artist-list/artist/name", "setArtistName",0);
		digester.addCallMethod("*/artist-list/artist/disambiguation", "setDisambiguation",0);
		digester.addSetNext("*/artist-list/artist", "add");
		
		List<ArtistSearchResult> artistSearchResult = null;
		try {
			artistSearchResult = digester.parse(is);
		} catch (Exception e) {
			
		}
		return artistSearchResult;
	}
	
	public static void main(String[] argv) {
		ArtistSearch artistSearch = new ArtistSearchMBImpl();
		ArtistSearchResult artistSearchResult = artistSearch.findArtistByApiId("8d3ee4ba-be21-470c-bb7c-4c124c3eb989");
		System.out.println(artistSearchResult.getArtistName());
		System.out.println(artistSearchResult.getApiID());
		System.out.println(artistSearchResult.getDisambiguation());
	}

	@Override
	public List<ArtistSearchResult> findArtist(String name, int numResults) {
		StringTemplate query = new StringTemplate(urlTemplate);
		try {
			name = URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
		}
        query.setAttribute("artist", name);
		query.setAttribute("limit", numResults);
		String xmlResult = URLFetch.getUrl(query.toString());
		return buildResultFromXml(xmlResult);
	}

	//http://musicbrainz.org/ws/2/artist/8d3ee4ba-be21-470c-bb7c-4c124c3eb989
	private static final String idUrlTemplate =  MusicBrainzSettings.SERVER_URL + MusicBrainzSettings.API_URL + "artist/$artistApiId$";
	
	@Override
	public ArtistSearchResult findArtistByApiId(String artistApiId) {
		StringTemplate query = new StringTemplate(idUrlTemplate);
		try {
			artistApiId = URLEncoder.encode(artistApiId, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
		}
        query.setAttribute("artistApiId", artistApiId);
		String xmlResult = URLFetch.getUrl(query.toString());
		return getArtistFromXml(xmlResult);
	}
	
	private ArtistSearchResult getArtistFromXml(String xmlResult) {
		InputStream is = new ByteArrayInputStream(xmlResult.getBytes());
		Digester digester = new Digester();
		digester.addObjectCreate("*/artist", ArtistSearchResult.class);
		digester.addSetProperties("*/artist", "id", "apiID");
		digester.addCallMethod("*/artist/name", "setArtistName",0);
		digester.addCallMethod("*/artist/disambiguation", "setDisambiguation",0);
		
		ArtistSearchResult artistSearchResult = null;
		try {
			artistSearchResult = digester.parse(is);
		} catch (Exception e) {
			
		}
		return artistSearchResult;
	}
	
}
