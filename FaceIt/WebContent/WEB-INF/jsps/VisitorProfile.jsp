<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib uri="http://www.springframework.org/tags" prefix="stags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1><stags:message code="app.header"/></h1>
	<a href="showUser"><stags:message code="user.user"/></a><br/>
	<a href="friends"><stags:message code="user.friends"/></a><br/>
	<a href="signout"><stags:message code="user.signout"/></a><br/>
		<h3><stags:message code="account.header.accountInfo"/></h3>
		<stags:message code="account.email"/> : ${user.email}<br/>
		<stags:message code="account.username"/> : ${user.userName}<br/>
		<h3><stags:message code="account.header.personalInfo"/></h3>
		<stags:message code="account.fname"/> : ${user.firstName}<br/>
		<stags:message code="account.lname"/> : ${user.lastName}<br/>
		<stags:message code="account.city"/> : ${user.city}<br/>
		<stags:message code="account.dob"/> : ${user.dob}<br/>
		<stags:message code="account.gender"/>${user.gender}<br/>
		<h3><stags:message code="account.header.profileInfo"/></h3>
		<stags:message code="account.aboutme"/> : ${user.aboutMe}<br/>
	<a href="changeDp">change profile picture</a><br/>
	<a href="account">edit profile</a>
</body>
</html>