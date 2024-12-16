$(document).ready(function () {
    $('#editLich').on('change', function () {
        if (this.checked) {
            $('#editLichKham').removeAttr('hidden');

            $('input[name=workTimeIdOld]').each(function () {
                var value = this.defaultValue;
                $('input[name=workTimeId]').each(function () {
                    if (this.defaultValue == value) {
                        this.checked = true;
                    }
                });

            });
        } else {

            $('input[name=workTimeId]').each(function () {
                this.checked = false;
            });
            $('#editLichKham').attr("hidden", true);
        }

    });
    $('#editPassword').on('change', function () {
        if (this.checked) {
            $('#changePassword').removeAttr('hidden');
        } else {
            $('#changePassword').attr("hidden", true);
            ;
        }

    });
    const editorInstance = new FroalaEditor('#description', {
        events: {
            'image.beforeUpload': function (files) {
                const editor = this
                if (files.length) {
                    var reader = new FileReader()
                    reader.onload = function (e) {
                        var result = e.target.result
                        editor.image.insert(result, null, null, editor.image.get())
                    }
                    reader.readAsDataURL(files[0])
                }
                return false
            }
        }
    })

//	 shortDescription

    const editorShortDescription = new FroalaEditor('#shortDescription', {
        events: {
            'image.beforeUpload': function (files) {
                const editor = this
                if (files.length) {
                    var reader = new FileReader()
                    reader.onload = function (e) {
                        var result = e.target.result
                        editor.image.insert(result, null, null, editor.image.get())
                    }
                    reader.readAsDataURL(files[0])
                }
                return false
            }
        }
    })

    var readURL = function (input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('.avatar').attr('src', e.target.result);
            }

            reader.readAsDataURL(input.files[0]);
        }
    }


    $("#img").on('change', function () {
        readURL(this);
    });
    $('#updateProfile')
        .validate(
            {
                rules: {
                    fullname: {
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
                    sex: {
                        required: true
                    },
//					specializedId:{
//						required : true
//					},
                    yearOfBirth: {
                        required: true
                    },
//					hospitalId:{
//						required : true
//					},
                    email: {
                        required: true,
                        email: true
                    },
                    workTimeId: {
                        required: true
                    },
                    password: {
                        required: true
                    },
                    confirmPassword: {
                        equalTo: "#password"
                    },
                    examinationPrice : {
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
                    phoneNumber: {
                        required: "Số điện thoại không được để trống",
                        number: "Bạn chỉ được phép nhập số",
                        minlength: "Bạn đã nhập sai số điện thoại",
                        maxlength: "Bạn đã nhập sai số điện thoại"
                    },
                    location: {
                        required: "Địa chỉ không được để trống"
                    },
                    sex: {
                        required: "Giới tính không được để trống"
                    },
                    specializedId: {
                        required: "Bạn chưa chọn mục này"
                    },
                    yearOfBirth: {
                        required: "Ngày sinh không được để trống"
                    },
                    hospitalId: {
                        required: "Bạn chưa chọn mục này"
                    },
                    email: {
                        required: "Email không được để trống",
                        email: "Bạn đã nhập sai định dạng email"
                    },
                    workTimeId: {
                        required: "Bạn chưa chọn mục này"
                    },
                    password: {
                        required: "Mật khẩu không được để trống"
                    },
                    confirmPassword: {
                        equalTo: "Mật khẩu không trùng khớp"
                    },
                    examinationPrice : {
                        required: "Giá khám không được để trống",
                        number: "Bạn chỉ được phép nhập số",
                        minlength: "Bạn đã nhập sai giá khám",
                        maxlength: "Bạn đã nhập sai giá khám"
                    }
                },

                submitHandler: function (form) {
                    alert('oke');
                }
            });
    $("#btnUpdateProfile")
        .click(
            function () {
                if ($("#updateProfile").valid()) {
                    var shortDescriptionHTML = editorShortDescription.html.get();
                    var descriptionHTML = editorInstance.html.get();
                    var stringDesctiption = $.trim(jQuery(descriptionHTML).text());
                    var stringShortDescription = $.trim(jQuery(shortDescriptionHTML).text());
                    if (stringDesctiption && stringShortDescription) {
                        event.preventDefault();
                        if (!$('#editLich').prop('checked')) {
                            $('input[name=workTimeIdOld]').each(function () {
                                var value = this.defaultValue;
                                $('input[name=workTimeId]').each(function () {
                                    if (this.defaultValue == value) {
                                        this.checked = true;
                                    }
                                });
                            });
                        }

                        // Get form
                        var form = $('#updateProfile')[0];
                        var urlpath = window.location.href;
                        var data = new FormData(form);
                        $
                            .ajax({
                                url: urlpath,
                                type: "POST",
                                enctype: 'multipart/form-data',
                                data: data,
                                processData: false,
                                contentType: false,
                                cache: false,
                                success: function (e) {
                                    if (e) {
                                        alert('Bạn đã cập nhập thành công');
                                        window.location.replace(urlpath)
                                    }
                                },
                                error: function (e) {
                                    alert('Đã có lỗi xảy ra !');
                                    window.location.replace(urlpath)
                                }
                            });
                    } else {

                        if (!stringDesctiption) {
                            alert("Bạn cần nhập lại mô tả");
                        }
                        if (!stringShortDescription) {
                            alert("Bạn cần nhập lại mô tả ngắn");
                        }
                    }
                } else {
                    alert("Bàn cần nhập đủ thông tin cần thiết")
                }


            });

});