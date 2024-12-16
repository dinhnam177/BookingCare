var password= document.getElementById("exampleInputPassword");
    var confirm_password= document.getElementById("exampleRepeatPassword");
    
    function validatePassword(){
    	if(password.value != confirm_password.value){
    		confirm_password.setCustomValidity("Password don't match");
    	}
    	else{
    		confirm_password.setCustomValidity('');
    	}
    }
    password.onchage= validatePassword;
    confirm_password.onkeyup= validatePassword;
    jQuery.fn.extend({
	    disable: function(state) {
	        return this.each(function() {
	            this.disabled = state;
	        });
	    }
	});
		$(document).ready(function() {
			$("#username").on("input", function(e) {
				$('#msg').hide();
				if ($('#username').val() == null || $('#username').val() == "") {
					$('#msg').show();
					$("#msg").html("Username is a required field.").css("color", "red");
					$('input[type="submit"], input[type="button"], button').disable(true);
				} else {
					$.ajax({
						type: 'post',
						url: "/user",
						data: JSON.stringify({username: $('#username').val()}),
						contentType: 'application/json; charset=utf-8',
						cache: false,
						beforeSend: function (f) {
							$('#msg').show();
							$('#msg').html('Checking...');
						},
						statusCode: {
						    500: function(xhr) {
						    	$('#msg').show();
						    	$("#msg").html("Username available").css("color", "green");
						    	$('input[type="submit"], input[type="button"], button').disable(false);
						    }
						},
						success: function(msg) {
							$('#msg').show();
							if(msg !== null || msg !== "") {
								$("#msg").html("Username already taken").css("color", "red");
								$('input[type="submit"], input[type="button"], button').disable(true);
							} else {
								$("#msg").html("Username available").css("color", "green");
								$('input[type="submit"], input[type="button"], button').disable(false);
							}
						},
						error: function(jqXHR, textStatus, errorThrown) {
							$('#msg').show();
							$("#msg").html(textStatus + " " + errorThrown).css("color", "red");
						}
					});
				}
			});
		});