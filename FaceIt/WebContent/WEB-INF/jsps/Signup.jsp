<!-- 
Author : Abhishek Samuel Y
email :	abhishek18samuel@gmail.com
 -->
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
<link rel="stylesheet" type="text/css" href="css/home.css" />
<link rel="icon" type="image/png" href="img/favicon.ico">
</head>
<body>
	<div class="navigator">
		<h1 id="logo_nav">
			<stags:message code="app.header" />
		</h1>
		<div class="dropdown" id="language">
			<button class="dropbtn"><stags:message code="app.button.changelanguage"/></button>
			<div class="dropdown-content">
				<a href="signupPage?locale=en_US"><img src="img/English.png"
					width="20" height="12"> English</a> <a href="signupPage?locale=fr_FR"><img
					src="img/French.png" width="20" height="12"> French</a>
			</div>
		</div>
	</div>
	<div id="content">
		<div id="logo">
			<h1 id="heading">
				<stags:message code="app.header" />
			</h1>
			<p id="title"><stags:message code="app.text" /></p>
		</div>
		<div id="form">
			<div id="card">
					<sforms:form action="submitUserSignup" modelAttribute="user">
					<stags:message code="signup.name" var="placeholderusername" />
					<sforms:input path="userName" placeholder="${placeholderusername}" size="25" id="textDesign" />
					<sforms:errors path="userName" />
					<br />
					<stags:message code="signup.email" var="placeholderemail" />
					<sforms:input path="email" placeholder="${placeholderemail}" size="25" id="textDesign" />
					<sforms:errors path="email" />${email}<br />
					<stags:message code="signup.password" var="placeholderpassword" />
					<sforms:input path="pasword" placeholder="${placeholderpassword}" size="25" id="textDesign" />
					<sforms:errors path="pasword" />${password}<br />
					<stags:message code="signup.repassword" var="placeholderretypepassword" />
					<sforms:input path="reTypePassword" placeholder="${placeholderretypepassword}" size="25" id="textDesign"  />
					<sforms:errors path="reTypePassword" />${password}<br />
					<stags:message code="signup.button.signup" var="placeholdersignup" />
					<input type="submit" value="${placeholdersignup}" id="buttonDesign" class="login">
				</sforms:form>
			</div>
		</div>
	</div>
</body>
</html>