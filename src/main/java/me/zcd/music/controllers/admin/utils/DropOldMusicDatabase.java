package me.zcd.music.controllers.admin.utils;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.zcd.music.model.db.gae.GAEModel;

public class DropOldMusicDatabase extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			Query q = pm.newQuery("select from Artist");
			try {
				List artistResults = (List) q.execute();
				pm.deletePersistentAll(artistResults);
			} finally {
				q.closeAll();
			}
		} finally {
			pm.close();
		}
		pm = GAEModel.get().getPersistenceManager();
		try {
			Query q = pm.newQuery("select from Track");
			try {
				List artistResults = (List) q.execute();
				pm.deletePersistentAll(artistResults);
			} finally {
				q.closeAll();
			}
		} finally {
			pm.close();
		}
		pm = GAEModel.get().getPersistenceManager();
		try {
			Query q = pm.newQuery("select from Album");
			try {
				List artistResults = (List) q.execute();
				pm.deletePersistentAll(artistResults);
			} finally {
				q.closeAll();
			}
		} finally {
			pm.close();
		}
	}
	
}