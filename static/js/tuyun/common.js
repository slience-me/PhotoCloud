layui.use(['laypage','layer'],function(){const laypage=layui.laypage,layer=layui.layer;$.get("/getimg",function(res){laypage.render({elem:'page-img',count:res.count,theme:'#FFB800',limit:15,jump:function(obj,first){$("#img-path").empty();$.get("/getimg",{"page":obj.curr,"limit":obj.limit},function(data){if(data.code===0){IMG_list=data.data;for(let i=0;i<IMG_list.length;i++){$("#img-path").append("<li><a href="+IMG_list[i]+" target='_blank'><img class='layui-anim lazy' data-anim=layui-anim-scale' src="+IMG_list[i]+"/></a></li>")}}},"json");if(!first){}}});},"json");})
jQuery(document).ready(function ($) {
    $(".lazy").lazyload({
        placeholder: "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fwww.1dfix.com%2Fpublic%2Fupload%2Ftemp%2F2019%2F05-23%2Fa211a3acb907256b00f48d3d09ce1d67.gif&refer=http%3A%2F%2Fwww.1dfix.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1630493569&t=6077d9ea32b77d90fb12ba67635e2737",
        effect: "fadeIn"
    });
});