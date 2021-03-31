<!-- 
Author : Abhishek Samuel Y
email :	abhishek18samuel@gmail.com
 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="stags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="sforms"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Just Chat</title>
<link rel="stylesheet" type="text/css" href="css/home.css" />
<link rel="icon" type="image/png" href="img/favicon.png">
</head>
<body>
	<div class="navigator">
		<h1 id="logo_nav">
			<stags:message code="app.header" />
		</h1>
		<div class="dropdown" id="language">
			<button class="dropbtn"><stags:message code="app.button.changelanguage"/></button>
			<div class="dropdown-content">
				<a href="home?locale=en_US"><img src="img/English.png" width="20" height="12"> <stags:message code="homepage.link.changeToEnglish"/></a> 
				<a href="home?locale=fr_FR"><img src="img/French.png" width="20" height="12"> <stags:message code="homepage.link.changeToFrench"/></a>
			</div>
		</div>
	</div>
	<div id="content">
		<div id="logo">
			<h1 id="heading"><stags:message code="app.header" /></h1>
			<p id="title"><stags:message code="app.text" /></p>
		</div>
		<div id="form">
			<div id="card">
				<sforms:form action="submitUserLogin" modelAttribute="login">
				
				<stags:message code="login.email" var="placeholderemail" />
				<sforms:input path="email" placeholder="${placeholderemail}" size="25" id="textDesign" />
					<sforms:errors path="email" />${email}<br />
					
					<stags:message code="login.password" var="placeholderpassword" />
					<sforms:input path="password" placeholder="${placeholderpassword}" size="25"
						id="textDesign" />
					<sforms:errors path="password" />${password}<br />
					<stags:message code="login.button.login" var="placeholderlogin" />
					
					<input type="Submit" value="${placeholderlogin}" id="buttonDesign" class="login">
					
				</sforms:form>
				
				<a href="#" id="link"><stags:message code="login.link.forgotPassword"/></a>
				<hr style="width: 80%">
				<stags:message code="login.button.signup" var="placeholdersignup" />
				<form action="signupPage">
					<input type="submit" value="${placeholdersignup}" id="buttonDesign"
						class="btn btn-default btn-lg btn-block responsive-width signup" />
				</form>
				</div>
		</div>
	</div>
</body>
</html>