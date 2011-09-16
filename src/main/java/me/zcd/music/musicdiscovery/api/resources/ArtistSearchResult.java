package me.zcd.music.musicdiscovery.api.resources;

/**
 *
 * @author mikehershey
 */
public class ArtistSearchResult {
	
	public String apiID;
	public String artistName;
	public String disambiguation;

	public String getDisambiguation() {
		return disambiguation;
	}

	public void setDisambiguation(String disambiguation) {
		this.disambiguation = disambiguation;
	}

	public String getApiID() {
		return apiID;
	}

	public void setApiID(String apiID) {
		this.apiID = apiID;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}
	
}
