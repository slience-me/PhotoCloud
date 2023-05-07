$(function () {
    //导航定位
    $(".nav li:eq(1)").addClass("navCur");
    $("#header-user").text(localStorage.getItem("name") !== null ? localStorage.getItem("name") : '未登录');
    if (localStorage.getItem("name") !== null){
        $("#g-h-login").remove();
        $("#g-h-regist").remove();
    } else {
        $("#g-h-logout").remove();
    }
    $("#div-logout").display = localStorage.getItem("name") !== null ? 'none' : 'block';
    //$("#div-logout").style('display', localStorage.getItem("name") !== null ? 'none' : 'block');
})
//设置cookie
function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}
// 删除cookie
function clearCookie(name) {
    setCookie(name, "", -1);
}
function tuyunLogout(){
    let config = {
        headers: {
            'x-access-token': localStorage.getItem('x-access-token')
        }
    }
    axios.get('/user/logout', config).then(function (result){
        if (result.data.status===0){
            layer.alert(result.data.msg);
            clearCookie('secret');
            document.cookie = "secret=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
            localStorage.clear()
            location.href = '/user/login.html'
        } else {
            document.cookie = "secret=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
            //clearCookie('secret');
            localStorage.clear()
            layer.alert(result.data.msg);
        }

    })
}