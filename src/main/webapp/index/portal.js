var socket;
var currentUser;
var credentials;
var token;
var request;
var LOGIN;

document.getElementById("login_btn").addEventListener("click", logIn); // is ok
document.getElementById("log_out_btn").addEventListener("click", logOut); // is
																			// ok
document.getElementById("create_account_btn").addEventListener("click",
		createAccount);
document.getElementById("creat_topic_btn").addEventListener("click",
		createTopic);
document.getElementById("subscription_topic_btn").addEventListener("click",
		subscribe);
document.getElementById("post_message_btn").addEventListener("click",
		postNewMessage);
/*
 * ajouter les gens qui se connectent dans la barre de côté ajouter les topics
 * abbonés qand on s'abonne envoyer message quand on crée topic, s'abonne, crée
 * un user, et post message
 */

function logIn() {
	if (token === undefined) {
		credentials = {
			login : document.getElementById("login").value,
			password : document.getElementById("password").value
		};
		LOGIN = credentials.login;
		console.log(LOGIN);

		request = new XMLHttpRequest();
		request.open("POST", "http://" + window.location.hostname
				+ ":8080/prototype-two/rest/auth/in");
		request.setRequestHeader("content-Type", "application/json");
		request.onreadystatechange = function() {
			if (request.readyState === XMLHttpRequest.DONE) {
				switch (request.status) {
				case 200:
					currentUser = credentials.login;
					token = request.responseText;
					openSocket();
					break;
				case 403:
					currentUser = null;
					document.getElementById("authentication_error").innerHTML = "Oops... These credentials are invalid.";
					break;
				case 418:
					currentUser = null;
					document.getElementById("authentication_error").innerHTML = "You are already connected somewhere!!";
					break;
				default:
					document.getElementById("authentication_error").innerHTML = "Oops... Looks like something is broken.";
				}
			}
		};
		request.send(JSON.stringify(credentials));
	}
	return;
}

function openSocket() {
	if (socket) {
		socket.close();
	}
	socket = new WebSocket("ws://" + window.location.hostname
			+ ":8080/prototype-two/" + credentials.login);
	socket.onopen = function(event) {
		document.getElementById("truc").style.display = "none";
		document.getElementById("dashboard").style.display = "block";
		// remplir la section properties
	};
	socket.onmessage = function(event) {
		if (typeof event.data === "string") {
			var webSocketMessage = JSON.parse(event.data);
			switch (webSocketMessage.topic) {
			case "administration":
				// envoyer le message
				displayProperties(webSocketMessage);
				break;
			case "connection": // is ok
				// extraire la liste, sous forme d'un texte
				// y ajouter le nouvel arrivant
				// remettre le tout dans le message
				cleanAvailableUsers();
				console.log("connect");
				displayAvailableUsers(webSocketMessage);
				break;
			case "new topic":
				// annoncer la disponibilité d'un nouveau topic
				displayNewMessage(webSocketMessage);
				break;
			default:
				// envoyer le message aux concernés
				displayNewMessage(webSocketMessage);
			}
		}
	};
	socket.onclose = function(event) {
		logOut();
	}
}

function logOut() {
	if (token !== "") {
		request = new XMLHttpRequest();
		request.open("PUT", "http://" + window.location.hostname
				+ ":8080/prototype-two/rest/auth/out");
		request.setRequestHeader("token", token);
		request.send();
		console.log(request.status)
		if (request.status === 0) {
			token = undefined;
			document.getElementById("truc").style.display = "block";
			document.getElementById("dashboard").style.display = "none";
		}
	}
}

function displayAvailableUsers(msg) {
	var contacts = document.getElementById("connected_users_title");
	console.log(contacts.value);
	console.log(msg);
	var whole = JSON.parse(msg.payload);
	console.log(whole[0]);
	 for (var i = 0; i < whole.length; i++) {
	 var navItem = document.createElement("li");
	 navItem.setAttribute("class", "nav-item");
	
	 var contact = document.createElement("div");
	 contact.setAttribute("class", "contact");
	 navItem.appendChild(contact);
	
	 var entry = document.createElement("svg");
	 entry.setAttribute("class", "bd-placeholder-img mr-2 rounded");
	 entry.setAttribute("width", "16");
	 entry.setAttribute("height", "16");
	 contact.appendChild(entry);
	
	 var rectangle = document.createElement("rect");
	 rectangle.setAttribute("width", "100%");
	 rectangle.setAttribute("height", "100%");
	 rectangle.setAttribute("fill", "#007bff");
	 entry.appendChild(rectangle);
	
	 var login = document.createElement("span");
	 login.appendChild(document.createTextNode(whole[i]));
	 contact.appendChild(login);
	 contacts.appendChild(navItem);
	 }
}

function cleanAvailableUsers() {
	var contacts = document.getElementById("connected_users_title");
	while (contacts.hasChildNodes()) {
		contacts.removeChild(contacts.lastChild);
	}
}

function displayProperties(msg) {
	console.log(msg);
	var whole = JSON.parse(msg.payload);
	console.log(whole.topicsNames);
	var fName = document.getElementById("properties_first_name").innerHTML + " ";
	var lName = document.getElementById("properties_last_name").innerHTML + " ";
	var loggin = document.getElementById("properties_login").innerHTML + " ";
	var list = document.getElementById("properties_subscriber_topics_list");
	document.getElementById("properties_first_name").innerHTML = fName + whole.firstName;
	document.getElementById("properties_last_name").innerHTML = lName + whole.lastName;
	document.getElementById("properties_login").innerHTML = loggin + whole.login;
	for (var i = 0;i < whole.topicsNames.length; i++){
		var topicElement = document.createElement("li");
		topicElement.setAttribute("class", "list-group-item d-flex justify-content-between align-items-center");
		topicElement.innerHTML = whole.topicsNames[i];
		document.getElementById("properties_subscriber_topics_list").appendChild(topicElement);
	}
}

function postNewMessage() {
	message = {
			topic:document.getElementById("message_topic").value,
			payload:document.getElementById("message_payload").value
	};
	console.log(JSON.stringify(message));
	console.log(LOGIN);
	if (message.payload !== undefined && message.payload !== "" && message.topic !== undefined && message.topic !== ""){
		request = new XMLHttpRequest();
		request.open("POST", "http://" + window.location.hostname
				+ ":8080/prototype-two/rest/" + LOGIN + "/post");
		request.setRequestHeader("content-Type", "application/json");
		request.setRequestHeader("token", token);
		request.send(JSON.stringify(message));
	}
}

function createAccount(){
	console.log("createAccount");
}

function createTopic(){
	console.log("createTopic");
}

function subscribe(){
	console.log("subscribe");
}

function displayNewMessage(msg){
	var whole = JSON.parse(msg.payload);
	console.log(whole);
	var list = document.getElementById("message_list");
	
	<div class="media text-muted pt-3">
    <svg class="bd-placeholder-img mr-2 rounded" width="32" height="32" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid slice" focusable="false" role="img" aria-label="Placeholder: 32x32"><title>Placeholder</title><rect width="100%" height="100%" fill="#007bff"></rect><text x="50%" y="50%" fill="#007bff" dy=".3em">32x32</text></svg>
    <p class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray">
        <strong class="d-block text-gray-dark">@username</strong> Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus.
    </p>
</div>
}
/*
 * function displayMessage(login, text) {
 * 
 * var sentByCurrentUer = currentUser === login;
 * 
 * var message = document.createElement("div"); message.setAttribute("class",
 * sentByCurrentUer === true ? "message sent" : "message received");
 * message.dataset.sender = login;
 * 
 * var sender = document.createElement("span"); sender.setAttribute("class",
 * "sender"); sender.appendChild(document.createTextNode(sentByCurrentUer ===
 * true ? "You" : login)); message.appendChild(sender);
 * 
 * var payload = document.createElement("span"); payload.setAttribute("class",
 * "payload"); payload.appendChild(document.createTextNode(text));
 * message.appendChild(payload);
 * 
 * var messages = document.getElementById("messages"); var lastMessage =
 * messages.lastChild; if (lastMessage && lastMessage.dataset.sender &&
 * lastMessage.dataset.sender === login) { message.className += "
 * same-sender-previous-message"; }
 * 
 * messages.appendChild(message); messages.scrollTop = messages.scrollHeight; }
 * 
 * function displayConnectedUserMessage(login) {
 * 
 * var sentByCurrentUer = currentUser === login;
 * 
 * var message = document.createElement("div"); message.setAttribute("class",
 * "message event");
 * 
 * var text = sentByCurrentUer === true ? "Welcome " + login : login + " joined
 * the chat"; var payload = document.createElement("span");
 * payload.setAttribute("class", "payload");
 * payload.appendChild(document.createTextNode(text));
 * message.appendChild(payload);
 * 
 * var messages = document.getElementById("messages");
 * messages.appendChild(message); }
 * 
 * function displayDisconnectedUserMessage(login) {
 * 
 * var message = document.createElement("div"); message.setAttribute("class",
 * "message event");
 * 
 * var text = login + " left the chat"; var payload =
 * document.createElement("span"); payload.setAttribute("class", "payload");
 * payload.appendChild(document.createTextNode(text));
 * message.appendChild(payload);
 * 
 * var messages = document.getElementById("messages");
 * messages.appendChild(message); }
 */