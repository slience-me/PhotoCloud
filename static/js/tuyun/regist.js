layui.use('layer',function(){const $=layui.jquery,layer=layui.layer;function checkUsername_login(){let label=$("#LAY-user-login-username");let username=label.val();let flag=!(username===''||username===undefined||username==null);if(flag){label.css("border","");}else{label.css("border","1px solid red");}return flag;}function checkPassword_login(){let label=$("#LAY-user-login-password");let password=label.val();let flag=!(password===''||password===undefined||password==null);if(flag){label.css("border","");}else{label.css("border","1px solid red");}return flag;}function checkCheckcode_login(){let label=$("#LAY-user-login-vercode");let check=label.val();let flag=!(check===''||check===undefined||check==null);if(flag){label.css("border","");}else{label.css("border","1px solid red");}return flag;}function confirmPassword(){let label=$("#LAY-user-login-repass");let confirmpassword=label.val();let flag=!(confirmpassword===''||confirmpassword===undefined||confirmpassword==null);if(flag){label.css("border","");}else{label.css("border","1px solid red");}return flag;}$(function(){$("#regist_form").click(function(){if(checkUsername_login()&&checkPassword_login()&&checkCheckcode_login()&&confirmPassword()){$.post("regist",$("#registForm").serialize(),function(data){let $=layui.jquery,layer=layui.layer;if(data.code===0){console.log("注册成功!");msg=data.msg;layer.alert(msg,{time:3*1000,success:function(layero,index){let timeNum=this.time/1000,setText=function(start){layer.title((start?timeNum:--timeNum)+' 秒后关闭',index);};setText(!0);this.timer=setInterval(setText,1000);if(timeNum<=0)clearInterval(this.timer);},end:function(){clearInterval(this.timer);}});}else{console.log("注册失败!");msg=data.msg;layer.alert(msg,{time:3*1000,success:function(layero,index){let timeNum=this.time/1000,setText=function(start){layer.title((start?timeNum:--timeNum)+' 秒后关闭',index);};setText(!0);this.timer=setInterval(setText,1000);if(timeNum<=0)clearInterval(this.timer);},end:function(){clearInterval(this.timer);}});}},"json");}});});});