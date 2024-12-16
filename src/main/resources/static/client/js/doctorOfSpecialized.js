

function setTimeInputDate(){
	var  lstInputDate = document.querySelectorAll('.inputDate');
	var date= new Date();

	lstInputDate.forEach((element) => {
		const d = new Date();
		var dayMin = d.getDate();
		var month;
		if(d.getMonth()+1>9){
			month=""+(d.getMonth()+1);
		}else{
			month="0"+ (d.getMonth()+1);
		}
		var year = d.getFullYear();
		var lastday =  new Date(year, d.getMonth() +1, 0).getDate();
		var dayMax;
		var monthMax;
		if(dayMin+7>lastday){
			dayMax=dayMin+7-lastday;
			if(d.getMonth()+2>9){
				monthMax=""+(d.getMonth()+2);
			}else{
				monthMax="0"+ (d.getMonth()+2);
			}
		}else{
			dayMax=dayMin+7;
			monthMax=month;
		}
		var minDate= year+'-'+month+'-'+dayMin;
		var maxDate= year+'-'+monthMax+'-'+dayMax;

        element.valueAsDate = date;
        element.max=maxDate;
        	element.min=minDate;
        var inputDate=element.value;
        var a= element.nextElementSibling.nextElementSibling.querySelectorAll(".time-link");
        a.forEach(element =>{
    		var href=element.href;
    		element.href=href+"/"+inputDate;
    	});
     
    });
}

setTimeInputDate();
function myFunction(val,idDoctor) {
	var idDoctor = val.getAttribute("data-idDoctor");
	var date = val.value;
	event.preventDefault();

	var urlpath=window.location.origin;
	var data = {};
	data['idDoctor']=idDoctor;
	data['date']=date;
	$
			.ajax({
				url : urlpath+"/api/changedate",
				type : "POST",
				contentType: "application/json",
				data: JSON.stringify(data),
				cache : false,
				success : function(result) {
					var a= val.nextElementSibling.nextElementSibling;
					a.innerHTML="";
					result.forEach((element) => {
						var insertElement='<a class="time-link" href="/book/'+idDoctor+'/'+element.id+'/'+date+'">'+element.time+'</a>';
						a.innerHTML=a.innerHTML+insertElement;
				        
				    });
					
					
				},
				error : function(e) {
					
						alert('Đã có lỗi xảy ra !');
					
				}
			});
}
