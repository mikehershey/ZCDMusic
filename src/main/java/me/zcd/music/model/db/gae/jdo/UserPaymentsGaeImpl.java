package me.zcd.music.model.db.gae.jdo;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.HashMap;
import java.util.Map;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import me.zcd.music.model.db.UserPayments;

/**
 *
 * @author mikehershey
 */
@PersistenceCapable(detachable="true")
public class UserPaymentsGaeImpl extends UserPayments {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String emailAddress;
	
	@Persistent(serialized = "true", defaultFetchGroup="true")
	private Map<String, Long> monthlyListenRecords;

	public UserPaymentsGaeImpl(String emailAddress) {
		this.key = KeyFactory.createKey(UserPaymentsGaeImpl.class.getSimpleName(), emailAddress);
		this.emailAddress = emailAddress;
		this.monthlyListenRecords = new HashMap<String, Long>();
	}
	
	@Override
	public String getUserEmail() {
		return this.emailAddress;
	}

	@Override
	public void setUserEmail(String email) {
		this.emailAddress = email;
	}

	@Override
	public Map<String, Long> getMonthlyListenRecords() {
		return this.monthlyListenRecords;
	}
	
}
