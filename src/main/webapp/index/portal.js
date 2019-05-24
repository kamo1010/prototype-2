var socket;
var currentUser;
var credentials;
var token;
var LOGIN;

document.getElementById("login_btn").addEventListener("click", logIn);
document.getElementById("log_out_btn").addEventListener("click", logOut);
document.getElementById("post_message_btn").addEventListener("click", postNewMessage);
/*
document.getElementById("creat_topic_btn").addEventListener("click", createTopic);
document.getElementById("subscription_topic_btn").addEventListener("click", subscribe);*/
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

        var request = new XMLHttpRequest();
        request.open("POST", "http://" + window.location.hostname +
            ":8080/prototype-two/rest/auth/in");
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
                    default:
                        document.getElementById("authentication_error").innerHTML = "Oops... Looks like something is broken.";
                }
            }
        };
        request.send(JSON.stringify(credentials));
    }
    return;
}

function switchDisplay() {
    document.getElementById("truc").style.display = "none";
    document.getElementById("dashboard").style.display = "block";
}

function openSocket() {
    if (socket) {
        socket.close();
    }
    socket = new WebSocket("ws://" + window.location.hostname +
        ":8080/prototype-two/" + credentials.login);
    socket.onopen = function(event) {
        switchDisplay();
        displayTextProperties();
        console.log("bonjour");
        displayPreviousMessages();
    };
    socket.onmessage = function(event) {
        if (typeof event.data === "string") {
            var webSocketMessage = JSON.parse(event.data);
            switch (webSocketMessage.topic) {
                case "administration":
                    cleanProperties();
                    displayProperties(webSocketMessage);
                    break;
                case "connection":
                    cleanAvailableUsers();
                    displayAvailableUsers(webSocketMessage);
                    break;
                case "new topic":
                    displayNewMessage(webSocketMessage);
                    break;
                default:
                    displayNewMessage(webSocketMessage);
                    break;
            }
        }
    };
    socket.onclose = function(event) {
        /*logOut();*/
    }
}

function displayTextProperties() {
    var request = new XMLHttpRequest();
    request.open("GET", "http://" + window.location.hostname +
        ":8080/prototype-two/rest/" + LOGIN);
    request.setRequestHeader("content-Type", "application/json");
    request.setRequestHeader("token", token);
    request.send();
    request.onreadystatechange = function() {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status === 200) {
                var properties = JSON.parse(request.responseText);
                var fName = document.getElementById("properties_first_name").innerHTML + " ";
                var lName = document.getElementById("properties_last_name").innerHTML + " ";
                var loggin = document.getElementById("properties_login").innerHTML + " ";
                document.getElementById("properties_first_name").innerHTML = fName + properties.firstName;
                document.getElementById("properties_last_name").innerHTML = lName + properties.lastName;
                document.getElementById("properties_login").innerHTML = loggin + properties.login;
                for (var i = 0; i < properties.topicsNames.length; i++) {
                    var topicElement = document.createElement("li");
                    topicElement.setAttribute("class", "list-group-item d-flex justify-content-between align-items-center");
                    topicElement.innerHTML = properties.topicsNames[i];
                    document.getElementById("properties_subscriber_topics_list").appendChild(topicElement);
                }
            }
        }
    }
}

function displayPreviousMessages() {
    var request = new XMLHttpRequest();
    request.open("GET", "http://" + window.location.hostname +
        ":8080/prototype-two/rest/" + LOGIN + "/notifications");
    request.setRequestHeader("content-Type", "application/json");
    request.setRequestHeader("token", token);
    request.send();
    request.onreadystatechange = function() {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status === 200) {
                var msgs = JSON.parse(request.responseText);
                console.log(msgs);
                var contacts = document.getElementById("connected_users_title");
                for (var i = 0; i < msgs.length; i++) {
                    displayNewMessage(msgs[i]);
                }
            }
        }
    }
}

function displayNewMessage(msg) {
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

function logOut() {
    if (token !== "") {
        var request = new XMLHttpRequest();
        request.open("PUT", "http://" + window.location.hostname +
            ":8080/prototype-two/rest/auth/out");
        request.setRequestHeader("token", token);
        request.send();
        request.onreadystatechange = function() {
            console.log(request.status);
            if (request.status === 200) {
                resetSession();
            }
        }
    }
}

function resetSession() {
    socket.close();
    token = undefined;
    LOGIN = undefined;
    cleanAvailableUsers();
    cleanProperties();
    cleanMessages();
    document.getElementById("truc").style.display = "block";
    document.getElementById("dashboard").style.display = "none";
}

function cleanAvailableUsers() {
    var contacts = document.getElementById("connected_users_title");
    while (contacts.hasChildNodes()) {
        contacts.removeChild(contacts.lastChild);
    }
}

function cleanProperties() {
    document.getElementById("properties_first_name").innerHTML = "First Name:";
    document.getElementById("properties_last_name").innerHTML = "Last Name:";
    document.getElementById("properties_login").innerHTML = "Login:";
    var list = document.getElementById("properties_subscriber_topics_list");
    while (list.hasChildNodes()) {
        list.removeChild(list.lastChild);
    }
}

function cleanMessages() {
    var list = document.getElementById("message_list");
    while (list.hasChildNodes()) {
        list.removeChild(list.lastChild);
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
/*
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
    var message = document.getElementById("input_topic").value;
    request = new XMLHttpRequest();
    request.open("POST", "http://" + window.location.hostname +
        ":8080/prototype-two/rest/" + LOGIN + "/topics");
    request.setRequestHeader("content-Type", "application/json");
    request.setRequestHeader("token", token);
    request.send(message);
}

function subscribe() {
    var message = document.getElementById("input_subscribe").value;
    request = new XMLHttpRequest();
    request.open("PUT", "http://" + window.location.hostname +
        ":8080/prototype-two/rest/" + LOGIN + "/subscribe");
    request.setRequestHeader("content-Type", "application/json");
    request.setRequestHeader("token", token);
    request.send(message);
}
*/