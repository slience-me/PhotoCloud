layui.use(['table', 'upload', 'element', 'layer'], function () {
    var $ = layui.jquery, table = layui.table, upload = layui.upload, element = layui.element, layer = layui.layer;
    var dataTable = table.render({
        elem: '#user-list',
        url: '/admin/userinfo',
        toolbar: '#toolbarDemo',
        defaultToolbar: ['filter', 'exports', 'print', {
            title: '提示',
            layEvent: 'LAYTABLE_TIPS',
            icon: 'layui-icon-tips'
        }],
        title: '用户数据表',
        cols: [[{type: 'checkbox', fixed: 'left'},
            {field: 'id', title: 'ID', width: 60, fixed: 'left', unresize: true, sort: true, type: 'numbers'},
            {field: 'username', title: '用户名', align: "center", width: 120},
            {field: 'password', title: '密码', align: "center", width: 90},
            {field: 'name', title: '昵称', align: "center", width: 120},
            {field: 'sex', title: '性别', width: 70, align: "center"},
            {field: 'email', title: '邮箱', width: 200, align: "center"},
            {field: 'status', title: '状态', width: 70, align: "center"},
            {field: 'date_joined', title: '注册时间', width: 200, align: "center"},
            {fixed: 'right', title: '操作', toolbar: '#barDemo', width: 130
        }]],
        page: true,
        initSort: {field: 'date_joined', type: 'desc'}
    });


    table.on('toolbar(test)', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        switch (obj.event) {
            case'getCheckData':
                var data = checkStatus.data;
                layer.alert(JSON.stringify(data));
                break;
            case'getCheckLength':
                var data = checkStatus.data;
                layer.msg('选中了：' + data.length + ' 个');
                break;
            case'isAll':
                layer.msg(checkStatus.isAll ? '全选' : '未全选');
                break;

            case'LAYTABLE_TIPS':
                layer.alert('这是工具栏右侧自定义的一个图标按钮');
                break;
        }
    });

    table.on('tool(test)', function (obj) {
        var data = obj.data;
        if (obj.event === 'del') {
            layer.confirm('真的删除行么', function (index) {
                $.get("/admin/removeuser", {"uid": data.uid}, function (data) {
                    if (data.code === 0) {
                        layer.msg('删除成功', {icon: 1, time: 1000});
                        window.location.reload();
                    } else {
                        msg = data.msg;
                        layer.msg('删除失败，请重试', {icon: 5, time: 3000});
                        layer.alert(msg);
                    }
                    layer.close(index);
                }, "json");
            });
        } else if (obj.event === 'user-img') {
            let go_url = "http://127.0.0.1:8000/admin/userimg?uid=" + data.uid;
            window.location.replace(go_url);
        }
    });
});