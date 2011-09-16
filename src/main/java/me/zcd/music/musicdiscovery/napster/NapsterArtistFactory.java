/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.musicdiscovery.napster;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import me.zcd.leetml.image.ImageService;
import me.zcd.leetml.image.ImageServiceFactory;
import me.zcd.music.musicdiscovery.napster.api.ArtistAlbums;
import me.zcd.music.musicdiscovery.napster.api.ArtistSearch;
import me.zcd.music.musicdiscovery.napster.api.Session;
import me.zcd.music.musicdiscovery.napster.api.Tracks;
import me.zcd.music.musicdiscovery.napster.api.resources.AlbumResponseHackContainer.AlbumResponse;
import me.zcd.music.musicdiscovery.napster.api.resources.ArtistResultsResourceHackContainer;
import me.zcd.music.musicdiscovery.napster.api.resources.ArtistResultsResourceHackContainer.ArtistResultsResource;
import me.zcd.music.musicdiscovery.napster.api.resources.TrackResponseHolder.TrackResponse;

import me.zcd.music.model.db.Album;
import me.zcd.music.model.db.Artist;
import me.zcd.music.model.db.Track;
import me.zcd.music.model.db.dao.AlbumDao;
import me.zcd.music.model.db.dao.ArtistDao;
import me.zcd.music.model.db.dao.TrackDao;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;
import me.zcd.music.model.db.utils.KeygenService;
import me.zcd.music.utils.LevenshteinDistance;

/**
 * Factory that uses napster rest API to get an artist/their albums/their
 * tracks.
 * 
 * @author mikehershey
 * 
 */
public class NapsterArtistFactory {

	Logger log = Logger.getLogger(NapsterArtistFactory.class.getName());
	
	private ArtistDao artistDao = DaoProviderFactory.getProvider().getArtistDao();
	private AlbumDao albumDao = DaoProviderFactory.getProvider().getAlbumDao();
	private TrackDao trackDao = DaoProviderFactory.getProvider().getTrackDao();
	private LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
	
	private ImageService imageService = ImageServiceFactory.get();

	/**
	 * Gets all info about an artist, persists the info to the local DB, then
	 * return the artist.
	 * 
	 * @param artistName
	 * @return
	 */
	public Artist LoadArtist(String artistName) {
		ArtistResultsResource artistResults = new ArtistSearch().findArtists(artistName);
		//search the results and use string distance to find the best
		ArtistResultsResourceHackContainer.Artist bestArtistResult = null;
		int closest = Integer.MAX_VALUE;
		for(ArtistResultsResourceHackContainer.Artist artistResponse : artistResults.getSearchResults().getArtist()) {
			//this is expensive
			int distance = levenshteinDistance.levenshteinDistance(artistResponse.getName(), artistName);
			if(distance < closest) {
				closest = distance;
				bestArtistResult = artistResponse;
			}
		}
		String artistKey = KeygenService.createArtistKey(bestArtistResult.getName());
		Artist acheck = artistDao.getArtist(artistKey);
		if (acheck != null) {
			//TODO check for new albums.
		} else {
			return loadNewArtist(bestArtistResult);
		}
		return null;
	}

	/**
	 * Fetchs an artist + all their albums + all their songs, persists it all
	 * 
	 * @param artistResponse
	 *            An artist response from napster.
	 * @return
	 */	
	private Artist loadNewArtist(me.zcd.music.musicdiscovery.napster.api.resources.ArtistResultsResourceHackContainer.Artist artistResponse) {
		Artist artist = artistDao.createNonpersistentArist(artistResponse.getName());
		
		byte[] image = Session.getSession().getAuthenticatedUrlData(artistResponse.getArtistPhotoURL());
		artist.setArtistArtKey(imageService.saveImage(image, "image/jpeg").getKey());
		
		loadAllAlbums(artistResponse.getId(), artist);
		artistDao.persistArtist(artist);
		return artist;
	}

	/**
	 * Finds all albums for the specified artist and saves them to the database
	 * It also adds all tracks and albums where they belong in the artist, so
	 * it would make sense to call this and then save your artist.
	 * @param artistId
	 * @param artist 
	 */
	private void loadAllAlbums(long artistId, Artist artist) {
		List<Album> allAlbums = new ArrayList<Album>();
		List<Track> allTracks = new ArrayList<Track>();
		AlbumResponse albumReponse = new ArtistAlbums().findAllAlbumsByArtistId(artistId);
		for (me.zcd.music.musicdiscovery.napster.api.resources.AlbumResponseHackContainer.Album albumResponse : albumReponse.getAlbums().getAlbum()) {
			Album album = albumDao.createNonpersistentAlbum(artist.getName(), albumResponse.getName());
			byte[] data = Session.getSession().getAuthenticatedUrlData(albumResponse.getAlbumArtURL());
			album.setAlbumArtKey(imageService.saveImage(data, "image/jpeg").getKey());
			album.setReleaseDate(albumResponse.getReleaseDate());
			
			allTracks.addAll(loadAllTracks(albumResponse.getId(), album, artist));
			allAlbums.add(album);
			
			artist.addAlbumKey(album.getKey());
		}
		trackDao.persistAllTracks(allTracks);
		albumDao.persistAllAlbums(allAlbums);
		artist.setGenre(allTracks.get(0).getGenre());
	}
	
	/**
	 * Finds all tracks for the specified albums and:
	 *		-adds the track to the album's list<Track>
	 *		-adds the track to the artist's list<Track>
	 * @param albumId
	 * @param album
	 * @param artist
	 * @return List of all tracks on this album
	 */
	private List<Track> loadAllTracks(long albumId, Album album, Artist artist) {
		List<Track> tracks = new ArrayList<Track>();
		try {
			TrackResponse tracksResponse = new Tracks().getTracksByAlbumId(albumId);
			int i = 1;
			for (me.zcd.music.musicdiscovery.napster.api.resources.TrackResponseHolder.Track tr : tracksResponse.getAlbumInfo().getTrack()) {
				Track track = trackDao.createNonpersistentTrack(album.getArtistName(), album.getName(), tr.getTrackName());
				track.setGenre(tr.getGenre());
				track.setTrackNumber(i);
				album.addTrackKey(track.getKey());
				artist.addTrackKey(track.getKey());
				tracks.add(track);
				i++;
			} 
		} catch(Exception e) {
			log.log(Level.SEVERE, "", e);
			return new ArrayList<Track>();
		}
		return tracks;
	}

}
