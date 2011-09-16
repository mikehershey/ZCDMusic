package me.zcd.music.controllers.ajax.track;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zcd.leetml.LeetmlAjaxController;
import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.leetml.bean.validation.rules.RequiredRule.Required;
import me.zcd.music.model.VoteReason;
import me.zcd.music.model.db.Track;
import me.zcd.music.model.db.dao.TrackDao;
import me.zcd.music.model.db.dao.YoutubeIdRatingDao;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;
import me.zcd.music.model.js.Playable;
import me.zcd.music.youtube.api.Search;

/**
 *
 * @author mikehershey
 */
public class RateYoutubeQuality extends LeetmlAjaxController implements Bean {
	
	private VoteReason downVoteReason;
	private String trackKey;
	
	private TrackDao trackDao = DaoProviderFactory.getProvider().getTrackDao();
	private YoutubeIdRatingDao youtubeIdRatingDao = DaoProviderFactory.getProvider().getYoutubeIdRatingDao();
	
	@ManagedField
	@Required
	public void setDownVoteReason(String downVoteReason) {
		this.downVoteReason = VoteReason.valueOf(downVoteReason);
	}
	
	@ManagedField
	@Required
	public void setTrackKey(String trackKey) {
		this.trackKey = trackKey;
	}
	
	@Override
	public Object service() {
		Track t = trackDao.getTrack(trackKey);
		if(t != null && t.getYoutubeLocation() != null && !t.getYoutubeLocation().isEmpty()) {
			long toAdd = 0l;
			switch(downVoteReason) {
				case GOOD:
					toAdd = 100l;
					break;
				case BAD_QUALITY:
					toAdd = -30l;
					break;
				case LIVE:
					toAdd = -70l;
					break;
				case WRONG_SONG:
					toAdd = -100l;
					break;
			}
			youtubeIdRatingDao.addRating(t.getYoutubeLocation(),t.getKey(), toAdd);
		}
		String youtubeId = null;
		if(!downVoteReason.equals(downVoteReason.GOOD)) {
			//if we are downvoting find a new youtube video id and set it.
			try {
				youtubeId = new Search().findYoutubeId(t.getArtistName(), t.getTitle(), t.getKey()).get(0);
			} catch(Exception e) {
				log.error("Error finding new youtube ID", e);
				this.resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			trackDao.setYoutubeId(this.trackKey, youtubeId);
		}
		return new Playable(t.getKey(), t.getTitle(), t.getArtistName(), t.getAlbumName(), youtubeId, Integer.toString(t.getTrackNumber()));
	}

	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Map<String, ValidationRule> invalidFields) {
		resp.setStatus(400);
	}
	
}
