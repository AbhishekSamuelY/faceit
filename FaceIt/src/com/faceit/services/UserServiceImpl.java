package com.faceit.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faceit.beans.Friend;
import com.faceit.beans.Message;
import com.faceit.beans.User;
import com.faceit.beans.WallIt;
import com.faceit.daos.UserDAO;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDAO dao;
	
	public boolean checkUserByEmail(String email) {
		return dao.checkUserByEmail(email);
	}

	public boolean createUser(User user) {
		return dao.createUser(user);
	}

	public User getUserByEmail(String email) {
		return dao.getUserByEmail(email);
	}

	public boolean updateUser(User user) {
		return dao.updateUser(user);
	}

	public List<User> getAllUsers() {
		return dao.getAllUsers();
	}

	public List<Friend> getUserFriends(Long userSl) {
		return dao.getUserFriends(userSl);
	}

	public boolean createUserFriend(User user) {
		return dao.createUserFriend(user);
	}

	public boolean deleteUser(User user) {
		return dao.deleteUser(user);
	}

	public boolean deleteFriend(User user) {
		return dao.deleteFriend(user);
	}

	public User getUser(Long sl) {
		return dao.getUser(sl);
	}

	public boolean updateUsersSharedcontacts(User user) {
		return dao.updateUsersSharedcontacts(user);
	}

	public boolean saveMessage(Message message) {
		return dao.saveMessage(message);
	}

	public boolean deleteMessage(Message message) {
		return dao.deleteMessage(message);
	}

	public List<Message> getSentMessage(Long userSl) {
		return dao.getSentMessage(userSl);
	}

	public List<Message> getRecievedMessage(Long userSl) {
		return dao.getRecievedMessage(userSl);
	}

	public List<Message> getAllMessage() {
		return dao.getAllMessage();
	}

	public String getUserProfliePic(Long sl) {
		return dao.getUserProfliePic(sl);
	}

	public Map<Long, String> getAllSlUsernames() {
		return dao.getAllSlUsernames();
	}

	public List<Long> getAllUserSl() {
		return dao.getAllUserSl();
	}

}
