package me.zcd.music.tests.integration.musicbrainz;

import java.util.List;
import me.zcd.music.musicdiscovery.api.resources.AlbumSearchResult;
import me.zcd.music.musicdiscovery.musicbrainz.api.AlbumSearchMBImpl;
import org.junit.Test;

/**
 *
 * @author mikehershey
 */
public class AlbumSearchMBImplTest {
	
	@Test
	public void testFindAllReleaseGroupsByArtistId() {
		AlbumSearchMBImpl albumSearch = new AlbumSearchMBImpl();
		List<AlbumSearchResult> albums = albumSearch.findAllAlbumsByArtist("8d3ee4ba-be21-470c-bb7c-4c124c3eb989");
		for(AlbumSearchResult rgr : albums) {
			System.out.println(rgr.getName());
			System.out.println(rgr.getApiId());
			System.out.println(rgr.getReleaseDate());
			System.out.println(rgr.getType());
			System.out.println("=====================");
		}
	}
	
}
