/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.controllers.admin.utils;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.zcd.music.model.db.gae.GAEModel;
import me.zcd.music.model.db.gae.jdo.GaeAlbumImpl;
import me.zcd.music.model.db.gae.jdo.GaeArtistImpl;
import me.zcd.music.model.db.gae.jdo.GaeTrackImpl;

public class DropMusicDatabase extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			Query q = pm.newQuery(GaeArtistImpl.class);
			try {
				List<GaeArtistImpl> artistResults = (List<GaeArtistImpl>) q.execute();
				pm.deletePersistentAll(artistResults);
			} finally {
				q.closeAll();
			}
		} finally {
			pm.close();
		}
		pm = GAEModel.get().getPersistenceManager();
		try {
			Query q = pm.newQuery(GaeTrackImpl.class);
			try {
				List<GaeTrackImpl> artistResults = (List<GaeTrackImpl>) q.execute();
				pm.deletePersistentAll(artistResults);
			} finally {
				q.closeAll();
			}
		} finally {
			pm.close();
		}
		pm = GAEModel.get().getPersistenceManager();
		try {
			Query q = pm.newQuery(GaeAlbumImpl.class);
			try {
				List<GaeAlbumImpl> artistResults = (List<GaeAlbumImpl>) q.execute();
				pm.deletePersistentAll(artistResults);
			} finally {
				q.closeAll();
			}
		} finally {
			pm.close();
		}
	}
	
}
