'use strict';
let modalcloseVideo = document.querySelector('.modal-close')
let messageClose = document.querySelector('.js-close')
let formChat = document.querySelector('.form-chat')
let message = document.querySelector('.message')
var  messageFormServer = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageElement = document.createElement('showMessgae');

var showMessgae = document.querySelector('#showMessgae');
//let message = document.querySelector('.message')
var connectingElement = document.querySelector('#connecting');
var idUser = document.querySelector('#idUser');

var btnSendMessage = document.querySelector('#btnSendMessage');
//var btnCallUser = document.querySelector('#btnCallUser');
//var stompClient = null;
//var idUser = null;
//var peer = new Peer(); 
//var peers={};
////peer.on('open',id=> {console.log(id)});
//var peerId=null;

/*btnCamera.onclick = function() {
	var urlpath=window.location.origin+"/call"
	 $.ajax({
	  	   url: urlpath,
	  	   data: '1',
	  	   contentType: "application/json",
	  	   success: function(data,response) {
	  		  var peerId=data.peer;
	  		 openStream()
	 	    .then(stream=>{
	 	        playstream('localStreamUser',stream);
	 	        const call = peer.call(peerId,stream);
	 	        call.on('stream',remoteStream=>playstream('remoteStreamUser',remoteStream));
	 	    })
	  	   },
	  	   type: 'POST'
	  	});
	
	   
};*/

messageClose.addEventListener('click', () => {
    formChat.classList.remove('active')
    message.classList.remove('hidden')

})

message.addEventListener('click', () => {
	formChat.classList.add('active');
    message.classList.add('hidden');
    var urlpath=window.location.origin+"/mesageUser";

    $.ajax({
    	   url: urlpath,
    	   error: function() {
    	      $('#info').html('<p>An error has occurred</p>');
    	   },
    	   data: '0',
    	  
    	   contentType: "application/json",
    	   success: function(data,response) {
    		   document.querySelector('#fullName').textContent=data[0].fullName;
    		   document.getElementById('avatar').src = data[0].img;
    		   showMessgae.innerHTML="";
    		   data[1].forEach(element => {
    			   var insertElement;
    			   if(element.senderId==0){
    				   insertElement='<div class="container darker ">'+'<img src="https://png.pngtree.com/png-vector/20190130/ourlarge/pngtree-hand-drawn-cartoon-male-doctor-elements-element-png-image_602590.jpg" alt="Avatar" style="width: 100%;" > <p>'+element.content+'</p> <span class="time-right"> '+element.createdDate+'</span> </div>';
       		               		        
    			   }else{
    				    insertElement='<div class="container ">'+'<img src="https://res.cloudinary.com/dtvkhopoe/image/upload/v1645000335/benh_nhan_ti17om.jpg" alt="Avatar" style="width: 100%;"  class="right"> <p>'+element.content+'</p> <span class="time-right"> '+element.createdDate+'</span> </div>';
       		       
    			   }
    			   showMessgae.innerHTML=showMessgae.innerHTML+insertElement;
    			   
    		   });
    	     console.log("oke");
    	   },
    	   type: 'POST'
    	});
    var scroll = document.querySelector('#showMessgae');
    var a= scroll.scrollHeight;
    scroll.scrollTop = 2177;
    
})

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

peer.on('call',call=>{
	peers['server']=call;
	 modalcall.classList.add('openn');
    openStream()
    .then(stream=>{
        call.answer(stream);
        playstream('localStreamUser',stream);
        call.on('stream',remoteStream=>playstream('remoteStreamUser',remoteStream));
        call.on('close', () => {
        	var video = document.getElementById('localStreamUser');
        	const mediaStream = video.srcObject;
        	const tracks = mediaStream.getTracks();
        	tracks.forEach(track => track.stop())
        	 modalcall.classList.remove('openn')
            alert('user nhan close')
          });
    })
});

modalcloseVideo.addEventListener('click', () => {
	if( peers['server']!=null){
		 peers['server'].close();
	}
	 var chatMessage = {
	        	senderId:idUser.defaultValue,
	            receiverId:'0',
	            content: "close",
	            type: 'CALL'
	        };
   stompClient.send("/app/close", {}, JSON.stringify(chatMessage));
    modalcall.classList.remove('openn')
})





$('#btnCallUser').click(()=>{
    const id= $('#remoteId').val();
    openStream()
    .then(stream=>{
        playstream('localStream',stream);
        const call = peer.call(id,stream);
        call.on('stream',remoteStream=>playstream('remoteStream',remoteStream));
    })

})

//answer

function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
        	senderId:idUser.defaultValue,
            receiverId:'0',
            content: messageInput.value,
//            type: 'CHAT'
        };
      stompClient.send("/app/server", {}, JSON.stringify(chatMessage));
      var today = new Date();
      var dd = String(today.getDate()).padStart(2, '0');
      var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
      var yyyy = today.getFullYear();

      today = mm + '/' + dd + '/' + yyyy;
      var  insertElement='<div class="container ">'+'<img src="https://res.cloudinary.com/dtvkhopoe/image/upload/v1645000335/benh_nhan_ti17om.jpg" alt="Avatar" style="width: 100%;"  class="right"> <p>'+messageContent+'</p> <span class="time-right"> '+today+'</span> </div>';
        showMessgae.innerHTML=showMessgae.innerHTML+insertElement;
        messageInput.value = '';
    }
    event.preventDefault();
    var scroll = document.querySelector('#showMessgae');
    scroll.scrollTop = scroll.scrollHeight;
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    if(message.type=="CALL"){
    	var video = document.getElementById('localStreamUser');
    	const mediaStream = video.srcObject;
    	const tracks = mediaStream.getTracks();
    	tracks.forEach(track => track.stop())
    	 modalcall.classList.remove('openn')
    	if( peers['server']!=null){
   		 peers['server'].close();
   	}
    }else{
    	var insertElement='<div class="container darker ">'+'<img src="https://png.pngtree.com/png-vector/20190130/ourlarge/pngtree-hand-drawn-cartoon-male-doctor-elements-element-png-image_602590.jpg" alt="Avatar" style="width: 100%;" > <p>'+message.content+'</p> <span class="time-right"> '+message.createdDate+'</span> </div>';
        showMessgae.innerHTML=showMessgae.innerHTML+insertElement;
    }
    var scroll = document.querySelector('#showMessgae');
    scroll.scrollTop = scroll.scrollHeight;
}
 
messageFormServer.addEventListener('submit', sendMessage, true);