const PORT = 8443;
const MAPPING = "/room";
const peerConnectionConfig = {
    'iceServers': [
        {'urls': 'stun:stun.l.google.com:19302'}
    ]
};

var ws;
var localStream;
var connections = {};
var uuidInBig;
var selfView = document.getElementById("selfView");
var remoteView = document.getElementById("remoteView");
let peerId =null;
let meId = document.getElementById('userid').value;

var container = document.getElementById("remoteVideosContainer");

/**
 * this initiate websocket connection
 * it is caled on page reload
 */
function init() {

    // get a local stream, show it in a self-view and add it to be sent
    navigator.mediaDevices.getUserMedia({video: true, audio: false}).then(function (stream) {
        console.log("Stream OK");
        localStream = stream;
        selfView.srcObject = localStream;
        selfView.autoplay = true;
        ws = new WebSocket('wss://' + window.location.hostname + ':' + PORT + MAPPING);
        ws.onmessage = processWsMessage;
        ws.onopen = handleWhenOpenWs;
        ws.onclose = logMessage;
        ws.onerror = logMessage;
    }).catch(function (error) {
        console.log("Stream NOT OK: " + error.name + ': ' + error.message);
    });

}

function processWsMessage(message) {
    var signal = JSON.parse(message.data);
    logMessage(signal);
    // you have logged in
    switch (signal.type) {
        case 'init':
            handleInit(signal);
            break;
        case 'logout':
            handleLogout(signal);
            break;
        case 'offer':
            handleOffer(signal);
            break;
        case 'answer':
            handleAnswer(signal);
            break;
        case 'ice':
            handleIce(signal);
            break;
        case 'exit':
            handleExit(signal);
            break;
        case 'show_id':
            handleShowId(signal);
            break;
        case 'callToUser':
            handleCaltoUser(signal);
            break;
        case 'cancel-caller':
            handleCancel(signal);
            break;
        //
    }

}

function handleInit(signal) {
    peerId = signal.sender;
    var connection = getRTCPeerConnectionObject(peerId);

    // make an offer, and send the SDP to sender.
    connection.createOffer().then(function (sdp) {
        connection.setLocalDescription(sdp);
        console.log('Creating an offer for', peerId);
        sendMessage({
            type: "offer",
            receiver: peerId,
            sender: meId,
            data: sdp
        });
    }).catch(function (e) {
        console.log('Error in offer creation.', e);
    });

}

function handleLogout(signal) {
    peerId = signal.sender;
    if (peerId == uuidInBig) {
        remoteView.srcObject = null;
    }
    delete connections[peerId];
    var videoElement = document.getElementById(peerId);
    delete videoElement;
}

function handleOffer(signal) {
    peerId = signal.sender;
    var connection = getRTCPeerConnectionObject(peerId);
    connection.setRemoteDescription(new RTCSessionDescription(signal.data)).then(function () {
        console.log('Setting remote description by offer from ' + peerId);
        // create an answer for the peedId.
        connection.createAnswer().then(function (sdp) {
            // and after callback set it locally and send to peer
            connection.setLocalDescription(sdp);
            sendMessage({
                type: "answer",
                receiver: peerId,
                sender:meId,
                data: sdp
            });

        }).catch(function (e) {
            console.log('Error in offer handling.', e);
        });

    }).catch(function (e) {
        console.log('Error in offer handling.', e);
    });
}

function handleAnswer(signal) {
    var connection = getRTCPeerConnectionObject(signal.sender);
    connection.setRemoteDescription(new RTCSessionDescription(signal.data)).then(function () {
        console.log('Setting remote description by answer from' + signal.sender);
    }).catch(function (e) {
        console.log('Error in answer acceptance.', e);
    });
}

function handleIce(signal) {
    if (signal.data) {
        console.log('Adding ice candidate');
        var connection = getRTCPeerConnectionObject(signal.sender);
        connection.addIceCandidate(new RTCIceCandidate(signal.data));
    }
}

function handleExit(signal) {
    if (signal.data) {
        console.log('Handle exit');
        remoteView.srcObject = null;
    }
}

//
function handleShowId(signal) {
    if (signal.data) {
        console.log('Handle show id:' + signal.data);
        document.getElementById('peerIdN').textContent = signal.data;
        meId = signal.data;
    }
}

function getRTCPeerConnectionObject(uuid) {

    if (connections[uuid]) {
        return connections[uuid];
    }

    var connection = new RTCPeerConnection(peerConnectionConfig);

    connection.addStream(localStream);

    // handle on ice candidate
    connection.onicecandidate = function (event) {
        console.log("candidate is: " + event.candidate);
        if (event.candidate) {
            sendMessage({
                type: "ice",
                receiver: uuid,
                sender: meId,
                data: event.candidate
            });
        }
    };

    // handle on track / onaddstream
    connection.onaddstream = function (event) {
        console.log('Received new stream from ' + uuid);
        var video = document.createElement("video");
        container.appendChild(video);
        video.id = uuid;
        video.width = 160;
        video.height = 120;
        video.className += " videoElement";
        video.autoplay = true;
        video.srcObject = event.stream;
        video.addEventListener('click', function (event) {
            setBigVideo(uuid);
        }, false);

    };

    connections[uuid] = connection;
    return connection;
}

function setBigVideo(uuid) {
    remoteView.srcObject = document.getElementById(uuid).srcObject;
    console.log('click')
    remoteView.autoplay = true;
    if (uuidInBig && document.getElementById(uuidInBig)) {
        document.getElementById(uuidInBig).classList.remove("active");
    }
    document.getElementById(uuid).classList.add("active");
    uuidInBig = uuid;
}

function sendMessage(payload) {
    ws.send(JSON.stringify(payload));
}

function logMessage(message) {
    console.log(message);
}

function handleWhenOpenWs() {
    sendMessage({
        type: "save",
        sender: document.getElementById('userid').value,
    });

}
function disconnect() {
    console.log('Disconnecting ');
    if (ws != null) {
        ws.close();
    }
}

function handleCancel(signal) {
    alert('da bi huy')
}

function handleCaltoUser(signal) {
    document.getElementById('wrapper-caller').style.display = 'block';
    document.getElementById('title-caller').textContent= signal.sender + 'is calling...';
    document.getElementById('answer').addEventListener('click', function (event) {
        document.getElementById('wrapper-caller').style.display = 'none';
        sendMessage({
            type: "init",
            receiver: signal.sender,
            sender: signal.receiver
        });
    }, false);
    document.getElementById('cancel').addEventListener('click', function (event) {
        document.getElementById('wrapper-caller').style.display = 'none';
        sendMessage({
            type: "cancel-caller",
            receiver: signal.sender,
            sender: signal.receiver
        });
    }, false);
}

//

// start
window.onload = init;

document.getElementById('exit').addEventListener('click', function (event) {
    sendMessage({
        type: "exit",
        receiver: peerId,
        sender: meId,
        data: peerId + 'exit'
    });
    connections[peerId].close();
}, false);


document.getElementById('call').addEventListener('click', function (event) {
    let perrDes = document.getElementById('peerDes').value;
    sendMessage({
        type: "callToUser",
        receiver: perrDes,
        sender: meId
    });
}, false);