package com.faceit.beans;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class WallItView {
	
	private Long sl;
	private String message;
	private String sender;
	private String profilepic;
	private int likes;
	private int dislikes;
	private String date;
	private Map<Long, String> likeList;
	
	public Long getSl() {
		return sl;
	}
	public void setSl(Long sl) {
		this.sl = sl;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getProfilepic() {
		return profilepic;
	}
	public void setProfilepic(String profilepic) {
		this.profilepic = profilepic;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public int getDislikes() {
		return dislikes;
	}
	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Map<Long, String> getLikeList() {
		return likeList;
	}
	public void setLikeList(Map<Long, String> likeList) {
		this.likeList = likeList;
	}
	
	@Override
	public String toString() {
		return "WallItView [sl=" + sl + ", message=" + message + ", sender=" + sender + ", profilepic=" + profilepic
				+ ", likes=" + likes + ", dislikes=" + dislikes + ", date=" + date + ", likeList=" + likeList + "]";
	}

}
