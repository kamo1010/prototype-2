var socket;
var currentUser;
var credentials;
var token;
var request;
var LOGIN;

document.getElementById("login_btn").addEventListener("click", logIn); // is ok
document.getElementById("log_out_btn").addEventListener("click", logOut); // is ok
document.getElementById("creat_topic_btn").addEventListener("click", createTopic);
document.getElementById("subscription_topic_btn").addEventListener("click", subscribe);
document.getElementById("post_message_btn").addEventListener("click", postNewMessage); //is ok
/*
 * ajouter les gens qui se connectent dans la barre de côté ajouter les topics
 * abbonés qand on s'abonne envoyer message quand on crée topic, s'abonne, crée
 * un user, et post message
 */

function logIn() {
    if (token === undefined) {
        credentials = {
            login: document.getElementById("login").value,
            password: document.getElementById("password").value
        };
        LOGIN = credentials.login;
        console.log(LOGIN);

        request = new XMLHttpRequest();
        request.open("POST", "http://" + window.location.hostname +
            ":8080/prototype-two/rest/auth/in");
        request.setRequestHeader("content-Type", "application/json");
        request.onreadystatechange = function() {
            if (request.readyState === XMLHttpRequest.DONE) {
                switch (request.status) {
                    case 200:
                        currentUser = credentials.login;
                        token = request.responseText;
                        request = new XMLHttpRequest();
                        request.open("GET", "http://" + window.location.hostname +
                            ":8080/prototype-two/rest/" + LOGIN + "/notifications");
                        request.setRequestHeader("content-Type", "application/json");
                        request.setRequestHeader("token", token);
                        request.send();
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
    socket = new WebSocket("ws://" + window.location.hostname +
        ":8080/prototype-two/" + credentials.login);
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
                    break;
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
        request.open("PUT", "http://" + window.location.hostname +
            ":8080/prototype-two/rest/auth/out");
        request.setRequestHeader("token", token);
        request.send();
        console.log(request.status)
        if (request.status === 200) {
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
        var elementToBeAdded = "<li>" +
            "<div class=\"media text-muted pt-3\"><svg class=\"bd-placeholder-img mr-2 rounded\" width=\"16\" height=\"16\" xmlns=\"http://www.w3.org/2000/svg\" preserveAspectRatio=\"xMidYMid slice\" focusable=\"false\" role=\"img\" aria-label=\"Placeholder: 16x16\">" +
            "<title>Placeholder</title>" +
            "<rect width=\"100%\" height=\"100%\" fill=\"#007bff\"></rect>" +
            "</svg>" +
            "<p class=\"media-body pb-3 mb-0 small lh-125 border-bottom border-gray\">" +
            "<strong class=\"d-block text-gray-dark\">" + whole[i] + "</strong>" +
            "</p>" +
            "</div></li>";
        contacts.innerHTML = contacts.innerHTML + elementToBeAdded;
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
    for (var i = 0; i < whole.topicsNames.length; i++) {
        var topicElement = document.createElement("li");
        topicElement.setAttribute("class", "list-group-item d-flex justify-content-between align-items-center");
        topicElement.innerHTML = whole.topicsNames[i];
        document.getElementById("properties_subscriber_topics_list").appendChild(topicElement);
    }
}

function postNewMessage() {
    message = {
        topic: document.getElementById("message_topic").value,
        payload: document.getElementById("message_payload").value
    };
    if (message.payload !== undefined && message.payload !== "" && message.topic !== undefined && message.topic !== "") {
        request = new XMLHttpRequest();
        request.open("POST", "http://" + window.location.hostname +
            ":8080/prototype-two/rest/" + LOGIN + "/post");
        request.setRequestHeader("content-Type", "application/json");
        request.setRequestHeader("token", token);
        request.send(JSON.stringify(message));
    }
}

function createAccount() { //not available in this version
    console.log("createAccount");
}

function createTopic() {
    var message = document.getElementById("input_topic");
    console.log("createTopic");
    request = new XMLHttpRequest();
    request.open("POST", "http://" + window.location.hostname +
        ":8080/prototype-two/rest/" + LOGIN + "/topics");
    request.setRequestHeader("content-Type", "application/json");
    request.setRequestHeader("token", token);
    request.send(JSON.stringify(message));
}

function subscribe() {
    console.log("subscribe");
    request = new XMLHttpRequest();
    request.open("PUT", "http://" + window.location.hostname +
        ":8080/prototype-two/rest/" + LOGIN + "/subscribe");
    request.setRequestHeader("content-Type", "application/json");
    request.setRequestHeader("token", token);
    request.send(JSON.stringify(message));
}

function displayNewMessage(msg) {
    console.log(msg);
    var list = document.getElementById("message_list");
    var elementToBeAdded =
        "<div class=\"media text-muted pt-3\"><svg class=\"bd-placeholder-img mr-2 rounded\" width=\"32\" height=\"32\" xmlns=\"http://www.w3.org/2000/svg\" preserveAspectRatio=\"xMidYMid slice\" focusable=\"false\" role=\"img\" aria-label=\"Placeholder: 32x32\">" +
        "<title>Placeholder</title>" +
        "<rect width=\"100%\" height=\"100%\" fill=\"#007bff\"></rect>" +
        "</svg>" +
        "<p class=\"media-body pb-3 mb-0 small lh-125 border-bottom border-gray\">" +
        "<strong class=\"d-block text-gray-dark\">#" + msg.topic + "</strong>" +
        "<strong class=\"d-block text-gray-dark\">@" + msg.author + " (" + msg.editionDate + ")" + "</strong>" +
        msg.payload +
        "</p>" +
        "</div>";
    list.innerHTML = elementToBeAdded + list.innerHTML;
}