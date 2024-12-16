var stompClient = null;
var idUser = document.querySelector('#idUser');
var peer = new Peer(); 
var peers={};
var peerId=null;
function connect() {
     
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    
   
    stompClient.connect({}, onConnected, onError);
  
}


connect();

function onConnected() {
	
	var url ="/topic/"+idUser.defaultValue;
		stompClient.subscribe(url, onMessageReceived);
		peer.on('open',id=> {peerId=id+"";
		var urlpath=window.location.origin+"/savePeerId";
		 $.ajax({
		  	   url: urlpath,
		  	   data: peerId,
		  	   contentType: "application/json",
		  	   success: function(data,response) {
		  	alert('save success sockjs');
		  	   },
		  	   type: 'POST'
		  	});
		});

}

//
function onError(error) {
 console.log('Socket disconect');
}