$(document)
    .ready(
        function () {
            let descriptionEdittor = null;
            let contentEditor = null;
            (function () {
                descriptionEdittor = new FroalaEditor("#description", {
                    // Define new link styles.
                    linkStyles: {
                        class1: 'Class 1',
                        class2: 'Class 2'
                    }
                })
            })();
            (function () {
                var curentPage = parseInt($('#curentPage').val());
                curentPage = !!curentPage ? curentPage : 1;
                if(!$('#totalPage').val() || $('#totalPage').val() == 0) return;

                window.pagObj = $('#pagination').twbsPagination({
                    totalPages: $('#totalPage').val(),
                    visiblePages: 4,
                    startPage: parseInt(curentPage),
                    onPageClick: function (event, page) {
                        if (page != parseInt($('#curentPage').val())) {
                            $('#page').val(page);
                            var url = window.location.pathname;
                            $('#formPagination').attr('action', url)
                            $('#formPagination').submit();
                        }
                    }

                });
            })();
            var count = 0;
            $('#uploadHospital')
                .validate(
                    {
                        rules: {
                            name: {
                                required: true

                            },
                            location: {
                                required: true

                            }
                        },
                        messages: {
                            name: {
                                required: "Tên không được để trống"
                            },
                            location: {
                                required: "Địa chỉ không được để trống"
                            }
                        },

                        submitHandler: function (form) {
                        }
                    });
            $("#addHospital")
                .click(
                    function () {

                        if ($("#uploadHospital").valid()) {
                            let descriptionHTML = descriptionEdittor.html.get();
                            let stringDesctiption = $.trim(jQuery(descriptionHTML).text());
                            if (stringDesctiption) {
                                event.preventDefault();
                                var form = $('#uploadHospital')[0];
                                var urlpath = window.location.origin;
                                var data = new FormData(form);
                                $
                                    .ajax({
                                        url: urlpath + "/admin/api/managerHospital/add",
                                        type: "POST",
                                        enctype: 'multipart/form-data',
                                        data: data,
                                        processData: false,
                                        contentType: false,
                                        cache: false,
                                        success: function (result) {
                                            if (result) {
                                                success:$('#modal')
                                                    .modal('hide');

                                                complete:$('#ok').modal(
                                                    'show');

                                                setTimeout(
                                                    function () {
                                                        window.location.reload()
                                                    }, 1500);
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
                                                        window.location.reload();
                                                    }, 1500);
                                            } else {
                                                alert('Đã có lỗi xảy ra !');
                                            }
                                        }
                                    });
                            } else {
                                alert("Bạn cần nhập lại phần mô tả")
                            }
                        } else {
                            alert("Bạn cần nhập đầy đủ thông tin cần thiết")
                        }


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
                if (count < 1) {
                    $("#btnDeleteHospital").prop("disabled", true);
                    $("#btnEditHospital").prop("disabled", true);
                    $("#btnAddHospital").prop("disabled", false);
                } else {
                    $("#btnAddHospital").prop("disabled", true);
                    $("#btnDeleteHospital").prop("disabled", false);
                    if (count == 1) {
                        $("#btnEditHospital").prop("disabled", false);
                    } else {
                        $("#btnEditHospital").prop("disabled", true);
                    }
                }
            };
            countChecked();

            $("input[type=checkbox]").on("click", countChecked);

            // editProduct
            $("#btnEditHospital").click(
                function (event) {
                    event.preventDefault();
                    let values = new Array();
                    let values2 = new Array();

                    $.each($("input[name='checkOne']:checked")
                            .closest("td").siblings("td"),
                        function () {
                            values.push($(this).text());
                        });
                    $.each($("input[name='checkOne']:checked")
                            .closest("td").siblings("input"),
                        function () {
                            values2.push($(this).val());
                        });


                    $('#id').val($("input[name='checkOne']:checked").val());
                    $('#name').val(values[0]);
                    $('#location').val(values[2])
                    descriptionEdittor.html.set(values2[0]);
                    $('#description').val(values2[0])
                    $('#imgOld').val(values[4]);
                    $('#longitude').val(values[6]);
                    $('#latitude').val(values[5]);
                    $("#addHospital").css("display", "none");
                    $("#editHospital").css("display", "block");
                    console.log("kinh do:", values[5]);
                    console.log("vi do:", values[6]);
                    valueKinhDo = Number(values[5]);
                    valueViDo = Number(values[6]);
                    initMap();

                });

            $("#btnAddHospital").click(
                function (event) {
                    event.preventDefault();
                    $('#id').val('');
                    $('#name').val('');
                    $('#location').val('')
                    descriptionEdittor.html.set('');
                    $('#description').val('')
                    $('#imgOld').val('');
                    $('#longitude').val('');
                    $('#latitude').val('');
                    valueKinhDo = Number(105.7899284362793);
                    valueViDo = Number( 21.017254034683987);
                    initMap();
                    $("#addHospital").css("display", "block");
                    $("#editHospital").css("display", "none");
                });


            $("#btnConfirmDelete").click(
                function (event) {
                    event.preventDefault();
                    let data = {};
                    let urlPath = window.location.origin + '/api/hospital/uDelete';
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
                            alert("Đã xoá thành công");
                            window.location.reload();
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


            $("#editHospital")
                .click(
                    function () {

                        if ($("#uploadHospital").valid()) {
                            var descriptionHTML = descriptionEdittor.html.get();
                            var stringDesctiption = $.trim(jQuery(descriptionHTML).text());
                            if (stringDesctiption) {
                                event.preventDefault();
                                var form = $('#uploadHospital')[0];
                                var urlpath = window.location.origin;
                                var data = new FormData(form);
                                $
                                    .ajax({
                                        url: urlpath + "/admin/api/managerHospital/edit",
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
                                                        window.location.reload();
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
                                                        window.location.reload();
                                                    }, 1500);
                                            } else {
                                                alert('Đã có lỗi xảy ra !');
                                            }
                                        }
                                    });
                            } else {
                                alert("Bạn cần nhập lại phần mô tả");
                            }
                        } else {
                            alert("Bạn cần nhập lại các thông tin cần thiết")
                        }
                    });
        });