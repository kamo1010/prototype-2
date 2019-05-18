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
/*ajouter les gens qui se connectent dans la barre de côté
ajouter les topics abbonés qand on s'abonne
envoyer message quand on crée topic, s'abonne, crée un user, et post message
*/

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
    socket.onopen = function(event) {
        document.getElementById("truc").style.display = "none";
        document.getElementById("dashboard").style.display = "block";
    };
    socket.onmessage = function(event) {
        if (typeof event.data === "string") {
            var webSocketMessage = JSON.parse(event.data);
            switch (webSocketMessage.topic) {
                case "Administration":
                    displayNewMessage(webSocketMessage);
                    break;
                case "Connection":
                    var beforeStatus = document.getElementById("connected_users_title").value;
                    cleanAvailableUsers();
                    document.getElementById("connected_users_title").appendChild(beforeStatus);
                    addAvailableUsers(webSocketMessage.login);
                    break;
                case "New Topic":
                    displayTopicCreation(webSocketMessage)
                    break;
                default:
                    displayNewMessage(webSocketMessage);
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
}

function sendMessage() {
    var messageToSend = {
        topic: document.getElementById("message_topic").value,
        payload: document.getElementById("message_payload").value
    };
    if (messageToSend.topic !== "") {
        if (messageToSend.payload !== "") {
            document.getElementById("message_topic").value = "";
            document.getElementById("message_payload").value = "";

            socket.send(JSON.stringify(messageToSend));
            return;
        }
    }
    return;
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
    var contact = document.createElement("li");
    contact.setAttribute("class", "nav-item");

    var icon = document.createElement("div");
    icon.setAttribute("class", "contact");
    contact.appendChild(icon);

    var status = document.createElement("div");
    status.setAttribute("class", "status");
    icon.appendChild(status);

    var payload = document.createElement("span");
    payload.setAttribute("class", "name");
    payload.appendChild(document.createTextNode(login));
    contact.appendChild(payload);

    var contacts = document.getElementById("connected_users_title");
    contacts.appendChild(contact);
}

function cleanAvailableUsers() {
    var contacts = document.getElementById("connected_users_title");
    while (contacts.hasChildNodes()) {
        contacts.removeChild(contacts.lastChild);
    }
}