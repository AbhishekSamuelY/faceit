package com.faceit.beans;

import java.util.Map;

public class ShowFriendsView {
	
	private String profilepic;
	private String friendName;
	private Long friendSl;
	private String friendsSince;
	private int mutualfriends;
	private Map<Long, String> friends;
	
	public String getProfilepic() {
		return profilepic;
	}
	public void setProfilepic(String profilepic) {
		this.profilepic = profilepic;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public Long getFriendSl() {
		return friendSl;
	}
	public void setFriendSl(Long friendSl) {
		this.friendSl = friendSl;
	}
	public String getFriendsSince() {
		return friendsSince;
	}
	public void setFriendsSince(String friendsSince) {
		this.friendsSince = friendsSince;
	}
	public int getMutualfriends() {
		return mutualfriends;
	}
	public void setMutualfriends(int mutualfriends) {
		this.mutualfriends = mutualfriends;
	}
	public Map<Long, String> getFriends() {
		return friends;
	}
	public void setFriends(Map<Long, String> friends) {
		this.friends = friends;
	}
	
}
