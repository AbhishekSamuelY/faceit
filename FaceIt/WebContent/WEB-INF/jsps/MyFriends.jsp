<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="stags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="sforms"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jtags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="jfun"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Just Chat</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- favicon -->
<link rel="icon" type="image/png" href="img/favicon.png">
<!-- stylesheets -->
<link rel="stylesheet" type="text/css" href="css/friends.css" />
<link rel="stylesheet" type="text/css" href="css/navigation.css" />
<link rel="stylesheet" type="text/css" href="css/tabs.css">
<!-- javascripts -->
<script type="text/javascript" src="script/responsiveNavbar.js"></script>
<script type="text/javascript" src="script/showFriendsList.js"></script>
<script type="text/javascript" src="script/tabs.js"></script>
<script type="text/javascript" src="script/usermap.js"></script>
</head>
<body onload="myFunction(${allUserEmails});">
	<div class="navigator" id="navigator">
		<div class="header" id="header">
			<h1 id="logo_nav">
				<stags:message code="app.header" />
			</h1>
			<a href="javascript:void(0);" class="icon"
				onclick="change('<stags:message code="user.link.home" />', '<stags:message code="user.link.friends" />', '<stags:message code="user.link.messages" />', '<stags:message code="user.link.profile" />');"><i
				class="fa fa-bars"></i></a>
		</div>
		<div class="nav" id="nav">
			<a href="user" id="home" class="regular"><img id="homeIcon"
				alt="home" src="img/home.png" width="22" height="22"></a> <a
				href="friends" id="friends" class="regular"><img
				id="friendsIcon" alt="friends" src="img/friends.png" width="25"
				height="25"></a> <a href="messages" id="messages" class="regular"><img
				id="messagesIcon" alt="messages" src="img/emails.png" width="25"
				height="25"></a> <a href="profile" id="profile" class="regular"><img
				id="profileIcon" alt="profile" src="img/profile.png" width="25"
				height="25"></a> <a href="signout" id="logout" class="signout"><stags:message
					code="user.link.logout" /></a>
		</div>
	</div>
	<div class="col">
		<div class="col-1"></div>
		<div class="col-2">
			<sforms:form action="findUser" modelAttribute="useremail">
				<stags:message code="myfriends.label.enteremail" />
				<sforms:input path="email" />
				<sforms:errors path="email" />
				<input type="submit" value="find">
			</sforms:form>
			<div id="heading">
				<h3>
					<stags:message code="myfriends.header.myFriends" />
				</h3>
			</div>

			<div id="mymessages">
				<div class="w3-row">
					<a href="javascript:void(0)"
						onclick="openMessages(event, 'Inbox');">
						<div
							class="w3-third tablink w3-bottombar  w3-border-red w3-hover-light-grey w3-padding">My
							Friends</div>
					</a> <a href="javascript:void(0)"
						onclick="openMessages(event, 'Sent');">
						<div
							class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Suggested
							friends</div>
					</a>
				</div>
				<div id="Inbox" class="w3-container messagetype w3-border-red"
					style="display: block;">
					<jtags:if test="${jfun:length(friendList) == 0}">
						<p>you have no friends yet!</p>
					</jtags:if>

					<jtags:forEach items="${friendList}" var="friend" varStatus="friendIndex">
						<div id="scard" class="row-main card">

							<div class="user-col">
								<!-- profile image -->
								<img class="user-img" alt="profilepic"
									src="${friend.profilepic}">
							</div>
							<div class="message-col">

								<div class="message-top">
									<a id="visitLink"
										href="visitFriend?friendId=${friend.friendSl}">
										<div class="user-name">
											<!-- user name -->
											<span id="username">${friend.friendName}</span>
										</div>
										<div class="user-time">
											<!-- time stamp -->
											<span id="date">${friend.friendsSince}</span>
										</div> <jtags:if test="${friend.mutualfriends > 0}">
											<div class="user-message">
												<!-- message -->
												<p id="mutualfriends">
													you and ${friend.friendName} share <span id="message">${friend.mutualfriends}</span>
													mutual friends <a href="javascript:void(0);" onclick="ShowFriendList(${friendIndex.index});" id="${friendIndex.index}" class="${friendIndex.index} showmoreFriends">more</a>
												</p>
											</div>
											<div id="${friendIndex.index}" class="${friendIndex.index} friendsList">
												<jtags:set var="mapsize"
													value="${jfun:length(friend.friends)}" />
												<jtags:forEach items="${friend.friends}" var="frnd"
													varStatus="index">
													<jtags:choose>
														<jtags:when test="${index.index lt mapsize-1}">
														<a id="friendsList"	href="visitFriend?friendId=${frnd.key}">${frnd.value},</a>
  														</jtags:when>
														<jtags:otherwise>
														<a id="friendsList" href="visitFriend?friendId=${frnd.key}">${frnd.value}</a>
														</jtags:otherwise>
													</jtags:choose>
												</jtags:forEach>
											</div>
										</jtags:if>
									</a>
								</div>

								<div class="message-bottom">
									<!-- delete button -->
									<a href="deleteFriend?sl=${friend.friendSl}"><img
										id="deleteimg" alt="delete" src="img/delete.png"></a>
								</div>
							</div>
						</div>
					</jtags:forEach>

					<!-- pagination -->
					<div class="pagination">
						<jtags:if test="${totalFriendsPages > 0}">
							<%--For displaying previous link --%>
							<jtags:if test="${friendsPageNumber >= 1}">
								<a class="pages text" href="paginateFriendList?pageNumber=1">First</a>
							</jtags:if>

							<%--For displaying link --%>
							<jtags:forEach begin="1" end="${totalFriendsPages}" var="i">
								<jtags:choose>
									<jtags:when test="${i!=friendsPageNumber}">
										<a class="pages inactive"
											href="paginateFriendList?pageNumber=${i}">${i}</a>
									</jtags:when>
									<jtags:otherwise>
										<span class="pages active">${i}</span>
									</jtags:otherwise>
								</jtags:choose>
							</jtags:forEach>

							<%--For displaying Next link --%>
							<jtags:if test="${friendsPageNumber <= totalFriendsPages}">
								<a class="pages text"
									href="paginateFriendList?pageNumber=${totalFriendsPages}">Last</a>
							</jtags:if>
						</jtags:if>
					</div>
				</div>
				<div id="Sent" class="w3-container messagetype"
					style="display: none">
					<jtags:if test="${jfun:length(suggestionList) == 0}">
						<p>no users yet!</p>
					</jtags:if>
					<jtags:forEach items="${suggestionList}" var="user">
						<div class="row-main card">
							<div class="user-col">
								<!-- profile image -->
								<img class="user-img" alt="profilepic" src="${user.profilepic}">
							</div>
							<div class="message-col">
								<div class="message-top">
									<a id="visitLink" href="visitFriend?friendId=${user.userSl}">
										<div class="user-name">
											<!-- user name -->
											<span id="username">${user.userName}</span>
										</div>
										<div class="user-time">
											<!-- time stamp -->
											<span id="date">${user.userSince}</span>
										</div>
									</a>
								</div>
								<div class="message-bottom">
									<!-- delete button -->
									<a href="addFriend?sl=${user.userSl}"><img id="deleteimg"
										alt="delete" src="img/addUser.png"></a>
								</div>
							</div>
						</div>
					</jtags:forEach>
					<!-- pagination -->
					<div class="pagination">
						<jtags:if test="${totalSuggestionPages != 0}">
							<%--For displaying previous link --%>
							<jtags:if test="${suggestionPageNumber >= 1}">
								<a class="pages text" href="paginateSuggestionList?pageNumber=1">First</a>
							</jtags:if>

							<%--For displaying link --%>
							<jtags:forEach begin="1" end="${totalSuggestionPages}" var="i">
								<jtags:choose>
									<jtags:when test="${i!=suggestionPageNumber}">
										<a class="pages inactive"
											href="paginateSuggestionList?pageNumber=${i}">${i}</a>
									</jtags:when>
									<jtags:otherwise>
										<span class="pages active">${i}</span>
									</jtags:otherwise>
								</jtags:choose>
							</jtags:forEach>

							<%--For displaying Next link --%>
							<jtags:if test="${suggestionPageNumber <= totalSuggestionPages}">
								<a class="pages text"
									href="paginateSuggestionList?pageNumber=${totalSuggestionPages}">Last</a>
							</jtags:if>
						</jtags:if>
					</div>
				</div>

			</div>

		</div>

		<div class="col-1"></div>
	</div>


</body>
</html>