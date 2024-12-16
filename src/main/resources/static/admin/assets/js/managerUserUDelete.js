$(document)
    .ready(
        function () {
            (function () {
                const editorInstance = new FroalaEditor(
                    '#description',
                    {
                        events: {
                            'image.beforeUpload': function (files) {
                                const editor = this
                                if (files.length) {
                                    var reader = new FileReader()
                                    reader.onload = function (e) {
                                        var result = e.target.result
                                        editor.image.insert(result,
                                            null, null,
                                            editor.image.get())
                                    }
                                    reader.readAsDataURL(files[0])
                                }
                                return false
                            }
                        }
                    })
            })();
            (function () {
                const editorshortDescription = new FroalaEditor(
                    '#shortDescription',
                    {
                        events: {
                            'image.beforeUpload': function (files) {
                                const editor = this
                                if (files.length) {
                                    var reader = new FileReader()
                                    reader.onload = function (e) {
                                        var result = e.target.result
                                        editor.image.insert(result,
                                            null, null,
                                            editor.image.get())
                                    }
                                    reader.readAsDataURL(files[0])
                                }
                                return false
                            }
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

            $("#img").change(function (event) {
                if (this.files && this.files[0]) {
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        $('#showImage').attr('src', reader.result);
                    }
                    reader.readAsDataURL(this.files[0]);
                }
            });
            var count = 0;

            $("#btnConfirmRestore")
                .click(
                    function (event) {
                        event.preventDefault();
                        let data = {};
                        let urlPath = window.location.origin + '/api/user/restore';
                        let ids = $(
                            'tbody input[name="checkOne"]:checked')
                            .map(function () {
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
            $('#uploadUser')
                .validate(
                    {
                        rules: {
                            fullname: {
                                required: true

                            },
                            username: {
                                required: true

                            },
                            phoneNumber: {
                                required: true,
                                number: true,
                                minlength: 10,
                                maxlength: 10
                            },
                            location: {
                                required: true
                            },
                            specializedId: {
                                required: true
                            },
                            workTimeId: {
                                required: true
                            },
                            roleName: {
                                required: true
                            },
                            email: {
                                required: true,
                                email: true
                            },
                            hospitalId: {
                                required: true
                            },
                            sex: {
                                required: true
                            },
                            yearOfBirth: {
                                required: true
                            },
                            password: {
                                required: true
                            },
                            confirmPassword: {
                                equalTo: "#password"
                            }
                        },
                        messages: {
                            fullname: {
                                required: "Tên không được để trống"
                            },
                            username: {
                                required: "Tài khoản không được để trống"
                            },
                            roleName: {
                                required: "Chức năng không được để trống"
                            },
                            phoneNumber: {
                                required: "Số điện thoại không được để trống ",
                                number: "Chỉ được phép nhập chữ số"
                            },
                            email: {
                                required: "Email không được để trống",
                                email: "Bạn đã nhập sai định dạng email"
                            },
                            hospitalId: {
                                required: "Bệnh viện không được để trống"
                            },
                            workTimeId: {
                                required: "Bạn chưa chọn ca khám",

                            },
                            location: {
                                required: "Bạn chưa chọn địa chỉ",
                            },
                            sex: {
                                required: "Giới tính không được để trống "
                            },
                            yearOfBirth: {
                                required: "Ngày sinh không được để trống "
                            },
                            specializedId: {
                                required: "Chuyên khoa không được để trống "
                            },
                            password: {
                                required: "Mật khẩu không được để trống"
                            },
                            confirmPassword: {
                                equalTo: "Mật khẩu không trùng khớp"
                            },
                        },

                        submitHandler: function (form) {
                            alert('oke');
                        }
                    });

            // checkAllCheckBox
            $("#checkAll").change(
                function () {
                    $("input[name='checkOne']").not(this).prop(
                        'checked', this.checked);
                    countChecked();
                });

            $("#roleName").change(function () {
                var value = $("#roleName").val();
                if (value != "ROLE_DOCTOR") {
                    $("#hospitalId").hide();
                    $(".checkWorkTime").hide();
                    $("#specializedId").hide();
                    if (value == "ROLE_USER") {
                        $('#img').hide();
                    }
                } else {
                    $("#hospitalId").show();
                    $(".checkWorkTime").show();
                    $("#specializedId").show();
                    $('#img').show();
                }

            });

            // checkAnyCheckBox
            var countChecked = function () {
                count = $("input[name='checkOne']:checked").length
                if (count < 1) {
                    $("#btnRestore").prop("disabled", true);
                    $("#btnDelete").prop("disabled", true);
                    $("#btnDetailUser").prop("disabled", true);
                } else {
                    $("#btnRestore").prop("disabled", false);
                    $("#btnDelete").prop("disabled", false);
                    if (count == 1) {
                        $("#btnDetailUser").prop("disabled", false);
                    } else {
                        $("#btnDetailUser").prop("disabled", true);
                    }
                }
            };

            countChecked();
            $("input[type=checkbox]").on("click", countChecked);

            // editProduct
            $("#btnDetailUser").click(
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
                    $('#fullname').val(values[0]);
                    $('#username').val(values[1]);
                    $('#showImage').attr("src", values2[2]);
                    $('#phoneNumber').val(values[3]);
                    $('#location').val(values[4]);
                    $('#specializedId').val(values[5]).change();
                    $('#yearOfBirth').val(values[6]);
                    $('#email').val(values[7]);
                    $('#shortDescription').html(values2[0]);
                    $('#description').html(values2[1]);
                    $('#hospitalId').val(values2[3]).change();
                });

            $("#btnConfirmDelete").click(
                function (event) {
                    event.preventDefault();
                    let data = {};
                    let urlPath = window.location.origin + '/api/user/delete';
                    let ids = $(
                        'tbody input[name="checkOne"]:checked')
                        .map(function () {
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