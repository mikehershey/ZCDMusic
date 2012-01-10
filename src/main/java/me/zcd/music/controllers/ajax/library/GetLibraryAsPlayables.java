package me.zcd.music.controllers.ajax.library;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zcd.leetml.LeetmlAjaxController;
import me.zcd.leetml.LeetmlController;
import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.music.model.UserLibraryTrack;
import me.zcd.music.model.db.UserLibrary;
import me.zcd.music.model.db.dao.UserLibraryDao;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;

/**
 *
 * @author mikehershey
 */
public class GetLibraryAsPlayables extends LeetmlAjaxController implements Bean {

	UserService userService = UserServiceFactory.getUserService();
	UserLibraryDao userLibraryDao = DaoProviderFactory.getProvider().getUserLibraryDao();
	
	private String emailAddress;

	@ManagedField
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	@Override
	public Object service() {
		Set<UserLibraryTrack> tracks = null;
		if(emailAddress != null && !emailAddress.isEmpty()) {
			UserLibrary userLibrary = userLibraryDao.getUserLibrary(emailAddress);
			if(userLibrary != null) {
				tracks = userLibrary.getTracks();
				return tracks;
			}
		}
		//return an error
		Map<String,String> errorResponse = new HashMap<String, String>();
		errorResponse.put("error", "Account not found. Are you registered?");
		return errorResponse;
	}

	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Map<String, ValidationRule> invalidFields) {
		resp.setStatus(400);
	}
	
}
