var messageForm = document.querySelector('#messageForm');
messageForm.addEventListener('submit', sendMessage, true);
var commentInput = document.querySelector('#comment');  
var createdByUserId = document.querySelector('#createdByUserId');
var removeComment = document.querySelector('#removeComment');
//
function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}
// Connect to WebSocket Server.
connect();
//
function onConnected() {
	var url ="/topic/comment";
		stompClient.subscribe(url, onMessageReceived);
}
//
function sendMessage(event) {
	 var messageContent = commentInput.value.trim();
	 commentInput.value='';
	 var idUser= commentInput.getAttribute("data-idUser");
	 var idHandbook= commentInput.getAttribute("data-idHandbook");
	 var comment = {
			 	idUser: idUser,
	            idHandbook: idHandbook,
	            content: messageContent
	        };
	 stompClient.send("/app/sendComment", {}, JSON.stringify(comment));
/*    alert('submit');*/
    event.preventDefault();
}

//
function onMessageReceived(payload) {
    var comment = JSON.parse(payload.body);
    var creaedByUserId= createdByUserId.getAttribute("data-createdbyuserid");
    var idUserSendComment= comment.idUser;
    var idUserCurrent= document.querySelector('#comment').getAttribute("data-idUser");
    var commentInput = document.querySelector('#showComment');
    var innerHTML ='<div class="card p-3 mt-2" id="'+comment.id+'"> <div class="d-flex justify-content-between align-items-center">'
    	innerHTML =  innerHTML+ '<div class="user d-flex flex-row align-items-center"> <img src="https://res.cloudinary.com/dtvkhopoe/image/upload/v1645000335/benh_nhan_ti17om.jpg"> <span><small class="font-weight-bold text-primary">'
    	innerHTML =  innerHTML+ comment.userName+'</small> <small class="font-weight-bold">'
    	innerHTML =  innerHTML+ comment.content+ '</small></span> </div> </div>'
    	innerHTML =  innerHTML+'<div class="action d-flex justify-content-between mt-2 align-items-center">'
    	innerHTML =  innerHTML+ '<div class="reply px-4">' 
    	if(idUserSendComment==idUserCurrent||idUserSendComment==creaedByUserId){
    		innerHTML =  innerHTML+ '<small id="removeComment" onclick="xoaComment('+comment.id+')" data-idComment="'+comment.id+'">Xoá</small>';
    	}
    	innerHTML =  innerHTML+ '</div> <div class="icons align-items-center"> <i class="fa fa-check-circle-o check-icon text-primary"></i> </div>'
    	innerHTML =  innerHTML+ ' </div> </div>';
    commentInput.innerHTML=innerHTML+commentInput.innerHTML
/*    alert('da nhan cmt');*/
}

//removeComment.addEventListener("click", function(){
//	var a = this.getAttribute("data-idComment")
//	  alert(a);
//	});
function xoaComment(id){
	var urlpath = window.location.origin;
	$.ajax({
		url :urlpath+"/api/comment/delete",
		type : "post",
		contentType : "application/json",
		data : JSON.stringify(id),
		cache : false,
		success : function(result) {
			alert("Xoá thành công");
			document.getElementById(id).remove();
		},
		error : function(e) {
			alert("Không thể xoá comment");
		}
	});
	 
}
