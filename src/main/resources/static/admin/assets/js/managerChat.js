'use strict';


var messageFormServer = document.querySelector('#messageFormServer');
var messageInputServer = document.querySelector('#messageInputServer');
var elementChat = document.querySelector('#elementChat');
var showLstUser = document.querySelector('#showLstUser');
var idUser;
var btnCallServer = document.querySelector('#btnCallServer');
var myId = document.querySelector('#myId');
var stompClient = null;
var username = null;

// Variable call video
const PORT = 8080;
const MAPPING = "/room";
const peerConnectionConfig = {
	'iceServers': [
		{'urls': 'stun:stun.l.google.com:19302'}
	]
};
let ws = null;
let peerIdRTC = null;
let localStream = null;
let connections = {};
let modal = document.getElementById('modal-wrapper');
let selfVideo = document.getElementById('self')
let remoteVideo = document.getElementById('remote');
let btnCancel = document.getElementById('btnCancel');
let btnAccept = document.getElementById('btnAccept');
let userLoginId = document.getElementById('userLoginId').value;
let showCam = true;
let turnOnMic = true;
let showScreen = true;
// Variable call video
function connect(e) {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}

// Connect to WebSocket Server.
connect();

function onConnected() {
	if (window.location.href.endsWith('/doctor/managerChat')) {
		stompClient.subscribe('/topic/' + myId.defaultValue, onMessageReceived);
	} else {
		stompClient.subscribe('/topic/server', onMessageReceived);
	}
}

function onError(error) {
	console.log('connect fail' + error);
}

function  sendMessage(event) {
	if(!idUser) {
		event.preventDefault();
		return;
	}
    let messageContent = messageInputServer.value.trim();
    if(messageContent && stompClient) {
    	username = document.querySelector('#userName').innerText.trim();
        let chatMessage = {
            receiver: username,
            senderId: myId.defaultValue,
            receiverId: idUser,
            content: messageInputServer.value,
        };
        stompClient.send("/app/sendToUSer", {}, JSON.stringify(chatMessage));
        let insertElement='<li class="clearfix">'+'<div class="message-data text-right">'+'<span class="message-data-time">Today</span>'+'<img src="https://png.pngtree.com/png-vector/20190130/ourlarge/pngtree-hand-drawn-cartoon-male-doctor-elements-element-png-image_602590.jpg" alt="avatar">'+'</div> <div class="message other-message float-right">'+messageContent +' </div>'+'</li>'
        elementChat.innerHTML=elementChat.innerHTML+insertElement;
        messageInputServer.value = '';
    }
    event.preventDefault();
    let scroll = document.querySelector('.chat-history');
    scroll.scrollTop = scroll.scrollHeight;
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    if(message.type=="CALL"){
		let video = document.getElementById('localStreamServer');
      	const mediaStream = video.srcObject;
      	const tracks = mediaStream.getTracks();
      	tracks.forEach(track => track.stop());
      	$('.modal-overlay').removeClass( 'openn');

    } else {
    if(message.senderId==idUser){
    	 var insertElement='<li class="clearfix">'+'<div class="message-data">'+'<img src="https://res.cloudinary.com/dtvkhopoe/image/upload/v1645000335/benh_nhan_ti17om.jpg" alt="avatar">'+'<span class="message-data-time">'+message.createdDate+'</span>'+'</div> <div class="message my-message">'+message.content +' </div>'+'</li>' ;
    	    elementChat.innerHTML=elementChat.innerHTML+insertElement;
    }
    	var userId=message.senderId+"";
    	  var urlpath=window.location.origin+"/showLstUser";
    	    $.ajax({
    	    	   url: urlpath,
    	    	   data: userId,
    	    	   error: function() {
    	    	      $('#info').html('<p>An error has occurred</p>');
    	    	   },
    	    	  
    	    	   contentType: "application/json",
    	    	   success: function(data,response) {
    	    		   console.log("call success");
    	    		   var lstShowMessage = document.querySelectorAll('.searchMesssage');

    	    			   for(var i=0;i<lstShowMessage.length;i++){
    	    				   if(lstShowMessage[i].childNodes[4].defaultValue==message.senderId){
    	    					   lstShowMessage[i].remove();
    	    				   }
    	    				  
    	    			   }
        	    			   var insertElement;
        	    				   insertElement='<li class="clearfix searchMesssage" ><img '+
        	    				   'src="https://png.pngtree.com/png-vector/20190130/ourlarge/pngtree-hand-drawn-cartoon-male-doctor-elements-element-png-image_602590.jpg" alt="avatar">' +
        	    				   ' <div class="about"> '+
        	    				   ' <div class="name"  >'+data.username+'</div>'+
        	    				 
        	    				   '<div class="status"> '+' <i class="fa fa-circle offline"></i> left 7 mins ago </div> </div>'
        	    				    +' <input value="'+data.id+'" class= "idUser" type="hidden" ></input>'+'</li>'     
        	   
        	    				   showLstUser.innerHTML=insertElement+showLstUser.innerHTML;
        	    		
    	    		   selectUser();
    	    	   },
    	    	   type: 'POST'
    	    	});
    }
    let scroll = document.querySelector('.chat-history');
    scroll.scrollTop = scroll.scrollHeight;
}

function messageUser(event) {
    var idValue= idUser;
    var urlpath=window.location.origin+"/api/selectUser";

    $.ajax({
    	   url: urlpath,
    	   data: idValue,
    	   error: function() {
    	      $('#info').html('<p>An error has occurred</p>');
    	   },
    	  
    	   contentType: "application/json",
    	   success: function(data,response) {
    		   var lstShowMessage = document.querySelectorAll('#showMessage');
    		   lstShowMessage.forEach(element =>{
    			   element.remove();
    		   })
    		   document.querySelector('#userName').textContent=data[0].name;
    		   document.getElementById('avatar').src = data[0].img;
    		   data[1].forEach(element => {
    			   var insertElement;
    			   if(element.senderId==myId.defaultValue){
    				   insertElement='<li class="clearfix" id="showMessage">'+'<div class="message-data text-right">'+'<span class="message-data-time">'+element.createdDate+'</span>'+'<img src="https://png.pngtree.com/png-vector/20190130/ourlarge/pngtree-hand-drawn-cartoon-male-doctor-elements-element-png-image_602590.jpg" alt="avatar">'+'</div> <div class="message other-message float-right">'+element.content +' </div>'+'</li>'
       		        
    			   }else{
    				   insertElement='<li class="clearfix" id="showMessage">'+'<div class="message-data">'+'<img src="https://res.cloudinary.com/dtvkhopoe/image/upload/v1645000335/benh_nhan_ti17om.jpg" alt="avatar">'+'<span class="message-data-time">'+element.createdDate+'</span>'+'</div> <div class="message my-message">'+element.content +' </div>'+'</li>'     
    			   }
    			   elementChat.innerHTML=elementChat.innerHTML+insertElement;
    			   
    		   });
			   document.querySelector('.chat-history').scrollTop= document.querySelector('.chat-history').scrollHeight
    	   },
    	   type: 'POST'
    	});
}

messageFormServer.addEventListener('submit', sendMessage, true);
function selectUser() {
	var searchMesssage = document.querySelectorAll('.searchMesssage ');
	searchMesssage.forEach((element) => {
        element.onclick= function(){
           idUser = this.childNodes[4].defaultValue;
           messageUser()
        }
        
    });
}
selectUser();

//Start handle call video
connectToWss();
function connectToWss() {
	ws = new WebSocket('wss://' + window.location.hostname + MAPPING);
	ws.onmessage = processWsMessage;
	ws.onopen = handleWhenOpenWs;
	ws.onclose = logMessage;
	ws.onerror = logMessage;
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
		case 'callToUser':
			handleCallToUser(signal);
			break;
		case 'cancel-caller':
			handleCancel(signal);
			break;
		default: {
			break;
		}
	}
}

const handleInit = async (signal) => {
	peerIdRTC = signal.sender;
	let connection = await getRTCPeerConnectionObject(peerIdRTC);

	// make an offer, and send the SDP to sender.
	connection
		.createOffer()
		.then(function (sdp) {
			connection.setLocalDescription(sdp);
			console.log('Creating an offer for', peerIdRTC);
			sendMessageRTC({
				type: 'offer',
				receiver: peerIdRTC,
				sender: userLoginId,
				data: sdp,
			});
		})
		.catch(function (e) {
			console.log('Error in offer creation.', e);
		});
}

function handleLogout(signal) {
	if (peerIdRTC === signal.sender) {
		console.log('Handle logout');
		remoteVideo.srcObject = null;
		selfVideo.srcObject = null;
		if(!!localStream.getTracks()) {
			localStream.getTracks().forEach(function (track) {
				track.stop();
			});
		}
		modal.style.display = 'none';
		document.getElementById('modal-notification').style.display = 'block';
		document.getElementById('modal-video').style.display = 'none';
		connections[peerIdRTC].close();
		delete connections[peerIdRTC];
		window.location.reload();
	}
}

const handleOffer = async (signal) => {
	peerIdRTC = signal.sender;
	let connection = await getRTCPeerConnectionObject(peerIdRTC);
	connection
		.setRemoteDescription(new RTCSessionDescription(signal.data))
		.then(function () {
			console.log('Setting remote description by offer from ' + peerIdRTC);
			// create an answer for the peedId.
			connection
				.createAnswer()
				.then(function (sdp) {
					// and after callback set it locally and send to peer
					connection.setLocalDescription(sdp);
					sendMessageRTC({
						type: 'answer',
						receiver: peerIdRTC,
						sender: userLoginId,
						data: sdp,
					});

				})
				.catch(function (e) {
					console.log('Error in offer handling.', e);
				});
		})
		.catch(function (e) {
			console.log('Error in offer handling.', e);
		});
}

const handleAnswer = async (signal) => {
	let connection = await getRTCPeerConnectionObject(signal.sender);
	connection
		.setRemoteDescription(new RTCSessionDescription(signal.data))
		.then(function () {
			console.log('Setting remote description by answer from' + signal.sender);
		})
		.catch(function (e) {
			console.log('Error in answer acceptance.', e);
		});
}

const handleIce = async (signal) => {
	if (signal.data) {
		console.log('Adding ice candidate');
		let connection = await getRTCPeerConnectionObject(signal.sender);
		connection.addIceCandidate(new RTCIceCandidate(signal.data));
	}
}

function handleExit(signal) {
	if (signal.data && signal.sender === peerIdRTC) {
		console.log('Handle exit');
		remoteVideo.srcObject = null;
		selfVideo.srcObject = null;
		if(!!localStream.getTracks()) {
			localStream.getTracks().forEach(function (track) {
				track.stop();
			});
		}
		modal.style.display = 'none';
		document.getElementById('modal-notification').style.display = 'block';
		document.getElementById('modal-video').style.display = 'none';
		connections[peerIdRTC].close();
		delete connections[peerIdRTC];
		window.location.reload();
	}
}

const getRTCPeerConnectionObject = async (uuid) => {
	if (connections[uuid]) {
		return connections[uuid];
	}

	let connection = new RTCPeerConnection(peerConnectionConfig);

	await navigator.mediaDevices
		.getUserMedia({ video: true, audio: true })
		.then(function (stream) {
			console.log('Stream OK');
			localStream = stream;
			console.log(localStream);
			selfVideo.srcObject = localStream;
		})
		.catch(function (error) {
			console.log('Stream NOT OK: ' + error.name + ': ' + error.message);
		});
	console.log('========')
	console.log(!!localStream)
	console.log(connection);
	connection.addStream(localStream);

	// handle on ice candidate
	connection.onicecandidate = function (event) {
		console.log('candidate is: ' + event.candidate);
		if (event.candidate) {
			sendMessageRTC({
				type: 'ice',
				receiver: uuid,
				sender: userLoginId,
				data: event.candidate,
			});
		}
	};

	// handle on track / onaddstream
	connection.onaddstream = function (event) {
		console.log('Received new stream from ' + uuid);
		remoteVideo.srcObject = event.stream;
	};

	connections[uuid] = connection;
	return connection;
}

function sendMessageRTC(payload) {
	ws.send(JSON.stringify(payload));
}

function logMessage(message) {
	console.log(message);
}

function handleWhenOpenWs() {
	console.log('conect success');
	sendMessageRTC({
		type: 'save',
		sender: userLoginId,
	});
}

function handleCancel(signal) {
	remoteVideo.srcObject = null;
	if(!!localStream && !!localStream.getTracks()) {
		localStream.getTracks().forEach(function (track) {
			track.stop();
		});
	}
	modal.style.display = 'none';
	document.getElementById('modal-notification').style.display = 'none';
	document.getElementById('modal-video').style.display = 'none';
	if (!!connections && !!connections[peerIdRTC]) {
		connections[peerIdRTC].close();
		delete connections[peerIdRTC];
	}
	alert('Cuộc gọi đã bị từ chối');
}

function handleCallToUser(signal) {
	modal.style.display = 'block';
	btnCancel.addEventListener('click', function () {
		sendMessageRTC({
			type: 'cancel-caller',
			receiver: signal.sender,
			sender: signal.receiver,
		});
	});

	btnAccept.addEventListener('click', function () {
		document.getElementById('modal-notification').style.display = 'none';
		document.getElementById('modal-video').style.display = 'block';

		navigator.mediaDevices
			.getUserMedia({ video: true, audio: true })
			.then(function (stream) {
				console.log('Stream OK');
				localStream = stream;
				console.log(localStream);
				selfVideo.srcObject = localStream;
			})
			.catch(function (error) {
				console.log('Stream NOT OK: ' + error.name + ': ' + error.message);
			});
		sendMessageRTC({
			type: 'init',
			receiver: signal.sender,
			sender: signal.receiver,
		});
	});
}

function eventCallOff() {
	sendMessageRTC({
		type: 'exit',
		receiver: !!Number(peerIdRTC) ? peerIdRTC : idUser,
		sender: userLoginId,
		data: peerIdRTC + 'exit',
	});
	if(!!localStream && !!localStream.getTracks()) {
		localStream.getTracks().forEach(function (track) {
			track.stop();
		});
	}
	if (!!connections && !!connections[peerIdRTC]) {
		connections[peerIdRTC].close();
		delete connections[peerIdRTC];
	}
	document.getElementById('modal-notification').style.display = 'none';
	document.getElementById('modal-video').style.display = 'none';
	modal.style.display = 'none';
	window.location.reload();

}

function changStatusCam() {
	const videoTrack = localStream.getTracks().find((track) => track.kind === 'video');
	videoTrack.enabled = !showCam;
	showCam = !showCam;
	if (showCam) {
		document.getElementById('video-off').style.display = 'block';
		document.getElementById('video-on').style.display = 'none';
	} else {
		document.getElementById('video-off').style.display = 'none';
		document.getElementById('video-on').style.display = 'block';
	}
}

function changeStatusMic() {
	const audioTrack = localStream.getTracks().find((track) => track.kind === 'audio');
	audioTrack.enabled = !turnOnMic;
	turnOnMic = !turnOnMic;
	if (turnOnMic) {
		document.getElementById('mic-on').style.display ='none';
		document.getElementById('mic-off').style.display ='block';
	}
	else {
		document.getElementById('mic-on').style.display = 'block';
		document.getElementById('mic-off').style.display = 'none';
	}
}

const shareScreen = async () => {
	if (!!showScreen) {
		const constraints = { video: { cursor: 'always' }, audio: false };
		const screenCaptureStream = await navigator.mediaDevices.getDisplayMedia(constraints);
		let videoTrack = screenCaptureStream.getVideoTracks()[0];
		localStream = screenCaptureStream;
		selfVideo.srcObject = localStream;
		connections[peerIdRTC].getSenders().forEach(function (rtpSender) {
			if (rtpSender.track.kind === 'video') {
				rtpSender
					.replaceTrack(videoTrack)
					.then(function () {
						console.log('Replaced video track from camera to screen');
					})
					.catch(function (error) {
						console.log('Could not replace video track: ' + error);
					});
			}
		});
	} else {
		const cameraStream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
		if (!!localStream) {
			localStream.getTracks().forEach(function (track) {
				track.stop();
			});
		}
		localStream = cameraStream;
		selfVideo.srcObject = localStream;
		let videoTrack = cameraStream.getVideoTracks()[0];
		let audioTrack = cameraStream.getAudioTracks()[0];
		connections[peerIdRTC].getSenders().forEach(function (rtpSender) {
			if (rtpSender.track.kind === 'video') {
				rtpSender
					.replaceTrack(videoTrack)
					.then(function () {
						console.log('Replaced video track from camera to screen');
					})
					.catch(function (error) {
						console.log('Could not replace video track: ' + error);
					});
			}
			if (rtpSender.track.kind === 'audio') {
				rtpSender
					.replaceTrack(audioTrack)
					.then(function () {
						console.log('Replaced video track from camera to screen');
					})
					.catch(function (error) {
						console.log('Could not replace video track: ' + error);
					});
			}
		});
	}

	showScreen = !showScreen;
	if(showScreen) {
		document.getElementById('share-screen').style.display ='none';
		document.getElementById('share-screen-off').style.display ='block';
	} else {
		document.getElementById('share-screen').style.display ='block';
		document.getElementById('share-screen-off').style.display ='none';
	}
}

btnCallServer.onclick = function() {
	if(!idUser) {
		alert("Bạn chưa chọn người nhận cuộc gọi");
		return;
	}
	document.getElementById('modal-wrapper').style.display = 'block';
	document.getElementById('modal-notification').style.display = 'none';
	document.getElementById('modal-video').style.display = 'block';
	sendMessageRTC({
		type: "callToUser",
		receiver: idUser,
		sender: userLoginId
	});
};

//End handle call video