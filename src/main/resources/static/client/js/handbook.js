$(document)
		.ready(

				function() {
					/*
					 * (function(){ new FroalaEditor("#description",{ // Define
					 * new link styles. linkStyles: { class1: 'Class 1', class2:
					 * 'Class 2' } }) })() shortDescription
					 */
					
					
					(function () {
						var a=$('#curentPage').val();
						  var curentPage=   parseInt($('#curentPage').val());
						
				        window.pagObj = $('#pagination').twbsPagination({
				        	totalPages: 10,
							visiblePages: 4,
							startPage: parseInt(curentPage),
				            onPageClick: function (event, page) {
				            	if(page!=parseInt($('#curentPage').val())){
									$('#page').val(page);
									var url= window.location.pathname;
									$('#formPagination').attr('action',url)
									$('#formPagination').submit();
				            	}
				            }
				            
				        });
				    })();
					
	


				});