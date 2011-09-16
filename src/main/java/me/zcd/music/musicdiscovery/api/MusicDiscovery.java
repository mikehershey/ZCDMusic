package me.zcd.music.musicdiscovery.api;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.zcd.leetml.logging.Log;
import me.zcd.leetml.logging.LogFactory;
import me.zcd.leetml.messages.MessageService;
import me.zcd.leetml.messages.gae.MessageServiceGaeImpl;
import me.zcd.music.model.db.Album;
import me.zcd.music.model.db.Artist;
import me.zcd.music.model.db.Track;
import me.zcd.music.model.db.dao.AlbumDao;
import me.zcd.music.model.db.dao.ArtistDao;
import me.zcd.music.model.db.dao.TrackDao;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;
import me.zcd.music.musicdiscovery.api.resources.AlbumSearchResult;
import me.zcd.music.musicdiscovery.api.resources.ArtistSearchResult;
import me.zcd.music.musicdiscovery.api.resources.TrackSearchResult;
import me.zcd.music.musicdiscovery.api.resources.TrackSearchResults;
import me.zcd.music.musicdiscovery.musicbrainz.api.AlbumSearchMBImpl;
import me.zcd.music.musicdiscovery.musicbrainz.api.ArtistSearchMBImpl;
import me.zcd.music.musicdiscovery.musicbrainz.api.TrackSearchMBImpl;

/**
 * Uses a music service to find metadata about an ar tist and store it. 
 * 
 * One instance per request; these are not thread safe.
 * 
 * @author mikehershey
 */
public class MusicDiscovery {
	
	private static final Log log = LogFactory.getLogger(MusicDiscovery.class);
	
	//TODO abstract music service so this isn't hardcoded
	private ArtistSearch artistSearch = new ArtistSearchMBImpl();
	private AlbumSearch albumSearch = new AlbumSearchMBImpl();
	private TrackSearch trackSearch = new TrackSearchMBImpl();
	
	private ArtistDao artistDao = DaoProviderFactory.getProvider().getArtistDao();
	private AlbumDao albumDao = DaoProviderFactory.getProvider().getAlbumDao();
	private TrackDao trackDao = DaoProviderFactory.getProvider().getTrackDao();
	
	public static final String MESSAGE_FINISHED_SUBJECT = "me.zcd.music.musicDiscovery.finished";
	public static final String Message_PROGRESS_SUBJECT = "me.zcd.music.musicDiscovery.progress";
	
	private String logForUser = null;
	
	private MessageService messageService = new MessageServiceGaeImpl();
	
	/**
	 * Uses the leetml message system to store messages about the progress 
	 * of the import. There are two subjects used me.zcd.music.musicDiscovery.progress
	 * which is used for status updates as its going, and
	 * me.zcd.music.musicDiscovery.finished for a completion message. (or whatever
	 * is in the constants above)
	 * @param user 
	 */
	public void setupLogging(String user) {
		this.logForUser = user;
	}
	
	public Artist discoverArtist(String artistName) {
		ArtistSearchResult artistSearchResult = artistSearch.findArtist(artistName);
		return doLoadArtistFromSearchResult(artistSearchResult);
	}
	
	public Artist discoverArtistByApiId(String artistApiId) {
		ArtistSearchResult artistSearchResult = artistSearch.findArtistByApiId(artistApiId);
		return doLoadArtistFromSearchResult(artistSearchResult);
	}
	
	private Artist doLoadArtistFromSearchResult(ArtistSearchResult artistSearchResult) {
		if(artistSearchResult == null) {
			this.saveFinishedMessage("Import of artist you requested failed. Likely musicbrainz is having a temporary outage. An admin has already been notified.");
			log.error("MusicDiscovery: Error finding artist that was requested to load from music brainz. Most likely a temporary outage. We really should be checking for these error responses.");
			return null;
		}
		this.saveProgressMessage("Begining import of artist " + artistSearchResult.getArtistName() + ". This may take quite a while. Feel free to leave this page, your music will still be loaded.");
		//TODO This is the wait for MB so their servers aren't flooded. Abstract this.
		try {
			Thread.sleep(3000);
		} catch (InterruptedException ex) {
			Logger.getLogger(MusicDiscovery.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		if(artistSearchResult == null) {
			return null;
		}
		Artist artist = artistDao.createNonpersistentArist(artistSearchResult.getArtistName());
		
		//set child keys
		List<Album> albums = discoverAllAlbumsOfArtist(artistSearchResult, artist);
		if(albums == null) {
			return null;
		}
		for(Album a : albums) {
			artist.addAlbumKey(a.getKey());
			for(String trackKey : a.getTrackKeys()) {
				artist.addTrackKey(trackKey);
			}
		}
		
		artistDao.persistArtist(artist);
		this.saveFinishedMessage("Finished importing artist: " + artist.getName() + ". You can now find them via search.");
		return artist;
	}
	
	private List<Album> discoverAllAlbumsOfArtist(ArtistSearchResult artistSearchResult, Artist artist) {
		List<Album> ret = new ArrayList<Album>();
		List<AlbumSearchResult> albumSearchResults = albumSearch.findAllAlbumsByArtist(artistSearchResult.getApiID());
		if(albumSearchResults == null) {
			this.saveFinishedMessage("Import of artist you requested failed. Likely musicbrainz is having a temporary outage. An admin has already been notified.");
			log.error("MusicDiscovery: Error finding artist that was requested to load from music brainz. Most likely a temporary outage. We really should be checking for these error responses. albumSearch.findAllAlbumsByArtist(artistSearchResult.getApiID() returned null");
			return null;
		}
		this.saveProgressMessage("Found " + albumSearchResults.size() + " releases by your requested artist, begining import of all releases.");
		//TODO This is the wait for MB so their servers aren't flooded. Abstract this.
		try {
			Thread.sleep(3000);
		} catch (InterruptedException ex) {
			Logger.getLogger(MusicDiscovery.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		for(AlbumSearchResult asr : albumSearchResults) {
			Album album = albumDao.createNonpersistentAlbum(artist.getName(), asr.getName() + " [" + asr.getType() + "]");
			album.setReleaseDate(asr.getReleaseDate());
			album.setType(asr.getType());
			
			//add all the tracks 
			List<String> trackKeys = new ArrayList<String>();
			List<Track> tracks = discoverAllTracksOfAlbum(asr, album);
			if(tracks == null) {
				continue;
			}
			for(Track t : tracks) {
				trackKeys.add(t.getKey());
			}
			album.setTrackKeys(trackKeys);
			
			ret.add(album);
		}
		
		//persist
		albumDao.persistAllAlbums(ret);
		
		return ret;
	}
	
	private List<Track> discoverAllTracksOfAlbum(AlbumSearchResult albumSearchResult, Album a) {
		List<Track> ret = new ArrayList<Track>();
		TrackSearchResults trackSearchResults = trackSearch.findTracksByAlbum(albumSearchResult.getApiId());
		//TODO This is the wait for MB so their servers aren't flooded. Abstract this.
		try {
			Thread.sleep(3000);
		} catch (InterruptedException ex) {
			Logger.getLogger(MusicDiscovery.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		if(trackSearchResults == null) {
			this.saveFinishedMessage("Failed to find songs for album: " + albumSearchResult.getName() + " we will omit this album, but continue loading this artist.");
			log.error("MusicDiscovery: Error finding artist that was requested to load from music brainz. Most likely a temporary outage. We really should be checking for these error responses. trackSearch.findTracksByAlbum(albumSearchResult.getApiId()) returned null for " + albumSearchResult.getName());
			return null;
		}
		this.saveProgressMessage("Found " + trackSearchResults.getTrackSearchResults().size() + " tracks for album: " + albumSearchResult.getName());
		
		for(TrackSearchResult tsr : trackSearchResults.getTrackSearchResults()) {
			Track track = trackDao.createNonpersistentTrack(a.getArtistName(), a.getName(), tsr.getName());
			track.setGenre("Unknown");
			track.setTrackNumber(tsr.getTrackNumber());
			
			ret.add(track);
		}
		
		//persist
		trackDao.persistAllTracks(ret);
		
		return ret;
	}
	
	private void saveProgressMessage(String message) {
		saveMessage(Message_PROGRESS_SUBJECT, message);
	}
	
	private void saveFinishedMessage(String message) {
		saveMessage(MESSAGE_FINISHED_SUBJECT, message);
	}
	
	private void saveMessage(String subject, String message) {
		if(this.logForUser != null) { 
			messageService.persistMessage(this.logForUser, subject, message);
		}
	}
	
}
