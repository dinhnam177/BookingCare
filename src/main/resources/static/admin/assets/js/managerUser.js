$(document)
    .ready(
        function () {
            let editorDescription;
            let editorShortDescription;
            (function () {
                editorDescription = new FroalaEditor(
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
                editorShortDescription = new FroalaEditor(
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
                            let url = window.location.pathname;
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

            $("#addUser")
                .click(
                    function () {

                        if ($("#uploadUser").valid()) {
                            event.preventDefault();

                            // Get form
                            var form = $('#uploadUser')[0];
                            var urlLocation = window.location.origin;
                            var urlpath = urlLocation.substring(0, urlLocation.indexOf('?') != -1 ? urlLocation.indexOf('?') : urlLocation.length);
                            var data = new FormData(form);
                            $
                                .ajax({
                                    url: urlpath
                                        + "/admin/api/managerUser/add",
                                    type: "POST",
                                    enctype: 'multipart/form-data',
                                    data: data,
                                    processData: false,
                                    contentType: false,
                                    cache: false,
                                    success: function (
                                        result) {
                                        if (result) {
                                            success: $(
                                                '#modal')
                                                .modal(
                                                    'hide');

                                            complete: $(
                                                '#ok')
                                                .modal(
                                                    'show');

                                            setTimeout(
                                                function () {
                                                    window.location
                                                        .replace(urlpath
                                                            + "/admin/managerUser")
                                                }, 1500);
                                        }
                                    },
                                    error: function (e) {
                                        if (e.status) {
                                            success: $(
                                                '#modal')
                                                .modal(
                                                    'hide');

                                            complete: $(
                                                '#ok')
                                                .modal(
                                                    'show');

                                            setTimeout(
                                                function () {
                                                    window.location
                                                        .replace(urlpath
                                                            + "/admin/managerUser")
                                                }, 1500);
                                        } else {
                                            alert('Đã có lỗi xảy ra !');
                                        }
                                    }
                                });
                        } else {
                            alert('Bạn cần điền lại thông tin')
                        }
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
                            },
                            examinationPrice: {
                                required: true,
                                number: true,
                                minlength: 6,
                                maxlength: 8
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
                            examinationPrice: {
                                required: "Giá khám không được để trống ",
                                number: "Chỉ được phép nhập chữ số"
                            }
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
                });
            $("#roleName").change(function () {
                let value = $("#roleName").val();
                if (value !== "ROLE_DOCTOR") {
                    // alert("hi");
                    $("#hospitalId").hide();
                    $(".checkWorkTime").hide();
                    $("#specializedId").hide();
                    $('#examinationPrice').hide();
                    if (value === "ROLE_USER") {
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
                console.log(count);
                if (count < 1) {
                    $("#btnAddUser").prop("disabled", false);
                    $("#btnDelete").prop("disabled", true);
                    $("#btnDetail").prop("disabled", true);
                } else {
                    $("#btnAddUser").prop("disabled", true);
                    $("#btnDelete").prop("disabled", false);
                    if (count == 1) {
                        $("#btnDetail").prop("disabled", false);
                    } else {
                        $("#btnDetail").prop("disabled", true);
                    }
                }
            };

            var checkUsername = function () {
                $("#username").on("input", function (e) {
                    $('#msg').hide();
                    if ($('#username').val() == null || $('#username').val() == "") {
                        $('#msg').show();
                        $("#msg").html("Username is a required field.").css("color", "red");
                        $('#addUser').hide();
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
                                500: function (xhr) {
                                    $('#msg').show();
                                    $("#msg").html("Username available").css("color", "green");
                                    $('#addUser').show();
                                }
                            },
                            success: function (msg) {
                                $('#msg').show();
                                if (msg !== null || msg !== "") {
                                    $("#msg").html("Username already taken").css("color", "red");
                                    $('#addUser').hide();
                                } else {
                                    $("#msg").html("Username available").css("color", "green");
                                    $('#addUser').show();
                                }
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                $('#msg').show();
                                $("#msg").html(textStatus + " " + errorThrown).css("color", "red");
                            }
                        });
                    }
                });
            }
            countChecked();

            $("input[type=checkbox]").on("click", countChecked);

            // editProduct
            $("#btnDetail").click(
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
                    $('#phoneNumber').val(values[3]);
                    $('#hospitalId').val(values[5]).change();
                    $('#specializedId').val(values[4]).change();
                    $('#email').val(values[6]);
                    $('#showImage').attr("src", values[7]);
                    $('#yearOfBirth').val(values[8]);
                    $('#location').val(values[9]);
                    $('#sex').val(values[10]).change();
                    $('#examinationPrice').val(values[11]);
                    $('#roleName').val(values[12]).change();
                    if (values[12] === 'ROLE_DOCTOR') {
                        $('.workTimeIdHidden').each(function( index ) {
                            let itemWkIdOfUser = $(this).val();
                            $('.checkWorkTime').each(function( index ) {
                                if ($( this ).val() === itemWkIdOfUser) {
                                    $(this).attr('checked', 'checked');
                                }
                            });
                        });
                    }
                    $( "#roleName" ).trigger( "change" );
                    editorShortDescription.html.set(values2[0]);
                    editorDescription.html.set(values2[0]);
                    $("#addUser").css("display", "none");
                    $('#uploadUser *').attr('readonly', true);
                    $('#uploadUser select').prop( "disabled", true );
                });


            function resetData() {
                $('#fullname').val("");
                $('#username').val("");
                $('#phoneNumber').val("");
                $('#hospitalId').val("").change();
                $('#specializedId').val("").change();
                $('#email').val("");
                $('#showImage').attr("src", "");
                $('#yearOfBirth').val("");
                $('#location').val("");
                $('#sex').val("").change();
                $('#examinationPrice').val("");
                $('#roleName').val("").change();
                $('.checkWorkTime').each(function( index ) {
                    $(this).prop('checked', false);
                });
                $( "#roleName" ).trigger( "change" )
                editorShortDescription.html.set("");
                editorDescription.html.set("");
                $('#uploadUser *').attr("readonly", false);
                $('#uploadUser select').prop( "disabled", false );
            }

            $("#btnAddUser").click(function (event) {
                event.preventDefault();
                resetData();
                $("#addUser").css("display", "block");
                $("#editUser").css("display", "none");
                checkUsername();

            });

            $("#btnConfirmDelete").click(
                function (event) {
                    event.preventDefault();
                    let data = {};
                    let urlPath = window.location.origin + '/api/user/uDelete';
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