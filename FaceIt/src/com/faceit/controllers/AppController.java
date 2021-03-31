package com.faceit.controllers;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.faceit.beans.Friend;
import com.faceit.beans.GetMessage;
import com.faceit.beans.Message;
import com.faceit.beans.MessageView;
import com.faceit.beans.ShowFriendsView;
import com.faceit.beans.ShowSuggestionView;
import com.faceit.beans.Status;
import com.faceit.beans.User;
import com.faceit.beans.UserEmail;
import com.faceit.beans.UserInput;
import com.faceit.beans.UserLogin;
import com.faceit.beans.WallIt;
import com.faceit.beans.WallItView;
import com.faceit.services.UserService;

@Controller
public class AppController {

	@Autowired
	private UserService service;
	@Autowired
	private HttpSession session;
	@Autowired
	private HttpSession temp;
	@Autowired
	ServletContext context;
	
	/*
	 * initial processing:
	 */

	Logger logger = LoggerFactory.getLogger(AppController.class);
	
	@RequestMapping({ "/", "/home" })
	public String showHome(Model model) {
		
		model.addAttribute("login", new UserLogin());
		return "Home";
	}

	@RequestMapping("submitUserLogin")
	public String login(@ModelAttribute("login") @Valid UserLogin login, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "Home";
		} else {

			boolean checklogin = service.checkUserByEmail(login.getEmail());

			if (checklogin) {
				User user = service.getUserByEmail(login.getEmail());
				if (user.getPasword().equals(login.getPassword())) {

					temp.invalidate();
					session.setAttribute("user", user);
					logger.info("set user session");

					//updateUser();
					List<WallItView> userWall = paginationWallIt(user.getSl(), 1);
					model.addAttribute("userWall", userWall);
					model.addAttribute("user", user);
					model.addAttribute("wallit", new WallIt());
					model.addAttribute("pageNumber", 1);
					model.addAttribute("totalPages", user.getWallit().size() / 10 == 0 ? user.getWallit().size() / 10
							: (user.getWallit().size() / 10) + 1);

					return "User";
				} else {
					model.addAttribute("password", "invalid password entered!");
					return "Home";
				}
			} else
				model.addAttribute("email", "email dosen't exist!");
			return "Home";
		}
	}
	

	
	@RequestMapping("signupPage")
	public String showSignup(Model model) {
		model.addAttribute("user", new User());
		return "Signup";
	}

	@RequestMapping("signout")
	public String signout(Model model) {
		session.invalidate();
		logger.info("user logout");
		model.addAttribute("login", new UserLogin());
		return "Home";
	}

	/**************************************************************************************
	 * views
	 ****************************************************************************************/

	@RequestMapping("messages")
	private String showMyMessages(Model model) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			List<String> friendsEmailList = getFriendsEmailList();
			List<MessageView> recievedMessages = paginationRecievedMessage(user.getSl(), 1);
			List<MessageView> sentMessages = paginationSentMessage(user.getSl(), 1);
			int sentMessageCount = getSentMessageCount(user.getSl());
			int receivedMessageCount = getReceivedMessageCount(user.getSl());
			model.addAttribute("user", user);
			model.addAttribute("recievedMessagesList", recievedMessages);
			model.addAttribute("sentMessagesList", sentMessages);
			model.addAttribute("pageNumber", 1);
			model.addAttribute("totalReceivedPages",
					receivedMessageCount / 10 == 0 ? receivedMessageCount / 10 : (receivedMessageCount / 10) + 1);
			model.addAttribute("totalSentPages",
					sentMessageCount / 10 == 0 ? sentMessageCount / 10 : (sentMessageCount / 10) + 1);
			model.addAttribute("freindsList", friendsEmailList);
			model.addAttribute("message", new GetMessage());
			return "MyMessages";
		} else {
			return "Home";
		}
	}

	@RequestMapping("profile")
	public String showProfile(Model model) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			model.addAttribute("user", user);
			model.addAttribute("status", new Status());
			return "Profile";
		} else {
			return "Home";
		}
	}

	@RequestMapping("visitFriend")
	public String showVisitor(Model model, Long friendId) {
		if (session.getAttribute("user") != null) {
			User user = service.getUser(friendId);
			temp.setAttribute("friend", user);
			List<WallItView> userWall = paginationWallIt(user.getSl(), 1);
			model.addAttribute("userWall", userWall);
			model.addAttribute("user", user);
			model.addAttribute("wallit", new WallIt());
			model.addAttribute("pageNumber", 1);
			model.addAttribute("totalPages", user.getWallit().size() / 10 == 0 ? user.getWallit().size() / 10
					: (user.getWallit().size() / 10) + 1);
			return "Visitor";
		} else {
			return "Home";
		}
	}

	@RequestMapping("visitorProfile")
	public String showVisitorProfile(Model model) {
		if (session.getAttribute("user") != null) {

			User friend = service.getUser((Long) temp.getAttribute("friend"));
			model.addAttribute("user", friend);
			return "VisitorProfile";
		} else {
			return "Home";
		}
	}

	@RequestMapping("saveStatus")
	public String saveStatus(@ModelAttribute("status") @Valid Status status, BindingResult result,Model model) {
		if (session.getAttribute("user") != null) {
			if(result.hasErrors()) {
				return "Profile";
			} else {
				User user = (User) session.getAttribute("user");
				if(user.getAboutMe() == null && status.getStatus() != null || !user.getAboutMe().equals(status.getStatus()) && !status.getStatus().equals("")) {
					user.setAboutMe(status.getStatus());
					boolean saveStatus = service.updateUser(user);
				}
				model.addAttribute("user", user);
				model.addAttribute("status", new Status());
				return "Profile";
			}
			
		} else {
			return "Home";
		}
	}
	
	@RequestMapping("account")
	public String showAccount(Model model) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			model.addAttribute("user", user);
			List<String> friendList = getFriendsEmailList();
			model.addAttribute("user", user);
			model.addAttribute("date", "Enter date in dd-mm-yyyy");
			return "Account";
		} else {
			return "Home";
		}
	}

	private List<String> getFriendsEmailList() {
		List<User> friendList = getFriendList();
		List<String> friendsEmailList = new ArrayList<String>();

		for (User usr : friendList) {
			friendsEmailList.add(usr.getEmail());
		}
		return friendsEmailList;
	}

	/***************************************************************************************
	 * visitor
	 ****************************************************************************************/

	@RequestMapping("paginatevisitorWallIt")
	public String showVisitorWallIt(Model model, int pageNumber) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("friend");
			List<WallItView> userWall = paginationWallIt(user.getSl(), pageNumber);
			model.addAttribute("userWall", userWall);
			model.addAttribute("user", user);
			model.addAttribute("wallit", new WallIt());
			model.addAttribute("pageNumber", pageNumber);
			model.addAttribute("totalPages", user.getWallit().size() / 10 == 0 ? user.getWallit().size() / 10
					: (user.getWallit().size() / 10) + 1);
			return "User";
		} else {
			return "Home";
		}
	}

	/***************************************************************************************
	 * user
	 ****************************************************************************************/

	@RequestMapping("submitUserSignup")
	public String signup(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "Signup";
		} else {
			boolean checkEmail = service.checkUserByEmail(user.getEmail());
			if (!checkEmail) {
				if (user.getPasword().equals(user.getReTypePassword())) {
					user.setJoiningDate(System.currentTimeMillis());
					user.setProfilePic("profile.png");
					boolean saveUser = service.createUser(user);
					if (saveUser) {
						session.setAttribute("user", user);
						model.addAttribute("wallit", new WallIt());
						return "User";
					} else {
						model.addAttribute("msg", "unable to save user!");
						return "Signup";
					}

				} else {
					model.addAttribute("password", "passwords dosent match!");
					return "Signup";
				}
			} else {
				model.addAttribute("email", "email already exists!");
				return "Signup";
			}
		}
	}
	
	

	/* when pagination is clicked */
	@RequestMapping("paginateWallIt")
	public String showWallIt(Model model, int pageNumber) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			List<WallItView> userWall = paginationWallIt(user.getSl(), pageNumber);
			model.addAttribute("userWall", userWall);
			model.addAttribute("user", user);
			model.addAttribute("wallit", new WallIt());
			model.addAttribute("pageNumber", pageNumber);
			model.addAttribute("totalPages", user.getWallit().size() / 10 == 0 ? user.getWallit().size() / 10
					: (user.getWallit().size() / 10) + 1);
			return "User";
		} else {
			return "Home";
		}
	}

	/* used in function inside appcontroller */
	@RequestMapping("paginationwallit")
	private List<WallItView> paginationWallIt(long usersl, int page) {
		User user = service.getUser(usersl);
		Map<Long, String> userNameSlMap = getUsernameSlMap();
		List<WallIt> userWallit = user.getWallit();
		List<WallItView> wallitList = new ArrayList<WallItView>();
		if (page == 1) {
			page -= 1;
		} else {
			page *= 10;
			page -= 10;
		}
		for (int i = page; i < page + 10 && i < userWallit.size(); i++) {
			WallIt wallIt = userWallit.get(i);
			WallItView view = new WallItView();
			view.setSl(wallIt.getSl());
			view.setSender(userNameSlMap.get(wallIt.getSender()));
			view.setProfilepic("pictures/" + service.getUserProfliePic(wallIt.getSender()));
			view.setMessage(wallIt.getMessage());
			view.setLikes(wallIt.getLikes());
			view.setDislikes(wallIt.getDislikes());
			view.setLikeList(getFriendsUsernameSlMap(wallIt.getLikeList()));

			LocalDate myDateObj = Instant.ofEpochMilli(wallIt.getDate()).atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy");
			String formattedDate = myDateObj.format(myFormatObj);
			view.setDate(formattedDate);
			wallitList.add(view);
		}
		return wallitList;
	}

	private Map<Long, String> getProfilepicSlMap() {

		List<User> users = service.getAllUsers();
		Map<Long, String> profilePicSlMap = new HashMap<Long, String>();
		for (User user : users) {
			profilePicSlMap.put(user.getSl(), user.getProfilePic());
		}

		return profilePicSlMap;
	}

	@RequestMapping("user")
	public String showUser(Model model) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			List<WallItView> userWall = paginationWallIt(user.getSl(), 1);
			model.addAttribute("userWall", userWall);
			model.addAttribute("user", user);
			model.addAttribute("wallit", new WallIt());
			model.addAttribute("pageNumber", 1);
			model.addAttribute("totalPages", user.getWallit().size() / 10 == 0 ? user.getWallit().size() / 10
					: (user.getWallit().size() / 10) + 1);
			return "User";
		} else {
			return "Home";
		}
	}
	
	@RequestMapping("showUser")
	public String showUser(@ModelAttribute("wallit") @Valid WallIt wallit, BindingResult result, Model model) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			List<WallIt> userWall = user.getWallit();
			model.addAttribute("userWall", userWall);
			model.addAttribute("user", user);
			model.addAttribute("wallit", new WallIt());
			return "User";
		} else {
			return "Home";
		}
	}

	private void updateUser() {
		updateSharedContacts();
		return;
	}

	/*************************************************************************************
	 * friends
	 **************************************************************************************/

	@RequestMapping("friends")
	public String showFriends(Model model) {
		if (session.getAttribute("user") != null) {
			model.addAttribute("useremail", new UserEmail());

			User user = (User) session.getAttribute("user");

			List<ShowFriendsView> friendList = paginationFriendsList(1);
			List<ShowSuggestionView> suggestionList = paginationSuggestionList(1);
			int friendListCount = getfriendListCount();
			int suggestionListCount = getsuggestionListCount();

			model.addAttribute("user", user);
			model.addAttribute("friendList", friendList);
			model.addAttribute("suggestionList", suggestionList);
			model.addAttribute("allUserEmails", getFriendsEmailList());
			model.addAttribute("friendsPageNumber", 1);
			model.addAttribute("suggestionPageNumber", 1);
			model.addAttribute("totalFriendsPages",
					friendListCount / 10 == 0 ? friendListCount / 10 : (friendListCount / 10) + 1);
			model.addAttribute("totalSuggestionPages",
					suggestionListCount / 10 == 0 ? suggestionListCount / 10 : (suggestionListCount / 10) + 1);
			model.addAttribute("useremail", new UserEmail());
			return "MyFriends";
		} else {
			return "Home";
		}
	}

	@RequestMapping("paginateSuggestionList")
	public String paginateSuggestionList(Model model, int pageNumber) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			List<ShowFriendsView> friendList = paginationFriendsList(1);
			List<ShowSuggestionView> suggestionList = paginationSuggestionList(pageNumber);
			int friendListCount = getfriendListCount();
			int suggestionListCount = getsuggestionListCount();
			model.addAttribute("user", user);
			model.addAttribute("friendList", friendList);
			model.addAttribute("suggestionList", suggestionList);
			model.addAttribute("friendsPageNumber", 1);
			model.addAttribute("suggestionPageNumber", pageNumber);
			model.addAttribute("totalFriendsPages",
					friendListCount / 10 == 0 ? friendListCount / 10 : (friendListCount / 10) + 1);
			model.addAttribute("totalSuggestionPages",
					suggestionListCount / 10 == 0 ? suggestionListCount / 10 : (suggestionListCount / 10) + 1);
			model.addAttribute("useremail", new UserEmail());
			return "MyFriends";
		} else {
			return "Home";
		}
	}

	@RequestMapping("paginateFriendList")
	public String paginateFriendList(Model model, int pageNumber) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			List<ShowFriendsView> friendList = paginationFriendsList(pageNumber);
			List<ShowSuggestionView> suggestionList = paginationSuggestionList(1);
			int friendListCount = getfriendListCount();
			int suggestionListCount = getsuggestionListCount();
			model.addAttribute("user", user);
			model.addAttribute("friendList", friendList);
			model.addAttribute("suggestionList", suggestionList);
			model.addAttribute("friendsPageNumber", pageNumber);
			model.addAttribute("suggestionPageNumber", 1);
			model.addAttribute("totalFriendsPages",
					friendListCount / 10 == 0 ? friendListCount / 10 : (friendListCount / 10) + 1);
			model.addAttribute("totalSuggestionPages",
					suggestionListCount / 10 == 0 ? suggestionListCount / 10 : (suggestionListCount / 10) + 1);
			model.addAttribute("useremail", new UserEmail());
			return "MyFriends";
		} else {
			return "Home";
		}
	}

	private int getsuggestionListCount() {
		List<User> usersList = service.getAllUsers();
		return usersList.size();
	}

	private int getfriendListCount() {
		User user = (User) session.getAttribute("user");
		return user.getFriends().size();
	}

	private List<ShowSuggestionView> paginationSuggestionList(int page) {
		User usr = (User) session.getAttribute("user");
		List<User> usersList = getSuggestionList(getSuggestionSList());
		List<ShowSuggestionView> SuggestionList = new ArrayList<ShowSuggestionView>();
		if (page == 1) {
			page -= 1;
		} else {
			page *= 10;
			page -= 10;
		}
		for (int i = page; i < page + 10 && i < usersList.size(); i++) {
			User user = usersList.get(i);
			if (user.getSl() != usr.getSl()) {
				ShowSuggestionView suggestionView = new ShowSuggestionView();
				suggestionView.setUserSl(user.getSl());
				suggestionView.setUserName(getUsernameSlMap().get(user.getSl()));
				suggestionView.setProfilepic("pictures/" + getProfilepicSlMap().get(user.getSl()));
				Long totalTime = System.currentTimeMillis() - user.getJoiningDate();
				float daysBetween = TimeUnit.DAYS.convert(totalTime, TimeUnit.MILLISECONDS);
				suggestionView.setUserSince("user since " + calculateDaysToYearsMonth(daysBetween));
				SuggestionList.add(suggestionView);
			}
		}
		return SuggestionList;
	}

	private List<User> getSuggestionList(List<Long> suggestionSList) {
		List<User> suggestionUsers= new ArrayList<User>();
		for(Long userSl : suggestionSList) {
			suggestionUsers.add(service.getUser(userSl));
		}
		return suggestionUsers;
	}

	private List<Long> getSuggestionSList() {

		User user = (User) session.getAttribute("user");
		List<Long> userSlList = service.getAllUserSl();
		List<Long> friendsList = getFriendsSlList(user.getFriends());
		userSlList.removeAll(friendsList);
		userSlList.remove(user.getSl());
		return userSlList;
	}

	private List<ShowFriendsView> paginationFriendsList(int page) {
		User user = (User) session.getAttribute("user");
		List<ShowFriendsView> friendsViewList = new ArrayList<ShowFriendsView>();
		List<Friend> friendList = user.getFriends();
		if (page == 1) {
			page -= 1;
		} else {
			page *= 10;
			page -= 10;
		}
		for (int i = page; i < page + 10 && i < friendList.size(); i++) {
			Friend friend = friendList.get(i);
			ShowFriendsView friendsView = new ShowFriendsView();
			friendsView.setFriendSl(friend.getFriendSl());
			friendsView.setFriendName(getUsernameSlMap().get(friend.getFriendSl()));
			friendsView.setProfilepic("pictures/" + getProfilepicSlMap().get(friend.getFriendSl()));

			Long totalTime = System.currentTimeMillis() - friend.getDate();
			float daysBetween = TimeUnit.DAYS.convert(totalTime, TimeUnit.MILLISECONDS);
			friendsView.setFriendsSince("friends since " + calculateDaysToYearsMonth(daysBetween));
			friendsView.setMutualfriends(friend.getSharedContacts().size());
			friendsView.setFriends(getFriendsUsernameSlMap(friend.getSharedContacts()));
			friendsViewList.add(friendsView);
		}
		return friendsViewList;
	}

	private String calculateDaysToYearsMonth(float daysBetween) {
		if (daysBetween < 30) {
			return (int) daysBetween + " days";
		} else if (daysBetween > 30 && daysBetween < 365) {
			return (int) daysBetween / 12 + " months";
		} else {
			return (int) daysBetween / 365 + "years";
		}
	}

	private Map<Long, String> getUsernameSlMap() {

		Map<Long, String> users = service.getAllSlUsernames();
		return users;
	}

	private Map<Long, String> getFriendsUsernameSlMap(List<Long> sharedContacts) {
		Map<Long, String> friendsUsernameSlMap = new HashMap<Long, String>();
		for (Long sl : sharedContacts) {
			friendsUsernameSlMap.put(sl, getUsernameSlMap().get(sl));
		}

		return friendsUsernameSlMap;
	}

	@RequestMapping("addFriend")
	public String addFriend(Model model, Long sl) {
		if (session.getAttribute("user") != null) {
			model.addAttribute("useremail", new UserEmail());

			User user = (User) session.getAttribute("user");
			List<Long> userFriendSlList = getFriendsSlList(user.getFriends());

			if (!userFriendSlList.contains(sl)) {
				Friend friend = new Friend();
				friend.setFriendSl(sl);
				friend.setDate(System.currentTimeMillis());
				List<Long> sharedContactsList = getContacts(sl);
				if (sharedContactsList.size() != 0)
					friend.getSharedContacts().addAll(sharedContactsList);

				user.getFriends().add(friend);
				boolean saveUserFriends = service.updateUser(user);

			}

			List<ShowFriendsView> friendList = paginationFriendsList(1);
			List<ShowSuggestionView> suggestionList = paginationSuggestionList(1);
			int friendListCount = getfriendListCount();
			int suggestionListCount = getsuggestionListCount();

			model.addAttribute("user", user);
			model.addAttribute("friendList", friendList);
			model.addAttribute("suggestionList", suggestionList);
			model.addAttribute("friendsPageNumber", 1);
			model.addAttribute("suggestionPageNumber", 1);
			model.addAttribute("totalFriendsPages",
					friendListCount / 10 == 0 ? friendListCount / 10 : (friendListCount / 10) + 1);
			model.addAttribute("totalSuggestionPages",
					suggestionListCount / 10 == 0 ? suggestionListCount / 10 : (suggestionListCount / 10) + 1);
			model.addAttribute("useremail", new UserEmail());

			return "MyFriends";
		} else {
			return "Home";
		}
	}

	@RequestMapping("deleteFriend")
	public String deleteUser(Model model, Long sl) {

		if (session.getAttribute("user") != null) {
			model.addAttribute("useremail", new UserEmail());

			User user = (User) session.getAttribute("user");

			Friend friend = getFriend(user.getSl(), sl);
			user.getFriends().remove(user.getFriends().indexOf(friend));

			boolean deleteUserFriends = service.updateUser(user);

			List<ShowFriendsView> friendList = paginationFriendsList(1);
			List<ShowSuggestionView> suggestionList = paginationSuggestionList(1);
			int friendListCount = getfriendListCount();
			int suggestionListCount = getsuggestionListCount();

			model.addAttribute("user", user);
			model.addAttribute("friendList", friendList);
			model.addAttribute("suggestionList", suggestionList);
			model.addAttribute("friendsPageNumber", 1);
			model.addAttribute("suggestionPageNumber", 1);
			model.addAttribute("totalFriendsPages",
					friendListCount / 10 == 0 ? friendListCount / 10 : (friendListCount / 10) + 1);
			model.addAttribute("totalSuggestionPages",
					suggestionListCount / 10 == 0 ? suggestionListCount / 10 : (suggestionListCount / 10) + 1);
			model.addAttribute("useremail", new UserEmail());

			return "MyFriends";
		} else {
			return "Home";
		}
	}

	private List<User> getFriendList() {
		User user = (User) session.getAttribute("user");
		List<Long> friendSlList = getFriendsSlList(user.getFriends());
		List<User> friendList = new ArrayList<User>();

		for (Long sl : friendSlList) {
			User usr = service.getUser(sl);
			friendList.add(usr);
		}

		return friendList;
	}

	private void updateSharedContacts() {

		User user = (User) session.getAttribute("user");

		if (user.getFriends().size() != 0) {
			List<Long> userFriendSlList = getFriendsSlList(user.getFriends());

			user = null;
			for (Long usrSl : userFriendSlList) {

				user = (User) session.getAttribute("user");
				List<Long> sharedContactsList = new ArrayList<Long>();
				List<Long> friendsFriendList = getFriendsSlList(service.getUserFriends(usrSl));

				if (friendsFriendList.size() != 0) {
					for (Long fSl : friendsFriendList) {
						if (userFriendSlList.contains(fSl))
							sharedContactsList.add(fSl);
					}

				}

				if (sharedContactsList.size() != 0) {
					if (!user.getFriends().get(getIndexFromList(usrSl, userFriendSlList)).getSharedContacts()
							.containsAll(sharedContactsList)) {
						user.getFriends().get(getIndexFromList(usrSl, userFriendSlList)).getSharedContacts()
								.addAll(sharedContactsList);
						boolean saveUser = service.updateUser(user);
					}
				}

			}
		}
	}

	private int getIndexFromList(Long Sl, List<Long> userFriendList) {
		return userFriendList.indexOf(Sl);
	}

	private List<Long> getContacts(Long friendSl) {

		List<Long> sharedContactsList = new ArrayList<Long>();
		User user = (User) session.getAttribute("user");

		if (user.getFriends().size() != 0) {
			List<Long> userFriendSlList = getFriendsSlList(service.getUserFriends(user.getSl()));
			List<Long> friendsFriendList = getFriendsSlList(service.getUserFriends(friendSl));

			if (friendsFriendList.size() != 0) {
				for (Long fSl : friendsFriendList) {
					if (userFriendSlList.contains(fSl))
						sharedContactsList.add(fSl);
				}
			}
		}
		return sharedContactsList;
	}

	/***********************************************************************************
	 * wall it
	 *************************************************************************************/
	@RequestMapping("sendWallit")
	public String sendWallit(@ModelAttribute("wallit") @Valid WallIt wallit, BindingResult result, Model model) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");

			if (result.hasErrors()) {
				return "User";
			} else {
				// check if the post was made by the user,
				// if posted by user then store the post into user data set
				if (temp.getAttribute("friend") == null) {
					wallit.setSender(user.getSl());
					wallit.setReciver(user.getSl());
					wallit.setDate(System.currentTimeMillis());
					user.getWallit().add(wallit);
					boolean saveWallIt = service.updateUser(user);
					List<WallItView> userWall = paginationWallIt(user.getSl(), 1);
					model.addAttribute("userWall", userWall);
					model.addAttribute("user", user);
					model.addAttribute("wallit", new WallIt());
					return "User";
				} else
				// if posted by visitor then store the post as visitor data set
				{
					User friend = (User) temp.getAttribute("friend");
					wallit.setSender(user.getSl());
					wallit.setReciver(friend.getSl());
					wallit.setDate(System.currentTimeMillis());
					friend.getWallit().add(wallit);
					boolean saveWallIt = service.updateUser(friend);
					List<WallItView> userWall = paginationWallIt(friend.getSl(), 1);
					model.addAttribute("userWall", userWall);
					model.addAttribute("user", user);
					model.addAttribute("wallit", new WallIt());
					return "Visitor";
				}
			}
		} else {
			return "Home";
		}
	}

	@RequestMapping("deletewallit")
	public String deleteWallIt(Model model, Long wallitId) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");

			// remove the wall it bean from the wall it list
			user.getWallit().remove(getIndexOfWallIt(wallitId, user.getSl()));
			boolean deleteWallIt = service.updateUser(user);

			List<WallItView> userWall = paginationWallIt(user.getSl(), 1);
			model.addAttribute("userWall", userWall);
			model.addAttribute("user", user);
			model.addAttribute("wallit", new WallIt());
			return "User";
		} else {
			return "Home";
		}
	}

	@RequestMapping("likeWallit")
	public String likeWallIt(Model model, Long wallitId) {
		User user = null;
		if (session.getAttribute("user") != null) {
			// check if friend session variable is empty, if it is then the user has
			// interacted himself
			if (temp.getAttribute("friend") == null) {
				user = (User) session.getAttribute("user");
				setWallItLikes(wallitId, user.getSl());
				List<WallItView> userWall = paginationWallIt(user.getSl(), 1);
				model.addAttribute("userWall", userWall);
				model.addAttribute("user", user);
				model.addAttribute("wallit", new WallIt());
				return "User";
			} else
			// check if friend session variable is not empty, if it is then the friend has
			// interacted
			{
				user = (User) temp.getAttribute("friend");
				setWallItLikes(wallitId, user.getSl());
				List<WallItView> userWall = paginationWallIt(user.getSl(), 1);
				model.addAttribute("userWall", userWall);
				model.addAttribute("user", user);
				model.addAttribute("wallit", new WallIt());
				return "Visitor";
			}
		} else {
			return "Home";
		}
	}

	@RequestMapping("dislikeWallit")
	public String dislikeWallIt(Model model, Long wallitId) {
		User user = null;
		if (session.getAttribute("user") != null) {

			// check if friend session variable is empty, if it is then the user has
			// interacted himself
			if (temp.getAttribute("friend") == null) {
				user = (User) session.getAttribute("user");
				setWallItDislikes(wallitId, user.getSl());
				List<WallItView> userWall = paginationWallIt(user.getSl(), 1);
				model.addAttribute("userWall", userWall);
				model.addAttribute("user", user);
				model.addAttribute("wallit", new WallIt());
				return "User";
			} else
			// check if friend session variable is not empty, if it is then the friend has
			// interacted
			{
				user = (User) temp.getAttribute("friend");
				setWallItDislikes(wallitId, user.getSl());
				List<WallItView> userWall = paginationWallIt(user.getSl(), 1);
				model.addAttribute("userWall", userWall);
				model.addAttribute("user", user);
				model.addAttribute("wallit", new WallIt());
				return "Visitor";
			}

		} else {
			return "Home";
		}
	}

	//get the wall it data of the friend and add users id into like dislist
	public void setWallItDislikes(Long wallitId, Long userSl) {
		User usr = (User) session.getAttribute("user");
		User user = service.getUser(userSl);
		int indexOfWallitId = getIndexOfWallIt(wallitId, user.getSl());

		// if user has already liked the post then remove him from like list decrease
		// likes then add him to dislike list and increase dislike
		if (user.getWallit().get(indexOfWallitId).getLikeList().contains(usr.getSl())) {
			user.getWallit().get(indexOfWallitId).getLikeList().remove(usr.getSl());
			user.getWallit().get(indexOfWallitId).setLikes(user.getWallit().get(indexOfWallitId).getLikes() - 1);
			user.getWallit().get(indexOfWallitId).getDislikeList().add(usr.getSl());
			user.getWallit().get(indexOfWallitId).setDislikes(user.getWallit().get(indexOfWallitId).getDislikes() + 1);
		} else
		// check if dislike list contains user, then remove user and decrement
		// dislikes
		if (user.getWallit().get(indexOfWallitId).getDislikeList().contains(usr.getSl())) {
			user.getWallit().get(indexOfWallitId).setDislikes(user.getWallit().get(indexOfWallitId).getDislikes() - 1);
			user.getWallit().get(indexOfWallitId).getDislikeList().remove(usr.getSl());
		} else
		// check if dislike list dosen't contains user, then add user and increment
		// dislikes
		if (!user.getWallit().get(indexOfWallitId).getDislikeList().contains(usr.getSl())) {
			user.getWallit().get(indexOfWallitId).setDislikes(user.getWallit().get(indexOfWallitId).getDislikes() + 1);
			user.getWallit().get(indexOfWallitId).getDislikeList().add(usr.getSl());
		}
		boolean dislikeWallIt = service.updateUser(user);
	}

	//get the wall it data of the friend and add users id into like list
	public void setWallItLikes(Long wallitId, Long userSl) {
		User usr = (User) session.getAttribute("user");
		User user = service.getUser(userSl);
		int indexOfWallitId = getIndexOfWallIt(wallitId, user.getSl());
		// if user has already disliked the post then remove him from dislike list
		// decrease dislikes then add him to like list and increase like
		if (user.getWallit().get(indexOfWallitId).getDislikeList().contains(usr.getSl())) {
			user.getWallit().get(indexOfWallitId).getDislikeList().remove(usr.getSl());
			user.getWallit().get(indexOfWallitId).setDislikes(user.getWallit().get(indexOfWallitId).getDislikes() - 1);
			user.getWallit().get(indexOfWallitId).getLikeList().add(usr.getSl());
			user.getWallit().get(indexOfWallitId).setLikes(user.getWallit().get(indexOfWallitId).getLikes() + 1);
		} else
		// check if like list contains user, then remove user and decrement
		// likes
		if (user.getWallit().get(indexOfWallitId).getLikeList().contains(usr.getSl())) {
			user.getWallit().get(indexOfWallitId).setLikes(user.getWallit().get(indexOfWallitId).getLikes() - 1);
			user.getWallit().get(indexOfWallitId).getLikeList().remove(usr.getSl());
		} else
		// check if like list dosen't contains user, then add user and increment likes
		if (!user.getWallit().get(indexOfWallitId).getLikeList().contains(usr.getSl())) {
			user.getWallit().get(indexOfWallitId).setLikes(user.getWallit().get(indexOfWallitId).getLikes() + 1);
			user.getWallit().get(indexOfWallitId).getLikeList().add(usr.getSl());
		}
		boolean dislikeWallIt = service.updateUser(user);
	}

	private List<Long> getWallItSlList(Long userSl) {
		User user = service.getUser(userSl);
		List<WallIt> wallItList = user.getWallit();
		List<Long> wallItSlList = new ArrayList<Long>();

		for (WallIt wallIt : wallItList) {
			wallItSlList.add(wallIt.getSl());
		}
		return wallItSlList;
	}

	private int getIndexOfWallIt(Long wallitId, Long userSl) {
		return getWallItSlList(userSl).indexOf(wallitId);
	}

	/*************************************************************************************
	 * account
	 ***************************************************************************************/
	@RequestMapping("submitAccount")
	public String submitUserAccount(@ModelAttribute("user") @Valid UserInput user, BindingResult result, Model model) {
		if (session.getAttribute("user") != null) {
			System.out.println("user: " + user);
			if (result.hasErrors()) {
				return "Account";
			} else {
				populateDataFromForm(user);
				User userOrg = (User) session.getAttribute("user");
				boolean update = service.updateUser(userOrg);
				if (update) {
					model.addAttribute("wallit", new WallIt());
					return "User";
				} else
					return "Account";
			}
		} else {
			return "Home";
		}
	}

	@RequestMapping("saveProfilePic")
	public String saveProfilePic(@RequestParam CommonsMultipartFile profilePic, HttpSession fileSession, Model model) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");

			String path = fileSession.getServletContext().getRealPath("/") + "resources/pictures/";
			String filename = user.getUserName() + "_" + user.getSl();

			user.setProfilePic(filename);
			boolean updateUser = service.updateUser(user);

			try {
				byte[] data = profilePic.getBytes();
				BufferedOutputStream outputStream = new BufferedOutputStream(
						new FileOutputStream(path + "/" + filename));
				outputStream.write(data);
				outputStream.flush();
				outputStream.close();
			} catch (Exception e) {
				System.out.println("saving profile pic: " + e);
			}

			user = service.getUser(user.getSl());
			model.addAttribute("user", user);
			return "Profile";
		} else {
			return "Home";
		}
	}

	/************************************************************************************
	 * Messages
	 **************************************************************************************/

	@RequestMapping("sendMessage")
	public String sendMessage(@ModelAttribute("message") @Valid GetMessage getMessage, BindingResult result,
			Model model) {
		if (session.getAttribute("user") != null) {
			if (result.hasErrors()) {
				return "MyMessages";
			} else {
				User sender = (User) session.getAttribute("user");
				User reciever = service.getUserByEmail(getMessage.getEmail());

				Message message = new Message();

				message.setSender(sender.getSl());
				message.setReceiver(reciever.getSl());
				message.setMessage(getMessage.getMessage());
				message.setDate(System.currentTimeMillis());

				boolean saveMessage = service.saveMessage(message);

				List<String> friendsEmailList = getFriendsEmailList();
				List<MessageView> recievedMessages = paginationRecievedMessage(sender.getSl(), 1);
				List<MessageView> sentMessages = paginationSentMessage(sender.getSl(), 1);
				int sentMessageCount = getSentMessageCount(sender.getSl());
				int receivedMessageCount = getReceivedMessageCount(sender.getSl());
				model.addAttribute("user", sender);
				model.addAttribute("recievedMessagesList", recievedMessages);
				model.addAttribute("sentMessagesList", sentMessages);
				model.addAttribute("pageNumber", 1);
				model.addAttribute("totalReceivedPages",
						receivedMessageCount / 10 == 0 ? receivedMessageCount / 10 : (receivedMessageCount / 10) + 1);
				model.addAttribute("totalSentPages",
						sentMessageCount / 10 == 0 ? sentMessageCount / 10 : (sentMessageCount / 10) + 1);
				model.addAttribute("freindsList", friendsEmailList);
				model.addAttribute("message", new GetMessage());
				return "MyMessages";
			}

		} else {
			return "Home";
		}
	}

	@RequestMapping("deleteMessage")
	public String deleteMessage(@ModelAttribute("message") @Valid GetMessage getMessage, BindingResult result,
			Model model, Long messageId) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			if (result.hasErrors()) {
				return "MyMessages";
			} else {
				boolean deleteMessage = service.deleteMessage(getMessage(messageId));

				List<String> friendsEmailList = getFriendsEmailList();
				List<MessageView> recievedMessages = paginationRecievedMessage(user.getSl(), 1);
				List<MessageView> sentMessages = paginationSentMessage(user.getSl(), 1);
				model.addAttribute("user", user);
				model.addAttribute("recievedMessagesList", recievedMessages);
				model.addAttribute("sentMessagesList", sentMessages);
				model.addAttribute("pageNumber", 1);
				model.addAttribute("freindsList", friendsEmailList);

				model.addAttribute("message", new GetMessage());
				return "MyMessages";
			}
		} else {
			return "Home";
		}
	}

	private Message getMessage(Long messageId) {
		List<Message> messageList = service.getAllMessage();

		for (Message message : messageList) {
			if (message.getSl() == messageId) {
				return message;
			}
		}
		return null;
	}

	private int getReceivedMessageCount(Long userSl) {
		List<Message> receivedMessageList = service.getRecievedMessage(userSl);

		return receivedMessageList.size();
	}

	private int getSentMessageCount(Long userSl) {
		List<Message> sentMessageList = service.getSentMessage(userSl);

		return sentMessageList.size();
	}

	@RequestMapping("paginateSentMessage")
	public String showSentMessages(@ModelAttribute("message") @Valid GetMessage getMessage, BindingResult result,
			Model model, int pageNumber) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			if (result.hasErrors()) {
				return "MyMessages";
			} else {
				List<String> friendsEmailList = getFriendsEmailList();
				List<MessageView> recievedMessages = paginationRecievedMessage(user.getSl(), 1);
				List<MessageView> sentMessages = paginationSentMessage(user.getSl(), pageNumber);
				int sentMessageCount = getSentMessageCount(user.getSl());
				int receivedMessageCount = getReceivedMessageCount(user.getSl());
				model.addAttribute("user", user);
				model.addAttribute("recievedMessagesList", recievedMessages);
				model.addAttribute("sentMessagesList", sentMessages);
				model.addAttribute("pageNumber", pageNumber);
				model.addAttribute("totalReceivedPages",
						receivedMessageCount / 10 == 0 ? receivedMessageCount / 10 : (receivedMessageCount / 10) + 1);
				model.addAttribute("totalSentPages",
						sentMessageCount / 10 == 0 ? sentMessageCount / 10 : (sentMessageCount / 10) + 1);
				model.addAttribute("freindsList", friendsEmailList);
				model.addAttribute("message", new GetMessage());
				return "MyMessages";
			}
		} else {
			return "Home";
		}
	}

	@RequestMapping("paginateRecievedMessage")
	public String showRecievedMessages(@ModelAttribute("message") @Valid GetMessage getMessage, BindingResult result,
			Model model, int pageNumber) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			if (result.hasErrors()) {
				return "MyMessages";
			} else {
				List<String> friendsEmailList = getFriendsEmailList();
				List<MessageView> recievedMessages = paginationRecievedMessage(user.getSl(), pageNumber);
				List<MessageView> sentMessages = paginationSentMessage(user.getSl(), 1);
				int sentMessageCount = getSentMessageCount(user.getSl());
				int receivedMessageCount = getReceivedMessageCount(user.getSl());
				model.addAttribute("user", user);
				model.addAttribute("recievedMessagesList", recievedMessages);
				model.addAttribute("sentMessagesList", sentMessages);
				model.addAttribute("pageNumber", pageNumber);
				model.addAttribute("totalReceivedPages",
						receivedMessageCount / 10 == 0 ? receivedMessageCount / 10 : (receivedMessageCount / 10) + 1);
				model.addAttribute("totalSentPages",
						sentMessageCount / 10 == 0 ? sentMessageCount / 10 : (sentMessageCount / 10) + 1);
				model.addAttribute("freindsList", friendsEmailList);
				model.addAttribute("message", new GetMessage());
				return "MyMessages";
			}
		} else {
			return "Home";
		}
	}

	private List<MessageView> paginationSentMessage(long userSl, int pageNumber) {

		Map<Long, String> userNameSlMap = getUsernameSlMap();
		List<Message> userMessage = service.getSentMessage(userSl);
		List<MessageView> messageList = new ArrayList<MessageView>();
		if (pageNumber == 1) {
			pageNumber -= 1;
		} else {
			pageNumber *= 10;
			pageNumber -= 10;
		}
		for (int i = pageNumber; i < pageNumber + 10; i++) {
			if (i < userMessage.size()) {
				Message message = userMessage.get(i);
				MessageView view = new MessageView();
				view.setSl(message.getSl());
				view.setSender(userNameSlMap.get(message.getSender()));
				view.setMessage(message.getMessage());
				view.setProfilepic("pictures/" + service.getUserProfliePic(message.getSender()));

				LocalDate myDateObj = Instant.ofEpochMilli(message.getDate()).atZone(ZoneId.systemDefault())
						.toLocalDate();
				DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy");
				String formattedDate = myDateObj.format(myFormatObj);
				view.setDate(formattedDate);
				messageList.add(view);
			}
		}
		return messageList;
	}

	private List<MessageView> paginationRecievedMessage(long userSl, int pageNumber) {

		Map<Long, String> userNameSlMap = getUsernameSlMap();
		List<Message> userMessage = service.getRecievedMessage(userSl);
		List<MessageView> messageList = new ArrayList<MessageView>();
		if (pageNumber == 1) {
			pageNumber -= 1;
		} else {
			pageNumber *= 10;
			pageNumber -= 10;
		}
		for (int i = pageNumber; i < pageNumber + 10; i++) {
			if (i < userMessage.size()) {
				Message message = userMessage.get(i);
				MessageView view = new MessageView();
				view.setSl(message.getSl());
				view.setSender(userNameSlMap.get(message.getSender()));
				view.setMessage(message.getMessage());
				view.setProfilepic("pictures/" + service.getUserProfliePic(message.getSender()));

				LocalDate myDateObj = Instant.ofEpochMilli(message.getDate()).atZone(ZoneId.systemDefault())
						.toLocalDate();
				DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy");
				String formattedDate = myDateObj.format(myFormatObj);
				view.setDate(formattedDate);
				messageList.add(view);
			}
		}
		return messageList;
	}

	/********************************************************************************
	 * Utility Functions
	 *********************************************************************************/

	@RequestMapping("findUser")
	public String searchFriendsByEmail(@ModelAttribute("useremail") @Valid UserEmail userEmail, BindingResult result,
			Model model) {
		User user = service.getUserByEmail(userEmail.getEmail());
		model.addAttribute("friendlist", user.getFriends());
		return "MyFriends";
	}

	private Friend getFriend(long userSl, Long friendSl) {

		List<Friend> userFriendList = service.getUserFriends(userSl);

		for (Friend friend : userFriendList) {
			if (friend.getFriendSl() == friendSl)
				return friend;
		}
		return null;
	}

	private List<Long> getFriendsSlList(List<Friend> users) {
		List<Long> slList = new ArrayList<Long>();
		for (Friend user : users) {
			slList.add(user.getFriendSl());
		}

		return slList;
	}

	private void populateDataFromForm(UserInput user) {

		User userOrg = (User) session.getAttribute("user");
		if (!user.getUserName().equals(null) && !userOrg.getUserName().equals(user.getUserName())) {
			userOrg.setUserName(user.getUserName());
		}
		if (!user.getPasword().equals(null) && !userOrg.getPasword().equals(user.getPasword())) {
			userOrg.setPasword(user.getPasword());
		}
		if (user.getFirstName() != null || !userOrg.getFirstName().equals(user.getFirstName())) {
			userOrg.setFirstName(user.getFirstName());
		}
		if (user.getLastName() != null || !userOrg.getLastName().equals(user.getLastName())) {
			userOrg.setLastName(user.getLastName());
		}
		if (user.getCity() != null || !userOrg.getCity().equals(user.getCity())) {
			userOrg.setCity(user.getCity());
		}
		//convert date string to long
		SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
		try {
		    Date d = f.parse(user.getDob());
		    long milliseconds = d.getTime();
		System.out.println(userOrg.getDob());
		System.out.println(milliseconds);
		if (userOrg.getDob() == null || milliseconds != 0 && userOrg.getDob() != milliseconds) {
			userOrg.setDob(milliseconds);
		}
		} catch (ParseException e) {
		    e.printStackTrace();
		}
		if (userOrg.getGender() == null || user.getGender() != null && !userOrg.getGender().equals(user.getGender())) {
			userOrg.setGender(user.getGender());
		}
		if (user.getAboutMe() != null || !userOrg.getAboutMe().equals(user.getAboutMe())) {
			userOrg.setAboutMe(user.getAboutMe());
		}
		session.setAttribute("user", userOrg);
	}

}
