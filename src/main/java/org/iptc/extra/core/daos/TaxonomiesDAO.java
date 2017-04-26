package org.iptc.extra.core.daos;

import org.iptc.extra.core.types.Taxonomy;
import org.mongodb.morphia.Datastore;

public class TaxonomiesDAO extends DAO<Taxonomy> {
	public TaxonomiesDAO(Datastore ds) {
		super(ds);
	}
}
