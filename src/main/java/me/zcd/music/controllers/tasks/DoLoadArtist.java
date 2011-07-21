package me.zcd.music.controllers.tasks;

import java.util.Hashtable;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.leetml.bean.validation.rules.RequiredRule.Required;
import me.zcd.music.napster.NapsterArtistFactory;

public class DoLoadArtist extends HttpServlet implements Bean {

	private static final long serialVersionUID = 1L;
	private String artistName;
	private String basicAuth;
	
	@ManagedField
	@Required
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}
	
	@ManagedField
	@Required
	public void setBasicAuth(String basicAuth) {
		this.basicAuth = basicAuth;
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		if(this.basicAuth.equals("ksh982oihfa83yhuhf3")) {
			new NapsterArtistFactory().LoadArtist(this.artistName);
			resp.setStatus(200);
		}
	}
	
	@Override
	public void onError(HttpServletRequest arg0, HttpServletResponse arg1, Hashtable<String, ValidationRule> arg2) {
		arg1.setStatus(400);
	}

}
