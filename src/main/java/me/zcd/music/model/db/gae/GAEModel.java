package me.zcd.music.model.db.gae;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public final class GAEModel {
    private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private GAEModel() {}

    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}