package me.zcd.music.controllers.request;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.zcd.leetml.LeetmlController;
import me.zcd.leetml.bean.Bean;
import me.zcd.leetml.bean.validation.ValidationRule;
import me.zcd.leetml.bean.validation.rules.ManagedField;
import me.zcd.leetml.bean.validation.rules.RequiredRule.Required;
import me.zcd.music.musicdiscovery.MusicDiscoveryApiProviderFactory;
import me.zcd.music.musicdiscovery.api.ArtistSearch;
import me.zcd.music.musicdiscovery.api.resources.ArtistSearchResult;

/**
 *
 * @author mikehershey
 */
public class GetArtistSuggestions extends LeetmlController implements Bean {

	private String artist;
	
	private ArtistSearch artistSearch = MusicDiscoveryApiProviderFactory.getProvider().getArtistSearch();
	
	@ManagedField
	@Required
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	@Override
	public String service() {
		List<ArtistSearchResult> artistSearchResults = artistSearch.findArtist(this.artist, 5);
		this.getTemplateContext().put("artistSearchResults", artistSearchResults);
		return "request.showArtistChoices";
	}
	
	@Override
	public void onError(HttpServletRequest req, HttpServletResponse resp, Map<String, ValidationRule> invalidFields) {
		resp.setStatus(400);
	}
	
}
