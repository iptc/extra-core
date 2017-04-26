package org.iptc.extra.core.daos;

import org.iptc.extra.core.types.Topic;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import com.mongodb.WriteResult;

public class TopicsDAO extends DAO<Topic> {

	public TopicsDAO(Datastore ds) {
		super(ds);
	}
	
	public Topic get(String topicId, String taxonomyId) {
		Query<Topic> q = createQuery()
			.filter("topicId", topicId)
			.filter("taxonomyId", taxonomyId);
		
		Topic topic = this.findOne(q);
		return topic;
	}
	
	public WriteResult delete(String topicId, String taxonomyId) {
		Query<Topic> q = createQuery()
			.filter("topicId", topicId)
			.filter("taxonomyId", taxonomyId);
		
		return this.deleteByQuery(q);
	}
	
}
