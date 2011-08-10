/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.youtube.api;

import com.google.gdata.client.Service.GDataRequest;
import com.google.gdata.client.youtube.YouTubeService;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

import me.zcd.music.utils.URLFetch;
import me.zcd.music.utils.XmlParser;
import java.util.logging.Logger;
import me.zcd.music.model.db.YoutubeIdRatings;
import me.zcd.music.model.db.dao.YoutubeIdRatingDao;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Search {

	private static final Logger log = Logger.getLogger(Search.class.getName());

	private YoutubeIdRatingDao youtubeIdRatingDao = DaoProviderFactory.getProvider().getYoutubeIdRatingDao();
	
	/**
	 * Attempts to find a link for the specified song. 
	 * It does this by searching youtube for "[artist] [song] lyrics"
	 * @param artistName
	 * @param trackName
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public List<String> findYoutubeId(String artistName, String trackName) throws ParserConfigurationException, SAXException, IOException {
		List<SearchResult> results = getYoutubeResults(artistName, trackName);
		return findBestResult(results, artistName, trackName);
	}

	/**
	 * Applies an algorithm to find the best match based on title/views
	 */
	private List<String> findBestResult(List<SearchResult> results, String artistName, String trackName) {
		Map<String, Integer> descriptionWordRankings = new HashMap<String, Integer>();
		//bad
		descriptionWordRankings.put("live", -50);
		descriptionWordRankings.put("cover", -50);
		descriptionWordRankings.put("@", -20);
		descriptionWordRankings.put("Instrumental", -30);

		Map<String, Integer> wordRankings = new HashMap<String, Integer>();
		//bad
		wordRankings.put("live", -100);
		wordRankings.put("cover", -100);
		wordRankings.put("Instrumental", -30);
		wordRankings.put("Remix", -10);
		wordRankings.put("@", -30);
		wordRankings.put("", 0);
		
		for (String part : replaceSpecialWithSpace(artistName).split(" ")) {
			part = part.toLowerCase();
			wordRankings.put(part, 10);
		}
		for (String part : replaceSpecialWithSpace(trackName).split(" ")) {
			part = part.toLowerCase();
			wordRankings.put(part, 10);
		}
		Map<SearchResult, Integer> scoredResults = new HashMap<SearchResult, Integer>();
		for (SearchResult result : results) {
			String title = replaceSpecialWithSpace(result.getTitle());
			int score = 0;
			for (String part : title.split(" ")) {
				part = part.toLowerCase();
				if (wordRankings.containsKey(part)) {
					score += wordRankings.get(part);
				} else {
					score -= 10;
				}
			}
			for (String part : replaceSpecialWithSpace(result.getDescription()).split(" ")) {
				if (part == null || part.equals("")) {
					continue;
				}
				part = part.toLowerCase();
				if (descriptionWordRankings.containsKey(part)) {
					score += descriptionWordRankings.get(part);
				}
			}
			YoutubeIdRatings youtubeIdRatings = youtubeIdRatingDao.getYoutubeIdRatings(result.getVideoId());
			if(youtubeIdRatings != null) {
				score += youtubeIdRatings.getRating();
			}
			scoredResults.put(result, score);
		}
		//sort results
		List<String> videoIds = new ArrayList<String>();
		int size = scoredResults.entrySet().size();
		for (int i = 0; i < size; i++) {
			int best = Integer.MIN_VALUE;
			SearchResult bestResult = null;
			for (Entry<SearchResult, Integer> entry : scoredResults.entrySet()) {
				if (entry.getValue() > best) {
					best = entry.getValue();
					bestResult = entry.getKey();
				}
			}
			scoredResults.remove(bestResult);
			videoIds.add(bestResult.getVideoId());
		}
		return videoIds;
	}

	public static String replaceSpecialWithSpace(String string) {
		return string.replaceAll("[\\[\\]\\(\\)/\\.-]", " ");
	}

	public List<SearchResult> getYoutubeResults(String artistName, String trackName) {
		try {
			List<SearchResult> results = new ArrayList<SearchResult>();
			//load the xml search results
			String searchTerm = artistName + " " + trackName + " -live";
			YouTubeService service = new YouTubeService(Settings.APPLICATION_NAME, Settings.YOUTUBE_DEVELOPER_KEY);
			String url = "http://gdata.youtube.com/feeds/api/videos?q=" + URLEncoder.encode(searchTerm, "UTF-8") + "&max-results=25&v=2";
			GDataRequest request = service.createFeedRequest(new URL(url));
			String xml = null;
			try {
				request.execute();
				InputStream responseStream = request.getResponseStream();
				final char[] buffer = new char[0x10000];
				StringBuilder out = new StringBuilder();
				Reader in = new InputStreamReader(responseStream, "UTF-8");
				int read;
				do {
					read = in.read(buffer, 0, buffer.length);
					if (read > 0) {
						out.append(buffer, 0, read);
					}
				} while (read >= 0);
				xml = out.toString();
				//log.info("response: " + xml);
			} finally {
				request.end();
			}
			Document doc = XmlParser.createDocument(xml);
			NodeList nodes = doc.getElementsByTagName("entry");
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				results.add(SearchResultBuilder.buildSearchResultsFromEntryNode(node));
			}
			return results;
		} catch (Exception e) {
			log.log(Level.WARNING, "Could not get search results!", e);
			return new ArrayList<SearchResult>();
		}
	}

	public static void main(String[] argv) throws ParserConfigurationException, SAXException, IOException {
		Search search = new Search();
		List<String> ss = search.findYoutubeId("ratatat", "wildcat");
		for (String s : ss) {
			System.out.println(s);
		}
	}
}
