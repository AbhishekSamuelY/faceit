<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="stags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="sforms"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Just Chat</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="css/account.css" />
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

			<h1 class="mainheader">${user.userName}'s profile</h1>
			<div class="container">
				<sforms:form action="submitAccount" modelAttribute="user"
					enctype="multipart/form-data">
					<h3>
						<stags:message code="account.header.accountInfo" />
					</h3>
					<div class="row">
						<div class="col-25">
							<label for="fname"><stags:message code="account.email" /></label>
						</div>
						<div class="col-75">
							<sforms:input path="email" disabled="true" id="fname" />
							<sforms:errors path="email" />
						</div>
					</div>
					<div class="row">
						<div class="col-25">
							<label for="fname"><stags:message code="account.username" /></label>
						</div>
						<div class="col-75">
							<sforms:input path="userName" />
							<sforms:errors path="userName" />
						</div>
					</div>
					<div class="row">
						<div class="col-25">
							<label for="fname"><stags:message code="account.password" /></label>
						</div>
						<div class="col-75">
							<sforms:input path="pasword" />
							<sforms:errors path="pasword" />${password}<br />
						</div>
					</div>
					<div class="row">
						<div class="col-25">
							<label for="fname"><stags:message
									code="account.confirmPassword" /></label>
						</div>
						<div class="col-75">
							<sforms:input path="reTypePassword" />
							<sforms:errors path="reTypePassword" />${password}<br />
						</div>
					</div>
					<h3>
						<stags:message code="account.header.personalInfo" />
					</h3>
					<div class="row">
						<div class="col-25">
							<label for="fname"><stags:message code="account.fname" /></label>
						</div>
						<div class="col-75">
							<sforms:input path="firstName" />
							<sforms:errors path="firstName" />
						</div>
					</div>
					<div class="row">
						<div class="col-25">
							<label for="fname"><stags:message code="account.lname" /></label>
						</div>
						<div class="col-75">
							<sforms:input path="lastName" />
							<sforms:errors path="lastName" />
						</div>
					</div>
					<div class="row">
						<div class="col-25">
							<label for="fname"><stags:message code="account.city" /></label>
						</div>
						<div class="col-75">
							<sforms:input path="city" />
							<sforms:errors path="city" />
						</div>
					</div>
					<div class="row">
						<div class="col-25">
							<label for="fname"><stags:message code="account.dob" /></label>
						</div>
						<div class="col-75">
							<sforms:input path="dob" type="date" />
							<sforms:errors path="dob" />${date}<br />
						</div>
					</div>
					<div class="row">
						<div class="col-25">
							<label for="fname"><stags:message code="account.gender" /></label>
						</div>
						<div class="col-75">
							<stags:message code="account.male" />
							<sforms:radiobutton path="gender" value="male" />
							<stags:message code="account.female" />
							<sforms:radiobutton path="gender" value="female" />
							<sforms:errors path="gender" />
						</div>
					</div>
					<input type="submit" value="update" />
				</sforms:form>
			</div>
		</div>
		<div class="col-1"></div>
	</div>

</body>
</html>