function change(homeInp, friendsInp, messagesInp, profileInp) {
	
	if (document.getElementById("navigator").className === "navigator") {

		/*
		1. change the class namefrom regular to regular responsive
		2. remove the existing image tag
		3. create a new tag for a header and append to it
		*/

		var idHome = document.getElementById("home");
		if (idHome.className === "regular") {
			idHome.className = "responsive-min";

			const homeEle = document.getElementById("homeIcon");
			homeEle.remove();

			var tagHome = document.createElement("span");
			tagHome.innerText = homeInp;
			var elmHome = document.getElementById("home");
			elmHome.appendChild(tagHome);
		}

		var idFriends = document.getElementById("friends");
		if (idFriends.className === "regular") {
			idFriends.className = "responsive-min";
				
			const friendsEle = document.getElementById("friendsIcon");
			friendsEle.remove();

			var tagHome = document.createElement("span");
			tagHome.innerText = friendsInp;
			var elmHome = document.getElementById("friends");
			elmHome.appendChild(tagHome);
		}
		
		var idMessages = document.getElementById("messages");
		if (idMessages.className === "regular") {
			idMessages.className = "responsive-min";
				
			const messagesEle = document.getElementById("messagesIcon");
			messagesEle.remove();

			var tagHome = document.createElement("span");
			tagHome.innerText = messagesInp;
			var elmHome = document.getElementById("messages");
			elmHome.appendChild(tagHome);
		}
		
		var idProfile = document.getElementById("profile");
		if (idProfile.className === "regular") {
			idProfile.className = "responsive-min";
				
			const profileEle = document.getElementById("profileIcon");
			profileEle.remove();
				
			var tagHome = document.createElement("span");
			tagHome.innerText = profileInp;
			var elmHome = document.getElementById("profile");
			elmHome.appendChild(tagHome);
		}
		
		var idLogout = document.getElementById("logout");
		if (idLogout.className === "signout") {
			idLogout.className = "responsive-min";
		}

		if(document.getElementById("navigator").className === "navigator")
			document.getElementById("navigator").className = "navigator-responsive";
		else
			document.getElementById("navigator").className = "navigator responsive";

		document.getElementById("header").className += " responsive";
		document.getElementById("nav").className += "-responsive";
			
	}	else if(document.getElementById("home").className === "responsive-max"){
		var idHome = document.getElementById("home");
			idHome.className = "responsive-min";

		var idFriends = document.getElementById("friends");
			idFriends.className = "responsive-min";
		
		var idMessages = document.getElementById("messages");
			idMessages.className = "responsive-min";
				
		var idProfile = document.getElementById("profile");
			idProfile.className = "responsive-min";
		
		var idLogout = document.getElementById("logout");
			idLogout.className = "responsive-min";
			
		if(document.getElementById("header").className === "header responsive")
			document.getElementById("header").className = "header";
		else
			document.getElementById("header").className = "header responsive"
		
		if(document.getElementById("navigator").className === "navigator responsive")
			document.getElementById("navigator").className = "navigator-responsive";
		else
		document.getElementById("navigator").className = "navigator responsive";
		
	}else if(document.getElementById("home").className === "responsive-min"){
		var idHome = document.getElementById("home");
			idHome.className = "responsive-max";

		var idFriends = document.getElementById("friends");
			idFriends.className = "responsive-max";
		
		var idMessages = document.getElementById("messages");
			idMessages.className = "responsive-max";
				
		var idProfile = document.getElementById("profile");
			idProfile.className = "responsive-max";
		
		var idLogout = document.getElementById("logout");
			idLogout.className = "responsive-max";
			
		if(document.getElementById("header").className === "header responsive")
			document.getElementById("header").className = "header";
		else
			document.getElementById("header").className = "header responsive";
		
		if(document.getElementById("navigator").className === "navigator responsive")
			document.getElementById("navigator").className = "navigator-responsive";
		else
			document.getElementById("navigator").className = "navigator responsive";
	}
}
