package com.faceit.beans;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class ShowFriends {

	private Long friendSl;
	private String friendUserName;
	private File friendPic;
	private int sharedContacts;
	
	public Long getFriendSl() {
		return friendSl;
	}
	public void setFriendSl(Long friendSl) {
		this.friendSl = friendSl;
	}
	public String getFriendUserName() {
		return friendUserName;
	}
	public void setFriendUserName(String friendUserName) {
		this.friendUserName = friendUserName;
	}
	public File getFriendPic() {
		return friendPic;
	}
	public void setFriendPic(File friendPic) {
		this.friendPic = friendPic;
	}
	public int getSharedContacts() {
		return sharedContacts;
	}
	public void setSharedContacts(int sharedContacts) {
		this.sharedContacts = sharedContacts;
	}
	
	@Override
	public String toString() {
		return "ShowFriends [friendSl=" + friendSl + ", friendUserName=" + friendUserName + ", friendPic=" + friendPic
				+ ", sharedContacts=" + sharedContacts + "]";
	}
	
}
