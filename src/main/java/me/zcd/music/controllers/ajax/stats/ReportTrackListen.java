package me.zcd.music.controllers.ajax.stats;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zcd.leetml.LeetmlController;
import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.leetml.bean.validation.rules.RequiredRule.Required;
import me.zcd.music.model.db.dao.TrackDao;
import me.zcd.music.model.db.dao.UserLibraryDao;
import me.zcd.music.model.db.dao.UserPaymentsDao;
import me.zcd.music.model.db.gae.dao.TrackDaoGaeImpl;
import me.zcd.music.model.db.gae.dao.UserLibraryDaoGaeImpl;
import me.zcd.music.model.db.gae.dao.UserPaymentsDaoGaeImpl;
import me.zcd.music.user.UserServiceFactory;

/**
 *
 * @author mikehershey
 */
public class ReportTrackListen extends LeetmlController implements Bean {

	private String trackKey;
	
	private UserLibraryDao userLibraryDao = new UserLibraryDaoGaeImpl();
	private TrackDao trackDao = new TrackDaoGaeImpl();
	private UserPaymentsDao userPaymentsDao = new UserPaymentsDaoGaeImpl();

	@ManagedField
	@Required
	public void setTrackKey(String trackKey) {
		this.trackKey = trackKey;
	}

	@Override
	public String service() {
		String email = UserServiceFactory.getUserService().getCurrentUsersEmailAddress();
		if(email != null) {
			//update the users play count
			userLibraryDao.incrementTrackPlayCount(trackKey, email);
			userPaymentsDao.incrementTrackCount(trackKey, email);
		}
		trackDao.incrementGlobalPlayCount(trackKey);
		return "200";
	}

	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Map<String, ValidationRule> invalidFields) {
		resp.setStatus(400);
	}
}
