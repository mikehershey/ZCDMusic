/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.model.db.gae.jdo;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.NotPersistent;
import me.zcd.leetml.gae.GAEModel;
import me.zcd.leetml.image.Image;
import me.zcd.leetml.image.ImageServiceFactory;
import me.zcd.leetml.image.gae.jdo.ImageGaeImpl;
import me.zcd.music.Settings;
import me.zcd.music.model.db.Album;
import me.zcd.music.model.db.dao.AlbumDao;
import me.zcd.music.model.db.dao.provider.DaoProviderFactory;
import me.zcd.music.utils.StringUtils;

@PersistenceCapable(detachable = "true")
public class GaeAlbumImpl implements Album {
	
	@NotPersistent
	private static Logger log = Logger.getLogger(GaeAlbumImpl.class.getName());
	
	@NotPersistent
	private AlbumDao albumDao = DaoProviderFactory.getProvider().getAlbumDao();

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String name;
	
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
	private String artistName;
	
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
	private Date releaseDate;
	
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
	private List<String> trackKeys;
	
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
	private String albumArtKey;
	
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
	private String albumArtUrl;
	
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
	private String type;
	
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
	private List<String> searchTerms = new ArrayList<String>();

	public String getAlbumArtUrl() {
		return albumArtUrl;
	}

	public void setAlbumArtUrl(String albumArtUrl) {
		this.albumArtUrl = albumArtUrl;
	}

	@Override
	public String getKey() {
		return this.key.getName();
	}
	
	@Override
	public void setKey(String key) {
		this.key = KeyFactory.createKey(GaeAlbumImpl.class.getSimpleName(), key.toLowerCase());
	}

	private void migrateToHavingSearchTerms() {
		if(searchTerms.size() < 1) {
			for(String titlePart : StringUtils.stripSpecialCharacters(name).split(" ")) {
				this.searchTerms.add(titlePart);
			}
			albumDao.persistAlbum(this);
		}
	}
	
	private void migrateToHavingImages() {
		
	}

	@Override
	public String getName() {
		
		if(this.name != null) {
			//album | single | ep | compilation | soundtrack
			return StringUtils.formatName(this.name.replace("[album]", "").replace("[single]", "").replace("[ep]", "").replace("[compilation]", "").replace("[soundtrack]", ""));
		}
		return null;
	}

	@Override
	public void setArtistName(String name) {
		if(name != null) {
			this.artistName = name.toLowerCase();
		}
	}
	
	@Override
	public String getArtistName() {
		if(this.artistName != null) {
			return StringUtils.formatName(this.artistName);
		}
		return null;
	}

	@Override
	public String getArtistKey() {
		return this.getKey().split(Settings.KEY_SEPERATOR)[0];
	}

	@Override
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	@Override
	public Date getReleaseDate() {
		return this.releaseDate;
	}

	@Override
	public void setTrackKeys(List<String> tracks) {
		this.trackKeys = tracks;
	}

	@Override
	public List<String> getTrackKeys() {
		return this.trackKeys;
	}

	@Override
	public void setAlbumArtKey(String albumArtKey) {
		this.albumArtKey = albumArtKey;
	}

	@Override
	public String getAlbumArtKey() {
		return this.albumArtKey;
	}

	@Override
	public void setName(String name) {
		if(name != null) {
			this.name = name.toLowerCase();
		}
	}

	@Override
	public void addTrackKey(String key) {
		this.trackKeys.add(key);
	}

	@Override
	public String getFormattedReleaseDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
		return sdf.format(releaseDate);
	}

	@Override
	public Image getAlbumImage() {
		if(StringUtils.containsOnlyNumbers(this.albumArtKey)) {
			//new form of Image object
			PersistenceManager pm = GAEModel.get().getPersistenceManager();
			Image i = null;
			log.info("Loading image (Image object): " + this.albumArtKey);
			try {
				i = pm.getObjectById(ImageGaeImpl.class, Long.parseLong(this.albumArtKey));
			} finally {
				pm.close();
			}
			return i;
		} else {
			log.info("Migrating old data");
			//migrate old blobstore only keys
			BlobstoreService blobStoreService = BlobstoreServiceFactory.getBlobstoreService();
			byte[] data = blobStoreService.fetchData(new BlobKey(this.albumArtKey), 0, BlobstoreService.MAX_BLOB_FETCH_SIZE-1);
			log.info("I have the data from blobstore!");
			Image i = ImageServiceFactory.get().saveImage(data, "image/jpg");
			if(i != null) {
				this.albumArtKey = i.getKey();
			}
			this.albumDao.persistAlbum(this);
			return i;
		}
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getType() {
		return this.type;
	}
	
}
