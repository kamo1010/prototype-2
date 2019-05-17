var socket;
var currentUser;
var credentials;
var token;

document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);
document.getElementById("signOut").addEventListener("click", logOut);


function logIn() {
    if (token === undefined) {
        credentials = {
            login: document.getElementById("login").value,
            password: document.getElementById("password").value
        };
        console.log(JSON.stringify(credentials));

        var request = new XMLHttpRequest();
        request.open("POST", "http://" + window.location.hostname + ":8080/prototype-two/rest/auth/in");
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
                        document.getElementById("authentication_error").innerHTML =
                            "Oops... These credentials are invalid.";
                        break;
                    case 418:
                        currentUser = null;
                        document.getElementById("authentication_error").innerHTML =
                            "You are already connected somewhere!!";
                        break;
                    default:
                        document.getElementById("authentication_error").innerHTML =
                            "Oops... Looks like something is broken.";
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
    socket = new WebSocket("ws://" + window.location.hostname + ":8080/prototype-two/" + credentials.login);
    socket.onopen = function(data) {
        document.getElementById("truc").style.display = "none";
        document.getElementById("dashboard").style.display = "block";
    };
    socket.onmessage = function(data) {
        if (typeof event.data === "string") {
            var webSocketMessage = JSON.parse(event.data);
            switch (webSocketMessage.type) {
                case "welcomeUser":
                    displayConnectedUserMessage(webSocketMessage.payload.username);
                    break;
                case "broadcastTextMessage":
                    displayMessage(webSocketMessage.payload.username, webSocketMessage.payload.content);
                    break;
                case "broadcastConnectedUser":
                    displayConnectedUserMessage(webSocketMessage.payload.username);
                    break;
                case "broadcastDisconnectedUser":
                    displayDisconnectedUserMessage(webSocketMessage.payload.username);
                    break;
                case "broadcastAvailableUsers":
                    cleanAvailableUsers();
                    for (var i = 0; i < webSocketMessage.payload.usernames.length; i++) {
                        addAvailableUsers(webSocketMessage.payload.usernames[i]);
                    }
                    break;
            }
        }
    };
}


function logOut() {
    if (token !== "") {
        var request = new XMLHttpRequest();
        request.open("PUT", "http://" + window.location.hostname + ":8080/prototype-two/rest/auth/out");
        request.setRequestHeader("token", token);
        request.send();
        console.log(request.status)
        if (request.status === 0) {
            token = undefined;
            document.getElementById("truc").style.display = "block";
            document.getElementById("dashboard").style.display = "none";
        }
    }
    console.log("coucou");
}

function sendMessage() {
    var messageToSend = {
        topic: document.getElementById("login").value,
        payload: document.getElementById("password").value
    };
    var topic = document.getElementById("topic_name").value;
    var text = document.getElementById("message").value;
    if (text != "") {
        document.getElementById("message").value = "";

        var exchangeMessage = {
            topic: "Administrator",
            payload: text
        };

        var webSocketMessage = {
            type: "sendTextMessage"
        };

        webSocketMessage.exchangeMessage = exchangeMessage;

        socket.send(JSON.stringify(exchangeMessage));
    }
}

function displayMessage(login, text) {

    var sentByCurrentUer = currentUser === login;

    var message = document.createElement("div");
    message.setAttribute("class", sentByCurrentUer === true ? "message sent" : "message received");
    message.dataset.sender = login;

    var sender = document.createElement("span");
    sender.setAttribute("class", "sender");
    sender.appendChild(document.createTextNode(sentByCurrentUer === true ? "You" : login));
    message.appendChild(sender);

    var payload = document.createElement("span");
    payload.setAttribute("class", "payload");
    payload.appendChild(document.createTextNode(text));
    message.appendChild(payload);

    var messages = document.getElementById("messages");
    var lastMessage = messages.lastChild;
    if (lastMessage && lastMessage.dataset.sender && lastMessage.dataset.sender === login) {
        message.className += " same-sender-previous-message";
    }

    messages.appendChild(message);
    messages.scrollTop = messages.scrollHeight;
}

function displayConnectedUserMessage(login) {

    var sentByCurrentUer = currentUser === login;

    var message = document.createElement("div");
    message.setAttribute("class", "message event");

    var text = sentByCurrentUer === true ? "Welcome " + login : login + " joined the chat";
    var payload = document.createElement("span");
    payload.setAttribute("class", "payload");
    payload.appendChild(document.createTextNode(text));
    message.appendChild(payload);

    var messages = document.getElementById("messages");
    messages.appendChild(message);
}

function displayDisconnectedUserMessage(login) {

    var message = document.createElement("div");
    message.setAttribute("class", "message event");

    var text = login + " left the chat";
    var payload = document.createElement("span");
    payload.setAttribute("class", "payload");
    payload.appendChild(document.createTextNode(text));
    message.appendChild(payload);

    var messages = document.getElementById("messages");
    messages.appendChild(message);
}

function addAvailableUsers(login) {

    var contact = document.createElement("div");
    contact.setAttribute("class", "contact");

    var status = document.createElement("div");
    status.setAttribute("class", "status");
    contact.appendChild(status);

    var payload = document.createElement("span");
    payload.setAttribute("class", "name");
    payload.appendChild(document.createTextNode(login));
    contact.appendChild(payload);

    var contacts = document.getElementById("contacts");
    contacts.appendChild(contact);
}

function cleanAvailableUsers() {
    var contacts = document.getElementById("contacts");
    while (contacts.hasChildNodes()) {
        contacts.removeChild(contacts.lastChild);
    }
}