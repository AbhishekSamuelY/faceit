<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="stags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="sforms"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jtags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Just Chat</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="css/profile.css" />
<link rel="stylesheet" type="text/css" href="css/navigation.css" />
<link rel="icon" type="image/png" href="img/favicon.png">
<script type="text/javascript" src="script/responsiveNavbar.js"></script>
<script type="text/javascript" src="script/changeStatus.js"></script>
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

			<div class="main-page">
				<div class="profilepicstatus">
					<div class="profilepicform">
						<sforms:form action="saveProfilePic" enctype="multipart/form-data">
							<label for="files" class="btn"><img id="picform"
								src="img/editProfilepic.png"></label>
							<input id="files" style="visibility: hidden;" type="file"
								name="profilePic" onchange="this.form.submit()">
						</sforms:form>
					</div>
					<div class="profilepic">
						<img class="pic" alt="profilePic"
							src="pictures/${user.profilePic}">

					</div>
					<div class="status">
						<jtags:choose>
							<jtags:when test="${not empty user.aboutMe}">
								<a id="statuspresent" class="statuspresent" href="javascript:void(0);" onclick="changeStatus('statuspresent')">"${user.aboutMe}"</a>
								<jtags:set var="placeholderStatus" value="${user.aboutMe}" />
							</jtags:when>
							<jtags:otherwise>
								<a id="nostatus" class="nostatus" href="javascript:void(0);" onclick="changeStatus('nostatus')">click to write you status</a>
								<jtags:set var="placeholderStatus" value="enter your status" />
							</jtags:otherwise>
						</jtags:choose>
						<sforms:form action="saveStatus" modelAttribute="status" id="changeStatus" class="changeStatus">
						<sforms:input path="status" placeholder="${placeholderStatus}" id="textDesign" />
					<sforms:errors path="status" />
							<input type="submit" value="submit" id="buttonDesign">
						</sforms:form></div>
				</div>
				<div class="others">
					<div class="header">

						<h1 class="mainheader">${user.userName}'s profile</h1>

						<a href="account"><img width="25" height="25"
							src="img/edit.png"></a>
					</div>
					<div class="accountinfo">
						<h3 class="subheader">
							<stags:message code="account.header.accountInfo" />
						</h3>
						<div class="accountinfocontents">
							<table class="table">
								<tr class="row">
									<td><div class="label">
											<stags:message code="profile.email" />
										</div></td>
									<td><div class="value">${user.email}</div></td>
								</tr>
								<tr class="row">
									<td><div class="label">
											<stags:message code="profile.username" />
										</div></td>
									<td><div class="value">${user.userName}</div></td>
								</tr>
							</table>
						</div>
						<div class="personalinfo">
							<h3 class="subheader">
								<stags:message code="account.header.personalInfo" />
							</h3>
							<div class="personalinfocontents">
								<table class="table">
									<tr class="row">
										<td><div class="label">
												<stags:message code="profile.fname" />
											</div></td>
										<td><div class="value">${user.firstName}</div></td>
									</tr>
									<tr class="row">
										<td><div class="label">
												<stags:message code="profile.lname" />
											</div></td>
										<td><div class="value">${user.lastName}</div></td>
									</tr>
									<tr class="row">
										<td><div class="label">
												<stags:message code="profile.city" />
											</div></td>
										<td><div class="value">${user.city}</div></td>
									</tr>
									<tr class="row">
										<td><div class="label">
												<stags:message code="profile.dob" />
											</div></td>
										<td><div class="value">${user.dob}</div></td>
									</tr>
									<tr class="row">
										<td><div class="label">
												<stags:message code="profile.gender" />
											</div></td>
										<td><div class="value">${user.gender}</div></td>
									</tr>
								</table>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
		<div class="col-1"></div>
	</div>

	<script type="text/javascript">
		function submitImage() {
			document.getElementById("form").submit();
		}
	</script>
</body>
</html>