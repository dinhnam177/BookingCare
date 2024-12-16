$(document)
    .ready(
        function () {
            (function () {
                let curentPage = parseInt($('#curentPage').val());
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
            $("#btnConfirmRestore")
                .click(
                    function (event) {
                        event.preventDefault();
                        let data = {};
                        let urlPath = window.location.origin + '/api/specicalized/restore';
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
                                alert("Đã khôi phục thành công");
                                window.location.reload();
                            },
                            error: function (e) {
                                alert('Đã có lỗi xảy ra !');
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
                    $("#btnDeleteSpecialized").prop("disabled", true);
                    $("#btnEditSpecialized").prop("disabled", true);
                    $("#btnRestoreSpecialized").prop("disabled", true);
                } else {
                    $("#btnRestoreSpecialized").prop("disabled", false);
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

                    var values = new Array();
                    var values2 = new Array();

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
                    $('#description').html(values2[0])
                    $('#img').attr("src", values[4]);
                });

            $("#btnConfirmDelete").click(
                function (event) {
                    event.preventDefault();
                    let data = {};
                    let urlPath = window.location.origin + '/api/specicalized/delete';
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
                            alert("Đã xoá thành công");
                            window.location.reload();
                        },
                        error: function (e) {
                            alert('Đã có lỗi xảy ra !');
                            window.location.reload();
                        }
                    });
                });
        });