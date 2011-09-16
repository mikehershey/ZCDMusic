package me.zcd.music.controllers;

import java.util.List;
import me.zcd.leetml.LeetmlController;
import me.zcd.leetml.messages.Message;
import me.zcd.leetml.messages.MessageService;
import me.zcd.leetml.messages.gae.MessageServiceGaeImpl;
import me.zcd.music.user.UserService;
import me.zcd.music.user.UserServiceFactory;

/**
 *
 * @author mikehershey
 */
public class FindMessages extends LeetmlController {
	
	private UserService userService = UserServiceFactory.getUserService();
	private MessageService messageService = new MessageServiceGaeImpl(); //TODO 
	
	@Override
	public String service() {
		String user = userService.getCurrentUsersEmailAddress();
		List<Message> messages = messageService.findAndDeleteMessagesForUser(user);
		this.getTemplateContext().put("messages", messages);
		return "showMessages";
	}
	
}
