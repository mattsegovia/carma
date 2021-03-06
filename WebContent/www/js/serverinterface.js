// This file will have all the connections needed to send/receive data from the server.

// The server address, change this to the webserver you want to use
var server = "http://localhost:8080/cs3337stu08";

// Checks if server is OK, then request server for the JSON array of all parking spots
function getList() {
	var xhttp;
	xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function () {
		if (this.readyState === 4 && this.status === 200) {
			console.log(this.responseText);
			displayListOfSpots(this.responseText);
		}
	};
	xhttp.withCredentials = true;
	xhttp.open("GET", server + "/listParkingSpots", true);
	xhttp.send();
}

/**
 * Send registration JSON to server with XMLHTTPRequest
 * @param userData, the JSON to pass to the server
 * @returns
 * TODO: add a return function, maybe start tracking userID
 */
function sendRegistration(userData) {
	var xhttp = new XMLHttpRequest();
	xhttp.open("POST", server + "/registration", true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.onreadystatechange = function () {
		if (this.readyState === 4 && this.status === 200) {
			var user = JSON.parse(this.responseText);
			document.cookie="ID=" + user.id + "; path=/";
			document.cookie="CARID=" + user.car+ "; path=/";

			window.location.href = "home.html";
		}
	};
	xhttp.send(userData);
}

/**
 * Send login JSON to server with XMLHTTPRequest
 * @param userData, the JSON to pass to the server
 * @returns
 * TODO: add a return function, maybe start tracking userID
 */
function sendLogin(userData) {
	var xhttp = new XMLHttpRequest();
	xhttp.open("POST", server + "/login", true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.onreadystatechange = function () {
		if (this.readyState === 4 && this.status === 200) {
			console.log(this.responseText);
			var user = JSON.parse(this.responseText);
			document.cookie="ID=" + user.id+ "; path=/";
			document.cookie="CARID=" + user.car+ "; path=/";

			window.location.href = "home.html";
			console.log("we here");
		}
	};
	xhttp.send(userData);
}

function sendLister(parkingData) {
	var xhttp = new XMLHttpRequest();
	xhttp.open("POST", server + "/addParkingSpot", true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.withCredentials = true;
	xhttp.onreadystatechange = function () {
		console.log("in callback");
		if(this.readyState === 4 && this.status === 200) {
			console.log(this.responseText);
			var setNum = JSON.parse(this.responseText);
			document.getElementById("spotID").value = setNum.spotID;
		}
	}
	xhttp.send(parkingData);
}

function deleteLister(parkingData){
	var xhttp = new XMLHttpRequest();
	xhttp.open("DELETE", server + "/addParkingSpot", true);
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.withCredentials = true;
	xhttp.send(parkingData);
	window.location.href = "home.html";
}

function getMatch() {
	var xhttp;
	xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function () {
		if (this.readyState === 4 && this.status === 200) {
			console.log(this.responseText);
			displayMatchLocation(this.responseText);
		}
	};
	xhttp.withCredentials = true;
	xhttp.open("GET", server + "/match", true);
	xhttp.send();
}

function sendReservation(spotID) {
	var xhttp;
	xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function () {
		if (this.readyState === 4 && this.status === 200) {
			var row = document.getElementById(spotID);
			row.classList.add("reserved");
			row.removeAttribute("onclick");
		}
	};
	xhttp.withCredentials = true;
	xhttp.open("POST", server + "/listParkingSpots?id=" + spotID, true);
	xhttp.send();
	
	
}

function sendMatchGPS(gpsLocation) {
	var xhttp;
	xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function () {
		if (this.readyState === 4 && this.status === 200) {
			console.log(this.responseText)
			var otherUserLocation = JSON.parse(this.responseText);
			//callback: set the pin on the map from otherUserLocation.latitude and .longitude
			otherUserPin(otherUserLocation.latitude, otherUserLocation.longitude);
			// also checks for null value from otherUserLocation.latitude and .longitude
		}
	};
	xhttp.withCredentials = true;
	xhttp.open("POST", server + "/match", true);
	xhttp.send(gpsLocation);
}


function getMaxMatch() {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function () {
		if (this.readyState === 4 && this.status === 200) {
			console.log(this.responseText);
			var response = JSON.parse(this.responseText);
			setMaxMatchCookie(response.maxMatch);
		}
	};
	xhttp.withCredentials = true;
	xhttp.open("GET", server + "/transition", true);
	xhttp.send();
}

function checkWinner() {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function () {
		if (this.readyState === 4 && this.status === 200) {
			console.log(this.responseText);
			var response = JSON.parse(this.responseText);
			checkIfWinner(response.winningMatch);
		}
	};
	xhttp.withCredentials = true;
	xhttp.open("POST", server + "/transition", true);
	xhttp.send();
}
