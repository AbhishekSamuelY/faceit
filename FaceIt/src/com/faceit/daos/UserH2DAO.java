package com.faceit.daos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.faceit.beans.Friend;
import com.faceit.beans.Message;
import com.faceit.beans.User;

@Repository
public class UserH2DAO implements UserDAO {

	@Autowired
	SessionFactory factory;

	public boolean checkUserByEmail(String email) {
		User user = null;
		try
		{
		Session session = factory.openSession();
		session.beginTransaction();
		Query query = session.createQuery("from User where email = :email");
		query.setParameter("email", email);
		user = (User) query.getSingleResult();
		session.getTransaction().commit();
		session.close();
		}
		catch (NoResultException e) {
		}
		if(user == null)
			return false;
		else
			return true;
	}

	public boolean createUser(User user) {
		boolean isUser = checkUserByEmail(user.getEmail());
		if (!isUser) {
			Session session = factory.openSession();
			session.beginTransaction();
			session.persist(user);
			session.getTransaction().commit();
			session.close();
			return true;
		}
		return false;
	}

	public boolean updateUser(User user) {

		Session session = factory.openSession();
		session.beginTransaction();
		session.update(user);
		session.getTransaction().commit();
		session.close();
		return true;
	}
	
	public boolean deleteUser(User user) {

		Session session = factory.openSession();
		session.beginTransaction();
		session.remove(user);
		session.getTransaction().commit();
		session.close();
		return true;
	}
	
	public User getUserByEmail(String email) {
		
		Session session = factory.openSession();
		session.beginTransaction();
		Query query = session.createQuery("from User where email = :email");
		query.setParameter("email", email);
		User user = (User) query.getSingleResult();
		session.getTransaction().commit();
		session.close();
		
		return user;
	}
	
	public User getUser(Long sl) {
		Session session = factory.openSession();
		session.beginTransaction();
		User user = session.get(User.class, sl);
		session.getTransaction().commit();
		session.close();
		return user;
	}
	
	public String getUserProfliePic(Long sl) {
		Session session = factory.openSession();
		session.beginTransaction();
		User user = session.get(User.class, sl);
		session.getTransaction().commit();
		session.close();
		return user.getProfilePic();
	}

	public List<User> getAllUsers() {

		Session session = factory.openSession();
		session.beginTransaction();
		List<User> users = session.createQuery("from User").getResultList();
		session.close();
		return users;
	}

	public boolean createUserFriend(User user) {
		
		Session session = factory.openSession();
		session.beginTransaction();
		session.persist(user);
		session.getTransaction().commit();
		session.close();
		return true;
	}

	public boolean deleteFriend(Long userSl, Friend friend) {
		Session session = factory.openSession();
		session.beginTransaction();
		List<Friend> friendsList = getUserFriends(userSl);
		int friendIndex = friendsList.indexOf(friend);
		User user = session.get(User.class, userSl);
		user.getFriends().size();
		user.getFriends().remove(friendIndex);
		session.evict(user);
		session.update(user);
		session.getTransaction().commit();
		session.close();
		return true;
	}
	public boolean deleteFriend(User user) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public List<Friend> getUserFriends(Long userSl) {
		Session session = factory.openSession();
		session.beginTransaction();
		User user = session.get(User.class, userSl);
		user.getFriends().size();
		List<Friend> friends = user.getFriends();
		session.getTransaction().commit();
		session.close();
		return friends;
	}
	
	public boolean updateUsersSharedcontacts(User user)
	{
		/*Session session = factory.openSession();
		session.beginTransaction();
		User user = session.get(User.class, userSl);
		user.getFriends().size();
		user.getFriends().remove(friendIndex);
		boolean deleteFriend = deleteFriend(user.getSl(), friend);
		user.getFriends().add(friend);
		session.update(user);
		session.save(friend);
		session.getTransaction().commit();
		session.close();*/
		return true;
	}

	public boolean saveMessage(Message message) {
			Session session = factory.openSession();
			session.beginTransaction();
			session.persist(message);
			session.getTransaction().commit();
			session.close();
			return true;
	}

	public boolean deleteMessage(Message message) {
		Session session = factory.openSession();
		session.beginTransaction();
		session.remove(message);
		session.getTransaction().commit();
		session.close();
		return true;
	}

	public List<Message> getAllMessage() {
		Session session = factory.openSession();
		session.beginTransaction();
		List<Message> messages = session.createQuery("from Message").getResultList();
		session.close();
		return messages;
	}



	public List<Message> getSentMessage(Long userSl) {
		Session session = factory.openSession();
		session.beginTransaction();
		List<Message> messages = session.createQuery("from Message WHERE sender = "+ userSl).getResultList();
		session.close();
		return messages;
	}

	public List<Message> getRecievedMessage(Long userSl) {
		Session session = factory.openSession();
		session.beginTransaction();
		List<Message> messages = session.createQuery("from Message WHERE receiver = "+userSl).getResultList();
		session.close();
		return messages;
	}

	public Map<Long, String> getAllSlUsernames() {
		Map<Long,String> slUserNameMap = new HashMap<Long,String>();
		Session session = factory.openSession();
		session.beginTransaction();
		Query query = session.createQuery("SELECT sl, userName from User");
		List<Object[]> users= query.getResultList();
	     for(Object[] user: users){
	    	 slUserNameMap.put((Long)user[0], (String)user[1]);
	     }
		session.close();
		return slUserNameMap;
	}

	public List<Long> getAllUserSl() {
		Session session = factory.openSession();
		session.beginTransaction();
		List<Long> userSlList = session.createQuery("SELECT sl from User").getResultList();
		session.close();
		return userSlList;
	}

}
