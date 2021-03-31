<!-- 
Author : Abhishek Samuel Y
email :	abhishek18samuel@gmail.com
 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="stags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="sforms"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jtags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="jfun"%>
<!DOCTYPE html>
<html>
<head>
<title>Just Chat</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="css/user.css" />
<link rel="stylesheet" type="text/css" href="css/navigation.css" />
<link rel="icon" type="image/png" href="img/favicon.png">
<script type="text/javascript" src="script/responsiveNavbar.js"></script>
<script type="text/javascript" src="script/showFriendsList.js"></script>
</head>
<body>
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
			<sforms:form action="sendWallit" modelAttribute="wallit">
				<div class="row">
					<stags:message code="user.label.wallitmessage"
						var="placeholderwallitmessage" />
					<div class="col-70">
						<sforms:input path="message"
							placeholder="${placeholderwallitmessage}" id="textarea" />
						<sforms:errors path="message" />
					</div>
					<stags:message code="user.button.wallit" var="placeholderwallit" />
					<div class="col-20">
						<input type="submit" value="${placeholderwallit}"
							id="buttonDesign"
							class="btn btn-default btn-lg btn-block responsive-width wallit">
					</div>
				</div>
			</sforms:form>
			<div class="heading">
				<h1>
					<stags:message code="user.label.header" />
				</h1>
			</div>
			<div class="messages">
				<jtags:if test="${jfun:length(userWall) == 0}">
					<p>you have no posts yet!</p>
				</jtags:if>
				<jtags:forEach items="${userWall}" var="wallit" varStatus="wallitIndex">
					<div class="row-main card">
						<div class="user-col">
							<!-- profile image -->
							<img class="user-img" alt="profilepic" src="${wallit.profilepic}">
						</div>
						<div class="message-col">
							<div class="message-top">
								<div class="user-name">
									<!-- user name -->
									<span id="username">${wallit.sender}</span>
								</div>
								<div class="user-time">
									<!-- time stamp -->
									<span id="date">${wallit.date}</span>
								</div>
								<div class="user-message">
									<!-- message -->
									<span id="message">${wallit.message}</span>
								</div>
								<jtags:if test="${wallit.likes > 0}">
									<div class="user-message">
										<!-- message -->
										<a href="javascript:void(0);"
											onclick="ShowFriendList(${wallitIndex.index});"
											id="${wallitIndex.index}"
											class="${wallitIndex.index} showmoreFriends">likes</a>
									</div>
									<div id="${wallitIndex.index}"
										class="${wallitIndex.index} friendsList">
										<jtags:set var="mapsize"
											value="${jfun:length(wallit.likeList)}" />
										<jtags:forEach items="${wallit.likeList}" var="usr"
											varStatus="index">
											<jtags:choose>
												<jtags:when test="${index.index lt mapsize-1}">
													<a id="friendsList" href="visitFriend?friendId=${usr.key}">${usr.value},</a>
												</jtags:when>
												<jtags:otherwise>
													<a id="friendsList" href="visitFriend?friendId=${usr.key}">${usr.value}</a>
												</jtags:otherwise>
											</jtags:choose>
										</jtags:forEach>
									</div>
								</jtags:if>
							</div>
						</div>
						<div class="likes-dislikes-col">
							<div class="likes-dislikes-row">
								<div class="likes-col">
									<div class="user-likes-count">
										<span id="likescount">${wallit.likes}</span>
									</div>
									<div class="likes-button">
										<a href="likeWallit?wallitId=${wallit.sl}"><img alt="like"
											src="img/like.png" id="likesimg"></a>
									</div>
								</div>
								<div class="dislikes-col">
									<div class="user-dislikes-count">
										<span id="dislikescount">${wallit.dislikes}</span>
									</div>
									<div class="dislikes-button">
										<a href="dislikeWallit?wallitId=${wallit.sl}"><img
											alt="dislike" src="img/dislike.png" id="dislikesimg"></a>
									</div>
								</div>
							</div>
						</div>
					</div>
				</jtags:forEach>
				<!-- pagination -->
				<div class="pagination">
					<jtags:if test="${totalPages > 0}">

						<%--For displaying previous link --%>
						<jtags:if test="${pageNumber >= 1}">
							<a class="pages text" href="paginatevisitorWallIt?pageNumber=1">First</a>
						</jtags:if>

						<%--For displaying link --%>
						<jtags:forEach begin="1" end="${totalPages}" var="i">
							<jtags:choose>
								<jtags:when test="${i!=pageNumber}">
									<a class="pages inactive" href="paginatevisitorWallIt?pageNumber=${i}">${i}</a>
								</jtags:when>
								<jtags:otherwise>
									<span class="pages active">${i}</span>
								</jtags:otherwise>
							</jtags:choose>
						</jtags:forEach>

						<%--For displaying Next link --%>
						<jtags:if test="${pageNumber <= totalPages}">
							<a class="pages text"
								href="paginatevisitorWallIt?pageNumber=${totalPages}">Last</a>
						</jtags:if>
					</jtags:if>
				</div>
			</div>
		</div>
		<div class="col-1"></div>

	</div>
</body>
</html>