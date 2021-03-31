package com.faceit.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class WallIt implements Serializable {

	@Id @GeneratedValue
	private Long sl;
	private String message;
	private Long sender;
	private Long reciver;
	private int likes;
	private int dislikes;
	private Long date;
	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Long> likeList = new ArrayList<Long>();
	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Long> dislikeList = new ArrayList<Long>();
	
	public Long getSl() {
		return sl;
	}
	private void setSl(Long sl) {
		this.sl = sl;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getSender() {
		return sender;
	}
	public void setSender(Long sender) {
		this.sender = sender;
	}
	public Long getReciver() {
		return reciver;
	}
	public void setReciver(Long reciver) {
		this.reciver = reciver;
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
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	public List<Long> getLikeList() {
		return likeList;
	}
	public void setLikeList(List<Long> likeList) {
		this.likeList = likeList;
	}
	public List<Long> getDislikeList() {
		return dislikeList;
	}
	public void setDislikeList(List<Long> dislikeList) {
		this.dislikeList = dislikeList;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dislikes;
		result = prime * result + likes;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((reciver == null) ? 0 : reciver.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
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
		WallIt other = (WallIt) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (dislikes != other.dislikes)
			return false;
		if (likes != other.likes)
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (reciver == null) {
			if (other.reciver != null)
				return false;
		} else if (!reciver.equals(other.reciver))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "WallIt [message=" + message + ", sender=" + sender + ", reciver=" + reciver + ", likes=" + likes
				+ ", dislikes=" + dislikes + ", date=" + date + "]";
	}
}
