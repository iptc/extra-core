package org.iptc.extra.core.daos;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import com.mongodb.WriteResult;

public class DAO<K> extends BasicDAO<K, ObjectId> {

	protected DAO(Datastore ds) {
		super(ds);
	}

	public K get(String id) {
		ObjectId oId = new ObjectId(id);
		return this.get(oId);
	}

	public WriteResult delete(String id) {
		ObjectId oId = new ObjectId(id);
		return this.deleteById(oId);
	}
	
	public boolean exists(String id) {
		Query<K> query = createQuery().filter("_id", new ObjectId(id));
		return this.exists(query);
	}
}
