package org.iptc.extra.core.daos;

import org.iptc.extra.core.types.Group;
import org.mongodb.morphia.Datastore;

public class GroupDAO extends DAO<Group> {
	
	public GroupDAO(Datastore ds) {
		super(ds);
	}
}
