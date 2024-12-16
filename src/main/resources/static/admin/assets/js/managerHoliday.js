$(document)
    .ready(
        function () {
            var count = 0;

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

            $("#btnAdd").click(
                function (event) {
                    event.preventDefault();
                    $('#btnConfirmEdit').hide();
                    $('#date').val(new Date().toJSON().slice(0, 10));
                    $('#date').attr('min', new Date().toJSON().slice(0, 10));
                    $('#date').attr('readonly', false);
                    $('input[name="workTimeId"][type="checkbox"]').prop('checked', false);
                });

            $("#btnEdit").click(
                function (event) {
                    event.preventDefault();
                    $('#btnConfirmAdd').hide();
                    let values = [];
                    let values2 = [];

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

                    $('input[name="workTimeId"][type="checkbox"]').prop('checked', false);
                    $('#idHoliday').val($("input[name='checkOne']:checked").val());
                    $('#date').val(values[0]);
                    $('#date').attr('readonly', true);
                    for (let i = 0; i < values2.length; ++i) {
                        let itemWkIdOfUser = values2[i];
                        $('.checkWorkTime').each(function () {
                            if ($(this).val() === itemWkIdOfUser) {
                                $(this).prop('checked', true);
                            }
                            $(this).removeAttr('disabled');
                        });
                    }
                });

            $("#btnInfo").click(
                function (event) {
                    event.preventDefault();
                    $('#btnConfirmAdd').hide();
                    $('#btnConfirmEdit').hide();
                    let values = [];
                    let values2 = [];

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

                    $('input[name="workTimeId"][type="checkbox"]').prop('checked', false);
                    $('#idHoliday').val($("input[name='checkOne']:checked").val());
                    $('#date').val(values[1]);
                    if(window.location.href.includes("admin")) {
                        $('#date').val(values[1]);
                    } else {
                        $('#date').val(values[0]);
                    }
                    $('#date').attr('readonly', true);
                    for (let i = 0; i < values2.length; ++i) {
                        let itemWkIdOfUser = values2[i];
                        $('.checkWorkTime').each(function () {
                            if ($(this).val() === itemWkIdOfUser) {
                                $(this).prop('checked', true);
                            }
                            $(this).attr('disabled', true);
                        });
                    }
                });

            $("#btnConfirmAdd")
                .click(
                    function (event) {
                        event.preventDefault();
                        let urlPath = window.location.origin + '/api/holiday';
                        let workTimeIds = $('#formHoliday input[type="checkbox"][name="workTimeId"]:checked').map(function () {
                            return $(this).val();
                        }).get();
                        let body = {
                            date: $('#date').val(),
                            workTimeIds: workTimeIds
                        }
                        $.ajax({
                            url: urlPath,
                            type: "post",
                            contentType: "application/json",
                            data: JSON.stringify(body),
                            cache: false,
                            success: function () {
                                alert("Đã đăng kí thành công");
                                window.location.reload();
                            },
                            error: function (xhr) {
                                alert(xhr.responseText);
                                window.location.reload();
                            }
                        });

                    });

            $("#btnConfirmDelete")
                .click(
                    function (event) {
                        event.preventDefault();
                        let urlPath = window.location.origin + '/api/holiday/delete';
                        let ids = $('tbody input[name="checkOne"]:checked').map(function () {
                            return $(this).val();
                        }).get();
                        let data = {};
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
                            error: function (xhr, status, error) {
                                alert(xhr.responseText);
                                window.location.reload();
                            }
                        });

                    });

            $("#btnConfirmEdit")
                .click(
                    function (event) {
                        event.preventDefault();
                        let urlPath = window.location.origin + '/api/holiday/update';
                        let workTimeIds = $('#formHoliday input[type="checkbox"][name="workTimeId"]:checked').map(function () {
                            return $(this).val();
                        }).get();
                        let body = {
                            id: $('#idHoliday').val(),
                            date: $('#date').val(),
                            workTimeIds: workTimeIds
                        }
                        $.ajax({
                            url: urlPath,
                            type: "post",
                            contentType: "application/json",
                            data: JSON.stringify(body),
                            cache: false,
                            success: function (result) {
                                alert("Cập nhật lịch nghỉ đã hoàn thành");
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
                    $("#btnAdd").prop("disabled", false);
                    $("#btnEdit").prop("disabled", true);
                    $("#btnInfo").prop("disabled", true);
                    $("#btnDelete").prop("disabled", true);

                } else {
                    $("#btnDelete").prop("disabled", false);
                    $("#btnAdd").prop("disabled", true);
                    $("#btnEdit").prop("disabled", true);
                    $("#btnInfo").prop("disabled", true);
                    if (count == 1) {
                        $("#btnEdit").prop("disabled", false);
                        $("#btnInfo").prop("disabled", false);
                    }
                }
            };
            countChecked();

            $("input[type=checkbox]").on("click", countChecked);

        });