var a=$('#bookingForm');
a.validate({
	rules: {
		nameScheduler: {
			required: true
			
		},
		phoneScheduer: {
			required: true,
			number:true,
            minlength:10,
            maxlength:10
		},
		namePatient: {
			required: true,
			
		},
		phonePatient: {
			required: true,
			number:true,
            minlength:10,
            maxlength:10
		},
		yearOfBirth:{
			required: true,
			number:true
		}
		,
		location:{
			required: true
		},
		reason:{
			required: true
		}
		
	},
	messages: {
		nameScheduler: {
			required: "Không được để trống "
			
		},
		phoneScheduer: {
			required: "Không được để trống ",
			number:"Nhập sai số điện thoại",
            minlength:"Nhập sai số điện thoại",
            maxlength:"Nhập sai số điện thoại"
		},
		namePatient: {
			required: "Không được để trống ",
			
		},
		phonePatient: {
			required: "Không được để trống ",
			number:"Nhập sai số điện thoại",
            minlength:"Nhập sai số điện thoại",
            maxlength:"Nhập sai số điện thoại"
		},
		yearOfBirth:{
			required: "Không được để trống",
			number:"Chỉ được nhập số"
		}
		,
		location:{
			required: "Không được để trống"
		},
		reason:{
			required: "Không được để trống"
		}
	},

	submitHandler: function(form) {
		var dataform = new FormData(form);
		var object = {};
		dataform.forEach(function(value, key){
			object[key] = value;
		});
		var json = JSON.stringify(object);
		var urlpath = window.location.origin;

		$.ajax({
			url: '/api/booking',
			type: "post",
			data: JSON.stringify(object),
			dataType: 'json',
			contentType: "application/json",
			cache: false,
			success: function(result) {
				if($('#bookOn').prop("checked")){
					sendNoti();
				}
				alert('Đăng kí thành công');
				window.location
					.replace(urlpath+"/home");
			},
			error: function(error) {
				alert('Đã có lỗi xảy ra !');
			}
		});

		// $.ajax({
		// 	url: "/api/booking",
		// 	type: "post",
		// 	data: JSON.stringify(object),
		// 	dataType: 'jsonp',
		// 	success: function(result) {
		// 		if($('#bookOn').prop("checked")){
		// 			sendNoti();
		// 		}
		// 			alert('Đăng kí thành công');
		// 			window.location
		// 			.replace(urlpath+"/home");
		// 	},
		// 	error: function(error) {
		// 		alert('Đã có lỗi xảy ra !');
		// 	}
		// });
	}
});

function connect() {
//	idUser = document.querySelector('#idUser').innerText.trim();
     
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    
   
    stompClient.connect({}, onConnected, onError);
  
};
function onConnected1() {
//		stompClient.subscribe(url, onMessageReceived);
	console.log('kêt nốt thông báo');
};
function sendMessage1() {
	var doctorId=$('#idDoctor').val();
	var userId=$('#idUser').val();
	var wkTime=$('#wkTime').val();
	var date=$('#date').val();
	
        var notificationForm = {
        		doctorId:doctorId,
        		userId:userId,
        		wkTime: wkTime,
        		date: date
        };
      stompClient.send("/app/notification", {}, JSON.stringify(notificationForm));

}

function sendNoti(){
	sendMessage1();
}


function check() {
  var x = document.getElementById("nguoiThan").checked;
  let chonguoihien = document.querySelector('.hide-cho-nguoi')
  let chominhhide = document.querySelector('.chominh-hide')
  var name=$('#nameScheduler');
  var phone=$('#phoneScheduer');
  if(x){
	  $('#nameScheduler').attr('required', true);
	  $('#phoneScheduer').attr('required', true);
	  $('#phoneScheduer').attr('pattern', "(\\+84|0)\\d{9,10}");
	  if($( ".hide-cho-nguoi" ).hasClass( "ok" )){
		  
	  }else{
		  chonguoihien.classList.toggle('ok');
		  
	  }
  }else{
	  $('#nameScheduler').attr('required', false);
	  $('#phoneScheduer').attr('required', false);
	  chonguoihien.classList.remove('ok');
	
  }
};

