package me.zcd.music.controllers;

/**
 * Shows the index page with mikehershey32@gmail.com's library preloaded.
 * @author mikehershey
 */
public class Demo extends me.zcd.music.controllers.Index {

	@Override
	public String service() {
		super.setShowLibrary("mikehershey32@gmail.com");
		return super.service();
	}
	
}
