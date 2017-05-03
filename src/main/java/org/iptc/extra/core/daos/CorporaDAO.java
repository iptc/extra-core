package org.iptc.extra.core.daos;

import org.iptc.extra.core.types.Corpus;
import org.mongodb.morphia.Datastore;

public class CorporaDAO extends DAO<Corpus> {
	public CorporaDAO(Datastore ds) {
		super(ds);
	}
}
