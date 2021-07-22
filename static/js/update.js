layui.use('layer', function() { //独立版的layer无需执行这一句
    var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句
    $(function () {
        $("#update_form").click(function () {
            //校验通过，发送ajax请求，提交表单的数据
            $.post("center", $("#updateForm").serialize(), function (data) {
                //处理服务器响应的数据   data {code:0/1,errorMsg:"注册失败！"}
                console.log(data)
                if (data.code === 0) {
                    console.log("修改信息成功!");
                    msg = data.msg;
                    layer.alert(msg, {
                        time: 3 * 1000
                        , success: function (layero, index) {
                            var timeNum = this.time / 1000, setText = function (start) {
                                layer.title((start ? timeNum : --timeNum) + ' 秒后关闭', index);
                            };
                            setText(!0);
                            this.timer = setInterval(setText, 1000);
                            if (timeNum <= 0) clearInterval(this.timer);
                        }
                        , end: function () {
                            clearInterval(this.timer);
                        }
                    });
                } else {
                    //注册失败，给errorMsg添加提示信息
                    console.log("修改信息失败!");
                    msg = data.msg;
                    layer = layui.layer;
                    layer.alert(msg, {
                        time: 3 * 1000
                        , success: function (layero, index) {
                            var timeNum = this.time / 1000, setText = function (start) {
                                layer.title((start ? timeNum : --timeNum) + ' 秒后关闭', index);
                            };
                            setText(!0);
                            this.timer = setInterval(setText, 1000);
                            if (timeNum <= 0) clearInterval(this.timer);
                        }
                        , end: function () {
                            clearInterval(this.timer);
                        }
                    });
                }
            }, "json");
        });
    });

    $(function () {
        $("#updatePwd_form").click(function () {
            //校验通过，发送ajax请求，提交表单的数据
            $.post("pwd", $("#updatePwdForm").serialize(), function (data) {
                //处理服务器响应的数据   data {code:0/1,errorMsg:"注册失败！"}
                console.log(data)
                if (data.code === 0) {
                    console.log("修改密码成功!");
                    msg = data.msg;
                    layer.alert(msg, {
                        time: 3 * 1000
                        , success: function (layero, index) {
                            var timeNum = this.time / 1000, setText = function (start) {
                                layer.title((start ? timeNum : --timeNum) + ' 秒后关闭', index);
                            };
                            setText(!0);
                            this.timer = setInterval(setText, 1000);
                            if (timeNum <= 0) clearInterval(this.timer);
                        }
                        , end: function () {
                            clearInterval(this.timer);
                        }
                    });
                } else {
                    //注册失败，给errorMsg添加提示信息
                    console.log("修改密码失败!");
                    msg = data.msg;
                    layer = layui.layer;
                    layer.alert(msg, {
                        time: 3 * 1000
                        , success: function (layero, index) {
                            var timeNum = this.time / 1000, setText = function (start) {
                                layer.title((start ? timeNum : --timeNum) + ' 秒后关闭', index);
                            };
                            setText(!0);
                            this.timer = setInterval(setText, 1000);
                            if (timeNum <= 0) clearInterval(this.timer);
                        }
                        , end: function () {
                            clearInterval(this.timer);
                        }
                    });
                }
            }, "json");
        });
    });
});








