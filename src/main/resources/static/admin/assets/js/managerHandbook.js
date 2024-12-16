$(document)
    .ready(
        function () {
            let descriptionEdittor = null;
            let contentEditor = null;
            (function () {
                descriptionEdittor = new FroalaEditor("#description", {
                    linkStyles: {
                        class1: 'Class 1',
                        class2: 'Class 2'
                    }
                })
            })();
            (function () {
                contentEditor = new FroalaEditor('#content', {
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
                        },
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
            $("#addHandbook")
                .click(
                    function () {
                        if ($("#uploadHandbook").valid()) {
                            var descriptionHTML = descriptionEdittor.html.get();
                            var contentHTML = contentEditor.html.get();
                            var stringDesctiption = $.trim(jQuery(descriptionHTML).text());
                            var stringContent = $.trim(jQuery(contentHTML).text());
                            if (stringDesctiption) {
                                if (stringContent) {
                                    event.preventDefault();
                                    var form = $('#uploadHandbook')[0];
                                    var urlLocation = window.location.href;
                                    var urlpath = urlLocation.substring(0, urlLocation.indexOf('?') != -1 ? urlLocation.indexOf('?') : urlLocation.length);
                                    var data = new FormData(form);
                                    $
                                        .ajax({
                                            url: urlpath + "/add",
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
                                                            window.location
                                                                .replace(urlpath)
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
                                    alert('Bạn cần nhập lại nội dung');
                                }
                            } else {
                                alert('Bạn cần nhập mô tả');
                            }


                            // Get form

                        } else {
                            alert("Bạn cần nhập lại thông tin cần thiết");
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
                    $("#btnDeleteHandbook").prop("disabled", true);
                    $("#btnEditHandbook").prop("disabled", true);
                    $("#btnAddHandbook").prop("disabled", false);
                } else {

                    $("#btnAddHandbook").prop("disabled", true);
                    $("#btnDeleteHandbook").prop("disabled", false);
                    if (count == 1) {
                        $("#btnEditHandbook").prop("disabled", false);

                    } else {
                        $("#btnEditHandbook").prop("disabled", true);
                    }
                }
            };
            countChecked();

            $("input[type=checkbox]").on("click", countChecked);

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
                    descriptionEdittor.html.set(values2[1]);
                    $('#description').val(values2[1])
                    var test = $('#descriptionHtml').val();
                    contentEditor.html.set(values2[0]);
                    $('#content').val(values2[0])
                    $('#createdBy').val(values[5])
                    $('#createdDate').val(values[6])
                    $("#addHandbook").css("display", "none");
                    $("#editHandbook").css("display", "block");
                });

            $("#btnAddHandbook").click(
                function (event) {
                    event.preventDefault();
                    $('#id').val('');
                    $('#title').val('');
                    $('#specializedId').val('').change();
                    descriptionEdittor.html.set('');
                    $('#description').val('')
                    contentEditor.html.set('');
                    $('#content').val('')
                    $('#createdBy').val('')
                    $('#createdDate').val('')
                    $("#addHandbook").css("display", "block");
                    $("#editHandbook").css("display", "none");
                });


            $("#btnConfirmDelete").click(
                function (event) {
                    event.preventDefault();
                    let data = {};
                    let urlLocation = window.location.href;
                    let urlpath = urlLocation.substring(0, urlLocation.indexOf('?') != -1 ? urlLocation.indexOf('?') : urlLocation.length);
                    let ids = $('tbody input[name="checkOne"]:checked').map(function () {
                        return $(this).val();
                    }).get();
                    data['ids'] = ids;
                    $.ajax({
                        url: urlpath + "/uDelete",
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


            $("#editHandbook")
                .click(
                    function () {
                        if ($("#uploadHandbook").valid()) {
                            var description = descriptionEdittor.html.get();
                            var content = contentEditor.html.get();
                            var stringContent = $.trim(content);
                            var stringDesctiption = $.trim(description);
                            if (stringDesctiption) {
                                if (stringContent) {
                                    event.preventDefault();
                                    var form = $('#uploadHandbook')[0];
                                    var urlLocation = window.location.href;
                                    var urlpath = urlLocation.substring(0, urlLocation.indexOf('?') != -1 ? urlLocation.indexOf('?') : urlLocation.length);
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
                                } else {
                                    alert("Bạn cần nhập lại nội dung");
                                }
                            } else {
                                alert("Bạn cần nhập lại mô tả");
                            }
                        } else {
                            alert("Bạn cần nhập lại thông tin");
                        }

                    });
        });