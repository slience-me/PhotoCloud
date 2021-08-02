layui.use(['table','upload','element','layer'],function(){var $=layui.jquery,table=layui.table,upload=layui.upload,element=layui.element,layer=layui.layer;var dataTable=table.render({elem:'#history-list',url:'data',toolbar:'#toolbarDemo',defaultToolbar:['filter','exports','print',{title:'提示',layEvent:'LAYTABLE_TIPS',icon:'layui-icon-tips'}],title:'用户数据表',cols:[[{type:'checkbox',fixed:'left'},{field:'id',title:'ID',width:60,fixed:'left',unresize:true,sort:true,type:'numbers'},{field:'url',title:'图片',align:"center",width:60,templet:"<div><img src='{{ d.url }}' style='width: 20px; height: 20px'></div>"},{field:'filename',title:'文件名',align:"center",width:80},{field:'url',title:'路径',align:"center",width:550},{field:'upload_date',title:'上传时间',width:200,align:"center"},{fixed:'right',title:'操作',toolbar:'#barDemo',width:120}]],page:true,initSort:{field:'upload_date',type:'desc'}});const upload_img_1=upload.render({elem:'#upload_img1',url:'show',multiple:false,auto:true,done:function(res){console.log(res);if(res.code>0){return layer.msg('上传失败',{icon:1,time:1000});}layer.msg('上传成功',{icon:1,time:1000});window.location.reload();}});function uplaod_Img(){upload_img_1}table.on('toolbar(test)',function(obj){var checkStatus=table.checkStatus(obj.config.id);switch(obj.event){case'getCheckData':var data=checkStatus.data;layer.alert(JSON.stringify(data));break;case'getCheckLength':var data=checkStatus.data;layer.msg('选中了：'+data.length+' 个');break;case'isAll':layer.msg(checkStatus.isAll?'全选':'未全选');break;case'upload_img':uplaod_Img();break;case'LAYTABLE_TIPS':layer.alert('这是工具栏右侧自定义的一个图标按钮');break;}});$('#copy-url').click(function(){var urls=$(this).data('url');var clipboard=new ClipboardJS('.copy',{text:function(){return urls;}});clipboard.on('success',function(e){layer.msg("链接复制成功")});clipboard.on('error',function(e){layer.alert('复制失败，请手动复制<br/>'+url);});});function myCopy(){let input=document.getElementById("input_copy_txt_to_board");input.select();document.execCommand('copy');layer.msg("链接复制成功",{icon:1,time:1000})}table.on('tool(test)',function(obj){var data=obj.data;if(obj.event==='del'){layer.confirm('真的删除行么',function(index){$.get("remove",{"pk":data.id},function(data){if(data.code===0){layer.msg('删除成功',{icon:1,time:1000});window.location.reload();}else{msg=data.msg;layer.msg('删除失败，请重试',{icon:5,time:3000});layer.alert(msg);}layer.close(index);},"json");});}else if(obj.event==='copy'){let url="http://127.0.0.1:8000"+data.url;let input=$("#input_copy_txt_to_board");input.attr('value',url);myCopy()}});});