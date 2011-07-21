package me.zcd.music;

import me.zcd.leetml.DefaultSettings;
import me.zcd.leetml.dispatcher.error.ErrorPage;
import me.zcd.music.errors.Email500Error;

/**
 *
 * @author mikehershey
 */
public class LeetmlSettings extends DefaultSettings {
	
	@Override
	public boolean isStripFormatting() {
		return false;
	}
	
	@Override
	public ErrorPage get500Page() {
		return new Email500Error();
	}
	
}