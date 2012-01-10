package me.zcd.music.controllers.ajax.track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zcd.leetml.LeetmlController;
import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.leetml.bean.validation.rules.RequiredRule.Required;
import me.zcd.music.model.db.YoutubeIdRatings;
import me.zcd.music.model.db.dao.TrackDao;
import me.zcd.music.model.db.dao.YoutubeIdRatingDao;
import me.zcd.music.model.db.gae.dao.TrackDaoGaeImpl;
import me.zcd.music.model.db.gae.dao.YoutubeIdRatingDaoGaeImpl;
import me.zcd.music.model.db.utils.KeygenService;
import me.zcd.music.model.js.YoutubeTrackSource;
import me.zcd.music.youtube.api.Search;
import me.zcd.music.youtube.api.SearchResult;

/**
 *
 * @author mikehershey
 */
public class GetSourcesForTrack extends LeetmlController implements Bean {

	private String trackKey;
	private TrackDao trackDao = new TrackDaoGaeImpl();
	private YoutubeIdRatingDao youtubeIdRatingsDao = new YoutubeIdRatingDaoGaeImpl();
	private Search youtubeSearch = new Search();
	
	private static final String TEMPLATE_PAGE = "trackSources";
	
	@ManagedField
	@Required
	public void setTrackKey(String trackKey) {
		this.trackKey = trackKey;
	}
	
	@Override
	public String service() {
		List<YoutubeTrackSource> ret = new ArrayList<YoutubeTrackSource>();
		//get the name and artist from trackKey to avoid a costly database fetch
		String artistName = KeygenService.getArtistFromTrackKey(trackKey);
		String trackName = KeygenService.getTrackFromTrackKey(trackKey);
		//sanity check
		if(artistName == null || trackName == null) {
			return TEMPLATE_PAGE;
		}
		//find youtube search results
		List<SearchResult> results = youtubeSearch.getYoutubeResults(artistName, trackName);
		for(SearchResult youtubeResult : results) {
			String description = youtubeResult.getDescription();
			if(description.length() > 99) {
				description = description.substring(0,97) + "...";
			}
			ret.add(new YoutubeTrackSource(youtubeResult.getTitle(), description, new Long(0), youtubeResult.getVideoId()));
		}
		//add in user feedback
		YoutubeIdRatings youtubeRatings = youtubeIdRatingsDao.getYoutubeIdRatings(trackKey);
		if(youtubeRatings != null) {
			Map<String, Long> ratings = youtubeRatings.getRatings();
			for(YoutubeTrackSource source : ret) {
				if(ratings.containsKey(source.getYoutubeId())) {
					source.setFeedbackRating(ratings.get(source.getYoutubeId()));
				}
			}
		}
		this.getTemplateContext().put("sources", ret);
		return TEMPLATE_PAGE;
	}

	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Map<String, ValidationRule> invalidFields) {
		resp.setStatus(400);
		try {
			resp.getWriter().println("Must provide trackKey");
		} catch (IOException ex) {
			Logger.getLogger(GetSourcesForTrack.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
}
