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
<link rel="stylesheet" type="text/css" href="css/messages.css" />
<link rel="stylesheet" type="text/css" href="css/navigation.css" />
<link rel="stylesheet" type="text/css" href="css/tabs.css">
<script type="text/javascript" src="script/tabs.js"></script>
<link rel="icon" type="image/png" href="img/favicon.png">
<script type="text/javascript" src="script/responsiveNavbar.js"></script>
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
			<div class="sendMessage">
					<sforms:form action="sendMessage" modelAttribute="message">
	
	
						<jtags:choose>
							<jtags:when test="${jfun:length(freindsList) == 0}">
								<jtags:set var="placeholderfriend"
									value="you have no friends yet!" />
							</jtags:when>
							<jtags:otherwise>
								<stags:message code="message.friend" var="placeholderfriend" />
							</jtags:otherwise>
						</jtags:choose>
	
						<sforms:select path="email" id="dropdownDesign">
							<sforms:option value="" label="${placeholderfriend}"
								id="dropdownitemsDesign" />
							<sforms:options items="${freindsList}" />
						</sforms:select>

					<stags:message code="message.message" var="placeholdermessage" />
					<sforms:input path="message" placeholder="${placeholdermessage}"
						id="textDesign" />
					<sforms:errors path="message" />

					<stags:message code="message.send" var="placeholdersend" />
					<input type="submit" value="${placeholdersend}" size="25"
						id="submitDesign">
				</sforms:form>
			</div>
			<div id="heading">
				<h1>
					<stags:message code="messages.header" />
				</h1>
			</div>
			<div id="mymessages">
				<div class="w3-row">
					<a href="javascript:void(0)"
						onclick="openMessages(event, 'Inbox');">
						<div
							class="w3-third tablink w3-bottombar  w3-border-red w3-hover-light-grey w3-padding">Inbox</div>
					</a> <a href="javascript:void(0)"
						onclick="openMessages(event, 'Sent');">
						<div
							class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Sent</div>
					</a>
				</div>
				<div id="Inbox" class="w3-container messagetype w3-border-red"
					style="display: block;">
					<jtags:if test="${jfun:length(recievedMessagesList) == 0}">
						<p>you have no messages yet!</p>
					</jtags:if>

					<jtags:forEach items="${recievedMessagesList}" var="message">
						<div class="row-main card">
							<div class="user-col">
								<!-- profile image -->
								<img class="user-img" alt="profilepic"
									src="${message.profilepic}">
							</div>
							<div class="message-col">
								<div class="message-top">
									<div class="user-name">
										<!-- user name -->
										<span id="username">${message.sender}</span>
									</div>
									<div class="user-time">
										<!-- time stamp -->
										<span id="date">${message.date}</span>
									</div>
									<div class="user-message">
										<!-- message -->
										<span id="message">${message.message}</span>
									</div>
								</div>
								<div class="message-bottom">
									<!-- delete button -->
									<a href="deleteMessage?messageId=${message.sl}"><img
										id="deleteimg" alt="delete" src="img/delete.png"></a>
								</div>
							</div>
						</div>
					</jtags:forEach>
					<!-- pagination -->
					<div class="pagination">
						<jtags:if test="${totalReceivedPages > 0}">
							<%--For displaying previous link --%>
							<jtags:if test="${pageNumber >= 1}">
								<a class="pages text"
									href="paginateRecievedMessage?pageNumber=1">First</a>
							</jtags:if>

							<%--For displaying link --%>
							<jtags:forEach begin="1" end="${totalReceivedPages}" var="i">
								<jtags:choose>
									<jtags:when test="${i!=pageNumber}">
										<a class="pages inactive"
											href="paginateRecievedMessage?pageNumber=${i}">${i}</a>
									</jtags:when>
									<jtags:otherwise>
										<span class="pages active">${i}</span>
									</jtags:otherwise>
								</jtags:choose>
							</jtags:forEach>

							<%--For displaying Next link --%>
							<jtags:if test="${pageNumber <= totalReceivedPages}">
								<a class="pages text"
									href="paginateRecievedMessage?pageNumber=${totalReceivedPages}">Last</a>
							</jtags:if>
						</jtags:if>
					</div>
				</div>

				<div id="Sent" class="w3-container messagetype"
					style="display: none">
					<jtags:if test="${jfun:length(sentMessagesList) == 0}">
						<p>you have no messages yet!</p>
					</jtags:if>
					<jtags:forEach items="${sentMessagesList}" var="message">
						<div class="row-main card">
							<div class="user-col">
								<!-- profile image -->
								<img class="user-img" alt="profilepic"
									src="${message.profilepic}">
							</div>
							<div class="message-col">
								<div class="message-top">
									<div class="user-name">
										<!-- user name -->
										<span id="username">${message.sender}</span>
									</div>
									<div class="user-time">
										<!-- time stamp -->
										<span id="date">${message.date}</span>
									</div>
									<div class="user-message">
										<!-- message -->
										<span id="message">${message.message}</span>
									</div>
								</div>
								<div class="message-bottom">
									<!-- delete button -->
									<a href="deleteMessage?messageId=${message.sl}"><img
										id="deleteimg" alt="delete" src="img/delete.png"></a>
								</div>
							</div>
						</div>
					</jtags:forEach>
					<!-- pagination -->
					<div class="pagination">
						<jtags:if test="${totalSentPages != 0}">
							<%--For displaying previous link --%>
							<jtags:if test="${pageNumber >= 1}">
								<a class="pages text" href="paginateSentMessage?pageNumber=1">First</a>
							</jtags:if>

							<%--For displaying link --%>
							<jtags:forEach begin="1" end="${totalSentPages}" var="i">
								<jtags:choose>
									<jtags:when test="${i!=pageNumber}">
										<a class="pages inactive"
											href="paginateSentMessage?pageNumber=${i}">${i}</a>
									</jtags:when>
									<jtags:otherwise>
										<span class="pages active">${i}</span>
									</jtags:otherwise>
								</jtags:choose>
							</jtags:forEach>

							<%--For displaying Next link --%>
							<jtags:if test="${pageNumber <= totalSentPages}">
								<a class="pages text"
									href="paginateSentMessage?pageNumber=${totalSentPages}">Last</a>
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