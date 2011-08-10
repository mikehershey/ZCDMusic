package me.zcd.music.controllers

import me.zcd.leetml.LeetmlController
import me.zcd.leetml.bean.Bean
import me.zcd.leetml.bean.validation.rules.ManagedField
import me.zcd.leetml.bean.validation.rules.RequiredRule
import me.zcd.music.model.db.dao.TrackDao
import me.zcd.music.model.db.dao.UserLibraryDao
import me.zcd.music.model.db.dao.provider.DaoProviderFactory
import me.zcd.music.user.UserService
import me.zcd.music.user.UserServiceFactory
import org.apache.commons.logging.LogFactory
import org.apache.commons.logging.Log
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import me.zcd.leetml.bean.validation.ValidationRule

/**
 *
 * @author mikehershey
 */
class AddTracksToLibrary extends LeetmlController implements Bean {
	
	private String[] trackKeys

    @ManagedField
	@RequiredRule.Required
    public void setTrackKeys(String[] trackKeys) {
        this.trackKeys = trackKeys;
    }
	
	private TrackDao trackDao = DaoProviderFactory.getProvider().getTrackDao()
	private UserLibraryDao userLibraryDao = DaoProviderFactory.getProvider().getUserLibraryDao()
	
	private Log log = LogFactory.getLog(AddTracksToLibrary.class)
	
	private UserService userService = UserServiceFactory.getUserService()
	
	@Override
	String service() {
        String emailAddress = userService.getCurrentUsersEmailAddress();
        if(emailAddress == null) {
            return "403";
        }
		try {
			userLibraryDao.addTracksToLibrary(trackKeys, emailAddress);
		} catch(any) {
            log.warn("Error saving tracks!", any);
			return "500";
		}
		return "200";
	}

    @Override
    void onError(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Map<String, ValidationRule> stringValidationRuleMap) {
        httpServletResponse.setStatus(400);
    }

}

