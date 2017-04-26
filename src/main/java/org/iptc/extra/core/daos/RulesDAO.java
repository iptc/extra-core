package org.iptc.extra.core.daos;

import org.iptc.extra.core.types.Rule;
import org.mongodb.morphia.Datastore;

public class RulesDAO extends DAO<Rule> {
	
	public RulesDAO(Datastore ds) {
		super(ds);
	}
}
