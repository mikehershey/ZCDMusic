package me.zcd.music.musicdiscovery.musicbrainz.api;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import me.zcd.leetml.logging.Log;
import me.zcd.leetml.logging.LogFactory;
import me.zcd.music.musicdiscovery.api.TrackSearch;
import me.zcd.music.musicdiscovery.api.resources.TrackSearchResult;
import me.zcd.music.musicdiscovery.api.resources.TrackSearchResults;
import me.zcd.music.musicdiscovery.musicbrainz.MusicBrainzSettings;
import me.zcd.music.utils.URLFetch;
import org.antlr.stringtemplate.StringTemplate;
import org.apache.commons.digester3.Digester;

/**
 *
 * @author mikehershey
 */
public class TrackSearchMBImpl implements TrackSearch {
	
	private static final Log log = LogFactory.getLogger(TrackSearchMBImpl.class); 

	//http://musicbrainz.org/ws/2/release/45641120-9137-3d9b-bb47-8915b1541a3d?inc=recordings
	private static final String urlTemplate = MusicBrainzSettings.SERVER_URL + MusicBrainzSettings.API_URL + "release/$albumApiId$?inc=recordings";
	
	@Override
	public TrackSearchResults findTracksByAlbum(String albumApiId) {
		StringTemplate query = new StringTemplate(urlTemplate);
		try {
			albumApiId = URLEncoder.encode(albumApiId, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
		}
        query.setAttribute("albumApiId", albumApiId);
		String xmlResult = URLFetch.getUrl(query.toString());
		return buildResultFromXml(xmlResult);
	}
	
	private TrackSearchResults buildResultFromXml(String xmlResult) {
		log.info(xmlResult);
		InputStream is = new ByteArrayInputStream(xmlResult.getBytes());
		Digester digester = new Digester();
		digester.addObjectCreate("*/medium-list", TrackSearchResults.class);
		digester.addObjectCreate("*/track", TrackSearchResult.class);
		digester.addSetProperties("*/track/recording", "id", "apiId");
		digester.addCallMethod("*/track/recording/title", "setName",0);
		digester.addCallMethod("*/track/position", "setTrackNumber",0);
		digester.addSetNext( "*/track", "addTrackSearchResult");
		
		TrackSearchResults trackSearchResults = null;
		try {
			trackSearchResults = digester.parse(is);
		} catch (Exception e) {
			
		}
		return trackSearchResults;
	}
	
	public static void main(String[] argv) {
		TrackSearch trackSearch = new TrackSearchMBImpl();
		
		TrackSearchResults trackSearchResults = trackSearch.findTracksByAlbum("45641120-9137-3d9b-bb47-8915b1541a3d");
		for(TrackSearchResult tsr : trackSearchResults.getTrackSearchResults()) {
			System.out.println(tsr.getName());
			System.out.println(tsr.getApiId());
			System.out.println(tsr.getTrackNumber());
			System.out.println("====================");
			
		}
	}
	
}