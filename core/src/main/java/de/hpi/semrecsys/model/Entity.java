package de.hpi.semrecsys.model;

import de.hpi.semrecsys.DBObject;
import de.hpi.semrecsys.persistence.EntityManager;
import de.hpi.semrecsys.utils.Namespacer;

import java.io.Serializable;

// default package
// Generated Feb 18, 2014 10:01:26 AM by Hibernate Tools 3.4.0.CR1

/**
 * 
 * 
 * Entity class created from entity uri
 */

public class Entity implements DBObject, Comparable<Entity> {

	/**
	 * 
	 */
	private int entityId;
	private String uri = "";
	private String name;
	private Integer count = 0;
	private boolean isMeta = false;
	private Namespacer longUri;

	public Entity() {
	}

	public Entity(String uri) {
		this(uri, null);
	}

	public Entity(String uri, String prefix) {
		uri = uri.replace(" ", "_");
		this.name = uri;
		if (prefix != null && !prefix.isEmpty()) {
			prefix = prefix + ":";
		} else {
			if (!uri.startsWith(":")) {
				prefix = ":";
			} else {
				prefix = "";
			}
		}
		this.uri = prefix + uri;
	}

	public int getEntityId() {
		return this.entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public String getUri() {
		return this.uri;
	}

	public boolean isEmpty() {
		return this.uri.isEmpty();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}

	public Serializable getId() {
		return entityId;
	}

	@Override
	public String toString() {
		return toSimpleString();
		// return "[entityId = " + getId() + ", uri = " + uri + "]";
	}

	public String toSimpleString() {
		String result = "";
		result += getUri();
		return result;
	}

	public String getLongUri(Namespacer namespacer) {
		return EntityManager.getLongUri(namespacer, this);
	}

	public Namespacer getLongUri() {
		return longUri;
	}

	public void setMeta(boolean isMeta) {
		this.isMeta = isMeta;
	}

	public boolean isMeta() {
		return isMeta || getUri().startsWith(":meta");
	}

	public String getName() {
		return name;
	}

	public int compareTo(Entity entity2) {
		return this.getUri().compareTo(entity2.getUri());
	}

	public void setCount(Integer count) {
		if (count == null) {
			count = 0;
		}
		this.count = count;
	}

	public Integer getCount() {
		return count;
	}

	public void setLongUri(Namespacer longUri) {
		this.longUri = longUri;
	}
}
