function openMessages(evt, messagetype) {
	var i, x, tablinks;
	x = document.getElementsByClassName("messagetype");
	for (i = 0; i < x.length; i++) {
		x[i].style.display = "none";
	}
	tablinks = document.getElementsByClassName("tablink");
	for (i = 0; i < tablinks.length; i++) {
		tablinks[i].className = tablinks[i].className.replace(" w3-border-red", "");
		element = document.getElementsByClassName("w3-bottombar-active");
	}
	document.getElementById(messagetype).style.display = "block";
	evt.currentTarget.firstElementChild.className += " w3-border-red";

}