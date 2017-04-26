package org.iptc.extra.core.daos;

import org.iptc.extra.core.types.Dictionary;
import org.mongodb.morphia.Datastore;

public class DictionariesDAO extends DAO<Dictionary> {
	public DictionariesDAO(Datastore ds) {
		super(ds);
	}
}
