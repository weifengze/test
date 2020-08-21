/**
 * @program: bjjh-demo
 * @Description: login
 * @Author: 魏丰泽
 * @Create: 2020-07-29 16:44
 **/

$(function () {
    validateKickout();
    validateRule();
    $('.imgcode').click(function () {
        var url = ctx + "captcha/captchaImage?type=" + captchaType + "&s=" + Math.random();
        $(".imgcode").attr("src", url);
    });
});

$.validator.setDefaults({
    submitHandler: function () {
        console.log("sfajiotjgirotjo")
        login();
    }
});

function login() {
    //$.modal.loading($("#btnSubmit").data("loading"));
    $("#btnSubmit").text("登录中……")
    let username = $.common.trim($("input[name='username']").val());
    let password = $.common.trim($("input[name='password']").val());
    let validateCode = $("input[name='validateCode']").val();
    let rememberMe = $("input[name='rememberme']").is(':checked');
    console.log(username)
    console.log(password)
    console.log(validateCode)
    console.log(rememberMe)
    $.ajax({
        type: "post",
        url: ctx + "login",
        data: {
            "username": username,
            "password": password,
            "validateCode": validateCode,
            "rememberMe": rememberMe
        },
        success: function (r) {
            if (r.code == 0) {
                location.href = ctx + 'index';
            } else {
                $("#btnSubmit").text("登录");
                //$.modal.closeLoading();
                $('.imgcode').click();
                $(".code").val("");
                let html = '<div class="alert alert-danger alert-dismissible" role="alert">' + r.msg + '</div>';
                $(".animation-fade").append(html);
            }
        }
    });
}

function validateRule() {
    // var icon = "<i class='fa fa-times-circle'></i> ";
    $("#loginForm").validate({
        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            }
        },
        messages: {
            username: {
                required: "请输入您的用户名",
            },
            password: {
                required: "请输入您的密码",
            }
        }
    })
}

function validateKickout() {
    if (getParam("kickout") == 1) {
        layer.alert("<font color='red'>您已在别处登录，请您修改密码或重新登录</font>", {
                icon: 0,
                title: "系统提示"
            },
            function (index) {
                //关闭弹窗
                layer.close(index);
                if (top != self) {
                    top.location = self.location;
                } else {
                    const url = location.search;
                    if (url) {
                        const oldUrl = window.location.href;
                        self.location = oldUrl.substring(0, oldUrl.indexOf('?'));
                    }
                }
            });
    }
}

function getParam(paramName) {
    var reg = new RegExp("(^|&)" + paramName + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);
    return null;
}