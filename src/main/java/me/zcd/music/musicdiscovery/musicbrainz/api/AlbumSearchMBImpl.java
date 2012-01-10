package me.zcd.music.musicdiscovery.musicbrainz.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import me.zcd.leetml.logging.Log;
import me.zcd.leetml.logging.LogFactory;
import me.zcd.music.musicdiscovery.api.AlbumSearch;
import me.zcd.music.musicdiscovery.api.resources.AlbumSearchResult;
import me.zcd.music.musicdiscovery.musicbrainz.MusicBrainzSettings;
import me.zcd.music.musicdiscovery.musicbrainz.resources.ReleaseGroupResult;
import me.zcd.music.musicdiscovery.musicbrainz.resources.ReleaseGroupResultsHolder;
import me.zcd.music.musicdiscovery.musicbrainz.resources.ReleaseResult;
import org.antlr.stringtemplate.StringTemplate;
import org.apache.commons.digester3.Digester;

/**
 * 
 * @author mikehershey
 */
public class AlbumSearchMBImpl implements AlbumSearch {

	private static final Log log = LogFactory.getLogger(AlbumSearchMBImpl.class);
	
	//http://musicbrainz.org/ws/2/artist/8d3ee4ba-be21-470c-bb7c-4c124c3eb989?inc=release-groups&limit=100
	private static final String urlTemplate = MusicBrainzSettings.SERVER_URL + MusicBrainzSettings.API_URL + "release-group?artist=$artistApiId$&limit=100&type=album%7Csingle%7Cep%7Ccompilation%7Csoundtrack&offset=$offset$";
	
	//http://musicbrainz.org/ws/2/release-group/2e3b231b-ab9f-4d85-950a-48708a1cc2fb?inc=releases
	private static final String releaseGroupTemplate = MusicBrainzSettings.SERVER_URL + MusicBrainzSettings.API_URL + "release-group/$releaseGroupApiId$?inc=releases&limit=100";

	public List<ReleaseGroupResult> findAllReleaseGroupsByArtist(String artistApiId) {
		StringTemplate query = new StringTemplate(urlTemplate);
		try {
			artistApiId = URLEncoder.encode(artistApiId, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
		}
		query.setAttribute("artistApiId", artistApiId);
		query.setAttribute("offset", "0");
		ReleaseGroupResultsHolder results = buildResultFromXml(query.toString()); //load page 1
		for(int i = 1; i*100 < results.getCount(); i++) {	
			results.addAll(getResultsForPage(artistApiId, i));
		}
		return results;
	}

	private ReleaseGroupResultsHolder buildResultFromXml(String url) {
		Digester digester = new Digester();
		digester.addObjectCreate("*/release-group-list", ReleaseGroupResultsHolder.class);
		digester.addSetProperties("*/release-group-list", "count", "count");
		digester.addObjectCreate("*/release-group-list/release-group", ReleaseGroupResult.class);
		digester.addSetProperties("*/release-group-list/release-group", "id", "apiId");
		digester.addSetProperties("*/release-group-list/release-group", "type", "type");
		digester.addCallMethod("*/release-group-list/release-group/title", "setName", 0);
		digester.addCallMethod("*/release-group-list/release-group/first-release-date", "setReleaseDate", 0);
		digester.addSetNext("*/release-group-list/release-group", "add");

		List<ReleaseGroupResult> releaseGroupResults = null;
		for(int i = 0; i < 5; i++) {
			try {
				releaseGroupResults = digester.parse(url);
				break;
			} catch (Exception e) {
				log.error("Error parsing xml", e);
			}
		}
		return (ReleaseGroupResultsHolder) releaseGroupResults;
	}
	
	private List<ReleaseGroupResult> getResultsForPage(String artistApiId, int page) {
		int offset = page * 100;
		StringTemplate query = new StringTemplate(urlTemplate);
		try {
			artistApiId = URLEncoder.encode(artistApiId, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
		}
		query.setAttribute("artistApiId", artistApiId);
		query.setAttribute("offset", offset);
		List<ReleaseGroupResult> results = buildResultFromXml(query.toString());
		return results;
	}

	@Override
	public List<AlbumSearchResult> findAllAlbumsByArtist(String artistApiId) {
		List<ReleaseGroupResult> results = findAllReleaseGroupsByArtist(artistApiId);
		List<AlbumSearchResult> ret = new ArrayList<AlbumSearchResult>();

		for (ReleaseGroupResult rgr : results) {
			AlbumSearchResult asr = new AlbumSearchResult();
			asr.setApiId(findReleaseIdFromReleaseGroup(rgr.getApiId()));
			if(asr.getApiId() == null) {
				continue;
			}
			asr.setName(rgr.getName());
			asr.setReleaseDate(rgr.getReleaseDate());
			asr.setType(rgr.getType());
			ret.add(asr);
		}

		return ret;
	}
	
	private String findReleaseIdFromReleaseGroup(String releaseGroupApiId) {
		try {
			Thread.sleep(MusicBrainzSettings.WAIT_TIME);
		} catch (InterruptedException ex) {
			log.error("Interupted exception", ex);
		}
		StringTemplate query = new StringTemplate(releaseGroupTemplate);
		try {
			releaseGroupApiId = URLEncoder.encode(releaseGroupApiId, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
		}
		query.setAttribute("releaseGroupApiId", releaseGroupApiId);
		
		Digester digester = new Digester();
		digester.addObjectCreate("*/release-list", ArrayList.class);
		digester.addObjectCreate("*/release-list/release", ReleaseResult.class);
		digester.addSetProperties("*/release-list/release", "id", "apiId");
		digester.addCallMethod("*/release-list/release/country", "setCountry", 0);
		digester.addSetNext("*/release-list/release", "add");

		List<ReleaseResult> releaseResults = null;
		for(int i = 0; i < 5; i++) {
			try {
				releaseResults = digester.parse(query.toString());
				break;
			} catch (Exception e) {
				log.error("Error parsing xml", e);
			}
		}

		ReleaseResult bestMatch = null;
		for(ReleaseResult rr : releaseResults) {
			if("US".equals(rr.getCountry())) {
				bestMatch = rr;
			}
		}
		
		if(bestMatch == null) {
			if(releaseResults.size() <= 0) {
				return null;
			}
			bestMatch = releaseResults.get(0);
		}
		
		return bestMatch.getApiId();
	}
	
	public static void main(String[] argv) {
		AlbumSearchMBImpl test = new AlbumSearchMBImpl();
		List<AlbumSearchResult> results = test.findAllAlbumsByArtist("01809552-4f87-45b0-afff-2c6f0730a3be");
		System.out.println(results.size());
		Set<String> cache = new HashSet<String>();
		for(AlbumSearchResult r : results) {
			if(cache.contains(r.getApiId())) {
				System.out.println("dup");
			}
			cache.add(r.getApiId());
		}
	}
}
