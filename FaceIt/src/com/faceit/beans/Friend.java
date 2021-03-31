package com.faceit.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Friend implements Serializable {

	@Id @GeneratedValue
	private Long sl;
	private Long friendSl;
	private Long date;
	@ElementCollection(fetch = FetchType.EAGER)
	private List<Long> sharedContacts = new ArrayList<Long>();
	
	public Long getSl() {
		return sl;
	}
	private void setSl(Long sl) {
		this.sl = sl;
	}
	public Long getFriendSl() {
		return friendSl;
	}
	public void setFriendSl(Long friendSl) {
		this.friendSl = friendSl;
	}
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	public List<Long> getSharedContacts() {
		return sharedContacts;
	}
	public void setSharedContacts(List<Long> sharedContacts) {
		this.sharedContacts = sharedContacts;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((friendSl == null) ? 0 : friendSl.hashCode());
		result = prime * result + ((sl == null) ? 0 : sl.hashCode());
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
		Friend other = (Friend) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (friendSl == null) {
			if (other.friendSl != null)
				return false;
		} else if (!friendSl.equals(other.friendSl))
			return false;
		if (sl == null) {
			if (other.sl != null)
				return false;
		} else if (!sl.equals(other.sl))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Friend [sl=" + sl + ", friendSl=" + friendSl + ", date=" + date + " ]";
	}
	
}
