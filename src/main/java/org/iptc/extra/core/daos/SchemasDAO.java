package org.iptc.extra.core.daos;

import org.iptc.extra.core.types.Schema;
import org.mongodb.morphia.Datastore;

public class SchemasDAO extends DAO<Schema> {
	
	public SchemasDAO(Datastore ds) {
		super(ds);
	}
}