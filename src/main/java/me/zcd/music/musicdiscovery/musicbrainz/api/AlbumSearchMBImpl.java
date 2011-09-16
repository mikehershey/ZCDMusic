package me.zcd.music.musicdiscovery.musicbrainz.api;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.zcd.leetml.logging.Log;
import me.zcd.leetml.logging.LogFactory;
import me.zcd.music.musicdiscovery.api.AlbumSearch;
import me.zcd.music.musicdiscovery.api.resources.AlbumSearchResult;
import me.zcd.music.musicdiscovery.musicbrainz.MusicBrainzSettings;
import me.zcd.music.musicdiscovery.musicbrainz.resources.ReleaseGroupResult;
import me.zcd.music.musicdiscovery.musicbrainz.resources.ReleaseResult;
import me.zcd.music.utils.URLFetch;
import org.antlr.stringtemplate.StringTemplate;
import org.apache.commons.digester3.Digester;

/**
 * 
 * @author mikehershey
 */
public class AlbumSearchMBImpl implements AlbumSearch {

	private static final Log log = LogFactory.getLogger(AlbumSearchMBImpl.class);
	//http://musicbrainz.org/ws/2/artist/8d3ee4ba-be21-470c-bb7c-4c124c3eb989?inc=release-groups&limit=100
	private static final String urlTemplate = MusicBrainzSettings.SERVER_URL + MusicBrainzSettings.API_URL + "artist/$artistApiId$?inc=release-groups&limit=100&type=album%7Csingle%7Cep%7Ccompilation%7Csoundtrack%7Clive";
	
	//http://musicbrainz.org/ws/2/release-group/2e3b231b-ab9f-4d85-950a-48708a1cc2fb?inc=releases
	private static final String releaseGroupTemplate = MusicBrainzSettings.SERVER_URL + MusicBrainzSettings.API_URL + "release-group/$releaseGroupApiId$?inc=releases&limit=100";

	public List<ReleaseGroupResult> findAllReleaseGroupsByArtist(String artistApiId) {
		StringTemplate query = new StringTemplate(urlTemplate);
		try {
			artistApiId = URLEncoder.encode(artistApiId, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
		}
		query.setAttribute("artistApiId", artistApiId);
		String xmlResult = URLFetch.getUrl(query.toString());
		List<ReleaseGroupResult> results = buildResultFromXml(xmlResult);

		return results;
	}

	private List<ReleaseGroupResult> buildResultFromXml(String xmlResult) {
		InputStream is = new ByteArrayInputStream(xmlResult.getBytes());
		Digester digester = new Digester();
		digester.addObjectCreate("*/release-group-list", ArrayList.class);
		digester.addObjectCreate("*/release-group-list/release-group", ReleaseGroupResult.class);
		digester.addSetProperties("*/release-group-list/release-group", "id", "apiId");
		digester.addSetProperties("*/release-group-list/release-group", "type", "type");
		digester.addCallMethod("*/release-group-list/release-group/title", "setName", 0);
		digester.addCallMethod("*/release-group-list/release-group/first-release-date", "setReleaseDate", 0);
		digester.addSetNext("*/release-group-list/release-group", "add");

		List<ReleaseGroupResult> releaseGroupResults = null;
		try {
			releaseGroupResults = digester.parse(is);
		} catch (Exception e) {
			log.error("Error parsing xml", e);
		}
		return releaseGroupResults;
	}

	@Override
	public List<AlbumSearchResult> findAllAlbumsByArtist(String artistApiId) {
		List<ReleaseGroupResult> results = findAllReleaseGroupsByArtist(artistApiId);
		List<AlbumSearchResult> ret = new ArrayList<AlbumSearchResult>();

		for (ReleaseGroupResult rgr : results) {
			AlbumSearchResult asr = new AlbumSearchResult();
			asr.setApiId(findReleaseIdFromReleaseGroup(rgr.getApiId()));
			asr.setName(rgr.getName());
			asr.setReleaseDate(rgr.getReleaseDate());
			asr.setType(rgr.getType());
			ret.add(asr);
		}

		return ret;
	}

	private String findReleaseIdFromReleaseGroup(String releaseGroupApiId) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException ex) {
			Logger.getLogger(AlbumSearchMBImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		StringTemplate query = new StringTemplate(releaseGroupTemplate);
		try {
			releaseGroupApiId = URLEncoder.encode(releaseGroupApiId, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
		}
		query.setAttribute("releaseGroupApiId", releaseGroupApiId);
		String xmlResult = URLFetch.getUrl(query.toString());
		
		InputStream is = new ByteArrayInputStream(xmlResult.getBytes());
		Digester digester = new Digester();
		digester.addObjectCreate("*/release-list", ArrayList.class);
		digester.addObjectCreate("*/release-list/release", ReleaseResult.class);
		digester.addSetProperties("*/release-list/release", "id", "apiId");
		digester.addCallMethod("*/release-list/release/country", "setCountry", 0);
		digester.addSetNext("*/release-list/release", "add");

		List<ReleaseResult> releaseResults = null;
		try {
			releaseResults = digester.parse(is);
		} catch (Exception e) {
			log.error("Error parsing xml", e);
		}

		ReleaseResult bestMatch = null;
		for(ReleaseResult rr : releaseResults) {
			if("US".equals(rr.getCountry())) {
				bestMatch = rr;
			}
		}
		
		if(bestMatch == null) {
			bestMatch = releaseResults.get(0);
		}
		
		return bestMatch.getApiId();
	}
}
