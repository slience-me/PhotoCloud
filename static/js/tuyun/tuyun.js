layui.use(['layer','upload','element'],function(){var layer=layui.layer,upload=layui.upload,element=layui.element,$=layui.jquery,time=0,files;var a=0;var skat=0;var jsonarray;var array=[];$("#back-to-top").click(function(){if($('html').scrollTop()){$('html').animate({scrollTop:0},1000);$("#img-multi").click();return false;}$('body').animate({scrollTop:0},1000);return false;});var demoListView=$('#demoList'),uploadListIns=upload.render({elem:'#img-multi',url:'show',accept:'file',multiple:true,size:4096,field:'file',auto:false,bindAction:'#testListAction',choose:function(obj){var filess=this.files=obj.pushFile();element.progress('demo','0%');for(key in filess){a=a+1;}skat=a;obj.preview(function(index,file,result){obj.upload(index,file);uploadListIns.config.elem.next()[0].value='';});},done:function(res,index,upload){a--;if(a===0){clearInterval(time);time=0;element.progress('demo','100%');}else{var ind=skat-a;var ss=100/skat*ind|0;element.progress('demo',ss+'%');}if(res.code===0){if(jsonarray==null||jsonarray==undefined){var jsonstr="[]";jsonarray=eval('('+jsonstr+')');}jsonarray.push(res.data);console.info(jsonarray);}else{array.push(this.files[index].name);console.info(array.toString());}if(a===0){var str="<div class='layui-row layui-col-space30 vip-goods-box'><p>以下为上传成功的图片</p>";for(kays in jsonarray){if(kays==="compare"){}else{str=str+"<a class='layui-col-xs12 layui-col-sm4 layui-col-md2'><div class='vip-goods-list'><img src='"+jsonarray[kays].url+"' alt=''><div class='vip-goods-footer'><button class='look layui-btn layui-btn-xs layui-bg-red flt-left ' data-url='"+jsonarray[kays].url+"'>预览</button> <button class='copy layui-btn layui-btn-xs layui-bg-red flt-right' data-url='"+'http://127.0.0.1:8000'+jsonarray[kays].url+"'>复制</button> </div></div></a>";}}for(kays in jsonarray){if(kays!=="compare"){delete jsonarray[kays];}}str=str+"<hr/><p>以及以下上传失败的文件<br/>"+array.toString()+"<br/><br/><br/><h3>严禁上传及分享如下类型的图片：</h3><ul>"+"<li>含有色情、暴力、宣扬恐怖主义的图片</li>"+"<li>侵犯版权、未经授权的图片</li>"+"<li>其他违反中华人民共和国法律的图片</li>"+"<li>其他违反香港法律的图片</li>"+"</ul></p></div>";array.length=0;layer.open({title:'信息出来啦',type:1,shadeClose:true,area:['80%','80%'],content:'<div style="padding: 20px;">'+str+'</div>'});}return delete this.files[index];},error:function(index,upload){console.info("网络出现问题");}});$(document).on('click','.look',function(){var url=$(this).data('url');window.open(url);});$(document).on('click','.copy',function(){var urls=$(this).data('url');var clipboard=new ClipboardJS('.copy',{text:function(){return urls;}});clipboard.on('success',function(e){layer.msg("链接复制成功")});clipboard.on('error',function(e){layer.alert('复制失败，请手动复制<br/>'+url);});});});