package me.zcd.music.controllers.ajax.stats;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zcd.leetml.LeetmlAjaxController;
import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.leetml.bean.validation.rules.RequiredRule.Required;
import me.zcd.music.model.db.dao.UserPaymentsDao;
import me.zcd.music.model.db.gae.dao.UserPaymentsDaoGaeImpl;

/**
 *
 * @author mikehershey
 */
public class GetUserTop10Tracks extends LeetmlAjaxController implements Bean {
	
	private String userEmail;
	
	private UserPaymentsDao userPaymentsDao = new UserPaymentsDaoGaeImpl();

	@ManagedField
	@Required
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	@Override
	public Object service() {
		return userPaymentsDao.getUserPayments(userEmail).getTopListens(10);
	}

	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Map<String, ValidationRule> invalidFields) {
		resp.setStatus(400);
	}
	
}
