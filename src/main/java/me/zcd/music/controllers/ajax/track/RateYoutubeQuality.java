package me.zcd.music.controllers.ajax.track;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zcd.leetml.LeetmlController;
import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.leetml.bean.validation.rules.RequiredRule.Required;
import me.zcd.music.model.DownVoteReason;
import me.zcd.music.model.db.Track;
import me.zcd.music.model.db.dao.TrackDao;
import me.zcd.music.model.db.dao.YoutubeIdRatingDao;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;

/**
 *
 * @author mikehershey
 */
public class RateYoutubeQuality extends LeetmlController implements Bean {
	
	private DownVoteReason downVoteReason;
	private String trackKey;
	
	private TrackDao trackDao = DaoProviderFactory.getProvider().getTrackDao();
	private YoutubeIdRatingDao youtubeIdRatingDao = DaoProviderFactory.getProvider().getYoutubeIdRatingDao();
	
	@ManagedField
	@Required
	public void setDownVoteReason(String downVoteReason) {
		this.downVoteReason = DownVoteReason.valueOf(downVoteReason);
	}
	
	@ManagedField
	@Required
	public void setTrackKey(String trackKey) {
		this.trackKey = trackKey;
	}
	
	@Override
	public String service() {
		Track t = trackDao.invalidateYoutubeId(trackKey);
		if(t != null && t.getYoutubeLocation() != null && !t.getYoutubeLocation().isEmpty()) {
			long toAdd = 0l;
			switch(downVoteReason) {
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
			youtubeIdRatingDao.addRating(t.getYoutubeLocation(), toAdd);
		}
		return "200";
	}

	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Map<String, ValidationRule> invalidFields) {
		resp.setStatus(400);
	}
	
}
