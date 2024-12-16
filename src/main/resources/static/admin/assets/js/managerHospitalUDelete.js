$(document)
    .ready(
        function () {
            (function () {
                let currentPage = parseInt($('#curentPage').val());
                currentPage = !!currentPage ? currentPage : 1;
                if(!$('#totalPage').val() || $('#totalPage').val() == 0) return;
                window.pagObj = $('#pagination').twbsPagination({
                    totalPages: $('#totalPage').val(),
                    visiblePages: 4,
                    startPage: parseInt(currentPage),
                    onPageClick: function (event, page) {
                        if (page != parseInt($('#curentPage').val())) {
                            $('#page').val(page);
                            let url = window.location.pathname;
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
                            alert('oke');
                        }
                    });
            $("#btnConfirmRestore")
                .click(
                    function (event) {
                        event.preventDefault();
                        let data = {};
                        let urlPath = window.location.origin + '/api/hospital/restore';
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
                                alert("Đã khôi phục thành công");
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
                    $("#btnAddHospital").prop("disabled", true);
                } else {
                    $("#btnAddHospital").prop("disabled", false);
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
                    $('#description').html(values2[0])
                    $('#img').attr("src", values[4]);
                    $('#longitude').val(values[6]);
                    $('#latitude').val(values[5]);
                    $("#addHospital").css("display", "none");
                    $("#editHospital").css("display", "block");
                    valueViDo = Number(values[5]);
                    valueKinhDo = Number(values[6]);
                    initMap();
                });

            $("#btnConfirmDelete").click(
                function (event) {
                    event.preventDefault();
                    let data = {};
                    let urlPath = window.location.origin + '/api/hospital/delete';
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
        });