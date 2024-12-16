'use strict';

var messageFormServer = document.querySelector('#messageFormServer');
var messageInputServer = document.querySelector('#messageInputServer');
var elementChat = document.querySelector('#elementChat');
var connectingElement = document.querySelector('#connecting');
var showLstUser = document.querySelector('#showLstUser');
var idUser;
var myId = document.querySelector('#idUser');
var btnCallServer = document.querySelector('#btnCallServer');
let modalclose = document.querySelector('.modal-close');
var scroll = document.querySelector('.chat-history');
var username = null;
var peer = new Peer();
var peers={};
var peerId=null;

function openStream(){
    const config={
        audio: true,
        video: true
    };
   
    return navigator.mediaDevices.getUserMedia(config);
}

function playstream(idVideo, stream){
    const video = document.getElementById(idVideo);
    video.srcObject= stream;
    video.play();
}


btnCallServer.onclick = function() {
	 var urlpath=window.location.origin+"/call";
	 var a= $('.modal-overlay');
	
	 $.ajax({
	  	   url: urlpath,
	  	   data: '2',
	  	   contentType: "application/json",
	  	   success: function(data,response) {
	  		  var peerId=data;
	  		 $('.modal-overlay').addClass( 'openn');
	  		 openStream()
	 	    .then(stream=>{
	 	        playstream('localStreamUser',stream);
	 	        const call = peer.call(peerId,stream);
	 	        call.on('stream',remoteStream=>playstream('remoteStreamUser',remoteStream));
	 	       peers['user']=call;
	 	      call.on('close', () => {
		 	    	 var video = document.getElementById('localStreamUser');
		 	        	const mediaStream = video.srcObject;
		 	        	const tracks = mediaStream.getTracks();
		 	        	tracks.forEach(track => track.stop());
		 	        	$('.modal-overlay').removeClass( 'openn');
		 	    	  alert('server tao close')
		 	    	  });
	 	    })
	  	   },
	  	   type: 'POST'
	  	});
		
	   
};

modalclose.addEventListener('click', () => {
	 var video = document.getElementById('localStreamUser');
	  	const mediaStream = video.srcObject;
	  	const tracks = mediaStream.getTracks();
	  	tracks.forEach(track => track.stop());
		$('.modal-overlay').removeClass( 'openn');
	if( peers['server']!=null){
		 peers['server'].close();
	}if( peers['user']!=null){
		 peers['user'].close();
	}
	username = document.querySelector('#userName').innerText.trim();
	 var chatMessage = {
	            receiver: username,
	            senderId: '4',
	            receiverId: '0',
	            content: "close",
	            type:"CALL"
	        };
    stompClient.send("/app/close", {}, JSON.stringify(chatMessage));
	
});


peer.on('call',call=>{
	peers['server']=call;
	 $('.modal-overlay').addClass( 'openn');
    openStream()
    .then(stream=>{
        call.answer(stream);
        playstream('localStreamServer',stream);
        call.on('stream',remoteStream=>playstream('remoteStreamServer',remoteStream));
        call.on('close', () => {
        	var video = document.getElementById('localStreamServer');
        	const mediaStream = video.srcObject;
        	const tracks = mediaStream.getTracks();
        	tracks.forEach(track => track.stop());
        	$('.modal-overlay').removeClass( 'openn');
            alert('server nhan close')
          });
    })
})

// Socket is creted in file 'createSockJs'
// Connect to WebSocket Server.
// connect();

// function connect(e) {
// 	if (!stompClient) {
// 		var socket = new SockJS('/ws');
// 		var stompClient = Stomp.over(socket);
// 		stompClient.connect({}, onConnected, onError);
// 	}
// }

function onConnected() {
    // Subscribe to the Public Topic
	var url ="/topic/"+myId.defaultValue;
	stompClient.subscribe(url, onMessageReceived);
		peer.on('open',id=> {peerId=id+"";
		var urlpath=window.location.origin+"/savePeerId";
		 $.ajax({
		  	   url: urlpath,
		  	   data: peerId,
		  	   contentType: "application/json",
		  	   success: function(data,response) {
		  	   },
		  	   type: 'POST'
		  	});
		});
}

function onError(error) {
	alert('loi')
}

function sendMessage(event) {
    var messageContent = messageInputServer.value.trim();
    if(messageContent && stompClient) {
    	username = document.querySelector('#userName').innerText.trim();
        var chatMessage = {
            receiver: username,
            senderId: myId.defaultValue,
            receiverId: idUser,
            content: messageInputServer.value,
        };
        if(idUser==0){
        	stompClient.send("/app/server", {}, JSON.stringify(chatMessage));
        }else{
        	stompClient.send("/app/sendToUSer", {}, JSON.stringify(chatMessage));
        }
        var insertElement='<li class="clearfix">'+'<div class="message-data text-right">'+'<span class="message-data-time">10:10 AM, Today</span>'+'<img src="https://png.pngtree.com/png-vector/20190130/ourlarge/pngtree-hand-drawn-cartoon-male-doctor-elements-element-png-image_602590.jpg" alt="avatar">'+'</div> <div class="message other-message float-right">'+messageContent +' </div>'+'</li>'
        elementChat.innerHTML=elementChat.innerHTML+insertElement;
        messageInputServer.value = '';
    }
    event.preventDefault();

	scroll.scrollTop = scroll.scrollHeight;
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    if(message.type=="CALL"){
    	var video = document.getElementById('localStreamUser');
	  	const mediaStream = video.srcObject;
	  	const tracks = mediaStream.getTracks();
	  	tracks.forEach(track => track.stop());
		$('.modal-overlay').removeClass( 'openn');
    	if( peers['server']!=null){
   		 peers['server'].close();
   	}
    	if( peers['user']!=null){
   		 peers['user'].close();
   	}
    }else{
    if(message.senderId==idUser){
    	 var insertElement='<li class="clearfix">'+'<div class="message-data">'+'<span class="message-data-time">'+message.createdDate+'</span>'+'</div> <div class="message my-message">'+message.content +' </div>'+'</li>' ;
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
    			   if(element.senderId!=myId.defaultValue){
    				   insertElement='<li class="clearfix" id="showMessage">'+'<div class="message-data">'+'<img src="'+data[0].img+'" alt="avatar">'+'<span class="message-data-time">'+element.createdDate+'</span>'+'</div> <div class="message my-message">'+element.content +' </div>'+'</li>'     
    			   }else{
    				   insertElement='<li class="clearfix" id="showMessage">'+'<div class="message-data text-right">'+'<span class="message-data-time">'+element.createdDate+'</span>'+'</div> <div class="message other-message float-right">'+element.content +' </div>'+'</li>'
    			   }
    			   elementChat.innerHTML=elementChat.innerHTML+insertElement;
    			   
    		   });
    	     console.log("oke");
    	   },
    	   type: 'POST'
    	});
	scroll.scrollTop = scroll.scrollHeight;
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