package me.zcd.music.model.db.gae.dao;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import me.zcd.leetml.gae.GAEModel;
import me.zcd.leetml.logging.Log;
import me.zcd.leetml.logging.LogFactory;
import me.zcd.music.model.db.UserPayments;
import me.zcd.music.model.db.dao.UserPaymentsDao;
import me.zcd.music.model.db.gae.jdo.UserPaymentsGaeImpl;

/**
 *
 * @author mikehershey
 */
public class UserPaymentsDaoGaeImpl implements UserPaymentsDao {
	
	private static final Log log = LogFactory.getLogger(UserPaymentsDaoGaeImpl.class);

	@Override
	public UserPayments getUserPayments(String email) {
		UserPaymentsGaeImpl userPayments = null;
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			try {
				userPayments = pm.getObjectById(UserPaymentsGaeImpl.class, email);
			} catch(JDOObjectNotFoundException e) {
				//make a new one.
				userPayments = (UserPaymentsGaeImpl) this.createNonpersistedUserPayments(email);
			}
		} catch(Exception e) {
			log.warn("Exception loading user payments", e);
		} finally {
			pm.close();
		}
		return userPayments;
	}

	@Override
	public UserPayments incrementTrackCount(String trackKey, String emailAddress) {
		log.debug(trackKey);
		UserPaymentsGaeImpl userPayments = null;
		PersistenceManager pm = GAEModel.get().getPersistenceManager();
		try {
			try {
				userPayments = pm.getObjectById(UserPaymentsGaeImpl.class, emailAddress);
				log.debug("found a user payments");
			} catch(JDOObjectNotFoundException e) {
				//make a new one.
				log.debug("made a new user payments");
				userPayments = (UserPaymentsGaeImpl) this.createNonpersistedUserPayments(emailAddress);
				pm.makePersistent(userPayments);
			}
			if(userPayments.getMonthlyListenRecords().containsKey(trackKey)) {
				log.debug("Found track in listens");
				Long count = userPayments.getMonthlyListenRecords().get(trackKey);
				count++;
				userPayments.getMonthlyListenRecords().put(trackKey, count);
			} else {
				log.debug("made a new record in listens");
				userPayments.getMonthlyListenRecords().put(trackKey, new Long(1));
			}
			JDOHelper.makeDirty(userPayments, "monthlyListenRecords");
			log.debug(userPayments.getMonthlyListenRecords().get(trackKey).toString());
		} catch(Exception e) {
			log.warn("Exception loading user library", e);
		} finally {
			pm.close();
		}
		return userPayments;
	}
	
	public UserPayments createNonpersistedUserPayments(String emailAddress) {
		UserPayments userLibrary = new UserPaymentsGaeImpl(emailAddress);
		return userLibrary;
	}
	
}
