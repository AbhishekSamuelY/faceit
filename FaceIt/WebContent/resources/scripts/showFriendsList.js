function ShowFriendList(index){

	/*revert changes to made earlier */
	var revertShowmorelinks = document.getElementsByClassName(" showmoreFriends");
	for (i = 0; i < revertShowmorelinks.length; i++) {
		revertShowmorelinks[i].className = revertShowmorelinks[i].className.replace(" clicked", "");
	}
	var revertfriendsListlinks = document.getElementsByClassName(" friendsList");
	for (i = 0; i < revertfriendsListlinks.length; i++) {
		revertfriendsListlinks[i].className = revertfriendsListlinks[i].className.replace(" clicked", "");
	}
	var tablinks = document.getElementsByClassName(index);
	for (i = 0; i < tablinks.length; i++) {
		tablinks[i].className += " clicked";
	}
	
	
}