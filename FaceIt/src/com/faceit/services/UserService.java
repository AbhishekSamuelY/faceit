package com.faceit.services;

import java.util.List;
import java.util.Map;

import com.faceit.beans.Friend;
import com.faceit.beans.Message;
import com.faceit.beans.User;

public interface UserService {

	//User operations
		public boolean checkUserByEmail(String email);
		public boolean createUser(User user);
		public User getUserByEmail(String email);
		public boolean updateUser(User user);
		public boolean deleteUser(User user);
		public User getUser(Long sl);
		public String getUserProfliePic(Long sl);
		public Map<Long,String> getAllSlUsernames();
		public List<Long> getAllUserSl();
		public List<User> getAllUsers();
		
		//User Friends Operations
		public boolean createUserFriend(User user);
		public boolean deleteFriend(User user);
		public List<Friend> getUserFriends(Long userSl);
		public boolean updateUsersSharedcontacts(User user);
		
		public boolean saveMessage(Message message);
		public boolean deleteMessage(Message message);
		public List<Message> getSentMessage(Long userSl);
		public List<Message> getRecievedMessage(Long userSl);
		public List<Message> getAllMessage();
	
}
