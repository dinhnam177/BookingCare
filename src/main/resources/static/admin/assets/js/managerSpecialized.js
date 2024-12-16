$(document)
    .ready(
        function () {
            let descriptionEdittor = null;
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
            $('#uploadSpecialized')
                .validate(
                    {
                        rules: {
                            name: {
                                required: true

                            },
                            code: {
                                required: true

                            }
                        },
                        messages: {
                            name: {
                                required: "Tên không được để trống"
                            },
                            code: {
                                required: "Địa chỉ không được để trống"
                            }
                        },

                        submitHandler: function (form) {
                            alert('oke');
                        }
                    });
            $("#addSpecialized")
                .click(
                    function () {

                        if ($("#uploadSpecialized").valid()) {
                            let descriptionHTML = descriptionEdittor.html.get();
                            let stringDesctiption = $.trim(jQuery(descriptionHTML).text());
                            if (stringDesctiption) {
                                event.preventDefault();
                                let form = $('#uploadSpecialized')[0];
                                let urlPath = window.location.origin;
                                let data = new FormData(form);
                                $
                                    .ajax({
                                        url: urlPath + "/api/specialized",
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
                                                        window.location.reload()
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
                    $("#btnDeleteSpecialized").prop("disabled", true);
                    $("#btnEditSpecialized").prop("disabled", true);
                } else {
                    $("#btnAddSpecialized").prop("disabled", true);
                    $("#btnDeleteSpecialized").prop("disabled", false);
                    if (count == 1) {
                        $("#btnEditSpecialized").prop("disabled", false);
                    } else {
                        $("#btnEditSpecialized").prop("disabled", true);
                    }
                }
            };
            countChecked();

            $("input[type=checkbox]").on("click", countChecked);

            // editProduct
            $("#btnEditSpecialized").click(
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
                    $('#code').val(values[2])
                    descriptionEdittor.html.set(values2[0]);
                    $('#description').val(values2[0])
                    $('#imgOld').val(values[4]);
                    $("#addSpecialized").css("display", "none");
                    $("#editSpecialized").css("display", "block");

                });

            $("#btnAddSpecialized").click(
                function (event) {
                    event.preventDefault();
                    $("#addSpecialized").css("display", "block");
                    $("#editSpecialized").css("display", "none");
                });


            $("#btnConfirmDelete").click(
                function (event) {
                    event.preventDefault();
                    let data = {};
                    let urlPath = window.location.origin + "/api/specicalized/uDelete";
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


            $("#editSpecialized")
                .click(
                    function () {

                        if ($("#uploadSpecialized").valid()) {
                            var descriptionHTML = descriptionEdittor.html.get();
                            var stringDesctiption = $.trim(jQuery(descriptionHTML).text());
                            if (stringDesctiption) {
                                event.preventDefault();
                                let form = $('#uploadSpecialized')[0];
                                let urlPath = window.location.origin + '/api/specialized/update';
                                let data = new FormData(form);
                                $
                                    .ajax({
                                        url: urlPath,
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
                                                        window.location.reload()
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
                            } else {
                                alert("Bạn cần nhập lại phần mô tả");
                            }
                        } else {
                            alert("Bạn cần nhập lại các thông tin cần thiết")
                        }
                    });
        });