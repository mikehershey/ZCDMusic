/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.controllers.tasks;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.zcd.leetml.LeetmlController;
import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.leetml.bean.validation.rules.RequiredRule.Required;
import me.zcd.music.musicdiscovery.api.MusicDiscovery;

public class DoLoadApiArtist extends LeetmlController implements Bean {

	private static final long serialVersionUID = 1L;
	private String artistApiId;
	private String basicAuth;
	private String forUser;
	
	@ManagedField
	@Required
	public void setArtistApiId(String artistApiId) {
		this.artistApiId = artistApiId;
	}
	
	@ManagedField
	@Required
	public void setBasicAuth(String basicAuth) {
		this.basicAuth = basicAuth;
	}
	
	@ManagedField
	public void setForUser(String forUser) {
		this.forUser = forUser;
	}
	
	@Override
	public String service() {
		if(this.basicAuth.equals("heuighisohdfj2834729yfw")) {
			MusicDiscovery md = new MusicDiscovery();
			md.setupLogging(this.forUser);
			md.discoverArtistByApiId(artistApiId);
			return "200";
		}
		return "403";
	}
	
	@Override
	public void onError(HttpServletRequest arg0, HttpServletResponse arg1, Map<String, ValidationRule> arg2) {
		arg1.setStatus(400);
	}

}
