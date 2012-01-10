package me.zcd.music.controllers.request;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zcd.leetml.LeetmlController;
import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.leetml.bean.validation.rules.RequiredRule.Required;
import me.zcd.music.user.UserService;
import me.zcd.music.user.UserServiceFactory;

/**
 * 
 * @author mikehershey
 */
public class QueueLoadArtistForApi57283 extends LeetmlController implements Bean {
	
	private String artistApiId;
	
	private UserService userService = UserServiceFactory.getUserService();
	
	@ManagedField
	@Required
	public void setArtistApiId(String artistApiId) {
		this.artistApiId = artistApiId;
	}
	
	@Override
	public String service() {
		Queue queue = QueueFactory.getQueue("api-import-queue");
		queue.add(withUrl("/tasks/DoLoadApiArtist.h7m1").param("artistApiId", this.artistApiId).param("basicAuth", "heuighisohdfj2834729yfw").method(Method.GET));
		try {
			resp.getOutputStream().println("Added to queue.");
		} catch (IOException e) {
			log.error("Error creating queue", e);
			return "500";
		}
		return "200";
	}

	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Map<String, ValidationRule> invalidFields) {
		resp.setStatus(400);
	}
	
}
