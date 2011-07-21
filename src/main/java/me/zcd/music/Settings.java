package me.zcd.music;

public class Settings {

	public static final String KEY_SEPERATOR = ";;;";
	
	public enum SupportedDatabases {
		AppEngine,
		Cassandra
	};
	
	public static final SupportedDatabases CURRENT_DATABASE = SupportedDatabases.AppEngine;
	
	public static final String errorEmailRecipiant = "errors@zcd.me";
	
}
