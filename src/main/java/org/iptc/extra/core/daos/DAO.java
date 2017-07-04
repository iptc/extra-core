package org.iptc.extra.core.daos;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import com.mongodb.WriteResult;

/**
 * 
 * @author manosetro
 *
 * @param <K>
 * 
 * Data Access Object class, used to access objects of type K for/to MongoDB.
 * 
 */
public class DAO<K> extends BasicDAO<K, ObjectId> {

	protected DAO(Datastore ds) {
		super(ds);
	}

	public K get(String id) {
		try {
			ObjectId oId = new ObjectId(id);
			return this.get(oId);
		}
		catch(Exception e) {
			return null;
		}
	}

	public WriteResult deleteById(String id) {
		ObjectId oId = new ObjectId(id);
		return this.deleteById(oId);
	}
	
	public boolean exists(String id) {
		Query<K> query = createQuery().filter("_id", new ObjectId(id));
		return this.exists(query);
	}
}
