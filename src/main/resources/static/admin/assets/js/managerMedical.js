$(document)
    .ready(
        function () {
            (function () {
                let currentPage = parseInt($('#currentPage').val());
                currentPage = !!currentPage ? currentPage : 1;
                if(!$('#totalPage').val() || $('#totalPage').val() == 0) return;

                window.pagObj = $('#pagination').twbsPagination({
                    totalPages: $('#totalPage').val(),
                    visiblePages: 4,
                    startPage: parseInt(currentPage),
                    onPageClick: function (event, page) {
                        if (page != parseInt($('#currentPage').val())) {
                            $('#page').val(page);
                            let url = window.location.pathname;
                            $('#formPagination').attr('action', url)
                            $('#formPagination').submit();
                        }
                    }

                });
            })();
            var count = 0;
            $("#btnComplete")
                .click(
                    function (event) {
                        event.preventDefault();
                        let data = {};
                        let urlPath = window.location.origin + window.location.pathname ;
                        let ids = $('tbody input[name="checkOne"]:checked').map(function () {
                            return $(this).val();
                        }).get();
                        data['ids'] = ids;
                        $.ajax({
                            url: urlPath + "/complete",
                            type: "post",
                            contentType: "application/json",
                            data: JSON.stringify(data),
                            cache: false,
                            success: function (result) {
                                alert("Đã hoàn thành ca khám");
                                window.location.reload();
                            },
                            error: function (xhr, status, error) {
                                alert(xhr.responseText);
                                window.location.reload();
                            }
                        });

                    });

            $("#btnConfirmPayment")
                .click(
                    function (event) {
                        event.preventDefault();
                        let data = {};
                        let urlPath = window.location.origin + "/api/medical/complete-payment";
                        let ids = $('tbody input[name="checkOne"]:checked').map(function () {
                            return $(this).val();
                        }).get();
                        data['ids'] = ids;
                        $.ajax({
                            url: urlPath,
                            type: "post",
                            contentType: "application/json",
                            data: JSON.stringify(data),
                            cache: false,
                            success: function (result) {
                                alert("Thanh toán thành công");
                                window.location.reload();
                            },
                            error: function (xhr, status, error) {
                                alert(xhr.responseText);
                                window.location.reload();
                            }
                        });

                    });

            // checkAllCheckBox
            $("#checkAll").change(
                function () {
                    $("input[name='checkOne']").not(this).prop('checked',
                        this.checked);
                    countChecked();
                });

            // checkAnyCheckBox
            var countChecked = function () {
                count = $("input[name='checkOne']:checked").length
                console.log(count);
                if (count < 1) {
                    $("#btnDeleteMedical").prop("disabled", true);
                    $("#btnInforMedia").prop("disabled", true);
                    $("#btnSendMedicalRecords").prop("disabled", true);
                    $("#btnComplete").prop("disabled", true);
                    $("#btnUpdateTimeClose").prop("disabled", true);
                    $("#btnPayment").prop("disabled", true);
                } else {

                    $("#btnComplete").prop("disabled", false);
                    $("#btnDeleteMedical").prop("disabled", false);
                    $("#btnPayment").prop("disabled", false);
                    if (count == 1) {
                        $("#btnInforMedia").prop("disabled", false);
                        $("#btnSendMedicalRecords").prop("disabled", false);
                        $("#btnUpdateTimeClose").prop("disabled", false);

                    } else {
                        $("#btnInforMedia").prop("disabled", true);
                        $("#btnSendMedicalRecords").prop("disabled", true);
                        $("#btnUpdateTimeClose").prop("disabled", true);
                    }
                }
            };
            countChecked();

            $("input[type=checkbox]").on("click", countChecked);

            // showMedical
            $("#btnInforMedia").click(
                function (event) {
                    event.preventDefault();

                    let values = new Array();
                    $.each($("input[name='checkOne']:checked")
                            .closest("td").siblings("td"),
                        function () {
                            values.push($(this).text());
                        });
                    console.log(values);

                    $('#id').val($("input[name='checkOne']:checked").val());
                    $("#namePatient").text(values[2]);
                    $("#phonePatient").text(values[3]);
                    $("#nameScheduler").text(values[6]);
                    $("#phoneScheduer").text(values[5]);
                    $("#yearOfBirth").text(values[10]);
                    $("#sex").text(values[7]);
                    $("#location").text(values[8]);
                    $("#reason").text(values[9]);
                    $("#doctorName").text(values[0]);
                    $("#wordTimeTime").text(values[1]);
                    $("#date").text(values[4]);
                    if (values[11] == 'on') {
                        $('#type').text("Tư vấn online");
                    } else {
                        $('#type').text("Khám tại cơ sở");
                    }
                    $("#hospitalName").text(values[12])
                    if (window.location.href.includes('/managerMedicalComplete')) {
                        $("#statusPayment").text(values[13])
                        let price = Number(values[14]);
                        price = price.toLocaleString('it-IT', {style : 'currency', currency : 'VND'});
                        $("#priceMedical").text(price)
                    } else {
                        $("#statusPayment").text(values[15])
                        let price = Number(values[16]);
                        price = price.toLocaleString('it-IT', {style : 'currency', currency : 'VND'});
                        $("#priceMedical").text(price)
                    }

                });
            // btnSendMedicalRecords
            $("#btnSendMedicalRecords").click(
                function (event) {
                    event.preventDefault();
                    // var values = new Array();
                    //
                    // $.each($("input[name='checkOne']:checked")
                    // 		.closest("td").siblings("td"),
                    // 	function() {
                    // 		values.push($(this).text());
                    // 	});
                    $('#medicalId').val($("input[name='checkOne']:checked").val());
                });
            //submit sendRecords

            $("#sendMedical").click(
                function (event) {
                    event.preventDefault();
                    if (!document.getElementById('medicalRecords').files[0]) {
                        alert('Vui lòng chọn file để gửi');
                        window.location.reload();
                    }
                    let form = $('#uploadMedicalRecords')[0];
                    let urlPath = 'http://165.232.161.206:8080/api/medical/uploadMedicalRecords';
                    let data = new FormData(form);
                    $.ajax({
                        url: urlPath,
                        type: "POST",
                        enctype: 'multipart/form-data',
                        data: data,
                        processData: false,
                        contentType: false,
                        cache: false,
                        success: function (result) {
                            alert("Gửi thành công");
                            window.location.reload();
                        },
                        error: function (e) {
                            alert('Đã có lỗi xảy ra !');
                            window.location.reload();
                        }
                    });
                });

            // show changeTimeCloseMedical
            $("#btnUpdateTimeClose").click(
                function (event) {
                    event.preventDefault();

                    let values = new Array();


                    $.each($("input[name='checkOne']:checked")
                            .closest("td").siblings("td"),
                        function () {
                            values.push($(this).text());
                        });
                    $('#idMedicalChange').val($("input[name='checkOne']:checked").val());
                    $("#namePatientTime").text(values[2]);
                    // $("#phonePatient").text(values[3]);
                    // $("#nameScheduler").text(values[6]);
                    // $("#phoneScheduer").text(values[5]);
                    // $("#yearOfBirth").text(values[10]);
                    // $("#sex").text(values[7]);
                    // $("#location").text(values[8]);
                    // $("#reason").text(values[9]);
                    // $("#doctorName").text(values[0]);
                    $("#wordTimeTimeOld").text(values[1]);
                    // $("#date").text(values[4]);
                    // if(values[11]=='on'){
                    // 	$('#type').text("Tư vấn online");
                    // }else{
                    // 	$('#type').text("Khám tại cơ sở");
                    // }
                    // $("#hospitalName").text(values[12])

                });

            // event changeTimeCloseMedical
            $("#changeTime").click(
                function (event) {
                    event.preventDefault();
                    let idMedicalChange = $("#idMedicalChange").val();
                    let idWkChange = $("input[name='workTimeId']:checked").val();
                    let values = {idWk: idWkChange, idMedical: idMedicalChange};
                    $.ajax({
                        url:  window.location.origin + '/api/media/change-time-close',
                        type: "post",
                        data: JSON.stringify(values),
                        dataType: 'json',
                        contentType: "application/json",
                        cache: false,
                        success: function (result) {

                            window.location.reload()
                            alert('Thay đổi thành công');
                        },
                        error: function (e) {
                            if (!!e.responseText) {
                                alert(e.responseText);
                            } else {
                                alert('Đã có lỗi xảy ra !');
                            }
                            window.location.reload();
                        }
                    });

                });

            $("#btnAddMedical").click(
                function (event) {
                    event.preventDefault();
                    $("#addMedical").css("display", "block");
                    $("#editMedical").css("display", "none");
                });


            $("#btnConfirmCancel").click(
                function (event) {
                    event.preventDefault();
                    let data = {};
                    let urlPath = window.location.origin + "/api/managerMedical/delete";
                    let ids = $('tbody input[name="checkOne"]:checked').map(function () {
                        return $(this).val();
                    }).get();
                    data['ids'] = ids;
                    $.ajax({
                        url: urlPath,
                        type: "POST",
                        contentType: "application/json",
                        data: JSON.stringify(data),
                        cache: false,
                        success: function (result) {
                            alert("Đã huỷ thành công");
                            window.location.reload();
                        },
                        error: function (e) {
                            alert('Đã có lỗi xảy ra !');
                            window.location.reload();
                        }
                    });
                });


            $("#editMedical")
                .click(
                    function () {
                        event.preventDefault();
                        var form = $('#uploadMedical')[0];
                        var urlpath = window.location.origin + window.location.pathname;
                        var data = new FormData(form);
                        $
                            .ajax({
                                url: urlpath + "/edit",
                                type: "post",
                                enctype: 'multipart/form-data',
                                data: data,
                                processData: false,
                                contentType: false,
                                cache: false,
                                success: function (result) {
                                    if (result) {


                                        $('#modal').modal('toggle');

                                        setTimeout(
                                            function () {
                                                window.location
                                                    .replace(urlpath)
                                            }, 1000);
                                        $('#ok').modal('show');

                                    }
                                },
                                error: function (e) {
                                    if (e.status) {
                                        success:$('#modal')
                                            .modal('hide');

                                        complete:$('#ok').modal(
                                            'show');

                                        setTimeout(
                                            function () {
                                                window.location.reload()
                                            }, 1500);
                                    } else {
                                        alert('Đã có lỗi xảy ra !');
                                    }
                                }
                            });
                    });
        });