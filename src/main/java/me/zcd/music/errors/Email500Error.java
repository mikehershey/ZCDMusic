/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.errors;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zcd.leetml.dispatcher.error.Default500Page;
import me.zcd.music.Settings;
import me.zcd.music.mail.MailService;
import me.zcd.music.mail.MailServiceFactory;
import me.zcd.music.mail.Message;
import me.zcd.music.utils.StringUtils;

/**
 *
 * @author mikehershey
 */
public class Email500Error extends Default500Page {

	private MailService mailService = MailServiceFactory.getMailService();
	
	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Exception e) {
		try {
			Message message = new Message();
			message.addTo(Settings.errorEmailRecipiant);
			message.setSubject("Music.zcd had an error!");
			message.setBody("StackTrace:\n\n" + StringUtils.getStackTrace(e));
			message.setSender("errors@zcd.me"); //TODO move to settings.
			
			mailService.send(message);
			super.onError(req, resp, e);
			resp.setStatus(200); //force 200 so error page is shown at all in ajax
		} catch (IOException ex) {
			Logger.getLogger(Email500Error.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
