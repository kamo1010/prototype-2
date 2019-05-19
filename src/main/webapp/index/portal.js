var socket;
var currentUser;
var credentials;
var token;
var request;

document.getElementById("login_btn").addEventListener("click", logIn); //is ok
document.getElementById("log_out_btn").addEventListener("click", logOut); //is ok
document.getElementById("create_account_btn").addEventListener("click", createAccount);
document.getElementById("creat_topic_btn").addEventListener("click", createTopic);
document.getElementById("subscription_topic_btn").addEventListener("click", subscribe);
document.getElementById("post_message_btn").addEventListener("click", postNewMessage);
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

        request = new XMLHttpRequest();
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
        //remplir la section properties
    };
    socket.onmessage = function(event) {
        if (typeof event.data === "string") {
            var webSocketMessage = JSON.parse(event.data);
            switch (webSocketMessage.topic) {
                case "Administration":
                    //envoyer le message
                    displayNewMessage(webSocketMessage);
                    break;
                case "Connection": //is ok
                    //extraire la liste, sous forme d'un texte
                    //y ajouter le nouvel arrivant
                    //remettre le tout dans le message
                    cleanAvailableUsers();
                    displayAvailableUsers(webSocketMessage);
                    break;
                case "New Topic":
                    //annoncer la disponibilité d'un nouveau topic
                    displayNewMessage(webSocketMessage.payload);
                    break;
                default:
                    //envoyer le message aux concernés
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

function displayAvailableUsers(wsmessage) {
    var i;
    for (i = 0; i < wsmessage.payload.length; i++) {
        var navItem = document.createElement("li");
        navItem.setAttribute("class", "nav-item");

        var contact = document.createElement("div");
        contact.setAttribute("class", "contact");
        navItem.appendChild(contact);

        var entry = document.createElement("svg");
        entry.setAttribute("class", "bd-placeholder-img mr-2 rounded");
        contact.appendChild(entry);

        var rectangle = document.createElement("rect");
        rectangle.setAttribute("width", "100%");
        rectangle.setAttribute("height", "100%");
        rectangle.setAttribute("fill", "#007bff");
        entry.appendChild(rectangle);

        var login = document.createElement("span");
        login.appendChild(document.createTextNode(wsmessage.payload[i]));
        contact.appendChild(login);

        var contacts = document.getElementById("connected_users_title");
        contacts.appendChild(navItem);
    }
}

function cleanAvailableUsers() {
    var contacts = document.getElementById("connected_users_title");
    while (contacts.hasChildNodes()) {
        contacts.removeChild(contacts.lastChild);
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