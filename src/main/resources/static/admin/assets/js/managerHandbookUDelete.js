$(document)
    .ready(
        function () {
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
            $('#uploadHandbook')
                .validate(
                    {
                        rules: {
                            title: {
                                required: true
                            },
                            description: {
                                required: true
                            }
                        },
                        messages: {
                            title: {
                                required: "Tiêu đề không được để trống"
                            },
                            description: {
                                required: "Mô tả không được để trống"
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
                        let urlPath = window.location.origin + '/api/handbook/restore';
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
                if (count < 1) {
                    $("#btnDeleteHandbook").prop("disabled", true);
                    $("#btnEditHandbook").prop("disabled", true);
                    $("#btnRestoreHandbook").prop("disabled", true);
                } else {

                    $("#btnRestoreHandbook").prop("disabled", false);
                    $("#btnDeleteHandbook").prop("disabled", false);
                    if (count == 1) {
                        $("#btnEditHandbook").prop("disabled", false);

                    } else {
                        $("#btnEditHandbook").prop("disabled", true);
                    }
                }
            };
            $("input[type=checkbox]").on("click", countChecked);
            countChecked();

            // editProduct
            $("#btnEditHandbook").click(
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
                    console.log(values);

                    $('#id').val($("input[name='checkOne']:checked").val());
                    $('#title').val(values[0]);
                    $('#specializedId').val(values[1]).change();
                    $('#description').html(values2[1])
                    $('#content').html(values2[0])
                    $('#createdBy').val(values[5])
                    $('#createdDate').val(values[6])
                    $('#img').attr('src', values[8]);
                    $("#editHandbook").css("display", "block");
                });

            $("#btnConfirmDelete").click(
                function (event) {
                    event.preventDefault();
                    let data = {};
                    let urlPath = window.location.origin + '/api/handbook/delete';
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
                            alert('Đã có lỗi xảy ra !');
                            window.location.reload();
                        }
                    });
                });
        });